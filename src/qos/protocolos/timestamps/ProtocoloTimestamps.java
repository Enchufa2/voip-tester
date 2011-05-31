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
package qos.protocolos.timestamps;

import utils.Temporizador;
import qos.protocolos.LongitudInvalidaException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.io.IOException;


public class ProtocoloTimestamps {


    /**
     * Socket no orientado a conexi贸n para el intercambio de Mensajes entre
     * las entidades generadoras de timestamps.
     * @see DatagramSocket
     */
    private DatagramSocket datagramSocket;

    /**
     * Mensajes de datos que enviar谩 o recibir谩 el socket mediante paquetes UDP.
     * @see MensajeProtoTimestamps
     */
    private MensajeProtoTimestamps mensajes[];


    /**
     * Inicializa el socket sin unirlo a ningun puerto ni host
     */
    public ProtocoloTimestamps() throws SocketException {

        this.datagramSocket = new DatagramSocket(null);
    }

    /**
     * Inicializa el socket a partir de un socket ya creado. Es responsabilidad del usuario
     * crear los mensajes que se vayan a intercambiar, incluyendo la cabecera y los datos
     * de los mismos.
     * 
     *
     * @param datagramSocket El socket que utilizar谩 para la comunicaci贸n
     */
    public ProtocoloTimestamps(DatagramSocket datagramSocket) {

        this.datagramSocket = datagramSocket;

    }

    /**
     * Crea un socket en modo receptor (servidor).  Es responsabilidad del usuario
     * crear los mensajes que se vayan a intercambiar, incluyendo la cabecera y los datos
     * de los mismos.
     *
     *
     * @param hostLocal         Nombre o direcci贸n IP local
     * @param puertoLocal       Puerto por el que el socket escuchar谩 las peticiones
     * @throws SocketException
     * @throws UnknownHostException
     * @see DatagramSocket
     */
    public ProtocoloTimestamps(String hostLocal, int puertoLocal)
            throws BindException, UnknownHostException, SocketException  {

        this.datagramSocket = new DatagramSocket(new InetSocketAddress(hostLocal, puertoLocal));

    }

    /**
     * Genera los timestamps, proporcionados por un objeto de la clase Temporizador, modo Emisor.
     * Consiste en obtener el tiempo en el que se env铆a cada mensaje/paquete.
     * Se envian mensajes cada tEE milisegundos. Antes de llamar a este m茅todo
     * debe de haberse creado los mensajes a enviar.
     *
     * @param tiempoEntreEnvios   Intervalo de tiempo en microsegundos que transcurre entre el envio de un paquete y el siguiente
     * @return timestampsEmisor   Los timestamps generados en el env铆o de cada paquete
     * @throws IOException
     * @see Temporizador
     * @see DatagramSocket
     */
    public long[] generarTimestampsEmisor(int tiempoEntreEnvios)
            throws IOException {

        long[] timestampsEmisor = new long[this.mensajes.length];
        DatagramPacket paqueteUDP = new DatagramPacket(
                this.mensajes[0].getBytesMensaje(),
                this.mensajes[0].longitudMensaje()
                );
        Temporizador temporizador = new Temporizador();

        int i=0;
        //this.datagramSocket.setSendBufferSize(this.mensajes[0].longitudDatosMensaje() + 32);    //+ H_UDP + H_IP +H_PT
  
        temporizador.iniciar();
        
        while(i < this.mensajes.length) {

            paqueteUDP.setData(this.mensajes[i].getBytesMensaje());
            this.datagramSocket.send(paqueteUDP);
            timestampsEmisor[i] = temporizador.timestamp();
            temporizador.esperar(tiempoEntreEnvios * 1000);        //conversion del tEE a nanosegundos y espera
            i++;
        }

        return timestampsEmisor;
    }

    
    /**
     * Genera los timestamps, proporcionados por un objeto de la clase Temporizador, modo Receptor.
     * Consiste en obtener el tiempo en el que se recibe cada mensaje/paquete.
     * Existe un tiempo de expiraci贸n asociado al socket, por lo que si se cumple dicho tiempo 
     * el socket dejar谩 de esperar la recepci贸n de m谩s mensajes. Este tiempo viene dado por:
     *                          texp = 2*max(rtt,2*tiempoEntreEnvios)
     *
     * Los datos recibidos se corresponden con mensajes del protocolo de timestamps, habiendo una relaci贸n
     * uno a uno entre paquete UDP y mensaje. As铆 los bytes de un DatragramPacket se corresponden con los bytes
     * de un MensajeProtoTiemstamps. Como los mensajes pueden recibirse desordenados, se utiliza un mensaje como
     * buffer para poder obtener el identificador de cabecera y asignar los datos recibidos al mensaje correspondiente
     * dentro del conjunto de mensajes que se esperan recibir. Si un mensaje se pierde, los bytes asociados a dicho
     * mensaje permanecen a null.
     *
     * @param rtt                    Tiempo de ida y vuelta al equipo remoto en microsegundos
     * @param tiempoEntreEnvios      Tiempo entre el env铆o de un mensaje y otro en microsegundos
     * @param longitudDatos          Longitud en bytes de los datos que contiene los mensajes
     *
     * @return timestampsRecepcion   Los timestamps generados en la recepcion de cada paquete
     * @throws IOException
     * @see Temporizador
     * @see DatagramSocket
     */
    public long[] generarTimestampsReceptor(int rtt, int tiempoEntreEnvios, int longitudDatos)
            throws SocketException,IOException {

        int numeroMensajes = this.mensajes.length;
        long[] timestampsReceptor = new long[numeroMensajes];

        long timestamp;
        int recibidos = 0;        //contador de mensajes recibidos
        Temporizador temporizador = new Temporizador();
        int identificadorCabecera;
        int longitudDatosPaquete = longitudDatos + MensajeProtoTimestamps.LONGITUD_IDENTIFICADOR;
        //reserva de espacio en memoria para los datos que se recibir谩n
        DatagramPacket[] paquetesUDP = new DatagramPacket[numeroMensajes];
        MensajeProtoTimestamps buffer = new MensajeProtoTimestamps(new byte[longitudDatos]);
        for (int i = 0; i < numeroMensajes; i++) {
            paquetesUDP[i] = new DatagramPacket(
                    new byte[longitudDatosPaquete],
                    longitudDatosPaquete
                    );
        }

        this.setTiempoExpiracion(rtt, tiempoEntreEnvios);
        //this.datagramSocket.setReceiveBufferSize(longitudDatos + 32);   //+ H_UDP + H_IP +H_PT

        temporizador.iniciar();
        //Recepcion de mensajes y generaci贸n de tiemstamps
        try{

            while (recibidos < numeroMensajes) {

                buffer.setBytesMensaje(paquetesUDP[recibidos].getData());
                this.datagramSocket.receive(paquetesUDP[recibidos]);
                timestamp = temporizador.timestamp();
                try {

                    identificadorCabecera = buffer.identificadorCabecera(numeroMensajes);
                    timestampsReceptor[identificadorCabecera] = timestamp;
                    mensajes[identificadorCabecera].setBytesMensaje(buffer.getBytesMensaje());

                } catch (IdCabeceraInvalidoException icie) {/*ignorar*/}

                recibidos++;
            }

        }catch (SocketTimeoutException ste) {/*ignorar*/}

        return timestampsReceptor;
    }

    

    /**
     * Devuelve el socket local
     *
     * @return datagramSocket  El socket que utiliza para la transmisi贸n y recepci贸n
     *                         de mensajes
     */
    public DatagramSocket getDatagramSocket () {

        return datagramSocket;
    }

    /**
     * Fija el socket local
     *
     * @param datagramSocket  El socket que utiliza para la transmisi贸n y recepci贸n
     *                         de mensajes
     */
    public void setDatagramSocket (DatagramSocket datagramSocket) {

        this.datagramSocket = datagramSocket;
    }


    /**
     * Devuelve el conjunto de mensajes enviados/recibidos en la transmisi贸n
     *
     * @return mensajes El conjunto de mensajes enviados/recibidos
     */
    public MensajeProtoTimestamps[] getMensajes() {

        return this.mensajes;
    }

    /**
     * As铆gna el conjunto de mensajes a enviar.
     *
     * @param mensajes    el conjunto de mensajes
     */
    public void setMensajes(MensajeProtoTimestamps[] mensajes) {
        
        this.mensajes = mensajes;
    }

    /**
     * Establece una conexi贸n con el equipo remoto.
     *
     * @param equipoRemoto      Nombre de dominio del receptor
     * @param puertoDestino     Puerto que escucha el servidor y hacia donde se dirigen las peticiones
     * @throws SocketException
     * @throws ConnectException
     */
    public void conectarA(String equipoRemoto, int puertoDestino) throws ConnectException, UnknownHostException {

        this.datagramSocket.connect(InetAddress.getByName(equipoRemoto), puertoDestino);
    }


    public void conectarA(InetSocketAddress isa)
            throws ConnectException, SocketException{

        this.datagramSocket.connect(isa);

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
            throws BindException, UnknownHostException, SocketException{

        if ( ! this.datagramSocket.isClosed()) {

             this.datagramSocket.close();

        }

        this.datagramSocket = new DatagramSocket(new InetSocketAddress(hostLocal,puertoLocal));
  

    }


    
    /**
     * Devuelve las direcciones locales a las que el socket est谩 unido.
     *
     * @return  Las direcciones locales a las que el socket est谩 unido.
     */
    public InetSocketAddress direccionesLocales() {

        if(this.datagramSocket == null || this.datagramSocket.isClosed())
            return null;

        return (InetSocketAddress) this.datagramSocket.getLocalSocketAddress();
    }


    /**
     * Devuelve las direcciones remotass a las que el socket est谩 conectado.
     *
     * @return  Las direcciones remotas a las que el socket est谩 conectado.
     */
    public InetSocketAddress direccionesRemotas() {

        if(this.datagramSocket == null || this.datagramSocket.isClosed())
            return null;
        
        return (InetSocketAddress) this.datagramSocket.getRemoteSocketAddress();
    }


    

    /**
     *
     * Cierra el socket liberando lso recursos asociados.
     *
     * @throws SocketException  Si una hebra llama a este m茅todo cuando el socket espera un paquete
     *
     */
    public void cerrarSocket() throws SocketException{

        this.datagramSocket.close();
    }
    


    /**
     *
     * Crea los mensajes que se enviar谩n al equipo remoto, a帽adiendo a cada uno
     * la cabecera correspondiente. Los datos a enviar por defectos son ceros.
     *
     * @param numeroMensajes          El n煤mero de mensajes que se crean/env铆an
     * @param longitudDatos           Longitud en bytes de los datos a enviar (sin cabecera)
     * @throws LongitudInvalidaException    Si <code>longitudDatos</code> es negativo o mayor que el
     *                                      m谩ximo permitido.
     *
     */
    public void crearMensajesParaEnviar(int numeroMensajes, int longitudDatos) throws LongitudInvalidaException{

        this.mensajes = new MensajeProtoTimestamps[numeroMensajes];

        for (int i = 0; i < numeroMensajes; i++) {

            this.mensajes[i] = new MensajeProtoTimestamps(longitudDatos);
            this.mensajes[i].setIdentificadorCabecera(i);
            
        }

    }

    /**
     * Prepara espacio en memoria para la recepci贸n de mensajes. Se reserva espacio
     * para la recepci贸n de tantos mensajes como se enviaron. Cada mensaje se crea
     * con el array de bytes a null.
     *
     * @param numeroMensajes          El n煤mero de mensajes que se crean/env铆an
     * 
     */
    public void crearMensajesParaRecibir(int numeroMensajes) {

        this.mensajes = new MensajeProtoTimestamps[numeroMensajes];

        for (int i = 0; i < numeroMensajes; i++) {

            this.mensajes[i] = new MensajeProtoTimestamps();

        }

    }


    /**
     * Asigna el identificador de cabecera de cada mensaje. Este identificador
     * es un entero que indica el orden de tranmsi贸n o recepci贸n de los mensajes.
     *
     * Este m茅todo es v谩lido cuando lo llama un emisor, puesto que el receptor no
     * puede conocer el orden de recepci贸n de los mensajes.
     *
     */
    public void setCabeceraMensajesDeEmisor() {
        for (int i = 0; i < this.mensajes.length; i++) {

            this.mensajes[i].setIdentificadorCabecera(i);
            
        }
    }
    

    /**
     * Ajusta el tiempo de expiraci贸n que debe tener el socket en modo recepci贸n,
     * dejando un margen suficiente para que lleguen los mensajes y que no sea muy largo
     * para que el receptor no espere durante mucho tiempo.
     *
     * Este tiempo de expiraci贸n depende del retardo entre los extremos y
     * el tiempo entre envios sucesivos de paquetes.
     *
     * @param rtt                   Tiempo de ida y vuelta en microsegundos
     * @param tiempoEntreEnvios     Tiempo entre envios de paquetes en microsegundos
     * 
     *
     * @throws SocketException
     */
    private void setTiempoExpiracion(int rtt, int tiempoEntreEnvios) throws SocketException {

            this.datagramSocket.setSoTimeout( 
                    (3*rtt + 100*tiempoEntreEnvios) / 1000
                    );

    }


    /**
     *
     * Busca en el array de mensajes recibidos aquellos que tengan el contenido de datos a null,
     * lo que indica que el mensaje correspondiente no se ha recibido. La restauraci贸n consiste
     * en a帽adir un -1 en la cabecera del mensaje de dicha posici贸n, y a帽adirle ceros como datos.
     *
     * @param  longitudDatos           Longitud en bytes de los datos a enviar (sin cabecera)
     * @throws LongitudInvalidaException    Si <code>longitudDatos</code> es negativo o mayor que el
     *                                      m谩ximo permitido.
     *
     */
    public void restaurarPaquetesPerdidos(int longitudDatos) {

        byte valorBytesMensajesPerdidos=1;
        for (int i = 0; i < this.mensajes.length; i++) {

            if (this.mensajes[i].getBytesMensaje() == null) {

                this.mensajes[i].setBytesMensaje
                        ( new byte[longitudDatos + MensajeProtoTimestamps.LONGITUD_IDENTIFICADOR ]);
                this.mensajes[i].setValorBytes(valorBytesMensajesPerdidos);
                this.mensajes[i].setIdentificadorCabecera(-1);

            }
        }
    }


}//fin de clase


