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
     * Socket no orientado a conexi贸n para el intercambio de Mensajes entre
     * las entidades que se sincronizan
     */
    private DatagramSocket datagramSocket;


    /**
     * Tiempo de expiraci贸n del socket por defecto
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
     * @param datagramSocket El socket que utilizar谩 para la comunicaci贸n
     */
    public ProtocoloSincronizacion(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    /**
     * Crea un socket para que acepte peticiones/respuesta por el puerto pasado
     * como argumento
     *
     * @param hostLocal         Nombre o direcci贸n IP local
     * @param puertoLocal    Puerto local por el que escucha las peticiones
     * @throws SocketException
     * @throws UnknownHostException
     */
    public ProtocoloSincronizacion(String hostLocal, int puertoLocal)
            throws BindException, UnknownHostException, SocketException {

        this.datagramSocket = new DatagramSocket(new InetSocketAddress(hostLocal,puertoLocal));
        //this.datagramSocket.setSoTimeout(4000);        //ajuste del tiempo de espera m谩ximo
    }

    /**
     * Devuelve el socket local
     *
     * @return datagramSocket  El socket que utiliza para la transmisi贸n y recepci贸n
     *                         de mensajes
     */
    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    /**
     * Fija el socket local
     *
     * @param datagramSocket  El socket que utiliza para la transmisi贸n y recepci贸n
     *                         de mensajes
     */
    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    /**
     * Establece una conexi贸n con el equipo remoto.
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
     * Desconecta el socket, si existe una conexi贸n con otro equipo.
     */
    public void desconectar() {

        this.datagramSocket.disconnect();
    }

    /**
     * Permite cambiar la interfaz de red y el puerto que utilizar谩 el socket
     * para recibir las peticiones.
     *
     * @param hostLocal     Direcci贸n IP o nombre de host asociado que se utilizar谩 como direcci贸n origen
     * @param puertoLocal   Puerto local por el que se atender谩n las peticiones
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
     * Devuelve las direcciones locales a las que el socket est谩 unido.
     *
     * @return  Las direcciones locales a las que el socket est谩 unido.
     */
    public InetSocketAddress direccionesLocales() {

        return (InetSocketAddress) this.datagramSocket.getLocalSocketAddress();
    }

    /**
     * Devuelve las direcciones remotass a las que el socket est谩 conectado.
     *
     * @return  Las direcciones remotas a las que el socket est谩 conectado.
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
     * Realiza el protocolo de sincronizaci贸n. Los timestamp se obtienen en nanosegundos.
     *
     * @return timestampsSincronizacion     Los timestamps obtenidos para calcular los par谩metros de sincronizaci贸n
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
     * encapsular los valores en el mensaje de sincronizaci贸n.
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


