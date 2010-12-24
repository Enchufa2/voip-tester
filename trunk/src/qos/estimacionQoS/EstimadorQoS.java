package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.ParametroQoS;

//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import audio.FormatoCodec;


/**
 * @author Antonio
 * @version 1.0
 * @created 19-ago-2010 21:01:47
 */
public abstract class EstimadorQoS {

    
     protected MedidaQoS medidaQoS;

     protected EstimadorQoS(MedidaQoS medidaQoS) {

         this.medidaQoS = medidaQoS;
     }

    

    /**
     * Aplica la función estimadora para obtener la medida de QoS
     *
     */
    public abstract void estimarMedidaQoS();

    /**
     * @return la medida de QoS
     */
    public final MedidaQoS getMedidaQoS() {

         return this.medidaQoS;
     }


}