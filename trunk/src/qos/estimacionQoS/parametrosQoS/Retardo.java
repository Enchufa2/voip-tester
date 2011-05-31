package qos.estimacionQoS.parametrosQoS;

public class Retardo extends ParametroQoS {


    /**
     * Inicializa el array de medidas del parámetro. Cada valor <code>i</code> del parámetro
     * representa el retardo del paquete recibido <code>i</code>.
     *
     * Por defecto las unidades son <code>milisegundos</code>
     * 
     * @param numeroMedidas     El número de medidas que contiene. Se corresponde
     *                          con el tamaño del array.
     *
     */
    public Retardo (int numeroMedidas) {
        super(numeroMedidas);
        super.setUnidades("segundo", "NINGUNA");
        super.unidad.setPrefijoUnidadNum("mili");
    }

    /**
     * Inicializa el array de medidas del parámetro. Cada valor <code>i</code> del parámetro
     * representa el retardo del paquete recibido <code>i</code>.
     *
     * Por defecto las unidades son <code>milisegundos</code>
     *
     * @param numeroMedidas     El número de medidas que contiene. Se corresponde
     *                          con el tamaño del array.
     * @param valorInicial      El valor al que se inicializan los valores del parámetro
     */
    public Retardo (int numeroMedidas, float valorInicial) {
        super(numeroMedidas, valorInicial);
        super.setUnidades("segundo", "NINGUNA");
        super.unidad.setPrefijoUnidadNum("mili");
    }


    /**
     * Modifica un valor del parámetro.
     *
     * @param posicion             posicion del valor que se modifica
     * @param valorParametro       valor del parámetro en las unidades que correspondan
     */
    public void setValorParametro (int posicion, float valorParametro) {
        this.valoresParametro[posicion]=Math.abs(valorParametro);
    }

    /**
     * Devuelve el nombre del parámetro
     *
     * @return El nombre del parámetro
     */
    public String getNombreParametro() {

        return "Delay";
    }


     /**
     * Devuelve la magnitud que representa el parámetro
     *
     * @return la magnitud que representa el parámetro
     */
    public String getNombreMagnitud(){

        return "time";
    }

    /**
     * Devuelve el valor que mejor representa la medida del parámetro.
     * Cada medida de retardo se corresponde con un paquete enviado/recibido y
     * es independiente a la anterior. Por tanto es más representativo el
     * retardo medio.
     *
     * @return      El valor medio de retardo en milisegundos
     */
    public float valorRepresentativo () {

        return super.valorMedio();
    }

    

}

