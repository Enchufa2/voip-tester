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
     * @param hostLocal     El nombre o direcci贸n del host local donde se encuentra el registro RMI
     * @param puertoLocal   El puerto local donde atiende las peticiones el registro
     *
     * @return <code>true</code>  Si el nodo est谩 registrado
     *
     * @throws RemoteException              Si ocurre un error en la operaci贸n con el elemento remoto
     * @throws NodoIOException              Si las direcciones de red forman una direcci贸n URL incorrecta
     *
     */
    //public boolean nodoRegistrado()
            //throws NodoIOException,RemoteException;

    /**
     * Fija los par谩metros de transmisi贸n que acepta el nodo para enviar o recibir mensajes
     *
     * @param pT    Los valores de los par谩metros de transmisi贸n
     *
     * @throws RemoteException      Si ocurre un error en la llamada al m茅todo remoto
     * @see ParametrosTransmision
     */
    public void setParametrosTransmision(ParametrosTransmision pT)
            throws RemoteException;


    /**
     * El nodo debe disponer de un procedimiento de sincronizaci贸n, basado en petici贸n y respuesta.
     * Este m茅todo llama al objeto para que cree un proceso de espera de petici贸n de sincronizaci贸n.
     *
     * @throws RemoteException              Si ocurre un error en la llamada al m茅todo remoto
     * @throws NodoSocketException          Si transcurre el tiempo m谩ximo de espera para la petici贸n de sincronizaci贸n
     * @throws NodoIOException              Si ocurre un error de E/S en el flujo de datos del socket
     *
     */
    public void esperarPeticionSincronizacion()
            throws RemoteException, NodoSocketException, NodoIOException;


    /**
     * El nodo utiliza un socket para el intercambio de mensajes en el protocolo de timestamps.
     * Este socket est谩 unido a una direcci贸n de red y puerto locales concretos (direcci贸n de socket).
     * Este m茅todo devuelve la direcci贸n del socket local a la que est谩 unido el socket del nodo
     *
     * @return      La direcci贸n del socket local a la que est谩 unido el socket del nodo
     *
     * @throws RemoteException              Si ocurre un error en la llamada al m茅todo remoto
     * @see InetSocketAddress
     *
     */
    public InetSocketAddress direccionLocalSocketProtoTimestamps()
            throws RemoteException;


    /**
     * El socket del nodo est谩 conectado con un socket remoto, con el que intercambia mensajes en
     * el protocolo de timestamps. Este m茅todo devuelve la direcci贸n del socket remoto al que est谩
     * conectado el socket local
     *
     * @return      La direcci贸n del socket remoto al que est谩 conectado el socket local
     *
     * @throws RemoteException              Si ocurre un error en la llamada al m茅todo remoto
     * @see InetSocketAddress
     *
     */
    public InetSocketAddress direccionRemotaSocketProtoTimestamps()
            throws RemoteException;


    /**
     * Ajusta la direcci贸n del socket local del protocolo de timestamps, especificando
     * el host y puerto local a los que se unir谩 el socket.
     *
     * @param hostLocal         Nombre de host o direcci贸n del host local a la que se une el socket
     * @param puertoLocal       Puerto local al que se une el socket
     *
     * @throws RemoteException          Si ocurre un error en la llamada al m茅todo remoto
     * @throws NodoSocketException      Si no es posible unir el socket local a la direcci贸n solicitada
     *                                  o si ocurre un error en el protocolo subyacente del socket
     * @throws NodoIOException          Si no se puede determinar la direcci贸n del host local
     *
     */
    public void configurarSocketProtoTimestamps(String hostLocal, int puertoLocal)
            throws RemoteException, NodoSocketException, NodoIOException;


    /**
     * Conecta el socket local del del protocolo de timestamps del nodo
     * con un socket remoto de direccion <code>isa</code>.
     *
     * @param isa   La direcci贸n del socket remoto
     *
     * @throws RemoteException          Si ocurre un error en la llamada al m茅todo remoto
     * @throws NodoSocketException      Si ocurre un error en el procedimiento de conexi贸n de los sockets
     *                                  o si ocurre un error en el protocolo subyacente del socket
     *
     * @see InetSocketAddress
     *
     */
    public void conectarSocketProtocoloTimestamps(InetSocketAddress isa)
            throws RemoteException, NodoSocketException;


    /**
     * Desconecta el socket local del nodo del socket remoto con el que est茅 conectado
     *
     *  @throws RemoteException          Si ocurre un error en la llamada al m茅todo remoto
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
     * Crea el buffer de mensajes, seg煤n los par谩metros de transmisi贸n, necesario para el env铆o
     * de mensajes en el protocolo de timestamps.
     *
     * @throws RemoteException          Si ocurre un error en la llamada al m茅todo remoto
     * @throws LongitudInvalidaException       Si la longitud de los mensajes es un n煤mero no v谩lido
     *
     */
    public void peticionCrearMensajesParaEnviar()
            throws  RemoteException;


    /**
     * Crea el buffer de mensajes, seg煤n los par谩metros de transmisi贸n, necesario para la recepci贸n
     * de mensajes en el protocolo de timestamps.
     *
     * @throws RemoteException          Si ocurre un error en la llamada al m茅todo remoto
     *
     */
    public void peticionCrearMensajesParaRecibir()
            throws RemoteException;


    /**
     * Llama al objeto para que realice una petici贸n de generar timestamps de emisor en el protocolo
     * de timestamps.
     *
     * @throws RemoteException              Si ocurre un error en la llamada al m茅todo remoto
     * @throws NodoIOException              Si ocurre un error de E/S en el flujo de datos del socket
     */
    public long[] peticionTimestampsEmisor()
            throws RemoteException, NodoIOException;


    /**
     * Llama al objeto para que realice una petici贸n de generar timestamps de receptor en el protocolo
     * de timestamps.
     *
     * @param rtt_us                        El tiempo de ida y vuelta en microsegundos al nodo remoto.
     *                                      Es un par谩metro para el protocolo de timestamps.
     * @throws RemoteException              Si ocurre un error en la llamada al m茅todo remoto
     * @throws NodoSocketException          si ocurre un error en el protocolo subyacente del socket
     * @throws NodoIOException              Si ocurre un error de E/S en el flujo de datos del socket
     */
    public long[] peticionTimestampsReceptor(int rtt_us)
            throws RemoteException, NodoSocketException, NodoIOException;


    /**
     * Rellena la carga 煤til de los mensajes con los bytes de audio especificados.
     * Los bytes de audio deben de tener una duracion igual a un intervalo, ya que
     * el contenido de audio en cada intervalo es el mismo.
     *
     * @param bytesAudio          bytes de audio correspondiente a un intervalo
     *                            que se introducen en el campo de datos de los mensajes
     *
     * @throws RemoteException      Si ocurre un error en la operaci贸n remota
     */
    public void mapearAudioEnMensajes(byte[] bytesAudio)
            throws RemoteException;


     /**
     * Obtiene la carga 煤til de los mensajes. Se supone que los mensajes transportan
     * audio por lo que los bytes de datos se trataran como tal.
     *
     * @return Los bytes de datos (audio) que transportan los mensajes. Los bytes
     *         se a帽aden al array devuelto de manera ordenada, seg煤n el identificador
     *         de cabecera del mensaje. Cada fila del array se corresponde con un intervalo
     * @throws RemoteException      Si ocurre un error en la operaci贸n remota
     */
    public byte[][] obtenerAudioDeMensajes()
            throws RemoteException;

    


}
