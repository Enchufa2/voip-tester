
package qos.estimacionQoS.parametrosQoS;

import qos.estimacionQoS.mos.Emodel;
/**
 *
 * @author Antonio
 */
public class FactorR extends ParametroQoS {

    /**
     * Contiene el factor R obtenido a partir del Emodel.
     * El parámetro es adimensional
     *
     * El factor R es una medida orientada a un conjunto de datos/mensajes
     * por lo que el parámetro queda representado por un solo valor.
     * 
     */
    public FactorR () {
        super(1);
    }



    /**
     * Contiene el factor R obtenido a partir del Emodel
     * El parámetro es adimensional
     *
     * @param valorInicial      El valor al que se inicializan los valores del parámetro
     */
    public FactorR ( float valorInicial) {
        super(1, valorInicial);
    }


     
    /**
     * Devuelve el nombre del parámetro
     * 
     * @return El nombre del parámetro
     */
    public String getNombreParametro() {
        
        return "E-model (R)";
    }


    /**
     * Devuelve la magnitud que representa el parámetro
     *
     * @return la magnitud que representa el parámetro
     */
    public String getNombreMagnitud(){

        return "R";
    }


    /**
     * Devuelve el valor que mejor representa la medida del parámetro, siendo en
     * este caso el único valor que alberga.
     *
     * @return      El valor del factor R que representa el parámetro
     */
    public float valorRepresentativo () {
        return super.valoresParametro[0];
    }

    /**
     * Realiza el cambio de factor R a nota MOS.
     */
    public float valorMos() {

        return Emodel.calcularMOS(super.valoresParametro[0]);
    }

}
