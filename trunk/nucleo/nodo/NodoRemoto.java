package nucleo.nodo;

import nucleo.*;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.protocolos.LongitudInvalidaException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.InetSocketAddress;

/**
 * @author Antonio
 * @version 1.0
 * @created 28-jul-2010 11:59:47
 */
public interface NodoRemoto extends Remote {


    /**
     * Comprueba si el objeto se encuentra registrado en el registro RMI
     *
     * @param hostLocal     El nombre o dirección del host local donde se encuentra el registro RMI
     * @param puertoLocal   El puerto local donde atiende las peticiones el registro
     *
     * @return <code>true</code>  Si el nodo está registrado
     *
     * @throws RemoteException              Si ocurre un error en la operación con el elemento remoto
     * @throws NodoIOException              Si las direcciones de red forman una dirección URL incorrecta
     *
     */
    //public boolean nodoRegistrado()
            //throws NodoIOException,RemoteException;

    /**
     * Fija los parámetros de transmisión que acepta el nodo para enviar o recibir mensajes
     *
     * @param pT    Los valores de los parámetros de transmisión
     *
     * @throws RemoteException      Si ocurre un error en la llamada al método remoto
     * @see ParametrosTransmision
     */
    public void setParametrosTransmision(ParametrosTransmision pT)
            throws RemoteException;


    /**
     * El nodo debe disponer de un procedimiento de sincronización, basado en petición y respuesta.
     * Este método llama al objeto para que cree un proceso de espera de petición de sincronización.
     *
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @throws NodoSocketException          Si transcurre el tiempo máximo de espera para la petición de sincronización
     * @throws NodoIOException              Si ocurre un error de E/S en el flujo de datos del socket
     *
     */
    public void esperarPeticionSincronizacion()
            throws RemoteException, NodoSocketException, NodoIOException;


    /**
     * El nodo utiliza un socket para el intercambio de mensajes en el protocolo de timestamps.
     * Este socket está unido a una dirección de red y puerto locales concretos (dirección de socket).
     * Este método devuelve la dirección del socket local a la que está unido el socket del nodo
     *
     * @return      La dirección del socket local a la que está unido el socket del nodo
     *
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @see InetSocketAddress
     *
     */
    public InetSocketAddress direccionLocalSocketProtoTimestamps()
            throws RemoteException;


    /**
     * El socket del nodo está conectado con un socket remoto, con el que intercambia mensajes en
     * el protocolo de timestamps. Este método devuelve la dirección del socket remoto al que está
     * conectado el socket local
     *
     * @return      La dirección del socket remoto al que está conectado el socket local
     *
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @see InetSocketAddress
     *
     */
    public InetSocketAddress direccionRemotaSocketProtoTimestamps()
            throws RemoteException;


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
     *
     */
    public void configurarSocketProtoTimestamps(String hostLocal, int puertoLocal)
            throws RemoteException, NodoSocketException, NodoIOException;


    /**
     * Conecta el socket local del del protocolo de timestamps del nodo
     * con un socket remoto de direccion <code>isa</code>.
     *
     * @param isa   La dirección del socket remoto
     *
     * @throws RemoteException          Si ocurre un error en la llamada al método remoto
     * @throws NodoSocketException      Si ocurre un error en el procedimiento de conexión de los sockets
     *                                  o si ocurre un error en el protocolo subyacente del socket
     *
     * @see InetSocketAddress
     *
     */
    public void conectarSocketProtocoloTimestamps(InetSocketAddress isa)
            throws RemoteException, NodoSocketException;


    /**
     * Desconecta el socket local del nodo del socket remoto con el que esté conectado
     *
     *  @throws RemoteException          Si ocurre un error en la llamada al método remoto
     */
    public void desconectarSocketPrototocoloTimestamps() throws RemoteException;



    /**
     * Cierra el socket que se utiliza en el protocolo de timestmaps
     *
     * @throws NodoSocketException          Si se cierra el socket mientras este se encuentra
     *                                      en espera de paquetes.
     *
     * @see ProtocoloTimestamps
     */
    public void cerrarSocketProtocoloTimestamps()
            throws RemoteException, NodoSocketException;




    /**
     * Crea el buffer de mensajes, según los parámetros de transmisión, necesario para el envío
     * de mensajes en el protocolo de timestamps.
     *
     * @throws RemoteException          Si ocurre un error en la llamada al método remoto
     * @throws LongitudInvalidaException       Si la longitud de los mensajes es un número no válido
     *
     */
    public void peticionCrearMensajesParaEnviar()
            throws  RemoteException;


    /**
     * Crea el buffer de mensajes, según los parámetros de transmisión, necesario para la recepción
     * de mensajes en el protocolo de timestamps.
     *
     * @throws RemoteException          Si ocurre un error en la llamada al método remoto
     *
     */
    public void peticionCrearMensajesParaRecibir()
            throws RemoteException;


    /**
     * Llama al objeto para que realice una petición de generar timestamps de emisor en el protocolo
     * de timestamps.
     *
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @throws NodoIOException              Si ocurre un error de E/S en el flujo de datos del socket
     */
    public long[] peticionTimestampsEmisor()
            throws RemoteException, NodoIOException;


    /**
     * Llama al objeto para que realice una petición de generar timestamps de receptor en el protocolo
     * de timestamps.
     *
     * @param rtt_us                        El tiempo de ida y vuelta en microsegundos al nodo remoto.
     *                                      Es un parámetro para el protocolo de timestamps.
     * @throws RemoteException              Si ocurre un error en la llamada al método remoto
     * @throws NodoSocketException          si ocurre un error en el protocolo subyacente del socket
     * @throws NodoIOException              Si ocurre un error de E/S en el flujo de datos del socket
     */
    public long[] peticionTimestampsReceptor(int rtt_us)
            throws RemoteException, NodoSocketException, NodoIOException;


    /**
     * Rellena la carga útil de los mensajes con los bytes de audio especificados.
     * Los bytes de audio deben de tener una duracion igual a un intervalo, ya que
     * el contenido de audio en cada intervalo es el mismo.
     *
     * @param bytesAudio          bytes de audio correspondiente a un intervalo
     *                            que se introducen en el campo de datos de los mensajes
     *
     * @throws RemoteException      Si ocurre un error en la operación remota
     */
    public void mapearAudioEnMensajes(byte[] bytesAudio)
            throws RemoteException;


     /**
     * Obtiene la carga útil de los mensajes. Se supone que los mensajes transportan
     * audio por lo que los bytes de datos se trataran como tal.
     *
     * @return Los bytes de datos (audio) que transportan los mensajes. Los bytes
     *         se añaden al array devuelto de manera ordenada, según el identificador
     *         de cabecera del mensaje. Cada fila del array se corresponde con un intervalo
     * @throws RemoteException      Si ocurre un error en la operación remota
     */
    public byte[][] obtenerAudioDeMensajes()
            throws RemoteException;

    


}