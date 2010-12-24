/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.ProbabilidadPerdidas;
//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import audio.FormatoCodec;


/**
 *
 * @author Antonio
 */
public class MedidaProbabilidadPerdidas extends MedidaQoS{

    /**
     * Se inicializa la  medida de acuerdo a las variables de transmisión de audio.
     * Existen medidas en las que es necesario disponer de datos de audio para su
     * estimación.
     * Se reserva espacio para representar tantas estimaciones de probabilidad de pérdidas
     * como número de intervalos indiquen lso parámetros de transmisión.
     *
     *
     * @param formatoCodec      Propiedades de los datos de audio utilizados para obtener esta medida
     * @param parametrosTransmision   Parametros de transmisión de audio utilizados para obtener esta medida
     *
     * @see ProbabilidadPerdidas
     */
    public MedidaProbabilidadPerdidas(
            FormatoCodec formatoCodec ,
            ParametrosTransmision parametrosTransmision
            ) {

        super(formatoCodec, parametrosTransmision);
        super.parametrosQoS =
                new ProbabilidadPerdidas [ super.parametrosTransmision.getNumeroIntervalos() ];

    }


    /**
     * Inicializa la medida sólo a partir de los parámetros de transmisión. Existen medidas
     * en las que el contenido de los datos no tiene por qué ser audio.
     *
     * Se reserva espacio para representar tantas estimaciones de probabilidad de pérdidas
     * como número de intervalos indiquen los parámetros de transmisión.
     *
     * @param parametrosTransmision
     * @see ProbabilidadPerdidas
     */
    public  MedidaProbabilidadPerdidas(
            ParametrosTransmision parametrosTransmision
            ) {

        super(parametrosTransmision);
        super.parametrosQoS =
                new ProbabilidadPerdidas [ super.parametrosTransmision.getNumeroIntervalos() ];

    }



    public float valorRepresentativo (int numeroIntervalo) {

        return super.parametrosQoS[numeroIntervalo].getValorParametro(0);
    }


}
