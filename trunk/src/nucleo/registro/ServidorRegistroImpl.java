/* 
Este fichero es parte de VoIPTester

    Copyright (C) 2009-2010 
    	      Antonio S醤chez Navarro (titosanxez@gmail.com)
	      Juan M. L髉ez Soler (juanma@ugr.es)
	      Jorge Navarro Ortiz (jorgenavarro@ugr.es)
    

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los t閞minos de la Licencia P鷅lica General GNU publicada 
    por la Fundaci髇 para el Software Libre, ya sea la versi髇 3 
    de la Licencia, o (a su elecci髇) cualquier versi髇 posterior.

    Este programa se distribuye con la esperanza de que sea 鷗il, pero 
    SIN GARANT虯 ALGUNA; ni siquiera la garant韆 impl韈ita 
    MERCANTIL o de APTITUD PARA UN PROP覵ITO DETERMINADO. 
    Consulte los detalles de la Licencia P鷅lica General GNU para obtener 
    una informaci髇 m醩 detallada. 

    Deber韆 haber recibido una copia de la Licencia P鷅lica General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.

*/


package nucleo.registro;

import nucleo.nodo.NodoRemoto;
import nucleo.nodo.NodoException;
import nucleo.nodo.Nodo;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.Naming;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;


/**
 *
 * @author Antonio
 */
public class ServidorRegistroImpl implements ServidorRegistro {


    /**
     * Contiene la asociaci贸n del cliente registrado y el nodo asociado, al cu谩l
     * puede realizarle peticiones remotas.
     * Solo se permite tener un cliente registrado en un instante dado.
     */
    Hashtable<ClienteNodo,Nodo> clienteRegistrado = new Hashtable<ClienteNodo,Nodo>(1);


    /**
     * Servicio de comprobaci贸n de actividad/disponibilidad del cliente.
     */
    private ComprobadorActividad comprobadorActividad;

    /**
     * Intervalo de tiempo que dispone el cliente para notificar al servidor
     * de su actividad antes de que el servidor de registro lo compruebe por s铆 mismo.
     */
    private int tiempo_expiracion_ms = 10000;


    /**
     * Nombre de host donde se ejecuta el servidor de registro
     */
    String hostLocal="localhost";

    /**
     * Puerto por el que atiende las peticione sel servidor de registro
     */
    int puertoLocal=4662;


    /**
     * Servicio de logging
     */
    Logger registroLogger;


    /**
     * Crea el servidor de registro
     *
     */
    public ServidorRegistroImpl() {


        try{

            this.registroLogger = Logger.getLogger("nucleo.registro");
            this.registroLogger.setLevel(Level.FINER);
            FileHandler fh = new FileHandler(
                    new File("logs" + File.separator + "servidorRegistro.log").getPath()
                    );
            fh.setFormatter(new SimpleFormatter());
            this.registroLogger.setUseParentHandlers(false);
            this.registroLogger.addHandler(fh);

        }catch(IOException ioe) {
            this.registroLogger.log(Level.FINER,"Error while creating log file", ioe);
            ioe.printStackTrace();
        }        

    }

    /**
     * Permite registrar a un cliente y enviarle el nodo que puede utilizar para
     * las peticiones remotas.
     *
     * @param clienteNodo   El cliente que pide ser registrado
     *
     * @throws RemoteException      Si hay un problema con las peticiones remotas
     * @throws RegistroException    Si no se puede crear el nodo remoto
     *                              Si no se puede crear el servicio de nodo remoto
     *                              Si no se puede enviar el nodo al cliente
     *                              Si existe otro cliente registrado
     *                              Si el servidor de registro no est谩 activo
     *
     * @see nucleo.nodo.Nodo
     * @see ClienteNodo
     */
    public synchronized void registrarCliente(ClienteNodo clienteNodo) 
            throws RemoteException, RegistroException {

        RegistroException rge = null;

        if ( this.clienteRegistrado.isEmpty()) {
    
            Nodo nodoRemoto = null;

            try {

                nodoRemoto = new Nodo();
                nodoRemoto.registrarNodo(this.hostLocal, this.puertoLocal);
                this.clienteRegistrado.put(clienteNodo, nodoRemoto);
                
            }catch (NodoException ne) {

                rge = new RegistroException(
                        "Could not set up the remote node",
                        ne.getCause());

            }catch (RemoteException re) {
                
                rge = new RegistroException(
                        "Could not export the remote node to RMI registry",
                        re);
                
            }finally {
                if (rge !=null) {
                    this.registroLogger.log(Level.FINER, "Failed to register the client", rge.getCause());
                    throw rge;
                }
            }

            try{

                clienteNodo.poseerNodoRemoto((NodoRemoto) nodoRemoto);
                this.registroLogger.finer("Client registered:"+ RemoteServer.getClientHost());
                this.comprobadorActividad = new ComprobadorActividad(this, this.tiempo_expiracion_ms);
                this.comprobadorActividad.start();
                this.registroLogger.finer("Activity checker started");
                
            }catch(RemoteException re) {

                
                rge = new RegistroException(
                        "Failed to get the remote node",
                        re);
                this.registroLogger.log(Level.FINER, "Error while sending the remote node to client", re);
                throw rge;

            }catch(ServerNotActiveException snae) {

                rge = new RegistroException(
                        "Register server not started",
                        snae);
                this.registroLogger.log(Level.FINER, "Register server not started", snae);

            }finally{

                if(rge !=null) {
                    this.eliminarCliente();
                    throw rge;
                }

            }
            
        }else if ( !this.clienteRegistrado.contains(clienteNodo) ){
            
            rge = new RegistroException(
                        "Could not get the remote node",
                        new Throwable("Already there is an active client")
                        );
            this.registroLogger.log(Level.FINER, "Failed to register the client", rge);
            throw rge;

        }
    }


    /**
     * Desconecta al cliente del servidor de registro, dej谩ndolo libre para otros clientes.
     * El nodo asociado tambi茅n se destruye.
     *
     * @param clienteNodo   El cliente que pide ser desconectado
     *
     * @throws RegistroException    Si el cliente que inicia la petici贸n es distinto al registrado
     *                              Si el servidor de registro no est谩 activo
     *
     * @see ClienteNodo
     * @see #eliminarCliente()
     */
    public synchronized void desconectarCliente(ClienteNodo clienteNodo)
            throws RemoteException, RegistroException {
     
        if(this.clienteRegistrado.containsKey(clienteNodo)) {
            
            this.eliminarCliente();
            clienteNodo.liberarNodoRemoto();
            try{

                this.registroLogger.finer("Client disconnected: " + RemoteServer.getClientHost());
                
            }catch(ServerNotActiveException snae) {

                RegistroException re = new RegistroException(
                        "Register server not started",
                        snae);
                this.registroLogger.log(Level.FINER, "Register server not started", re);
            }
       
        }else {

            RegistroException re = new RegistroException(
                        "Could not possible disconnect",
                        new Throwable("Client different from registered")
                        );
            this.registroLogger.log(Level.FINER, "Disconnection failed",re);
        }
    }



    /**
     * Se notifica al servidor de actividad por parte del cliente registrado, y
     * se reinicia el temporizador.
     *
     * @param clienteNodo           El cliente que notifica
     *
     * @throws RegistroException    Si el cliente que inicia la petici贸n es distinto al registrado
     *                              Si el servidor de registro no est谩 activo
     *
     * @see ClienteNodo
     * @see ComprobadorActividad
     *
     */
     public synchronized void notificarActividad(ClienteNodo clienteNodo)
             throws RemoteException, RegistroException {

         if(this.clienteRegistrado.containsKey(clienteNodo)) {

             this.comprobadorActividad.interrupt();
             try{

                this.registroLogger.finer("Register server notified by: " + RemoteServer.getClientHost() );
                
             }catch(ServerNotActiveException snae) {

                RegistroException re = new RegistroException(
                        "Register server not started",
                        snae);
                this.registroLogger.log(Level.FINER, "Register server not started", re);
            }

         }else {

             throw new RegistroException(
                        "Could not possible notify to server",
                        new Throwable("Client different from registered")
                        );
         }
     }


     /**
      * Destruye el nodo asociado el cliente y deja libre la asociaci贸n cliente - nodo
      * 
      */
     public void eliminarCliente()  {

         Nodo nodoRemoto = this.getNodoRemoto();
         try{

            nodoRemoto.getProtocoloTimestamps().cerrarSocket();
            this.registroLogger.finer("Remote socket closed");

         }catch (SocketException se) {
             
             //el nodo se encontraba en proceso de generaci贸n de timestamps
              this.registroLogger.log(Level.FINER, "Remote socket closed", se);
         }
         
         try{

            nodoRemoto.desconectarNodo();
            this.registroLogger.finer("Remote node unexported from RMI registry");
            
         }catch (NodoException ne) {

             this.registroLogger.log(Level.FINER, ne.getMessage(), ne.getCause());

         }catch (RemoteException re) {

             this.registroLogger.log(Level.FINER,"Error while unexporting node from RMI Registry", re);
             
         }finally {

            //this.comprobadorActividad.detenerComprobacion();
            this.clienteRegistrado.remove(this.getClienteNodo());
            nodoRemoto = null;
            System.gc();
            System.runFinalization();
            this.registroLogger.finer("Node finalized");
         }
           
     }


     /**
      * Comprueba si existe un cliente en la asociaci贸n de cliente registrado
      *
      * @return   <code>true</code>     Si aparece un cliente en la asociaci贸n de cliente registrado
      */
     public boolean existeClienteRegistrado() {
         
         return this.clienteRegistrado.keys().hasMoreElements();
     }


     /**
      * Ajusta el periodo de actividad del cliente
      *
      * @param tiempoExpiracion_ms      El tiempo de expiraci贸n en milisegundos
      */
     public void setTiempoExpiracion(int tiempoExpiracion_ms) {

        this.tiempo_expiracion_ms = tiempoExpiracion_ms;
        this.registroLogger.finer("Expiration time set to " + tiempoExpiracion_ms + " ms");
    }

     /**
      * Devuelve el nodo asociado al cliente registrado
      *
      * @return     El nodo asociado al cliente registrado
      *
      * @see Nodo
      */
     public Nodo getNodoRemoto() {

         return this.clienteRegistrado.elements().nextElement();
     }


     /**
      * Devuelve el cliente registrado
      *
      * @return el cliente registrado
      *
      * @see ClienteNodo
      */
     public ClienteNodo getClienteNodo() {

         return this.clienteRegistrado.keys().nextElement();
     }


    /**
     * Devuelve la referencia remota al servidor de registro.
     *
     * @param hostLocal        Nombre de host del servidor de registro
     * @param puertoLocal       Puerto por donde atiende las peticiones el servidor de registro
     *
     * @throws RegistroException    Si las direcciones de red forman una URL no v谩lida
     *                              Si no esta registrado en el registro RMI
     * @throws RemoteException      Si ocurre un error en la operaci贸n remota
     *
     * @return la referencia remota al servidor de registro
     */
    public static ServidorRegistro obtenerServidorRegistro(
            String hostLocal ,
            int puertoLocal
            )throws RegistroException, RemoteException {

        ServidorRegistro servidorRegistro = null;
        String url = "rmi://"
                    + hostLocal
                    + ":"
                    + puertoLocal
                    +  "//"+ "servidorRegistro";

        RegistroException re = null;
        try{
            servidorRegistro = (ServidorRegistro) Naming.lookup(url);

        }catch(NotBoundException nbe) {

            re = new RegistroException("Register server not bound to: " + url, nbe);

        }catch(MalformedURLException murle) {

            re = new RegistroException("Malformed URL:" + url, murle);

        }finally{
            if (re != null) throw re;
        }

        return servidorRegistro;

    }

    /**
     * Activa el servidor de registro. Registra el objeto en el registro RMI
     *
     * @throws RegistroException    Si las direcciones de red forman una URL no v谩lida
     * @throws RemoteException      Si ocurre un error en la operaci贸n remota
     */
    public void registrarServidor(String hostLocal, int puertoLocal)
            throws RemoteException, RegistroException {


        String url = "rmi://"
                    + hostLocal
                    + ":"
                    + puertoLocal
                    +  "//"+ "servidorRegistro";

        RegistroException re = null;
        UnicastRemoteObject.exportObject(this);
        try {

            Naming.rebind(url, this);
            this.registroLogger.finer("Register server bount to: " + url);
            this.hostLocal = hostLocal;
            this.puertoLocal = puertoLocal;
            
        }catch (MalformedURLException murle) {

            String mensaje = "Malformed URL:" + url;
            UnicastRemoteObject.unexportObject(this, true);
            this.registroLogger.log(Level.FINER, mensaje , murle);
            throw new RegistroException(mensaje, murle);

        }

    }


    /**
     * Descativa el servidor de registro. Elimina el objeto del registro RMI
     *
     * @throws RegistroException    Si las direcciones de red forman una URL no v谩lida
     *                              Si el servidor no se encuentra activado
     * @throws RemoteException      Si ocurre un error en la operaci贸n remota
     */
    public void desconectarServidor()
            throws RegistroException, RemoteException {

        RegistroException re = null;
        String mensajeError;

        if(this.existeClienteRegistrado())
            this.eliminarCliente();
        
        String url = "rmi://"
                + this.hostLocal
                + ":"
                + this.puertoLocal
                +  "//"
                + "servidorRegistro";
        try{
            
            Naming.unbind(url);
            this.registroLogger.finer("Register server unbound from: " + url);
            
        }catch (NotBoundException nbe) {

            mensajeError = "Register server not bount to: " + url;
            this.registroLogger.log(Level.FINER, mensajeError, nbe);
            re = new RegistroException(mensajeError, nbe);
            throw re;
            
        }catch (MalformedURLException murle) {

            mensajeError = "Malformed URL:" + url;
            this.registroLogger.log(Level.FINER, mensajeError, murle);
            re = new RegistroException(mensajeError, murle);
            throw re;
        }

        UnicastRemoteObject.unexportObject(this, true);
        this.hostLocal = null;
        this.puertoLocal = -1;

    }


    /**
     * Comprueba si el servidor est谩 activo/exportado en el registro RMI
     *
     * @throws RegistroException    Si las direcciones de red forman una URL no v谩lida
     * @throws RemoteException      Si ocurre un error en la operaci贸n remota
     *
     */
    public boolean servidorRegistrado()
            throws RegistroException,RemoteException {

        boolean registrado = false;
        String url ="rmi://" + this.hostLocal + ":" + this.puertoLocal +  "//" + "servidorRegistro";

        try {
            if(Naming.list(url)!=null ) {

                registrado = true;

            }
        }catch (MalformedURLException murle) {

            throw new RegistroException("Malformed URL:" + url, murle);
        }

        return registrado;
    }


    
}

