package qos.estimacionQoS.parametrosQoS;


/**
 */
public class AnchoBanda extends ParametroQoS {


    /**
     * Inicializa el array de medidas del parámetro.
     * El elemento i representa el retardo para el paquete i.
     *
     * Por defecto las unidades del parámetro son <code>KB/s</code>
     *
     * @param numeroMedidas     El número de medidas que contiene. Se corresponde
     *                          con el tamaño del array.
     */
    public AnchoBanda (int numeroMedidas) {
        super(numeroMedidas);
        super.setUnidades("Byte", "segundo");
        super.unidad.setPrefijoUnidadNum("kilo");
    }


    /**
     * Inicializa el array de medidas del parámetro.
     * El elemento i representa el retardo para el paquete i.
     *
     * Por defecto las unidades del parámetro son <code>KB/s</code>
     *
     * @param numeroMedidas     El número de medidas que contiene. Se corresponde
     *                          con el tamaño del array.
     * @param valorInicial      El valor al que se inicializan los valores del parámetro
     */
    public AnchoBanda (int numeroMedidas, float valorInicial) {
        super(numeroMedidas, valorInicial);
        super.setUnidades("Byte", "segundo");
        super.unidad.setPrefijoUnidadNum("kilo");
    }


   /**
     * Devuelve el nombre del parámetro
     *
     * @return El nombre del parámetro
     */
    public String getNombreParametro() {

        return "Bandwidth";
    }

    /**
     * Devuelve la magnitud que representa el parámetro
     *
     * @return la magnitud que representa el parámetro
     */
    public String getNombreMagnitud(){

        return "bitrate";
    }
   
    /**
     * Devuelve el valor que mejor representa la medida del parámetro.
     * El ancho de banda se mide mejor cuanto más datos se reciban de manera 
     * consecutiva. Sin embargo la variabilidad del retardo que sufren los datos
     * puede provocar que algunos valores se alejen del valor verdadero. Por tanto
     * para proporcionan un valor óptimo adecuado se realiza un suavizado de todos
     * los valores según:
     *      ABopt(n) = ABopt(n-1) +(AB(n)-ABopt(n-1))/10
     * 
     *
     * @return      El valor óptimo de ancho de banda en bytes/segundo
     */
    public float valorRepresentativo () {

        float valorOptimo = ParametroQoS.VALOR_DEFECTO;
        int valoresValidos=0;
        for (int i=0; i<super.valoresParametro.length; i++) {

            if (super.valoresParametro[i] != ParametroQoS.VALOR_DEFECTO) {
                valoresValidos++;

                if(valoresValidos > 1) {
                    valorOptimo +=(super.valoresParametro[i] - valorOptimo)/10;

                }else {
                    valorOptimo = super.valoresParametro[i];
                }
            }
        }
        return valorOptimo;
    }


}

