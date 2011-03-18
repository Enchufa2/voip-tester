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


package nucleo.cliente;

import nucleo.registro.ClienteNodo;
import nucleo.registro.ServidorRegistro;
import nucleo.registro.RegistroException;
import nucleo.registro.ServidorRegistroImpl;
import nucleo.registro.ClienteNodoImpl;
import java.rmi.RemoteException;
import java.rmi.NoSuchObjectException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author Antonio
 */
public class ControlCliente {


    /**
     * Objeto remoto (que accede el cliente) que representa el servidor de registro
     *
     * @see ServidorRegistro
     */
    public ServidorRegistro servidorRegistro;


    /**
     * Objeto remoto (que accede el servidor) que represente el cliente registrado
     *
     * @see ClienteNodo
     */
    private ClienteNodoImpl clienteNodo;


    /**
     * Elemento que controla las operaciones del nodo local y del nodo remoto
     *
     * @see ControlNodos
     */
    private ControlNodos controlNodos;


    /**
     * clienteLogger
     *
     * @see Logger
     */
    Logger clienteLogger;

    /**
     * Crea el objeto a partir del controlador de nodos
     *
     * @param controlNodos      Controlador de las operaciones de los nodos local y remoto
     *
     * @see ControlNodos
     */
    public ControlCliente(ControlNodos controlNodos){

        this.controlNodos = controlNodos;
        this.clienteLogger = Logger.getLogger("nucleo.cliente");
        System.setProperty("java.rmi.server.disableHttp","true");
    }


    /**
     * Operaci贸n de conexi贸n con el servidor. Primero accede al registro RMI remoto
     * para obtener la referencia remota del servidor de registro. Despu茅s inicia el proceso
     * de registro del sistema.
     *
     * @param hostRemoto    Direcci贸n o nombre de host remoto
     * @param puertoRemoto  Puerto remoto
     *
     * @throws ControlClienteException      Si ocurre un fallo en cualquiera de las operaciones
     */
    public void conectarConServidor(String hostRemoto, int puertoRemoto) 
            throws ControlClienteException{

        String mensaje = "";

        try{

            this.finalizarCliente();
            this.clienteNodo = new ClienteNodoImpl(this.controlNodos);
            this.clienteLogger.log(Level.FINER, "Client started");

        }catch(RemoteException re) {

            mensaje = "error while starting the cliente service";
            this.clienteLogger.log(Level.FINER, mensaje, re);
            throw new ControlClienteException(mensaje, re);
        }

        try{

            this.servidorRegistro = ServidorRegistroImpl.obtenerServidorRegistro(hostRemoto, puertoRemoto);
            this.clienteLogger.log(Level.FINER, "Register server available");

        }catch(RemoteException re) {

            mensaje ="Connection remote operation failed";
            this.clienteLogger.log(Level.FINER, mensaje, re);
            throw new ControlClienteException(mensaje, re);

        }catch (RegistroException rge) {

            mensaje ="Register server connection failed";
            this.clienteLogger.log(Level.FINER, mensaje, rge);
            throw new ControlClienteException(
                    mensaje,
                    new Throwable(rge.getMessage() + ": " + rge.getCause().getMessage())
                    );
        }

        try{

            this.servidorRegistro.registrarCliente(this.clienteNodo);
            this.clienteLogger.log(Level.FINER, "Register process completed successfully");

        }catch(RemoteException re) {

            mensaje ="Register remote operation failed";
            this.clienteLogger.log(Level.FINER, mensaje, re);
            throw new ControlClienteException(mensaje, re);

        }catch (RegistroException rge) {

            mensaje ="Register process failed";
            this.clienteLogger.log(Level.FINER, mensaje, rge);
            throw new ControlClienteException(
                    mensaje,
                    new Throwable(rge.getMessage() + ": " + rge.getCause().getMessage())
                    );
        }
        
    }


    /**
     * Operaci贸n de desconexi贸n del servidor. Primero se cerra la sesi贸n en el registro y a
     * continuaci贸n anula la referencia remota.
     *
     * @throws ControlClienteException      Si ocurre un fallo en cualquiera de las operaciones
     *
     */
    public void desconectarConServidor() 
            throws ControlClienteException {

        String mensaje="";
        
            try {

                this.servidorRegistro.desconectarCliente(this.clienteNodo);
                this.clienteLogger.log(Level.FINER, "Session closed at register server");

            } catch (RemoteException re) {

                mensaje ="Disconnection remote operation failed. Disconnection forced";
                this.clienteLogger.log(Level.FINER, mensaje, re);
                throw new ControlClienteException(mensaje, re);

            } catch (RegistroException rge) {

                mensaje ="Disconnection process failed. Disconnection forced";
                this.clienteLogger.log(Level.FINER, mensaje, rge);
                throw new ControlClienteException(
                    mensaje,
                    new Throwable(rge.getMessage() + ": " + rge.getCause().getMessage())
                    );
                
            }catch (NullPointerException npe) {

                throw new ControlClienteException("Not connected to a register server " , new Throwable());
                
            } finally {

                this.finalizarCliente();
            }
        
    }


    /**
     * Env铆a una notificaci贸n de disponibilidad al servidor.
     * Sirve como comprobaci贸n del estado de la conexi贸n, tanto para el servidor como para el cliente.
     *
     * @throws ControlClienteException      Si ocurre un fallo en cualquiera de las operaciones
     * 
     */
    public void notificarActividadServidor()
            throws ControlClienteException {

        String mensaje ="";

            try {

                this.servidorRegistro.notificarActividad(this.clienteNodo);
                this.clienteLogger.log(Level.FINER, "Notification sent to register server");

            } catch (RemoteException re) {

                mensaje ="Notification remote operation failed";
                this.clienteLogger.log(Level.FINER, mensaje, re);
                throw new ControlClienteException(mensaje, re);

            } catch (RegistroException rge) {

                mensaje ="Notification process failed";
                this.clienteLogger.log(Level.FINER, mensaje, rge);
                throw new ControlClienteException(
                    mensaje,
                    new Throwable(rge.getMessage() + ": " + rge.getCause().getMessage())
                    );

            } catch (NullPointerException npe) {

                throw new ControlClienteException(
                        " Not connected to a server" ,
                        new Throwable("There is not a reference to server")
                        );
            }
    }


    /**
     * Elimina el cliente que accede al servidor de registro
     *
     */
    private void finalizarCliente() {
        try {

            UnicastRemoteObject.unexportObject(this.clienteNodo, true);

        } catch (NoSuchObjectException nsoe) {
        }
        this.servidorRegistro = null;
        this.clienteNodo = null;
        System.runFinalization();
        System.gc();
        this.clienteLogger.log(Level.FINER, "Cliente finalized");
    }


    /**
     * 
     */
    public ControlNodos getControlNodos() {

        return this.controlNodos;
    }
    
}

