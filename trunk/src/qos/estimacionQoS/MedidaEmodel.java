/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.FactorR;
//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import audio.FormatoCodec;

/**
 *
 * @author Antonio
 */
public class MedidaEmodel extends MedidaQoS{


    private int gmin;

    /**
     * Se inicializa la  medida de acuerdo a las variables de transmisión de audio.
     * Existen medidas en las que es necesario disponer de datos de audio para su
     * estimación.
     * Se reserva espacio para representar tantas estimaciones del factor R del modelo E
     * como número de intervalos indiquen lso parámetros de transmisión.
     *
     *
     * @param formatoCodec              Propiedades de los datos de audio utilizados para obtener esta medida
     * @param parametrosTransmision     Parametros de transmisión utilizados para obtener esta medida
     * @param gmin                      Número mínimo de paquetes recibidos consecutivos para considerar que no
     *                                  hay ráfaga de pérdidas. Relacionado con el parámetro de ráfaga para la
     *                                  obtención del factor R
     *
     * @see FactorR
     */
    public MedidaEmodel(
            FormatoCodec formatoCodec ,
            ParametrosTransmision parametrosTransmision,
            int gmin
            ) {


        super(formatoCodec, parametrosTransmision);
        super.parametrosQoS =
                new FactorR [ super.parametrosTransmision.getNumeroIntervalos() ];
        this.gmin = gmin;

    }



    /**
     * Inicializa la medida sólo a partir de los parámetros de transmisión. Existen medidas
     * en las que el contenido de los datos no tiene por qué ser audio.
     * Se reserva espacio para representar tantas estimaciones del factor R del modelo E
     * como número de intervalos indiquen lso parámetros de transmisión.
     *
     *
     * @param parametrosTransmision     Parametros de transmisión utilizados para obtener esta medida
     * @param gmin                      Número mínimo de paquetes recibidos consecutivos para considerar que no
     *                                  hay ráfaga de pérdidas. Relacionado con el parámetro de ráfaga para la
     *                                  obtención del factor R
     *
     * @see FactorR
     */
    public MedidaEmodel(
            ParametrosTransmision parametrosTransmision,
            int gmin
            ) {


        super(parametrosTransmision);
        super.parametrosQoS =
                new FactorR [ super.parametrosTransmision.getNumeroIntervalos() ];
        this.gmin = gmin;

    }


     public float valorRepresentativo (int numeroIntervalo) {

        return super.parametrosQoS[numeroIntervalo].getValorParametro(0);
    }

}
