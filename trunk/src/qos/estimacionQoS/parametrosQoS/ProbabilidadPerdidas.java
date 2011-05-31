package qos.estimacionQoS.parametrosQoS;


public class ProbabilidadPerdidas extends ParametroQoS {



    /**
     * Inicializa el array de medidas del parámetro.
     * La probabilidad de pérdidas es una medida orientada a un conjunto de datos/mensajes
     * por lo que el parámetro queda representado por un solo valor.
     *
     */
    public ProbabilidadPerdidas () {
        super(1);
    }


    /**
     * Inicializa el array de medidas del parámetro
     * La probabilidad de pérdidas es una medida orientada a un conjunto de datos/mensajes
     * por lo que el parámetro queda representado por un solo valor.
     * 
     * El parámetro es adimensional
     *
     * @param valorInicial      El valor al que se inicializan los valores del parámetro
     */
    public ProbabilidadPerdidas (float valorInicial) {
        super(1, valorInicial);
    }


    /**
     * Devuelve el nombre del parámetro
     *
     * @return El nombre del parámetro
     */
    public String getNombreParametro() {

        return "Loss probability";
    }

     /**
     * Devuelve la magnitud que representa el parámetro
     *
     * @return la magnitud que representa el parámetro
     */
    public String getNombreMagnitud(){

        return "Probability";
    }

    /**
     * Devuelve el valor que mejor representa la medida del parámetro, siendo
     * en este caso el único valor que alberga.
     *
     * El parámetro es adimensional
     *
     * @return      El valor de probabilidad de pérdidas que representa el parámetro
     */
    public float valorRepresentativo () {

        return super.valoresParametro[0];
    }

    
   


}

