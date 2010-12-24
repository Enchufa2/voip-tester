
package qos.estimacionQoS;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.estimacionQoS.parametrosQoS.DistribucionPerdidas;
//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import audio.FormatoCodec;


/**
 * @author Antonio
 * @version 1.0
 * @updated 19-ago-2010 21:08:35
 */
public final class EstimadorDistribucionPerdidas
        extends EstimadorQoS {

    /**
     * Timestamps en nanosegundos de los paquetes recibidos. Cada elemento i del
     * vector contiene el instante de recepción del paquete i
     */
    private long timestampsPaquetesRecibidos[];


    /**
     * Longitud de ráfaga máxima (en número de paquetes) que contendrá el
     * análisis de la distribución. Fija el tamaño del vector de valores de
     * <code>DistribucionPerdidas</code>
     *
     * @see DistribucionPerdidas
     */
    private int rafagaMaxima;


    /**
     * Valor máximo de tamaño de ráfaga que se puede analizar, independientemente
     * de si el intervalo contiene más mensajes
     */
    private static final int RAFAGA_MAXIMA_ESTIMABLE = 50;


    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación.
     *
     * @param formatoCodec              Propiedades de los datos de audio utilizados para obtener la medida
     * @param parametrosTransmision     Parametros de transmisión de audio utilizados para obtener la medida
     * @param timestampsPR              Timestamps de los paquetes recibidos asociados a los datos de transmisión
     * @param rafagaMaxima              Longitud de ráfaga máxima (en número de paquetes) que contendrá el
     *                                  análisis de la distribución
     */
    public EstimadorDistribucionPerdidas(
            FormatoCodec formatoCodec ,
            ParametrosTransmision parametrosTransmision ,
            long[] timestampsPR ,
            int rafagaMaxima) {
        
        super(
                new MedidaDistribucionPerdidas(formatoCodec, parametrosTransmision)
                );
        //this.medidaDistribucionPerdidas = new MedidaQoS<DistribucionPerdidas>(formatoAudio, parametrosAudio, parametrosTransmision);
        this.timestampsPaquetesRecibidos = timestampsPR;
        this.rafagaMaxima = rafagaMaxima;
    }


    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación.
     * La distribución de pérdidas puede estimarse sin la necesidad de incluir datos de audio en los paquetes.
     *
     * @param parametrosTransmision     Los parámetros de transmisión utilizados para obtener la medida
     * @param timestampsPR              Timestamps de los paquetes recibidos asociados a los datos de transmisión
     * @param rafagaMaxima              Longitud de ráfaga máxima (en número de paquetes) que contendrá el
     *                                  análisis de la distribución
     */
    public EstimadorDistribucionPerdidas(
            ParametrosTransmision parametrosTransmision ,
            long[] timestampsPR ,
            int rafagaMaxima) {

        super(
                new MedidaDistribucionPerdidas(parametrosTransmision)
                );
        this.timestampsPaquetesRecibidos = timestampsPR;
        this.rafagaMaxima = rafagaMaxima;
    }



    /**
     * Calcula la distribución de la probabilidad de pérdidas. Para ello se obtiene
     * la distribución de frecuencia del número de paquetes consecutivos perdidos.
     *
     * Devuelve un vector donde el índice i+1 representa el tamaño de ráfaga y
     * el valor correspondiente el número de veces que ocurre dicha ráfaga.
     * No se tendrán en cuenta las ráfagas mayores que <code>rafagaMaxima</code>
     *
     * Este procedimiento se aplica de manera indpendiente a cada intervalo, es decir,
     * no se tienen en cuenta mensajes adyacentes a un cambio de intervalo.
     *
     *
     */
    public void estimarMedidaQoS() {

        int mensajesPorIntervalo = super.medidaQoS.getParametrosTransmision().
                getMensajesPorIntervalo();
        int numeroIntervalos = super.medidaQoS.getParametrosTransmision().
                getNumeroIntervalos();

        DistribucionPerdidas distribucionPerdidas_k; 
        int limite_inferior;
        int limite_superior;
        int i;
        int longitudRafaga;

        for (int k = 0; k < numeroIntervalos; k++) {
            
            i = 0;
            longitudRafaga = 0;
            distribucionPerdidas_k = new DistribucionPerdidas(this.rafagaMaxima);
            limite_inferior = k * mensajesPorIntervalo;
            limite_superior = (k + 1) * mensajesPorIntervalo;
            while (i < mensajesPorIntervalo) {

                longitudRafaga = buscarRafaga(limite_inferior + i, limite_superior, this.timestampsPaquetesRecibidos);

                if (longitudRafaga == 0) {

                    i++;

                } else {

                    i += longitudRafaga;
                    if (longitudRafaga < this.rafagaMaxima) {

                        distribucionPerdidas_k.getValoresParametro()[longitudRafaga - 1]++;

                    }
                }
            }

            super.medidaQoS.addParametroQoS(k, distribucionPerdidas_k);
        }
    }



    /**
     * Busca la ráfaga más larga dentro del vector de timestamps,
     * a partir de la posicion dada por <code>posicionInical</code>, si esta
     * representa un paquete perdido.
     * Si en la posición indicada no representa un paquete perdido, se devuelve
     * 0.
     *
     * @param posicionInicial   Posicion inicial a partir de la cual se busca la rafaga
     * @return  El tamaño de la ráfaga más grande encontrada
     *
     */
    public static int buscarRafaga(int posicionInicial, long[] timestampsPaquetesRecibidos) {


        if (posicionInicial >= timestampsPaquetesRecibidos.length) {

            return 0;

        }else if(timestampsPaquetesRecibidos[posicionInicial] != 0) {

            return 0;

        }

        return buscarRafaga(posicionInicial + 1, timestampsPaquetesRecibidos) + 1;

    }

   /**
     * Buscar la ráfaga más larga dentro de un subconjunto del vector de timestamps,
     * a partir de la posicion dada por <code>posicionInical</code>,si esta
     * representa un paquete perdido, hasta la posicion dada por <code>posicionFinal</code>
     * Si en la posición indicada no representa un paquete perdido, se devuelve
     * 0.
     *
     * @param posicionInicial   Posicion inicial a partir de la cual se busca la rafaga
     * @param posicionFinal     Posicion final a partir de la cual se deja de buscar
     * @return  El tamaño de la ráfaga más grande encontrada dentro del subconjunto
     *
     */
    public static int buscarRafaga(int posicionInicial, int posicionFinal, long[] timestampsPaquetesRecibidos) {


        if (posicionInicial >= timestampsPaquetesRecibidos.length) {

            return 0;

        } else if (posicionInicial > posicionFinal) {

            return 0;

        } else if (timestampsPaquetesRecibidos[posicionInicial] != 0) {

            return 0;

        }

        return buscarRafaga(posicionInicial+1, posicionFinal, timestampsPaquetesRecibidos) + 1;

    }

}
