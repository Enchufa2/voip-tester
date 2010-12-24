
package qos.estimacionQoS.parametrosQoS;

/**
 *
 * @author Antonio
 */
public class MosPesq extends ParametroQoS {

    
    /**
     * Cada valor del parámetro contiene una nota MOS obtenida a través del test PESQ
     * El parámetro es adimensional
     *
     * El Mos Pesq es una medida orientada a un conjunto de datos/mensajes
     * por lo que el parámetro queda representado por un solo valor.
     * 
     */
    public MosPesq () {

        super(1);
    }


    /**
     * Devuelve el nombre del parámetro
     *
     * @return El nombre del parámetro
     */
    public String getNombreParametro() {

        return "PESQ (MOS)";
    }

     /**
     * Devuelve la magnitud que representa el parámetro
     *
     * @return la magnitud que representa el parámetro
     */
    public String getNombreMagnitud(){

        return "";
    }


    /**
     * Devuelve el valor que mejor representa la medida del parámetro, siendo en
     * este caso el único valor que alberga.
     *
     * El parámetro es adimensional
     *
     * @return      El valor Mos que representa el parámetro
     */
    public float valorRepresentativo () {
        return super.valoresParametro[0];
    }

    
}
