
package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.FactorR;
import qos.estimacionQoS.parametrosQoS.Retardo;
import qos.estimacionQoS.mos.Emodel;
import qos.estimacionQoS.mos.ModeloPerdidasMarkov;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import audio.FormatoCodec;

   

/**
 * @author Antonio
 * @version 1.0
 * @updated 19-ago-2010 21:08:40
 */
public class EstimadorEmodel extends EstimadorQoS {

    /**
     * Permite obtener el factor R del modelo E de la recomendación G.107.
     * @see Emodel
     */
    private Emodel emodel;

    /**
     * Timestamps en nanosegundos de los paquetes recibidos. Cada elemento i del
     * vector contiene el instante de recepción del paquete i
     */
    private long timestampsPaquetesRecibidos[];


    /**
     * Estima los valores de retardo necesarios para el cálculo del factor R
     */
    private EstimadorRetardo estimadorRetardo;



    /**
     * Estima información de pérdidas (probabilidad y ráfagas) necesaria para
     * el calculo del factor R
     *
     * @see ModeloPerdidasMarkov
     */
    private ModeloPerdidasMarkov modeloPerdidas;



    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación. 
     *
     * @param formatoCodec              Propiedades de los datos de audio utilizados para obtener la medida
     * @param parametrosTransmision     Parametros de transmisión de audio utilizados para obtener la medida
     * @param timestampsPE              Timestamps de los paquetes enviados asociados a los datos de transmisión
     * @param timestampsPR              Timestamps de los paquetes recibidos asociados a los datos de transmisión
     * @param gmin                      Define el número de paquetes recibidos consecutivos que se consideran fuera
     *                                  de ráfaga. Es necesario para el modelo de pérdidas de markov. @see ModeloPerdidasMarkov
     *
     */
    public EstimadorEmodel(
            FormatoCodec formatoCodec,
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPE,
            long[] timestampsPR,
            int gmin) {

        super(
                new MedidaEmodel(formatoCodec, parametrosTransmision, gmin)
                );

        emodel = new Emodel();
        this.timestampsPaquetesRecibidos = timestampsPR;
        this.modeloPerdidas = new ModeloPerdidasMarkov(gmin);
        this.estimadorRetardo = new EstimadorRetardo(
                formatoCodec ,
                parametrosTransmision ,
                timestampsPE ,
                timestampsPR
                );

    }


    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación.
     *
     * @param parametrosTransmision     Parametros de transmisión de audio utilizados para obtener la medida
     * @param timestampsPE              Timestamps de los paquetes enviados asociados a los datos de transmisión
     * @param timestampsPR              Timestamps de los paquetes recibidos asociados a los datos de transmisión
     * @param gmin                      Define el número de paquetes recibidos consecutivos que se consideran fuera
     *                                  de ráfaga. Es necesario para el modelo de pérdidas de markov. @see ModeloPerdidasMarkov
     *
     */
    public EstimadorEmodel(
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPE,
            long[] timestampsPR,
            int gmin) {

        super(
                new MedidaEmodel(parametrosTransmision, gmin)
                );

        emodel = new Emodel();
        this.timestampsPaquetesRecibidos = timestampsPR;
        this.modeloPerdidas = new ModeloPerdidasMarkov(gmin);
        this.estimadorRetardo = new EstimadorRetardo(
                parametrosTransmision ,
                timestampsPE ,
                timestampsPR
                );
    }

    /**
     * Obtiene el factor R del modelo E descrito en la recomendación G.107, teniendo en cuenta
     * el retardo (t,ta,tr) , las pérdidas (ppl) y las ráfagas (burstr).
     *
     * Se obtiene una estimación del factor R en cada intervalo, considerandose independientes.
     *
     * @see EstimadorRetardo;
     * @see ModeloPerdidasMarkov
     * @see Emodel
     */
    public void estimarMedidaQoS() {

        int numeroIntervalos = super.getMedidaQoS().getParametrosTransmision().
                getNumeroIntervalos();
        int mensajesPorIntervalo = super.getMedidaQoS().getParametrosTransmision().
                getMensajesPorIntervalo();

        this.estimadorRetardo.estimarMedidaQoS();
        Retardo retardo_k ;
        FactorR factorR_k ;
        float probGap;
        float retMedio;
        float ppl;

        for (int k = 0; k < numeroIntervalos; k++) {

            retardo_k = (Retardo) this.estimadorRetardo.getMedidaQoS().getParametroQoS(k);
            factorR_k = new FactorR();
            this.modeloPerdidas.analizarPerdidas(
                    timestampsPaquetesRecibidos ,
                    k*mensajesPorIntervalo ,
                    (k+1)*mensajesPorIntervalo
                    );
            probGap = this.modeloPerdidas.p1();
            retMedio = retardo_k.valorMedio();

            if(retMedio==Retardo.VALOR_DEFECTO) {
                retMedio=0;
            }

            this.emodel.setT(retMedio);
            this.emodel.setTa(retMedio);
            this.emodel.setTr(2*retMedio);

            ppl = this.modeloPerdidas.p3();

            if ( (probGap==0.f)) {

                this.emodel.setPpl(100);
                this.emodel.setBurstR(1);

            }else if(probGap != 1f){

                this.emodel.setPpl(ppl*100);
                this.emodel.setBurstR(1 / ( this.modeloPerdidas.p13()
                        + this.modeloPerdidas.p23()
                        + this.modeloPerdidas.p31()) );

            }else{
                this.emodel.setPpl(ppl*100);
                this.emodel.setBurstR(1);

            }

            factorR_k.setValorParametro(0, this.emodel.calcularR());
            super.medidaQoS.addParametroQoS(k,factorR_k);
        }

    }


    /**
     * Devuelve el retardo asociado a la medida del factor R
     *
     * @return  El retardo asociado a la medida del factor R
     *
     * @see Retardo
     * @see MedidaRetardo
     */
    public MedidaQoS getMedidaQoSRetardo() {

        return this.estimadorRetardo.getMedidaQoS();
    }

}
