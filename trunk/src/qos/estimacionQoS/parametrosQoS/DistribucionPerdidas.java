/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.estimacionQoS.parametrosQoS;

/**
 *
 * @author Antonio
 */
public class DistribucionPerdidas extends ParametroQoS{


    /**
     * Inicializa el array de medidas del parámetro.
     * Si i es el índice de cada elemento, el contenido es el número veces que se
     * da la ráfaga de tamaño i+1.
     * Por defecto, se inicializa el array considerando que no hay ráfaga, es decir,
     * con todos los valores a cero.
     *
     * Por defecto las unidades del parámetro son <code>paquetes</code>
     *
     * @param numeroMedidas     El número de valores medidos que contiene. Se corresponde
     *                          con el tamaño del array.
     */
    public DistribucionPerdidas (int numeroMedidas) {
        super(numeroMedidas,0);
        super.setUnidades("NINGUNA","NINGUNA");
    }


    /**
     * Inicializa el array de medidas del parámetro.
     * Si i es el índice de cada elemento, el contenido es el número veces que se
     * da la ráfaga de tamaño i+1.
     *
     * Por defecto las unidades del parámetro son <code>paquetes</code>
     * 
     * @param rafagaMaxima      El tamaño máximo de ráfaga contemplado en el parámetro
     * @param valorInicial      El valor al que se inicializan los valores del parámetro
     */
    public DistribucionPerdidas(int rafagaMaxima, float valorInicial) {

        super(rafagaMaxima, valorInicial);
        super.setUnidades("NINGUNA","NINGUNA");

    }


    /**
     * Devuelve el nombre del parámetro
     *
     * @return El nombre del parámetro
     */
    public String getNombreParametro() {

        return "Loss distribution";
    }

     /**
     * Devuelve la magnitud que representa el parámetro
     *
     * @return la magnitud que representa el parámetro
     */
    public String getNombreMagnitud(){

        return "repetitiveness";
    }

    
    /**
     * Devuelve el valor que mejor representa la medida del parámetro.
     * Dado que para la distribución de pérdidas no existe un valor óptimo como tal,
     * el valor más representativo será la ráfaga de pérdidas promedio
     *
     * @return      El tamaño de ráfaga promedio de todos los tamaños considerados
     */
    public float valorRepresentativo () {

        return this.valorMedio();
    }

   /**
     * Calcula el tamaño de ráfaga medio de todas las ráfagas consideradas
     *
     * @return valorMedio   El valor medio en las unidades que correspondan.
     *
     */
    @Override
    public float valorMedio() {

        float rafagaPromedio = .0f;
        int perdidasTotales = 0;

        for (int i=0; i<super.valoresParametro.length; i++) {

            rafagaPromedio += super.valoresParametro[i]*i;
            perdidasTotales += super.valoresParametro[i];
        }

        if (perdidasTotales == 0) {

            return 0f;
        }
        return rafagaPromedio/perdidasTotales;

    }


    /**
     * Obtiene el tamaño de ráfaga promedio considerando un tamaño mínimo y
     * máximo de ráfaga.
     *
     * @param rafagaMinima      El tamaño mínimo de ráfaga en el promedio
     * @param rafagaMaxima      El tamaño máximo de ráfaga en el promedio
     * @return valoresMedio     El tamaño medio de las ráfagas comprendidas entre
     *                          entre <code>rafagaMinimo</code> y <code>rafagaMaxima</code>
     */
    @Override
    public float valorMedio(int rafagaMinima, int rafagaMaxima){

        float rafagaPromedio = .0f;
        int perdidasTotales = 0;

        for (int i=rafagaMinima; i<rafagaMaxima; i++) {

            rafagaPromedio += super.valoresParametro[i]*i;
            perdidasTotales += super.valoresParametro[i];
        }

        if (perdidasTotales == 0) {

            return 0f;
        }
        return rafagaPromedio/perdidasTotales;
    }

}
