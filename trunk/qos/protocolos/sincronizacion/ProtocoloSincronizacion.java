package qos.protocolos.sincronizacion;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.net.BindException;

import java.io.IOException;
import utils.Temporizador;

public class ProtocoloSincronizacion {

    /**
     * Socket no orientado a conexión para el intercambio de Mensajes entre
     * las entidades que se sincronizan
     */
    private DatagramSocket datagramSocket;


    /**
     * Tiempo de expiración del socket por defecto
     */
    public static final int TIEMPO_EXPIRACION=5000;

    /**
     * Inicializa el socket sin unirlo a ningun puerto ni host
     */
    public ProtocoloSincronizacion() throws SocketException {

        this.datagramSocket = new DatagramSocket(null);
    }

    /**
     * Inicializa el socket a partir de un socket ya creado.
     *
     * @param datagramSocket El socket que utilizará para la comunicación
     */
    public ProtocoloSincronizacion(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    /**
     * Crea un socket para que acepte peticiones/respuesta por el puerto pasado
     * como argumento
     *
     * @param hostLocal         Nombre o dirección IP local
     * @param puertoLocal    Puerto local por el que escucha las peticiones
     * @throws SocketException
     * @throws UnknownHostException
     */
    public ProtocoloSincronizacion(String hostLocal, int puertoLocal)
            throws BindException, UnknownHostException, SocketException {

        this.datagramSocket = new DatagramSocket(new InetSocketAddress(hostLocal,puertoLocal));
        //this.datagramSocket.setSoTimeout(4000);        //ajuste del tiempo de espera máximo
    }

    /**
     * Devuelve el socket local
     *
     * @return datagramSocket  El socket que utiliza para la transmisión y recepción
     *                         de mensajes
     */
    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    /**
     * Fija el socket local
     *
     * @param datagramSocket  El socket que utiliza para la transmisión y recepción
     *                         de mensajes
     */
    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    /**
     * Establece una conexión con el equipo remoto.
     *
     * @param servidorSinc      Nombre de dominio del receptor
     * @param puertoDestino     Puerto que escucha el servidor y hacia donde se dirigen las peticiones
     * @throws SocketException
     */
    public void conectarA(String servidorSinc, int puertoDestino)
            throws SocketException, UnknownHostException {
        this.datagramSocket.connect(InetAddress.getByName(servidorSinc), puertoDestino);
    }

    /**
     * Desconecta el socket, si existe una conexión con otro equipo.
     */
    public void desconectar() {

        this.datagramSocket.disconnect();
    }

    /**
     * Permite cambiar la interfaz de red y el puerto que utilizará el socket
     * para recibir las peticiones.
     *
     * @param hostLocal     Dirección IP o nombre de host asociado que se utilizará como dirección origen
     * @param puertoLocal   Puerto local por el que se atenderán las peticiones
     * @throws BindException
     * @throws UnknownHostException
     * @throws SocketException
     */
    public void cambiarDireccionesLocales(String hostLocal, int puertoLocal)
            throws BindException, UnknownHostException, SocketException {

        if ( ! this.datagramSocket.isBound() ) {

            this.datagramSocket.bind(new InetSocketAddress(hostLocal,puertoLocal));

        }else {
            this.datagramSocket.close();

            this.datagramSocket = new DatagramSocket(new InetSocketAddress(hostLocal,puertoLocal));
        }

    }
    

    /**
     * Devuelve las direcciones locales a las que el socket está unido.
     *
     * @return  Las direcciones locales a las que el socket está unido.
     */
    public InetSocketAddress direccionesLocales() {

        return (InetSocketAddress) this.datagramSocket.getLocalSocketAddress();
    }

    /**
     * Devuelve las direcciones remotass a las que el socket está conectado.
     *
     * @return  Las direcciones remotas a las que el socket está conectado.
     */
    public InetSocketAddress direccionesRemotas() {

        return (InetSocketAddress) this.datagramSocket.getRemoteSocketAddress();
    }

    /**
     *
     * Cierra el socket liberando los recursos asociados.
     *
     */
    public void cerrarSocket() {

        this.datagramSocket.close();
    }

    /**
     * Realiza el protocolo de sincronización. Los timestamp se obtienen en nanosegundos.
     *
     * @return timestampsSincronizacion     Los timestamps obtenidos para calcular los parámetros de sincronización
     *                                      timestampsSincronizacion[0]==T1
     *                                      timestampsSincronizacion[1]==T2
     *                                      timestampsSincronizacion[2]==T3
     *                                      timestampsSincronizacion[3]==T4
     * @throws IOException
     * @throws SocketTimeoutException
     * @see ParametrosSincronizacion
     */
    public long[] obtenerTimestampsSincronizacion()
            throws SocketTimeoutException, IOException {

        long[] timestampsSincronizacion = new long[4];
        Temporizador temporizador = new Temporizador();
        DatagramPacket paqueteUDP = new DatagramPacket(new byte[MensajeSincronizacion.LONGITUD_MENSAJE], MensajeSincronizacion.LONGITUD_MENSAJE);
        //Mensaje mensajeSinc = new Mensaje(paqueteUDP.getData());
        MensajeSincronizacion mensajeSinc = new MensajeSincronizacion(paqueteUDP.getData());
        this.datagramSocket.setSoTimeout(TIEMPO_EXPIRACION);
        
        temporizador.iniciar();
        this.datagramSocket.send(paqueteUDP);
        timestampsSincronizacion[0] = temporizador.timestamp();       //marca T1
        this.datagramSocket.receive(paqueteUDP);
        timestampsSincronizacion[3] = temporizador.timestamp();       //marca T4
        //Extraer timestamps generados en el servidor.
        timestampsSincronizacion[1] = mensajeSinc.extraerLong(mensajeSinc.POSICION_T2);  //marca T2
        timestampsSincronizacion[2] = mensajeSinc.extraerLong(mensajeSinc.POSICION_T3);  //marca T3

        return timestampsSincronizacion;
    }

    /**
     * Realiza las operaciones del servidor, es decir, calcular T2, T3 y
     * encapsular los valores en el mensaje de sincronización.
     *
     * @throws IOException
     * @throws SocketTimeoutException
     * @see ParametrosSincronizacion
     */
    public void esperarPeticionSincronizacion()
            throws SocketTimeoutException, IOException {

        long T2;
        Temporizador temporizador = new Temporizador();
        MensajeSincronizacion mensajeSinc = new MensajeSincronizacion(MensajeSincronizacion.LONGITUD_MENSAJE);
        DatagramPacket paqueteUDP = new DatagramPacket(mensajeSinc.getBytesMensaje(), MensajeSincronizacion.LONGITUD_MENSAJE);
        this.datagramSocket.setSoTimeout(TIEMPO_EXPIRACION);

        temporizador.iniciar();
        this.datagramSocket.receive(paqueteUDP);
        T2 = temporizador.timestamp();
        mensajeSinc.encapsularLong(mensajeSinc.POSICION_T2, T2);
        mensajeSinc.encapsularLong(mensajeSinc.POSICION_T3, temporizador.timestamp());      //marca T3
        this.datagramSocket.send(paqueteUDP);
     
    }

    private void setTiempoExpiracion(int rtt_ms) throws SocketException{

        if(rtt_ms !=0) {

            this.datagramSocket.setSoTimeout(2*rtt_ms);

        }else {

            this.datagramSocket.setSoTimeout(ProtocoloSincronizacion.TIEMPO_EXPIRACION);

        }
    }
}//fin de clase

