package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.estimacionQoS.parametrosQoS.Jitter;
//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import audio.FormatoCodec;

/**
 * @author Antonio
 * @version 1.0
 * @updated 19-ago-2010 21:08:44
 */
public final class EstimadorJitter extends
        EstimadorQoS {


    /**
     * Timestamps en nanosegundos de los paquetes enviados. Cada elemento i del vector
     * contiene el instante de envío del paquete i
     */
    private long timestampsPaquetesEnviados[];

    
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
     * @param timestampsPE              Timestamps de los paquetes enviados asociados a los datos de transmisión
     * @param timestampsPR              Timestamps de los paquetes recibidos asociados a los datos de transmisión
     */
    public EstimadorJitter(
            FormatoCodec formatoCodec,
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPE,
            long[] timestampsPR
            ) {

            super(
                new MedidaJitter(formatoCodec, parametrosTransmision)
                );

        this.timestampsPaquetesEnviados = timestampsPE;
        this.timestampsPaquetesRecibidos = timestampsPR;

    }

    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación.
     * El jitter puede estimarse sin la necesidad de incluir datos de audio en los paquetes.
     *
     * @param parametrosTransmision     Los parámetros de transmisión utilizados para obtener la medida
     * @param timestampsPE      Timestamps de los paquetes enviados asociados a los datos de transmisión
     * @param timestampsPR      Timestamps de los paquetes recibidos asociados a los datos de transmisión
     */
    public EstimadorJitter(
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPE,
            long[] timestampsPR) {

        super(
                new MedidaJitter(parametrosTransmision)
                );

        this.timestampsPaquetesEnviados = timestampsPE;
        this.timestampsPaquetesRecibidos = timestampsPR;

    }

    /**
     * Estima la diferencia de retardo entre dos paquetes (jitter).
     * Si no existe timestamp para un paquete n, no será posible calcular
     * el jitter ni para el paquete n ni para el paquete n+1;
     *
     * La estimación de la diferencia de retardo viene dada por:
     * D(n,n-1)=[Tr(n)-Te(n)]-[Tr(n-1)-Te(n-1)]   para n>0
     *
     * donde
     *      D(n,n-1): Diferencia de retardo entre el paquete n y el n-1
     *      Tr(n): tiempo de rececpión del paquete n
     *      Te(n): tiempo de envío del paquete n
     *
     *  Las unidades de D(n, n-1) son ms
     */
    public void estimarMedidaQoS() {

        int numeroIntervalos = super.medidaQoS.getParametrosTransmision().
                getNumeroIntervalos();
        int valoresPorIntervalo = super.medidaQoS.getParametrosTransmision().
                getMensajesPorIntervalo();

        Jitter jitter_k;
        float jitterPaquete_i;      //D(i,i-1)
        int limite_inferior;

        for (int k = 0; k < numeroIntervalos; k++) {

            jitter_k = new Jitter(valoresPorIntervalo);
            limite_inferior = k * valoresPorIntervalo;
            for (int i = limite_inferior + 1; i < (k + 1) * valoresPorIntervalo; i++) {

                if (this.timestampsPaquetesRecibidos[i] != 0) {

                    if (this.timestampsPaquetesRecibidos[i - 1] != 0) {

                        jitterPaquete_i = (this.timestampsPaquetesRecibidos[i] - this.timestampsPaquetesEnviados[i]) -
                                (this.timestampsPaquetesRecibidos[i - 1] - this.timestampsPaquetesEnviados[i - 1]);
                        jitter_k.setValorParametro(i - limite_inferior, jitterPaquete_i / 1000000f);

                    }

                } else {

                    i++;       //El jitter del paquete i+1 tampoco se puede calcular

                }
            }
            super.medidaQoS.addParametroQoS(k, jitter_k);

        }

    }


}

