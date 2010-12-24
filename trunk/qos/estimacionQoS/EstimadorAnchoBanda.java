package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.ParametroQoS;
import qos.estimacionQoS.parametrosQoS.AnchoBanda;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import audio.FormatoCodec;


/**
 * @author Antonio
 * @version 1.0
 * @updated 19-ago-2010 21:08:31
 */
public final class EstimadorAnchoBanda
        extends EstimadorQoS {

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
    public EstimadorAnchoBanda(
            FormatoCodec formatoCodec,
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPR) {

        //this.medidaAnchoBanda = new MedidaQoS<AnchoBanda>(formatoAudio, parametrosAudio, parametrosTransmision);
        super(
                new MedidaAnchoBanda(formatoCodec, parametrosTransmision)
                );
        this.timestampsPaquetesRecibidos = timestampsPR;

    }


    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación.
     * El ancho de banda puede estimarse sin la necesidad de incluir datos de audio en los paquetes.
     *
     * @param parametrosTransmision     Los parámetros de transmisión utilizados para obtener la medida
     * @param timestampsPR      Timestamps de los paquetes recibidos asociados a los datos de transmisión
     */
    public EstimadorAnchoBanda(
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPR) {

        super(
                new MedidaAnchoBanda(parametrosTransmision)
                );

        this.timestampsPaquetesRecibidos = timestampsPR;
    }

    
    /**
     * Estima el ancho de banda observado en la llegada 2 o más paquetes consecutivos.
     * Sea <code>no</code> el primer paquete de una ráfaga de paquetes recibidos,
     * por cada recepción de un paquete consecutivo <code>n</code>, <code>n>1</code> :
     *
     * AB(n)= (n-1)* Ld)/ (Tr(n)-Tr(no) ) para n>1
     *
     * donde
     *      AB(n)= ancho de banda observado en la llegada del paquete n
     *      Ld = longitud en bytes de los mensajes
     *      Tr(n)= instante de llegada del paquete n
     *      Tr(no)= instante de llegada de primer paquete de una ráfaga de paquetes recibidos
     *
     * Si Tr(i)=0, AB(i) y AB(i+1) indeterminados.
     *
     * Las unidades de AB(n) son KB/s
     *
     * Este procedimiento se aplica de manera indpendiente a cada intervalo, es decir,
     * no se tienen en cuenta mensajes consecutivos adyacentes a un cambio de intervalo.
     *
     * @see ParametrosTransmision
     * @see ParametroQoS
     *
     */
    public void estimarMedidaQoS() {

        int numeroIntervalos = super.medidaQoS.getParametrosTransmision().
                getNumeroIntervalos();
        int valoresPorIntervalo = super.medidaQoS.getParametrosTransmision().
                getMensajesPorIntervalo();
        int longitudDatos = super.medidaQoS.getParametrosTransmision().
                getLongitudDatos();

        AnchoBanda anchoBanda_k;
        float anchoBanda_i;     //AB(n)
        int rafagaPaquetesConsecutivos;// = 0;
        long tiempoInicial = 0;
        int limiteInferior;
        long tiempoTranscurrido;

        for (int k = 0; k < numeroIntervalos; k++) {

            anchoBanda_k = new AnchoBanda(valoresPorIntervalo);
            rafagaPaquetesConsecutivos = 0;
            limiteInferior = k * valoresPorIntervalo;
            for (int i = limiteInferior; i < (k + 1) * valoresPorIntervalo; i++) {

                if (this.timestampsPaquetesRecibidos[i] != 0) {

                    rafagaPaquetesConsecutivos++;
                    if (rafagaPaquetesConsecutivos == 1) {

                        tiempoInicial = this.timestampsPaquetesRecibidos[i];

                    } else if (rafagaPaquetesConsecutivos > 1) {

                        tiempoTranscurrido = this.timestampsPaquetesRecibidos[i] - tiempoInicial;
                        if(tiempoTranscurrido > 0){
                            anchoBanda_i = 1000000f * ((rafagaPaquetesConsecutivos - 1) * longitudDatos) /tiempoTranscurrido;
                            anchoBanda_k.setValorParametro(i - limiteInferior, anchoBanda_i);
                        }

                    }

                } else {
                    
                    rafagaPaquetesConsecutivos = 0;
                }

            }
            super.medidaQoS.addParametroQoS(k, anchoBanda_k);
        }

    }

    
    

}



