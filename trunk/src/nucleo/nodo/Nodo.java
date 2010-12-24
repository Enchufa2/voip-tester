package nucleo.nodo;

import nucleo.*;
import qos.protocolos.timestamps.ProtocoloTimestamps;
import qos.protocolos.sincronizacion.ProtocoloSincronizacion;
import qos.protocolos.Mensaje;
import qos.protocolos.LongitudInvalidaException;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.protocolos.GestorDatosMensaje;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.net.BindException;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.net.InetSocketAddress;
import java.net.ConnectException;
import java.net.SocketTimeoutException;


/**
 * @author Antonio
 * @version 1.0
 * @created 28-jul-2010 11:59:47
 */
public class Nodo implements NodoRemoto {


    /**
     * Parametros de transmisión que utiliza el nodo para el intercambio de datos
     * en el protocolo de timestamps
     *
     * @see ParametrosTransmision
     * @see ProtocoloTimestamps
     */
    private ParametrosTransmision parametrosTransmision;


    /**
     * Objeto que proporciona el servicio de protocolo de timestamps
     *
     * @see ProtocoloTimestamps
     */
    private ProtocoloTimestamps protocoloTimestamps;


    /**
     * Objeto que proporciona el servicio de protocolo de sincronización
     *
     * @see ProtocoloSincronizacion
     */
    private ProtocoloSincronizacion protocoloSincronizacion;


    /**
     * Nombre o dirección del nodo local al que se une el registro RMI
     */
    private String hostLocal;


    /**
     * Puerto del nodo local al que se une el registro RMI
     */
    private int puertoLocal=-1;

    /**
     * Nombre del nodo en el registro RMI
     *
     */
    public String nombreNodo="nodoRemoto";


    /**
     * Crea el nodo, con el nombre por defecto y sin registrarse
     *
     * @throws NodoException   Si ocurre un error de socket al iniciar  el objeto
     *                         que proporciona el servicio de protocolo de timestamps
     * 
     */
    public Nodo()
            throws NodoException {

        try {

            this.protocoloTimestamps = new ProtocoloTimestamps();

        }catch(SocketException se) {
            
            throw new NodoException("Error while setting up the timestamp protocol socket",se);
            
        }

        this.protocoloSincronizacion = new ProtocoloSincronizacion(null);       
    }


    /* Implementación de métodos remotos */    

    /**
     * Fija los parámetros de transmisión que acepta el nodo para enviar o recibir mensajes
     *
     * @param pT    Los valores de los parámetros de transmisión
     *
     * @throws RemoteException      Si ocurre un error en la llamada al método remoto
     * @see ParametrosTransmision
     */
    public synchronized void setParametrosTransmision( ParametrosTransmision pT )
            throws RemoteException {

        this.parametrosTransmision = pT;
    }


    /**
     * El nodo debe disponer de un procedimiento de sincronización, basado en petición y respuesta.
     * Este método llama al objeto para que cree un proceso de espera de petición de sincronización.
     *
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @throws NodoSocketException          Si transcurre el tiempo máximo de espera para la petición de sincronización
     * @throws NodoIOException              Si ocurre un error de E/S en el flujo de datos del socket
     *
     * @see ProtocoloSincronizacion
     */
    public synchronized void esperarPeticionSincronizacion()
            throws RemoteException, NodoSocketException, NodoIOException {

        this.protocoloSincronizacion.setDatagramSocket(
                this.protocoloTimestamps.getDatagramSocket()
                );
        try{

            this.protocoloSincronizacion.esperarPeticionSincronizacion();

        }catch(SocketTimeoutException ste) {

            throw new NodoSocketException("Timer expired while waiting the synchronization request", ste);

        }catch (IOException ioe) {

            throw new NodoIOException("I/O error in socket operation",ioe);
        }

    }

    /**
     * El nodo utiliza un socket para el intercambio de mensajes en el protocolo de timestamps.
     * Este socket está unido a una dirección de red y puerto locales concretos (dirección de socket).
     * Este método devuelve la dirección del socket local a la que está unido el socket del nodo
     *
     * @return      La dirección del socket local a la que está unido el socket del nodo
     *
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @see InetSocketAddress
     * @see ProtocoloTimestamps
     *
     */
    public InetSocketAddress direccionLocalSocketProtoTimestamps()
            throws RemoteException {
       
        return this.protocoloTimestamps.direccionesLocales();
    }

    /**
     * El socket del nodo está conectado con un socket remoto, con el que intercambia mensajes en
     * el protocolo de timestamps. Este método devuelve la dirección del socket remoto al que está
     * conectado el socket local
     *
     * @return      La dirección del socket remoto al que está conectado el socket local
     *
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @see InetSocketAddress
     * @see ProtocoloTimestamps
     */
     public InetSocketAddress direccionRemotaSocketProtoTimestamps()
             throws RemoteException {

         return this.protocoloTimestamps.direccionesRemotas();
     }


    /**
     * Ajusta la dirección del socket local del protocolo de timestamps, especificando
     * el host y puerto local a los que se unirá el socket.
     *
     * @param hostLocal         Nombre de host o dirección del host local a la que se une el socket
     * @param puertoLocal       Puerto local al que se une el socket
     *
     * @throws RemoteException          Si ocurre un error en la llamada al método remoto
     * @throws NodoSocketException      Si no es posible unir el socket local a la dirección solicitada
     *                                  o si ocurre un error en el protocolo subyacente del socket
     * @throws NodoIOException          Si no se puede determinar la dirección del host local
     * @see ProtocoloTimestamps
     */
    public synchronized void configurarSocketProtoTimestamps(String hostLocal, int puertoLocal)
            throws RemoteException, NodoSocketException, NodoIOException {

        this.protocoloTimestamps.desconectar();
        try{

            this.protocoloTimestamps.cambiarDireccionesLocales(
                    hostLocal ,
                    puertoLocal
                    );

        }catch(BindException be) {

            throw new NodoSocketException(
                    "Unable to bind the socket to: " + hostLocal + ":" + puertoLocal,
                    be
                    );

        }catch (SocketException se){

            throw new NodoSocketException ("Operation socket failed", se);

        }catch (UnknownHostException uhe) {

            throw new NodoIOException (
                    "Unable to know the host address:" + hostLocal,
                    uhe
                    );

        }

    }

    /**
     * Conecta el socket local del protocolo de timestamps del
     * nodo con un socket remoto de direccion <code>isa</code>.
     *
     * @param isa   La dirección del socket remoto
     *
     * @throws RemoteException          Si ocurre un error en la llamada al método remoto
     * @throws NodoSocketException      Si ocurre un error en el procedimiento de conexión de los sockets
     *                                  o si ocurre un error en el protocolo subyacente del socket
     *
     * @see InetSocketAddress
     * @see ProtocoloTimestamps
     */
    public synchronized void conectarSocketProtocoloTimestamps(InetSocketAddress isa)
            throws RemoteException, NodoSocketException {

        if(isa == null) {

            throw new NodoSocketException("Invalid socket address:" + isa, null);
        }

        try{

            this.protocoloTimestamps.conectarA(isa);

        }catch (ConnectException ce) {

            throw new NodoSocketException ("Failed to connect to: " + isa, ce);

        }catch (SocketException se) {

            throw new NodoSocketException ("Operation socket failed", se);

        }

    }

    /**
     * Desconecta el socket local del nodo del socket remoto con el que esté conectado
     *
     * @throws RemoteException          Si ocurre un error en la llamada al método remoto
     * @see ProtocoloTimestamps
     */
    public void desconectarSocketPrototocoloTimestamps()
            throws RemoteException {

        this.protocoloTimestamps.desconectar();
    }


    /**
     * Cierra el socket que se utiliza en el protocolo de timestmaps
     *
     * @throws NodoSocketException          Si se cierra el socket mientras este se encuentra
     *                                      en espera de paquetes.
     *
     * @see ProtocoloTimestamps
     */
    public void cerrarSocketProtocoloTimestamps()
            throws RemoteException, NodoSocketException {

        try{

            this.protocoloTimestamps.cerrarSocket();
            
        }catch (SocketException se) {

            throw new NodoSocketException("Socket was closed while receiving packets", se);
        }

    }

    /**
     * Crea el buffer de mensajes, según los parámetros de transmisión, necesario para el envío
     * de mensajes en el protocolo de timestamps.
     *
     * @throws RemoteException                 Si ocurre un error en la llamada al método remoto
     * @throws LongitudInvalidaException       Si la longitud de los mensajes es un número no válido
     *
     * @see ProtocoloTimestamps
     */
    public synchronized void peticionCrearMensajesParaEnviar()
            throws LongitudInvalidaException, RemoteException {

        this.protocoloTimestamps.crearMensajesParaEnviar(
                this.parametrosTransmision.numeroDeMensajes(),
                this.parametrosTransmision.getLongitudDatos()
                );

    }

    /**
     * Crea el buffer de mensajes, según los parámetros de transmisión, necesario para la recepción
     * de mensajes en el protocolo de timestamps.
     *
     * @throws RemoteException          Si ocurre un error en la llamada al método remoto
     *
     * @see ProtocoloTimestamps
     *
     */
    public synchronized void peticionCrearMensajesParaRecibir() throws RemoteException{

        this.protocoloTimestamps.crearMensajesParaRecibir(
                this.parametrosTransmision.numeroDeMensajes()
                );
        
    }

    /**
     * Llama al objeto para que realice una petición de generar timestamps de emisor en el protocolo
     * de timestamps.
     *
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @throws NodoIOException              Si ocurre un error de E/S en el flujo de datos del socket
     *
     * @see ProtocoloTimestamps
     */
    public synchronized long[] peticionTimestampsEmisor()
            throws RemoteException, NodoIOException {

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        long[] timestampsE = null;
        try {

            timestampsE = this.protocoloTimestamps.generarTimestampsEmisor(
                this.parametrosTransmision.getTiempoEntreEnvios()
                );

        }catch(IOException ioe) {

            throw new NodoIOException("I/O error while sending packets", ioe);
        }

        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        return timestampsE;

    }


    /**
     * Llama al objeto para que realice una petición de generar timestamps de receptor en el protocolo
     * de timestamps.
     *
     * @param rtt_us                        El tiempo de ida y vuelta en microsegundos al nodo remoto.
     *                                      Es un parámetro para el protocolo de timestamps.
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @throws NodoSocketException          si ocurre un error en el protocolo subyacente del socket
     * @throws NodoIOException              Si ocurre un error de E/S en el flujo de datos del socket
     *
     * @see ProtocoloTimestamps
     */
    public synchronized long[] peticionTimestampsReceptor(int rtt_us)
            throws RemoteException,NodoSocketException, NodoIOException {

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        long[] timestampsR;
        try{

            timestampsR = this.protocoloTimestamps.generarTimestampsReceptor (
                rtt_us,
                this.parametrosTransmision.getTiempoEntreEnvios(),
                this.parametrosTransmision.getLongitudDatos()
                );

        }catch(SocketException se) {

            throw new NodoSocketException ("Error while receiving packets", se);

        }catch(IOException ioe) {

            throw new NodoIOException("I/O error while receiving packets", ioe);
        }

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        return timestampsR;

    }

    

    /**
     * Rellena la carga útil de los mensajes con los bytes de audio especificados.
     * Los bytes de audio deben de tener una duracion igual a un intervalo, ya que
     * el contenido de audio en cada intervalo es el mismo.
     *
     * @param bytesAudioIntervalo bytes de audio correspondiente a un intervalo
     *                            que se introducen en el campo de datos de los mensajes
     *
     * @throws RemoteException      Si ocurre un error en la operación remota
     */
    public synchronized void mapearAudioEnMensajes(byte[] bytesAudioIntervalo)
            throws RemoteException{

        GestorDatosMensaje gestorDatosMensaje = new GestorDatosMensaje(
                this.protocoloTimestamps.getMensajes()
                );
        int numeroIntervalos = this.parametrosTransmision.getNumeroIntervalos();
        int mensajesPorIntervalo = this.parametrosTransmision.getMensajesPorIntervalo();
        for (int i=0; i<numeroIntervalos; i++) {
            gestorDatosMensaje.deByteArrayAmensajes(
                    bytesAudioIntervalo, 
                    0,
                    bytesAudioIntervalo.length,
                    i*mensajesPorIntervalo
                    );
        }
        
    }

   
    /**
     * Obtiene la carga útil de los mensajes. Se supone que los mensajes transportan
     * audio por lo que los bytes de datos se trataran como tal.
     *
     * @return Los bytes de datos (audio) que transportan los mensajes. Los bytes
     *         se añaden al array devuelto de manera ordenada, según el identificador
     *         de cabecera del mensaje. Cada fila del array se corresponde con un intervalo
     *
     * @throws RemoteException      Si ocurre un error en la operación remota
     */
    public synchronized byte[][] obtenerAudioDeMensajes()
            throws RemoteException{

        this.protocoloTimestamps.restaurarPaquetesPerdidos(
                this.parametrosTransmision.getLongitudDatos()
                );
       
        GestorDatosMensaje gestorDatosMensaje = new GestorDatosMensaje(
                this.protocoloTimestamps.getMensajes()
                );
        int numeroIntervalos = this.parametrosTransmision.getNumeroIntervalos();
        int mensajesPorIntervalo = this.parametrosTransmision.getMensajesPorIntervalo();
        int longitudDatosMensaje = this.parametrosTransmision.getLongitudDatos();
        byte[][] bytesAudioRecibido =
                new byte[numeroIntervalos][mensajesPorIntervalo*longitudDatosMensaje];

        for (int i=0; i<numeroIntervalos; i++) {

            gestorDatosMensaje.deMensajesAbyteArray(
                    bytesAudioRecibido[i],
                    mensajesPorIntervalo,
                    i*mensajesPorIntervalo
                    );
        }

        return bytesAudioRecibido;


    }



    /*** Métodos propios no remotos ***/



    /**
     * Registra el nodo en el registro RMI (permite invocarle llamadas remotas) con el nombre
     * del nodo que posea el objeto
     *
     * @param hostLocal     El nombre o dirección del host local donde se encuentra el registro RMI
     * @param puertoLocal   El puerto local donde atiende las peticiones el registro
     *
     * @throws RemoteException              Si ocurre un error en la operación con el elemento remoto
     * @throws MalformedURLException        Si las direcciones de red forman una dirección URL incorrecta
     *
     */
    public void registrarNodo(String hostLocal, int puertoLocal)
            throws  NodoIOException, RemoteException {
  
        String url = "rmi://" + hostLocal + ":" + puertoLocal + "//" + this.nombreNodo;
        try {

            UnicastRemoteObject.exportObject(this);
            Naming.rebind(url, this);
            this.hostLocal = hostLocal;
            this.puertoLocal = puertoLocal;

        }catch(MalformedURLException murle) {

            UnicastRemoteObject.unexportObject(this, true);
            throw new NodoIOException("Malformed URL: " + url, murle);
        }

    }


    /**
     * Elmina la asociación del nodo con el nombr que posea el objetodel registro RMI
     *
     *
     * @throws RemoteException              Si ocurre un error en la operación con el elemento remoto
     * @throws NotBoundException            Si el objeto no está registrado/asociado
     * @throws MalformedURLException        Si las direcciones de red forman una dirección URL incorrecta
     *
     */
    public void desconectarNodo()
            throws RemoteException, NodoIOException, NodoException {

        String url = "rmi://" + this.hostLocal + ":" + this.puertoLocal + "//" + this.nombreNodo;
        try{

            Naming.unbind(url);
            UnicastRemoteObject.unexportObject(this, true);
            this.hostLocal = null;
            this.puertoLocal = -1;
            
        }catch(MalformedURLException murle) {

            throw new NodoIOException("Malformed URL: " + url, murle);

        }catch (NotBoundException nbe) {

            throw new NodoException("There is not a registered node bound to: " + url, nbe);
        }
        
    }        

    /**
     * El nodo debe disponer de un procedimiento de sincronización, basado en petición y respuesta.
     * Este método llama al objeto para que cree un proceso de envío de petición de sincronización.
     *
     * @throws SocketTimeoutException       Si transcurre el tiempo máximo de espera para la respuesta
     *                                      a la petición de sincronización
     * @throws IOException                  Si ocurre un error de E/S
     *
     * @see ProtocoloSincronizacion
     */
    public long[] peticionTimestampsSincronizacion()
            throws NodoSocketException, NodoIOException{

        long[] timestampsSinc;
        this.protocoloSincronizacion.setDatagramSocket(
                this.protocoloTimestamps.getDatagramSocket()
                );
        try{

            timestampsSinc = this.protocoloSincronizacion.obtenerTimestampsSincronizacion();

        }catch(SocketTimeoutException ste) {

            throw new NodoSocketException("Timer expired while waiting server response", ste);
        
        }catch (IOException ioe) {
            
            throw new NodoIOException("I/O error operation in socket", ioe);
            
        }

        return timestampsSinc;
    }

   
    
    /**
     * Devuelve el objeto que realiza el procedimiento del protocolo de timestamps
     *
     * @return       el objeto que realiza el procedimiento del protocolo de timestamps
     * @see ProtocoloTimestamps
     *
     */
    public ProtocoloTimestamps getProtocoloTimestamps() {

        return this.protocoloTimestamps;
    }

    /**
     * Devuelve los parámetros de transmisión que utiliza el nodo para el intercambio
     * de mensajes en el protocolo de timestamps
     *
     * @return      Los parámetros de transmisión que utiliza el nodo epara el intercambio
     *              de mensajes en el protocolo de timestamps
     *
     * @see ParametrosTransmision
     * @see ProtocoloTimestamps
     */
    public ParametrosTransmision getParametrosTransmision() {

        return this.parametrosTransmision;
    }


    /**
     *  Devuelve el buffer de mensajes disponible para el protocolo de timestamps
     *
     * @return el buffer de mensajes disponible para el protocolo de timestamps
     * 
     * @see ProtocoloTimestamps
     */
    public Mensaje[] getMensajes() {

        return this.protocoloTimestamps.getMensajes();
    }
   

    /**
     * Asigna un nombre al nodo, que se utiliza para crear la asociación
     * en el registro RMI
     *
     * @param nombreNodo    El nombre del nodo que utiliza en la asociciación en el registro RMI
     */
    public void setNombreNodo(String nombreNodo) {
        this.nombreNodo = nombreNodo;
    }

    /**
     * Devuelve el nombre del nodo, que se utiliza para crear la asociación
     * en el registro RMI
     *
     * @return    El nombre del nodo que utiliza en la asociciación en el registro RMI
     */
    public String getNombreNodo() {

        return this.nombreNodo;
    }
    
}
