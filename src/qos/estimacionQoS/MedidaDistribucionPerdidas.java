/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.DistribucionPerdidas;
//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import audio.FormatoCodec;


/**
 *
 * @author Antonio
 */
public class MedidaDistribucionPerdidas extends MedidaQoS{


    /**
     * Se inicializa la  medida de acuerdo a las variables de transmisión de audio.
     * Existen medidas en las que es necesario disponer de datos de audio para su
     * estimación.
     * Se reserva espacio para representar tantas estimaciones de distribucion de pérdidas
     * como número de intervalos indiquen lso parámetros de transmisión.
     *
     *
     * @param formatoCodec      Propiedades de los datos de audio utilizados para obtener esta medida
     * @param parametrosTransmision   Parametros de transmisión de audio utilizados para obtener esta medida
     *
     * @see DistribucionPerdidas
     */
    public MedidaDistribucionPerdidas(
            FormatoCodec formatoCodec ,
            ParametrosTransmision parametrosTransmision
            ){

        super(formatoCodec, parametrosTransmision);
        super.parametrosQoS =
                new DistribucionPerdidas [ super.parametrosTransmision.getNumeroIntervalos() ];

    }


    /**
     * Inicializa la medida sólo a partir de los parámetros de transmisión. Existen medidas
     * en las que el contenido de los datos no tiene por qué ser audio.
     *
     * Se reserva espacio para representar tantas estimaciones de distribución de pérdidas
     * como número de intervalos indiquen los parámetros de transmisión.
     *
     * @param parametrosTransmision
     * @see DistribucionPerdidas
     */
    public  MedidaDistribucionPerdidas(
            ParametrosTransmision parametrosTransmision
            ) {

        super(parametrosTransmision);
        super.parametrosQoS =
                new DistribucionPerdidas [ super.parametrosTransmision.getNumeroIntervalos() ];

    }



    public float valorRepresentativo (int numeroIntervalo) {

        return 0;
    }


    /**
     * Devuelve los valores del eje de abcisas para cada intervalo, que se corresponde
     * con el tamañao de la ráfaga en paquetes
     *
     * @return  la matriz con los valores donde cada fila representa los valores del eje
     */
    public float[][] valoresEjeX(){

        int numeroIntervalos = this.parametrosTransmision.getNumeroIntervalos();
        int rafagaMaxima = this.parametrosQoS[0].numeroDeValores();
        float[][] ejeX = new float[numeroIntervalos][rafagaMaxima];

        for(int i=0; i<numeroIntervalos; i++) {

            for(int j=0; j<rafagaMaxima; j++) {
                ejeX[i][j] = j+1;
            }
        }

        return ejeX;
    }


     public String unidadesEjeXtoString() {

        return "messages";
    }

    public String nombreEjeX() {

        return "Burst length";
    }
}
