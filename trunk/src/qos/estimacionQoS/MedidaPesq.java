/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.MosPesq;
//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import audio.FormatoCodec;

/**
 *
 * @author Antonio
 */
public class MedidaPesq extends MedidaQoS{


    /**
     * Se inicializa la  medida de acuerdo a las variables de transmisión de audio.
     * Existen medidas en las que es necesario disponer de datos de audio para su
     * estimación.
     * Se reserva espacio para representar tantas estimaciones de MOS del test Pesq
     * como número de intervalos indiquen lso parámetros de transmisión.
     *
     *
     * @param formatoCodec      Propiedades de los datos de audio utilizados para obtener esta medida
     * @param parametrosTransmision   Parametros de transmisión de audio utilizados para obtener esta medida
     *
     * @see MosPesq
     */
    public MedidaPesq(
            FormatoCodec formatoCodec,
            ParametrosTransmision parametrosTransmision
            ) {

        super(formatoCodec, parametrosTransmision);
        if (formatoCodec == null) throw new IllegalArgumentException("Es necesario indicar el codec empleado");

        super.parametrosQoS =
                new MosPesq [ super.parametrosTransmision.getNumeroIntervalos() ];

    }


     public float valorRepresentativo (int numeroIntervalo) {

        return super.parametrosQoS[numeroIntervalo].getValorParametro(0);
    }

}
