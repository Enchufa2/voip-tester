/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nucleo.cliente;

import nucleo.nodo.NodoRemoto;
import nucleo.nodo.NodoException;
import nucleo.nodo.Nodo;
import nucleo.*;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.protocolos.sincronizacion.ParametrosSincronizacion;
import qos.protocolos.sincronizacion.EstimadorParametrosSincronizacion;
import qos.protocolos.sincronizacion.ProtocoloSincronizacion;
import qos.protocolos.timestamps.ProtocoloTimestamps;
import qos.protocolos.LongitudInvalidaException;
import qos.estimacionQoS.MedidaQoS;
import qos.estimacionQoS.EstimadorQoS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.rmi.RemoteException;
import java.net.InetSocketAddress;


/**
 *
 * @author Antonio
 */
public class ControlNodos {



    /**
     * Nodo local del cliente
     *
     * @see Nodo
     */
    Nodo nodo;

    /**
     * Nodo remoto sobre el que cliente realiza peticiones
     *
     * @see NodoRemoto
     */
    NodoRemoto nodoRemoto;

    /**
     * Timestamps de emisor generados por petición en el protocolo de tiemstamps
     *
     * @see ProtocoloTimestamps
     * @see #obtenerTimestampsEnlaceAscendente(int)
     * @see #obtenerTimestampsEnlaceDescendente(int)
     */
    private long[] timestampsEmisor;

    /**
     * Timestamps de recepción generados por petición en el protocolo de tiemstamps
     * @see ProtocoloTimestamps
     * @see #obtenerTimestampsEnlaceAscendente(int)
     * @see #obtenerTimestampsEnlaceDescendente(int)
     */
    private long[] timestampsReceptor;


    /**
     * Timestamps de sincronización generados por petición en el protocolo de sincronizacion
     *
     * @see ProtocoloSincronizacion
     * @see #sincronizarConNodoRemoto() 
     */
    private long[] timestampsSincronizacion;


    /**
     * Elemento de registro de oeraciones del programa
     */
    private Logger controlLogger;



    /**
     * Inicializa el nodo local, con el nombre por defecto y sin registrarse
     *
     * @throws NodoClienteException  Si no es posible crear el nodo local
     *                          
     *
     * @see Nodo
     */
    public ControlNodos(File directorioLogs)
            throws NodoClienteException {

        this.controlLogger = Logger.getLogger("nucleo.cliente");
        try{

            FileHandler fh = new FileHandler (new File(directorioLogs, "operacionesNodos.log").getPath());
            fh.setFormatter(new SimpleFormatter());
            this.controlLogger.addHandler(fh);

        }catch(IOException ioe) {

            System.out.println("Error while creating log file");
            ioe.printStackTrace();
        }
        //fh.setLevel(Level);
        
        this.controlLogger.setLevel(Level.FINER);

        new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodo = new Nodo();
                return null;

            }
        }.procesarOperacion(new Object[]{}, "set up local node");


    }

    
    /**
     * Conecta los sockets del protocolo de timestamps de los nodos local y remoto
     * 
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (loca)
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     *
     *
     */
    public void conectarSockets()
            throws NodoClienteException, NodoServidorException {

        this.controlLogger.entering("ControlNodos", "connectSockets");

        InetSocketAddress isaRemota=null, isaLocal=null;
        
        isaRemota  = (InetSocketAddress) new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException {

                return ControlNodos.this.nodoRemoto.direccionLocalSocketProtoTimestamps();

            }
        }.procesarOperacion(null, "get server local socket address");
        

        new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodo.conectarSocketProtocoloTimestamps(
                        (InetSocketAddress) argsOperacion[0]
                        );
                return null;

            }
        }.procesarOperacion(new Object[]{isaRemota}, "Connect to server socket");
            

       isaLocal  = (InetSocketAddress) new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodo.direccionLocalSocketProtoTimestamps();

            }
        }.procesarOperacion(null, "get client local socket address");

           
        new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodoRemoto.conectarSocketProtocoloTimestamps(
                        (InetSocketAddress) argsOperacion[0]
                        );
                return null;

            }
        }.procesarOperacion(new Object[]{isaLocal}, "Connect to client socket");

         this.controlLogger.exiting("ControlNodos", "connectSockets");

    }


    /**
     * Desconecta los sockets del protocolo de timestamps de los nodos local y remoto
     *
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (loca)
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     *
     *
     */
    public void desconectarSockets()
            throws NodoClienteException, NodoServidorException {

       this.controlLogger.entering("ControlNodos", "disconnectSockets");

        String mensajeOperacion = "Disconnect local socket";

        new NodoClienteHandler() {

            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException {

                ControlNodos.this.nodo.desconectarSocketPrototocoloTimestamps();
                return null;

            }
        }.procesarOperacion(null, mensajeOperacion);


        new NodoServidorHandler() {

            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException {

                ControlNodos.this.nodoRemoto.desconectarSocketPrototocoloTimestamps();
                return null;

            }
        }.procesarOperacion(null, mensajeOperacion);
        
        this.controlLogger.exiting("ControlNodos", "disconnectSockets");
      
    }


    /**
     * Cierra el socket cliente del protocolo de timestamps
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (local)
     */
    public void cerrarSocketNodoLocal()
            throws NodoClienteException{

        this.controlLogger.entering("ControlNodos", "closeLocalSocket");

        new NodoClienteHandler() {

            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException {

                ControlNodos.this.nodo.cerrarSocketProtocoloTimestamps();
                return null;

            }
        }.procesarOperacion(null, "close local socket");

        this.controlLogger.exiting("ControlNodos", "closeLocalSocket");

    }

    /**
     * Cierra el socket servidor del protocolo de timestamps
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     */
    public void cerrarSocketNodoRemoto() throws NodoServidorException{

        this.controlLogger.entering("ControlNodos", "closeRemoteSocket");
        
        new NodoServidorHandler() {

            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException {

                ControlNodos.this.nodoRemoto.cerrarSocketProtocoloTimestamps();
                return null;

            }
        }.procesarOperacion(null, "close local socket");

        this.controlLogger.exiting("ControlNodos", "closeRemoteSocket");
    }


    /**
     * Comprueba si los sockets del protocolo de timestamps de los nodos local y remoto
     * están conectados
     *
     * @return <code>true</code>    Si los sockets están conectados
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (local)
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     */
    public boolean socketsConectados() throws NodoClienteException, NodoServidorException {

        this.controlLogger.entering("ControlNodos", "socketsConnected");

        boolean conectados = false;
        
        InetSocketAddress isaRemoto = (InetSocketAddress) new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodoRemoto.direccionLocalSocketProtoTimestamps();

            }
        }.procesarOperacion(null, "get server local socket address");

        InetSocketAddress isaLocal = (InetSocketAddress) new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodo.direccionLocalSocketProtoTimestamps();

            }
        }.procesarOperacion(null, "get cliente local socket address");
        
        InetSocketAddress isaRemotoConectado =  (InetSocketAddress) new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodoRemoto.direccionRemotaSocketProtoTimestamps();

            }
        }.procesarOperacion(null, "get server remote socket address");


        InetSocketAddress isaLocalConectado = (InetSocketAddress) new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodo.direccionRemotaSocketProtoTimestamps();

            }
        }.procesarOperacion(null, "get cliente local socket address");
        
        
        if (    (isaLocal != null) && (isaRemoto !=null) &&
                (isaLocalConectado !=null) && (isaRemotoConectado!=null)
                )
            conectados = isaLocal.equals(isaRemotoConectado) && isaRemoto.equals(isaLocalConectado);


        this.controlLogger.exiting("ControlNodos", "socketsConnected");
        
        return conectados;
        
     
    }


    /**
     * Ajusta la dirección del socket local del protocolo de timestamps,
     * especificando el host y puerto local a los que se unirá el socket.
     *
     * @param hostLocal         Nombre de host o dirección del host local a la que se une el socket
     * @param puertoLocal       Puerto local al que se une el socket
     *
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (local)
     *
     */
    public void configurarSocketNodoLocal(String hostLocal, int puertoLocal)
            throws NodoClienteException{

        this.controlLogger.entering("ControlNodos", "configureLocalNodeSocket");
        
        new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodo.configurarSocketProtoTimestamps(
                        (String)  argsOperacion[0],
                        (Integer) argsOperacion[1]
                        );
                return null;

            }
        }.procesarOperacion(new Object[]{hostLocal, puertoLocal}, "set up local node socket" );

        this.controlLogger.exiting("ControlNodos", "configureLocalNodeSocket");
        
        
    }

    /**
     * Permite configurar de manera remota la dirección remota del socket
     * del protocolo de timestamp del nodo remoto
     *
     * @param hostRemoto         Nombre de host o dirección del host remoto a la que se une el socket
     * @param puertoRemoto       Puerto remoto al que se une el socket
     *
     * @throws NodoServidorException     Si ocurre un error con en la operación efectuada en el nodo servidor (remoto)
     *
     */
    public void configurarSocketNodoRemoto(String hostRemoto, int puertoRemoto)
           throws NodoServidorException{

       this.controlLogger.entering("ControlNodos", "configureRemoteNodeSocket");
        
       new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodoRemoto.configurarSocketProtoTimestamps(
                        (String)  argsOperacion[0],
                        (Integer) argsOperacion[1]
                        );
                return null;

            }
        }.procesarOperacion(new Object[]{hostRemoto, puertoRemoto}, "set up remote node socket" );

        this.controlLogger.exiting("ControlNodos", "configureRemoteNodeSocket");

       
    }


    /**
     * Realiza el procedimiento de sincronización con el nodo remoto, obteniéndo los timestamps
     * de sincronización.
     * La petición de espera de sincronización del remoto se realiza en una hebra para evitar el
     * bloqueo que produce dicha petición.
     *
     *
     * @throws InterruptedException     Si la hebra que atiende la petición se ve interrumpida
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (local)
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     *
     * @see HebraPeticionSincronizacion
     */
    public void sincronizarConNodoRemoto()
            throws NodoClienteException, NodoServidorException, InterruptedException {

        this.controlLogger.entering("ControlNodos", "synchronizeWhitRemoteNode");
        
        NodoServidorHandler nsh = new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                nodoRemoto.esperarPeticionSincronizacion();
                return null;

            }
        };
        HebraPeticionSincronizacion hebraPeticionSincronizacion =
                new HebraPeticionSincronizacion(nsh);
        hebraPeticionSincronizacion.start();

        this.timestampsSincronizacion = (long[]) new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodo.peticionTimestampsSincronizacion();

            }
        }.procesarOperacion(null, "synchronization timestamps request");
  
        hebraPeticionSincronizacion.join();
        hebraPeticionSincronizacion.comprobarExcepcionSincronizacion();

        this.controlLogger.exiting("ControlNodos", "synchronizeWhitRemoteNode");

        
    }
    
    
    /**
     * Estima los parámetros de sincronización offset local y tiempo de ida y vuelta,
     * respecto al nodo remoto
     *
     * @see EstimadorParametrosSincronizacion
     */
    public ParametrosSincronizacion estimarParametrosSincronizacion() {

        return new EstimadorParametrosSincronizacion(
                this.timestampsSincronizacion
                ).estimarParametrosSincronizacion();


        
    }


    /**
     *  Fija los parámetros de transmisión en el nodo local y remoto
     *
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (local)
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     *
     */
    public void ajustarParametrosTransmision(ParametrosTransmision pT)
            throws NodoClienteException, NodoServidorException  {

        this.controlLogger.entering("ControlNodos", "setTransmissionParameters");

        String mensajeOperacion = "set transmission parameters";
        Object[] args = new Object[]{pT};

        new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodo.setParametrosTransmision(
                        (ParametrosTransmision) argsOperacion[0]
                        );
                return null;

            }
        }.procesarOperacion(args, mensajeOperacion );


        new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodoRemoto.setParametrosTransmision(
                        (ParametrosTransmision) argsOperacion[0]
                        );
                return null;

            }
        }.procesarOperacion(args, mensajeOperacion);

        this.controlLogger.exiting("ControlNodos", "setTransmissionParameters");
        
    }



    /**
     * Configura los objetos que proporcionan el servicio del protocolo de timestamps,
     * creando los buffers de mensajes de emisión en el nodo local y de recepción en
     * el nodo remoto.
     *
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (local)
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     * @throws LongitudInvalidaException       Si la longitud de los mensajes es un número no válido
     *
     */
    public void crearMensajesEnlaceAscendente()
            throws NodoClienteException, NodoServidorException {

        this.controlLogger.entering("ControlNodos", "createUplinkMessages");

        new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodo.peticionCrearMensajesParaEnviar();
                return null;

            }
        }.procesarOperacion(null, "create sending messages buffer" );


        new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodoRemoto.peticionCrearMensajesParaRecibir();
                return null;

            }
        }.procesarOperacion(null, "create receiving messages buffer");
        
        this.controlLogger.exiting("ControlNodos", "createUplinkMessages");

    }

    /**
     * Configura los objetos que proporcionan el servicio del protocolo de timestamps,
     * creando los buffers de mensajes de emisión en el nodo remoto y de recepción en
     * el nodo local.
     *
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (local)
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     *
     */
    public void crearMensajesEnlaceDescendente()
            throws NodoClienteException, NodoServidorException  {

       this.controlLogger.entering("ControlNodos", "createDownlinkMessages");

        new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodoRemoto.peticionCrearMensajesParaEnviar();
                return null;

            }
        }.procesarOperacion(null, "create sending messages buffer");

        new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodo.peticionCrearMensajesParaRecibir();
                return null;

            }
        }.procesarOperacion(null, "create receiving messages buffer" );

        this.controlLogger.exiting("ControlNodos", "createDownlinkMessages");

    }


    /**
     * Rellena los datos de los mensajes del nodo local con el audio codificado,
     * de acuerdo a los parámetros de transmisión especificados.
     * Este método se llama cuando se desea enviar audio en mensajes desde el nodo local al
     * remoto.
     * 
     *
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo local (cliente)
     *
     */
    public void mapearAudioNodoLocal(byte[] audioComprimido)
            throws NodoClienteException{

        this.controlLogger.entering("ControlNodos", "mapLocalNodeAudio");

        new NodoClienteHandler() {

            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodo.mapearAudioEnMensajes(
                        ( byte[] ) argsOperacion[0]
                        );
                return null;

            }
        }.procesarOperacion(new Object[]{audioComprimido}, "map audio into messages at local node");

        this.controlLogger.exiting("ControlNodos", "mapLocalNodeAudio");
   
    }

    /**
     * Rellena los datos de los mensajes del nodo remoto con el audio codificado,
     * de acuerdo a los parámetros de transmisión especificados.
     * Este método se llama cuando se desea enviar audio en mensajes desde el nodo remoto al
     * local.     
     *
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo remoto (servidor)
     *
     */
    public void mapearAudioNodoRemoto(byte[] audioComprimido)
            throws NodoServidorException{

        this.controlLogger.entering("ControlNodos", "mapRemoteNodeAudio");

        //FormatoCodec formatoObjetivo = this.gestorCodecsAudio.getFormatoCodec(nombreCodec);
        //int bytesPorIntervalo = this.nodo.getParametrosTransmision().numeroBytesPorIntervalo();
        //int duracionIntervaloEnUS = (int) formatoObjetivo.duracionEnMicrosegundos(bytesPorIntervalo);
        //byte[] audioComprimido = this.comprimirAudio(formatoObjetivo, duracionIntervaloEnUS);

        new NodoServidorHandler() {

            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                ControlNodos.this.nodoRemoto.mapearAudioEnMensajes(
                        ( byte[] ) argsOperacion[0]
                        );
                return null;

            }
        }.procesarOperacion(new Object[]{audioComprimido}, "map audio into messages at remote node");

        this.controlLogger.exiting("ControlNodos", "mapRemoteNodeAudio");
    }



    /**
     * Obtiene los bytes de datos de los mensajes recibidos en el nodo local, y los interpreta como audio
     * codificado.
     * Este método se llama cuando el nodo local recibe mensajes de audio enviado por el nodo remoto
     *
     * @return                         Los bytes de audio recibido. Cada fila representa los bytes para un intervalo
     * @throws NodoClienteException    Si ocurre un error en la la operación efectuada en el nodo local (cliente)
     *
     */
    public byte[][] audioRecibidoNodoLocal()
            throws NodoClienteException {

        this.controlLogger.entering("ControlNodos", "localNodeReceivedAudio");

        byte[][] audioRecibido = ( byte[][] ) new NodoClienteHandler() {

            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodo.obtenerAudioDeMensajes();

            }
        }.procesarOperacion(null, "get audio form messages");

        //FormatoCodec formatoCodec= this.gestorCodecsAudio.getFormatoCodec(nombreCodec);

        //File[] recibidos = this.descomprimirAudio(audioRecibido, formatoCodec);
        this.controlLogger.exiting("ControlNodos", "localNodeReceivedAudio");

        return audioRecibido;

   
    }


    /**
     * Obtiene los bytes de datos de los mensajes recibidos en el nodo remoto, y los interpreta como audio
     * codificado. 
     * Este método se llama cuando el nodo remoto recibe mensajes de audio enviado por el nodo local
     *
     * @return                         Los bytes de audio recibido. Cada fila representa los bytes para un intervalo
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo remoto (Servidor)
     *
     */
     public byte[][] audioRecibidoNodoRemoto()
           throws  NodoServidorException  {

         this.controlLogger.entering("ControlNodos", "remoteNodeReceivedAudio");

         byte[][] audioRecibido = ( byte[][] ) new NodoServidorHandler() {

            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodoRemoto.obtenerAudioDeMensajes();

            }
        }.procesarOperacion(null, "get audio from messages");

        //FormatoCodec formatoCodec= this.gestorCodecsAudio.getFormatoCodec(nombreCodec);

        //File[] recibidos = this.descomprimirAudio(audioRecibido, formatoCodec);
        
        this.controlLogger.exiting("ControlNodos", "remoteNodeReceivedAudio");

        return audioRecibido;

    }

     


    /**
     * Realiza las operaciones necesarias en el protocolo de timestamps
     * para que el nodo local actúe en modo emisor y el nodo remoto en modo receptor.
     * La petición de modo receptor se realiza en una hebra para evitar el bloqueo.
     *
     * @param rtt_us        Tiempo de ida y vuelta en microsegundos.
     *                      Necesario en el protocolo de timestmaps
     *
     * @throws InterruptedException     Si la hebra que atiende la petición se ve interrumpida
     * 
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (local)
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     *
     * @see ProtocoloTimestamps
     * @see HebraTimestampsReceptor
     *
     */
    public void obtenerTimestampsEnlaceAscendente(int rtt_us)
            throws NodoClienteException, NodoServidorException, InterruptedException {


       this.controlLogger.entering("ControlNodos", "getUplinkTimestamps");

       NodoServidorHandler nsh = new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodoRemoto.peticionTimestampsReceptor(
                        (Integer) argsOperacion[0]
                        );
                
            }
        };

        HebraTimestampsReceptor hebraTimestampsReceptor =
                new HebraTimestampsReceptor(nsh, this, rtt_us);
        hebraTimestampsReceptor.start();

        try{
            Thread.sleep(rtt_us/1000 + 1);
        }catch(InterruptedException ie) {}

        this.timestampsEmisor = (long[]) new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodo.peticionTimestampsEmisor();

            }
        }.procesarOperacion(null, "sender timestamps request");

        hebraTimestampsReceptor.join();
        hebraTimestampsReceptor.comprobarExcepcionHebra();
        this.timestampsReceptor = hebraTimestampsReceptor.getTimestampsReceptor();

        this.controlLogger.exiting("ControlNodos", "getUplinkTimestamps");

    }


    /**
     * Realiza las operaciones necesarias en el protocolo de timestamps
     * para que el nodo local actúe en modo receptor y el nodo remoto en modo emisor.
     * La petición de modo receptor se realiza en una hebra para evitar el bloqueo.
     *
     * @param rtt_us        Tiempo de ida y vuelta en microsegundos.
     *                      Necesario en el protocolo de timestmaps
     *
     * @throws InterruptedException         Si la hebra que atiende la petición se ve interrumpida
     * @throws NodoClienteException     Si ocurre un error con en la operación efectuada en el nodo cliente (local)
     * @throws NodoServidorException    Si ocurre un error en la la operación efectuada en el nodo servidor (servidor)
     *
     * @see ProtocoloTimestamps
     * @see HebraTimestampsEmisor
     *
     */
    public void obtenerTimestampsEnlaceDescente(int rtt_us)
            throws NodoClienteException, NodoServidorException, InterruptedException {


        this.controlLogger.entering("ControlNodos", "getDownlinkTimestamps");
        NodoServidorHandler nsh = new NodoServidorHandler() {
            public Object operacionServidor(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodoRemoto.peticionTimestampsEmisor();
            }
        };

        HebraTimestampsEmisor hebraTimestampsEmisor =
                new HebraTimestampsEmisor(nsh, this);
        hebraTimestampsEmisor.start();

        this.timestampsReceptor = (long[]) new NodoClienteHandler() {
            public Object operacionCliente(Object[] argsOperacion)
                    throws NodoException, RemoteException  {

                return ControlNodos.this.nodo.peticionTimestampsReceptor(
                        (Integer) argsOperacion[0]
                        );

            }
        }.procesarOperacion(new Object[]{rtt_us}, "receiver timestamps request");

        hebraTimestampsEmisor.join();
        hebraTimestampsEmisor.comprobarExcepcionHebra();
        this.timestampsEmisor = hebraTimestampsEmisor.getTimestampsEmisor();

        this.controlLogger.exiting("ControlNodos", "getDownlinkTimestamps");

    }

  

    /**
     * Corrige las marcas de tiempo de emisor eliminando el offset de los relojes
     * entre el equipo local y el remoto
     *
     * @see EstimadorParametrosSincronizacion
     */
    public void corregirTimestampsEmisor(long offsetLocal) {

        int numeroTimestamps = this.timestampsEmisor.length;
        
        for (int i = 0; i < numeroTimestamps; i++) {

            if (this.timestampsEmisor[i] != 0) {

                this.timestampsEmisor[i] += offsetLocal;
            }
        }

    }


    /**
     * Corrige las marcas de tiempo de receptor eliminando el offset de los relojes
     * entre el equipo local y el remoto
     *
     * @see EstimadorParametrosSincronizacion
     */
    public void corregirTimestampsReceptor(long offsetLocal) {

        int numeroTimestamps = this.timestampsReceptor.length;
        
        for (int i = 0; i < numeroTimestamps; i++) {

            if (this.timestampsReceptor[i] != 0) {

                this.timestampsReceptor[i] += offsetLocal;
            }
        }

    }


    
    /**
     * Realiza el conjunto de estimaciones indicadas por <code>estimadoresQoS</code>,
     * y devuelve el conjunto de medidas obtenidas por cada estimador.
     *
     * @param estimadoresQoS        El conjunto de estimaciones de QoS
     * @return                      El conjunto de medidas obtenidas
     *
     * @see EstimadorQoS
     * @see MedidaQoS
     * 
     */
    public static ArrayList<MedidaQoS> estimarParametrosQos(ArrayList<EstimadorQoS> estimadoresQoS) {

        ArrayList<MedidaQoS> medidasQoS =
                new ArrayList<MedidaQoS>(estimadoresQoS.size());

        for(EstimadorQoS eqos: estimadoresQoS) {

            eqos.estimarMedidaQoS();
            medidasQoS.add( eqos.getMedidaQoS());

        }
        
        return medidasQoS;
    }
    
    /**
     * Devuelve el nodo local
     *
     * @return El nodo local
     *
     * @see Nodo
     */
    public Nodo getNodoLocal() {

        return this.nodo;
    }

    /**
     * Devuelve el nodo remoto
     *
     * @return El nodo remoto
     *
     * @see NodoRemoto
     */
    public NodoRemoto getNodoRemoto() {

        return this.nodoRemoto;
    }


    public void setNodoRemoto(NodoRemoto nodoRemoto) {

        this.nodoRemoto = nodoRemoto;
    }

    /**
     * Devuelve los timestamps de emisor generados por el protocolo de timestamps
     *
     * @return Los timestamps de emisor generados por el protocolo de timestamps
     */
    public long[] getTimestampsEmisor() {

        return this.timestampsEmisor;
    }

    /**
     * Devuelve los timestamps de receptor generados por el protocolo de timestamps
     *
     * @return Los timestamps de receptor generados por el protocolo de timestamps
     */
    public long[] getTimestampsReceptor() {

        return this.timestampsReceptor;
    }


    /**
     * Clase anidada que controla, manejando las excepciones oportunas, las operaciones
     * que realiza el nodo local
     * *
     */
    public abstract class NodoClienteHandler {

        public Object procesarOperacion(Object[] argsOperacion, String mensajeOperacion)
                throws NodoClienteException{

            Object retorno = null;
            String mensajeLog="";
            try{

                retorno = this.operacionCliente(argsOperacion);
                mensajeLog = "Success at client: " + mensajeOperacion;
                ControlNodos.this.controlLogger.fine(mensajeLog);

            }catch(NodoException ne) {

                mensajeLog = "Error at client: " + mensajeOperacion;
                ControlNodos.this.controlLogger.log(Level.FINE, mensajeLog, ne.getCause());
                throw new NodoClienteException("Error at client: " + ne.getMessage(), ne);

            }catch(RemoteException re) {

                mensajeLog = "Error at client: " + mensajeOperacion;
                ControlNodos.this.controlLogger.log(Level.FINE, mensajeLog, re);
                throw new NodoClienteException("Error at local RMI registry", re);
                
            }

            return retorno;
        }

        /**
         * Debe de implementarse con la operación del nodo local
         *
         * @param argsOperacion     Argumentos que requiere la operación del nodo local
         * @return                  El resultado de la operación
         */
        public abstract Object operacionCliente(Object[] argsOperacion)
                throws NodoException, RemoteException;
    }


 /**
     * Clase anidada que controla, manejando las excepciones oportunas, las operaciones
     * que realiza el nodo remoto
     *
     *
     */
    public abstract class NodoServidorHandler {

        public Object procesarOperacion(Object[] argsOperacion, String mensajeOperacion)
                throws NodoServidorException{

            Object retorno = null;
            String mensajeLog="";
            try{

                retorno = this.operacionServidor(argsOperacion);
                mensajeLog = "Success at server: " + mensajeOperacion;
                ControlNodos.this.controlLogger.fine(mensajeLog);

            }catch(NodoException ne) {

                mensajeLog = "Error at server: " + mensajeOperacion;
                ControlNodos.this.controlLogger.log(Level.FINE, mensajeLog, ne.getCause());
                throw new NodoServidorException("Error at server: " + ne.getMessage(), ne);

            }catch(RemoteException re) {

                mensajeLog = "Error at server: " + mensajeOperacion;
                ControlNodos.this.controlLogger.log(Level.FINE, mensajeLog, re);
                throw new NodoServidorException("Remote operation failed", re);
            }

            return retorno;
        }

        /**
         * Debe de implementarse con la operación del nodo remoto
         *
         * @param argsOperacion     Argumentos que requiere la operación del nodo remoto
         * @return                  El resultado de la operación
         */
        public abstract Object operacionServidor(Object[] argsOperacion)
                throws NodoException, RemoteException;
    }
}
