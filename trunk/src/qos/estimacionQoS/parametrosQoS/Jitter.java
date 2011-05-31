package qos.estimacionQoS.parametrosQoS;

public class Jitter extends ParametroQoS {


    /**
     * Inicializa el array de medidas del parámetro. Cada valor del parámetro representa
     * la diferencia de retardo entre paquetes, es decir, cada valor <code>i</code> contiene
     * la diferencia de retardo entre el paquete recibido <code>i</code> e <code>i-1</code>.
     *
     * Por defecto las unidades son <code>milisegundos</code>
     *
     * @param numeroMedidas     El número de medidas que contiene. Se corresponde
     *                          con el tamaño del array.
     */
    public Jitter (int numeroMedidas) {
        super(numeroMedidas);
        super.setUnidades("segundo", "NINGUNA");
        super.unidad.setPrefijoUnidadNum("mili");
    }

    
    /**
     * Inicializa el array de medidas del parámetro. Cada valor del parámetro representa
     * la diferencia de retardo entre paquetes, es decir, cada valor <code>i</code> contiene
     * la diferencia de retardo entre el paquete recibido <code>i</code> e <code>i-1</code>.
     *
     * Por defecto las unidades son <code>milisegundos</code>
     *
     * @param numeroMedidas     El número de medidas que contiene. Se corresponde
     *                          con el tamaño del array.
     * @param valorInicial      El valor al que se inicializan los valores del parámetro
     */
    public Jitter (int numeroMedidas, float valorInicial) {
        super(numeroMedidas, valorInicial);
        super.setUnidades("segundo", "NINGUNA");
        super.unidad.setPrefijoUnidadNum("mili");
    }



     /**
     * Devuelve el valor máximo, en valor absoluto,  de todos los valores del parámetro.
     * Se descarta el valor ParametroQoS.VALOR_DEFECTO ya que significa ausencia de valor medido.
     *
     * @return valorMaximo     El valor máximo del parámetro en las unidades que correspondan
     *
     */
    @Override
    public float valorMaximo(){
        float valorMaximo=Float.NEGATIVE_INFINITY;

        for (int i=0; i<super.valoresParametro.length; i++ ) {
            if (super.valoresParametro[i]!=ParametroQoS.VALOR_DEFECTO) {
                if ( Math.abs(super.valoresParametro[i]) > valorMaximo) {
                    valorMaximo=super.valoresParametro[i];
                }
            }
        }

         if(valorMaximo == Float.NEGATIVE_INFINITY)
            return Float.NaN;
        
        return valorMaximo;
    }


    /**
     * Devuelve el valor mínimo, en valor absoluto, de todos los valores del parámetro
     *
     * @return valorMinimo     El valor mínimo del parámetro en las unidades que correspondan
     */
    @Override
    public float valorMinimo(){
        float valorMinimo=Float.POSITIVE_INFINITY;

        for (int i=0; i<super.valoresParametro.length; i++ ) {
            if (super.valoresParametro[i]!=ParametroQoS.VALOR_DEFECTO) {
                if (Math.abs(super.valoresParametro[i]) < valorMinimo) {
                    valorMinimo=super.valoresParametro[i];
                }
            }
        }

         if(valorMinimo == Float.POSITIVE_INFINITY)
            return Float.NaN;
        
        return valorMinimo;
    }


    /**
     * Calcula la media de los valores del parámetro, en valor absoluto
     *
     * @return valorMedio   El valor medio en las unidades que correspondan.
     *
     */
    @Override
    public float valorMedio() {
        float valorMedio=0.0f;
        int valoresValidos=0;
        for (int i=0; i<super.valoresParametro.length; i++) {
            if (super.valoresParametro[i]!=ParametroQoS.VALOR_DEFECTO) {
                valorMedio+=Math.abs(super.valoresParametro[i]);
                valoresValidos++;
            }
        }
        if(valoresValidos==0.0f){
            return ParametroQoS.VALOR_DEFECTO;
        }
        return valorMedio/valoresValidos;
    }




    /**
     * Calcula la media de un subconjunto de valores del parámetro, en valor absoluto
     * Se descarta el valor ParametroQoS.VALOR_DEFECTO ya que significa ausencia de valor medido.
     *
     * @param posicionInicial   Posicion del primer valor del subconjunto
     * @param posicionFinal     Posicion del último valor del subconjunto
     *
     * @return valorMedio   El valor medio en las unidades que correspondan.
     *
     */
    @Override
    public float valorMedio(int posicionInicial, int posicionFinal) {
        float valorMedio=0.0f;
        int valoresValidos=0;
        for (int i=posicionInicial; i<posicionFinal; i++) {
            if (super.valoresParametro[i]!=ParametroQoS.VALOR_DEFECTO) {
                valorMedio+=Math.abs(super.valoresParametro[i]);
                valoresValidos++;
            }
        }

        if(valoresValidos==0.0f){
            return ParametroQoS.VALOR_DEFECTO;
        }
        return valorMedio/valoresValidos;
    }



    /**
     * Devuelve el nombre del parámetro
     *
     * @return El nombre del parámetro
     */
    public String getNombreParametro() {

        return "Jitter";
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
     * Devuelve el valor que mejor representa la medida del parámetro. Este se obtiene
     * aplicando un suavizado segun el RFC 1889 Apéndice A.8:
     *
     * Jopt(n)=Jopt(n-1)+(|J(n)|-Jopt(n-1))/16
     * 
     *
     * @return      El valor óptimo en las unidades que correspondan
     */
    public float valorRepresentativo () {
       
        float valorOptimo = super.VALOR_DEFECTO;
        int valoresValidos=0;
        for (int i=0; i<super.valoresParametro.length; i++) {
             
            if (super.valoresParametro[i] != super.VALOR_DEFECTO) {
                valoresValidos++;
                
                if(valoresValidos > 1) {
                    valorOptimo +=(Math.abs(super.valoresParametro[i]) - valorOptimo)/16;

                }else {
                    valorOptimo = super.valoresParametro[i];
                }
            }
        }
        return valorOptimo;
    }


}

