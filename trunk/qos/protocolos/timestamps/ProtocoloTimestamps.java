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
     * Socket no orientado a conexión para el intercambio de Mensajes entre
     * las entidades generadoras de timestamps.
     * @see DatagramSocket
     */
    private DatagramSocket datagramSocket;

    /**
     * Mensajes de datos que enviará o recibirá el socket mediante paquetes UDP.
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
     * @param datagramSocket El socket que utilizará para la comunicación
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
     * @param hostLocal         Nombre o dirección IP local
     * @param puertoLocal       Puerto por el que el socket escuchará las peticiones
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
     * Consiste en obtener el tiempo en el que se envía cada mensaje/paquete.
     * Se envian mensajes cada tEE milisegundos. Antes de llamar a este método
     * debe de haberse creado los mensajes a enviar.
     *
     * @param tiempoEntreEnvios   Intervalo de tiempo en microsegundos que transcurre entre el envio de un paquete y el siguiente
     * @return timestampsEmisor   Los timestamps generados en el envío de cada paquete
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
     * Existe un tiempo de expiración asociado al socket, por lo que si se cumple dicho tiempo 
     * el socket dejará de esperar la recepción de más mensajes. Este tiempo viene dado por:
     *                          texp = 2*max(rtt,2*tiempoEntreEnvios)
     *
     * Los datos recibidos se corresponden con mensajes del protocolo de timestamps, habiendo una relación
     * uno a uno entre paquete UDP y mensaje. Así los bytes de un DatragramPacket se corresponden con los bytes
     * de un MensajeProtoTiemstamps. Como los mensajes pueden recibirse desordenados, se utiliza un mensaje como
     * buffer para poder obtener el identificador de cabecera y asignar los datos recibidos al mensaje correspondiente
     * dentro del conjunto de mensajes que se esperan recibir. Si un mensaje se pierde, los bytes asociados a dicho
     * mensaje permanecen a null.
     *
     * @param rtt                    Tiempo de ida y vuelta al equipo remoto en microsegundos
     * @param tiempoEntreEnvios      Tiempo entre el envío de un mensaje y otro en microsegundos
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
        //reserva de espacio en memoria para los datos que se recibirán
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
        //Recepcion de mensajes y generación de tiemstamps
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
     * @return datagramSocket  El socket que utiliza para la transmisión y recepción
     *                         de mensajes
     */
    public DatagramSocket getDatagramSocket () {

        return datagramSocket;
    }

    /**
     * Fija el socket local
     *
     * @param datagramSocket  El socket que utiliza para la transmisión y recepción
     *                         de mensajes
     */
    public void setDatagramSocket (DatagramSocket datagramSocket) {

        this.datagramSocket = datagramSocket;
    }


    /**
     * Devuelve el conjunto de mensajes enviados/recibidos en la transmisión
     *
     * @return mensajes El conjunto de mensajes enviados/recibidos
     */
    public MensajeProtoTimestamps[] getMensajes() {

        return this.mensajes;
    }

    /**
     * Asígna el conjunto de mensajes a enviar.
     *
     * @param mensajes    el conjunto de mensajes
     */
    public void setMensajes(MensajeProtoTimestamps[] mensajes) {
        
        this.mensajes = mensajes;
    }

    /**
     * Establece una conexión con el equipo remoto.
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
            throws BindException, UnknownHostException, SocketException{

        if ( ! this.datagramSocket.isClosed()) {

             this.datagramSocket.close();

        }

        this.datagramSocket = new DatagramSocket(new InetSocketAddress(hostLocal,puertoLocal));
  

    }


    
    /**
     * Devuelve las direcciones locales a las que el socket está unido.
     *
     * @return  Las direcciones locales a las que el socket está unido.
     */
    public InetSocketAddress direccionesLocales() {

        if(this.datagramSocket == null || this.datagramSocket.isClosed())
            return null;

        return (InetSocketAddress) this.datagramSocket.getLocalSocketAddress();
    }


    /**
     * Devuelve las direcciones remotass a las que el socket está conectado.
     *
     * @return  Las direcciones remotas a las que el socket está conectado.
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
     * @throws SocketException  Si una hebra llama a este método cuando el socket espera un paquete
     *
     */
    public void cerrarSocket() throws SocketException{

        this.datagramSocket.close();
    }
    


    /**
     *
     * Crea los mensajes que se enviarán al equipo remoto, añadiendo a cada uno
     * la cabecera correspondiente. Los datos a enviar por defectos son ceros.
     *
     * @param numeroMensajes          El número de mensajes que se crean/envían
     * @param longitudDatos           Longitud en bytes de los datos a enviar (sin cabecera)
     * @throws LongitudInvalidaException    Si <code>longitudDatos</code> es negativo o mayor que el
     *                                      máximo permitido.
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
     * Prepara espacio en memoria para la recepción de mensajes. Se reserva espacio
     * para la recepción de tantos mensajes como se enviaron. Cada mensaje se crea
     * con el array de bytes a null.
     *
     * @param numeroMensajes          El número de mensajes que se crean/envían
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
     * es un entero que indica el orden de tranmsión o recepción de los mensajes.
     *
     * Este método es válido cuando lo llama un emisor, puesto que el receptor no
     * puede conocer el orden de recepción de los mensajes.
     *
     */
    public void setCabeceraMensajesDeEmisor() {
        for (int i = 0; i < this.mensajes.length; i++) {

            this.mensajes[i].setIdentificadorCabecera(i);
            
        }
    }
    

    /**
     * Ajusta el tiempo de expiración que debe tener el socket en modo recepción,
     * dejando un margen suficiente para que lleguen los mensajes y que no sea muy largo
     * para que el receptor no espere durante mucho tiempo.
     *
     * Este tiempo de expiración depende del retardo entre los extremos y
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
     * lo que indica que el mensaje correspondiente no se ha recibido. La restauración consiste
     * en añadir un -1 en la cabecera del mensaje de dicha posición, y añadirle ceros como datos.
     *
     * @param  longitudDatos           Longitud en bytes de los datos a enviar (sin cabecera)
     * @throws LongitudInvalidaException    Si <code>longitudDatos</code> es negativo o mayor que el
     *                                      máximo permitido.
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

