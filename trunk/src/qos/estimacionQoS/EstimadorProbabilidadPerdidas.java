package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.estimacionQoS.parametrosQoS.ProbabilidadPerdidas;
//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import audio.FormatoCodec;

/**
 * @author Antonio
 * @version 1.0
 * @updated 19-ago-2010 21:08:49
 */
public final class EstimadorProbabilidadPerdidas extends
        EstimadorQoS {

    /**
     * Timestamps en nanosegundos de los paquetes recibidos. Cada elemento i del
     * vector contiene el instante de recepción del paquete i
     */
    private long timestampsPaquetesRecibidos[];



    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación.
     *
     * @param formatoCodec              Propiedades de los datos de audio utilizados para obtener la medida
     * @param parametrosTransmision     Parametros de transmisión de audio utilizados para obtener la medida
     * @param timestampsPR              Timestamps de los paquetes recibidos asociados a los datos de transmisión
     */
    public EstimadorProbabilidadPerdidas(
            FormatoCodec formatoCodec,
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPR) {

       super(
                new MedidaProbabilidadPerdidas(formatoCodec, parametrosTransmision)
                );

        this.timestampsPaquetesRecibidos = timestampsPR;

    }

    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación.
     * La probabilidad de pérdidas puede estimarse sin la necesidad de incluir
     * datos de audio en los paquetes.
     *
     * @param parametrosTransmision     Los parámetros de transmisión utilizados para obtener la medida
     * @param timestampsPR      Timestamps de los paquetes recibidos asociados a los datos de transmisión
     */
    public EstimadorProbabilidadPerdidas(
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPR) {

       super(
                new MedidaProbabilidadPerdidas(parametrosTransmision)
                );

        this.timestampsPaquetesRecibidos = timestampsPR;


    }

    /**
     * Estima la probabilidad de pérdidas en cada intervalo k como:
     * P(k)= Np(k)/Nt(k)
     *
     * donde
     *      P(k)= probabilidad de pérdidas obtenida en el intervalo k
     *      Np(k)= número de paquetes perdidos en el intervalo k
     *      Nt(k)= número de paquetes enviados en el intervalo k
     */
    public void estimarMedidaQoS() {

        int numeroIntervalos = super.medidaQoS.getParametrosTransmision().
                getNumeroIntervalos();
        int valoresPorIntervalo = super.medidaQoS.getParametrosTransmision().
                getMensajesPorIntervalo();

        ProbabilidadPerdidas probabilidadPerdidas_k;
        int paquetesPerdidos_k;                     //Np(k)
        float probPerdidas_k;                       //P(k)
        int limite_inferior;
        int limite_superior;

        for (int k = 0; k < numeroIntervalos; k++) {

            probabilidadPerdidas_k = new ProbabilidadPerdidas();
            paquetesPerdidos_k = 0;
            limite_inferior = k * valoresPorIntervalo;
            limite_superior = (k + 1) * valoresPorIntervalo;

            for (int i = limite_inferior; i < limite_superior; i++) {

                if (this.timestampsPaquetesRecibidos[i] == 0) {

                    paquetesPerdidos_k++;

                }

            }

            probPerdidas_k = ( (float) paquetesPerdidos_k )/ valoresPorIntervalo;
            probabilidadPerdidas_k.setValorParametro(0, probPerdidas_k);
            super.medidaQoS.addParametroQoS(k, probabilidadPerdidas_k);
        }

    }



}

