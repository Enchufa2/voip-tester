package qos.estimacionQoS;


import qos.estimacionQoS.parametrosQoS.MosPesq;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import audio.FormatoCodec;
import qos.estimacionQoS.mos.Pesq;
import qos.estimacionQoS.mos.PesqException;
import java.io.File;





/**
 * @author Antonio
 * @version 1.0
 * @updated 19-ago-2010 21:08:46
 */
public class EstimadorPesq extends 
        EstimadorQoS {

    /**
     * Permite realizar el test Pesq descrito en la recomendación P.862 de la ITU-T.
     */
    private Pesq pesq;


    /**
     * Ficheros que representan las señales de audio originales del test. Cada
     * señal de audio se corresponde con los datos enviados durante un intervalo
     * @see File
     */

    private File originales[];


    /**
     * Ficheros que representan las señales de audio degradadas del test. Cada
     * señal de audio se corresponde con los datos recibidos durante un intervalo
     * @see File
     */
    private File degradadas[];


    /**
     * Duración mínima en milisegundos de la señal de audio sobre la que el
     * Pesq puede realizar el test.
     */
    public final static int DURACION_INTERVALO_MIN = 2500;


    /**
     * Contiene las excepciónes que pueda producirse en cada ejecución del Pesq
     *
     * @see Pesq
     */
    private PesqException[] excepcionesPesq;






   /**
    * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
    * necesarias para la estimación. Para el Pesq es necesario disponer de los datos de audio
    * originales y los obtenidos tras la recepción de los mensajes
    *
    * @param formatoCodec               Propiedades de los datos de audio utilizados para obtener la medida
    * @param parametrosTransmision      Parametros de transmisión de audio utilizados para obtener la medida
    * @param originales                  Señales de audio correspondientes a los datos enviados en cada intervalo
    * @param degradadas                 Señales de audio correspondientes a los datos recibidos en cada intervalo
    * @param rutaPesq                   Ruta donde se encuentra el ejecutable que realiza el test Pesq
    *
    * @throws PesqException             Si el ejecutable no se encuentra en la ruta indicada por <code>rutaPesq</code>
    */
    public EstimadorPesq (
            FormatoCodec formatoCodec ,
            ParametrosTransmision parametrosTransmision ,
            File[] originales ,
            File[] degradadas ,
            File rutaPesq) {

        super(
                new MedidaPesq(formatoCodec, parametrosTransmision)
                );
        pesq = new Pesq(rutaPesq);
        this.originales = originales;
        this.degradadas = degradadas;
        this.excepcionesPesq = new PesqException
                [ parametrosTransmision.getNumeroIntervalos() ];
        //super.medidaQoS = new MedidaQoS<MosPesq>(formatoAudio, parametrosAudio, parametrosTransmision);
    }


   /**
    * Realiza el test Pesq tantas veces como señales haya disponibles, (es decir, tantas veces como número
    * de intervalos).
    * La realización del test Pesq se lleva a cabo mediante la clase <code>Pesq</code>, que utiliza el ejecutable
    * obtenido a partir del código fuente que se proporciona en la recomendación P.862 de la ITU-T. Si se
    * produce un fallo durante la ejecución del Pesq, el valor correspondiente de esa medida permanecerá al
    * valor por defecto de la clase <code>MosPesq</code>.
    *
    * @see Pesq
    *
    */
    public void estimarMedidaQoS() {

        int numeroIntervalos = this.medidaQoS.getParametrosTransmision().
                getNumeroIntervalos();
        MosPesq mosPesq_k;

        for (int k = 0; k < numeroIntervalos; k++) {

            mosPesq_k = new MosPesq();
            try {

                this.pesq.ejecutarPesq(
                        this.originales[k],
                        this.degradadas[k],
                        this.medidaQoS.formatoCodec.getFormatoRawAudio().getMuestrasPorSegundo()
                        );
                mosPesq_k.setValorParametro(0, pesq.salidaDeComandoAValorMos());

            } catch (PesqException pe) {

                this.excepcionesPesq[k] = pe;

            }     
            
            super.medidaQoS.addParametroQoS(k, mosPesq_k);
        }


    }
    
    
    
    /**
     * Devuelve el conjunto de ficheros que representan las señales de audio originales.
     *
     * @return  El conjunto de ficheros que representan las señales de audio originales
     */
    public File[] getOriginales() {

        return this.originales;
    }

    /**
     * Devuelve el conjunto de ficheros que representan las señales de audio degradadas.
     *
     * @return  El conjunto de ficheros que representan las señales de audio degradadas
     */
    public File[] getDegradadas() {

        return this.degradadas;
    }


    /**
     * Devuelve el objeto <code>Pesq</code> utilizado para la realización del test
     *
     * @return El objeto que realiza el test Pesq.
     * @see Pesq
     */
    public Pesq getPesq() {

        return this.pesq;
    }


    /**
     * Comprueba el tipo de excepción que pudo producirse en la ejcución
     * del PEsq. De ser se lanza dicha excepción. En caso contrario el método no hace nada.
     *
     * @param numeroIntervalo     Intervalo correspondiente a la excepción a comprobar
     * 
     */
    public void comprobarExcepcionPesq(int numeroIntervalo) 
            throws PesqException{

        if (this.excepcionesPesq[numeroIntervalo] !=null) {

            throw this.excepcionesPesq[numeroIntervalo];

        }
    }


    /**
     * Devuelve el conjunto de excepciones que pueden producirse en la ejecución
     * del Pesq.
     *
     * @return El conjunto de excepciones que pueden producirse en la ejecución del
     * Pesq.
     */
    public Exception[] getExcepcionesPesq() {

        return this.excepcionesPesq;
    }

}




