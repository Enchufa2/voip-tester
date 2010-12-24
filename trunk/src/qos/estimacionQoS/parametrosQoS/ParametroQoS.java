package qos.estimacionQoS.parametrosQoS;

import java.text.DecimalFormat;
import utils.Unidades;


public abstract class ParametroQoS{

    /**
     * Valores medidos del parametro. Cada elemento del array se corresponde
     * con un valor medido referente a un paquete.
     */
    protected float[] valoresParametro;


    /**
     * Unidades de los valores del parámetro
     * @see Unidades
     */
    protected Unidades unidad;
    

    /**
     * Valor al que se inicializan los valores del parámetro. 
     */
    public static final float VALOR_DEFECTO=Float.NEGATIVE_INFINITY;

    
    /**
     * Inicializa el array de medidas del parámetro. El valor por defecto
     * es <code>VALOR_DEFECTO</code>, de manera que si un elemento del array contiene este
     * valor significa que no contiene un valor medido del parámetro.
     *
     * @param numeroMedidas     El número de valores medidos que contiene. Se corresponde
     *                          con el tamaño del array.
     */
    public ParametroQoS (int numeroMedidas) {
        this.valoresParametro=new float[numeroMedidas];
        for (int i=0; i<numeroMedidas; i++) {
            this.valoresParametro[i]=ParametroQoS.VALOR_DEFECTO;
           
        }
        this.unidad = new Unidades();
    }

    /**
     * Inicializa el array de medidas del parámetro. El valor por defecto
     * es <code>valorInicial</code>.
     *
     * @param numeroMedidas     El número de valores medidos que contiene. Se corresponde
     *                          con el tamaño del array.
     * @param valorInicial      El valor al que se inicializan los valores del parámetro
     */
    public ParametroQoS (int numeroMedidas, float valorInicial) {
        this.valoresParametro=new float[numeroMedidas];
        for (int i=0; i<numeroMedidas; i++) {
            this.valoresParametro[i]=valorInicial;

        }
        this.unidad = new Unidades();
    }



    /**
     * Devuelve los valores del parámetro, en las unidades que correspondan.
     *
     * @return valoresParametro    valores del parámetro en las unidades que correspondan.
     */
    public float[] getValoresParametro () {
        return this.valoresParametro;
    }


    /**
     * Fija los valores del parámetro
     *
     * @param valoresParametro    Valores del parámetro en las unidades que correspondan.
     */
    public void setValoresParametro (float[] valoresParametro) {
        this.valoresParametro = valoresParametro;
    }


    /**
     * Devuelve la unidad de los valores del parámetro
     *
     * @return la unidad de los valores parámetro
     */
    public Unidades getUnidad() {

        return this.unidad;

    }    
    

    /**
     * Asigna las unidades en las que se encuentran los valores del parámetro
     *
     * @param   unidadesNum     Unidades del numerador de los valores del parámetro
     * @param   unidadesDem     Unidades del denominador de los valores del parámetro
     */
    public void setUnidades(String unidadesNum, String unidadesDem) {

        this.unidad.setUnidadNum(unidadesNum);
        this.unidad.setUnidadDem(unidadesDem);
    }


    /**
     * Devuelve el nombre del parámetro
     *
     * @return El nombre del parámetro
     */
    public abstract String getNombreParametro();


    /**
     * Devuelve la magnitud que representa el parámetro
     *
     * @return la magnitud que representa el parámetro
     */
    public abstract String getNombreMagnitud();

    /**
     * Devuelve el valor máximo de todos los valores del parámetro.
     * Se descarta el valor ParametroQoS.VALOR_DEFECTO ya que significa ausencia de valor medido.
     *
     * @return valorMaximo     El valor máximo del parámetro en las unidades que correspondan
     *
     */
    public float valorMaximo(){
        
        float valorMaximo=Float.NEGATIVE_INFINITY;
        for (int i=0; i<this.valoresParametro.length; i++ ) {
            if (this.valoresParametro[i]!=ParametroQoS.VALOR_DEFECTO) {
                if (this.valoresParametro[i] > valorMaximo) {
                    valorMaximo=this.valoresParametro[i];
                }
            }
        }

        if(valorMaximo == Float.NEGATIVE_INFINITY)
            return Float.NaN;
        
        return valorMaximo;
    }


    /**
     * Devuelve el valor mínimo de todos los valores del parámetro
     * Se descarta el valor ParametroQoS.VALOR_DEFECTO ya que significa ausencia de valor medido.
     *
     * @return valorMinimo     El valor mínimo del parámetro en las unidades que correspondan
     */
    public float valorMinimo(){
        float valorMinimo=Float.POSITIVE_INFINITY;
        for (int i=0; i<this.valoresParametro.length; i++ ) {
            if (this.valoresParametro[i]!= ParametroQoS.VALOR_DEFECTO) {
                if (this.valoresParametro[i] < valorMinimo) {
                    valorMinimo=this.valoresParametro[i];
                }
            }
        }
        if(valorMinimo == Float.POSITIVE_INFINITY)
            return Float.NaN;

        return valorMinimo;
    }


    /**
     * Calcula la media de los valores del parámetro
     *
     * @return valorMedio   El valor medio en las unidades que correspondan.
     *
     */
    public float valorMedio() {
        float valorMedio=0.0f;
        int valoresValidos=0;
        for (int i=0; i<this.valoresParametro.length; i++) {
            if (this.valoresParametro[i]!=ParametroQoS.VALOR_DEFECTO) {
                valorMedio+=this.valoresParametro[i];
                valoresValidos++;
            }
        }
        if(valoresValidos==0.0f){
            return Float.NaN;
        }
        return valorMedio/valoresValidos;
    }


    /**
     * Calcula la media de un subconjunto de valores del parámetro
     * Se descarta el valor ParametroQoS.VALOR_DEFECTO ya que significa ausencia de valor medido.
     *
     * @param posicionInicial   Posicion del primer valor del subconjunto
     * @param posicionFinal     Posicion del último valor del subconjunto
     *
     */
    public float valorMedio(int posicionInicial, int posicionFinal) {
        float valorMedio=0.0f;
        int valoresValidos=0;
        for (int i=posicionInicial; i<posicionFinal; i++) {
            if (this.valoresParametro[i]!=ParametroQoS.VALOR_DEFECTO) {
                valorMedio+=this.valoresParametro[i];
                valoresValidos++;
            }
        }

        if(valoresValidos==0.0f){
            return ParametroQoS.VALOR_DEFECTO;
        }
        return valorMedio/valoresValidos;
    }


    /**
     * Modifica un valor del parámetro.
     *
     * @param posicion             posicion del valor que se modifica
     * @param valorParametro       valor del parámetro en las unidades que correspondan
     */
    public void setValorParametro (int posicion, float valorParametro) {
        this.valoresParametro[posicion]=valorParametro;
    }

    /**
     * Devuelve un valor del parámetro
     *
     * @param posicion      Posición del valor que se devuelve
     * @return              El valor correspondiente a la posición indicada
     *
     */
    public float getValorParametro (int posicion) {

        return this.valoresParametro[posicion];
    }


    /**
     * Devuelve un valor del parámetro en la unidad básica valor*10^factorEscala
     *
     * @param posicion      Posición del valor que se devuelve
     * @return              El valor en la unidad básica correspondiente a la posición indicada
     *
     */
    public float getValorParametroUnidadBasica (int posicion) {

        return this.unidad.getValorUnidadBasica(
                this.valoresParametro[posicion]
                );
    }


    /**
     * Comprueba si un valor concreto del parámetro es válido
     *
     * @param posicion      Posicion del valor a comprobar
     * @return <code>true</code>    Si el valor es válido
     */
    public boolean esValorValido (int posicion) {

        if (this.valoresParametro[posicion]
                == ParametroQoS.VALOR_DEFECTO) return false;
        else return true;
    }


    /**
     * Devuelve la longitud del vector que contiene los valores del parámetro
     *
     * @return      El numero de medidas o valores estimados
     */
    public int numeroDeValores() {

        return this.valoresParametro.length;
    }

    
    /**
     * Devuelve el valor final más representativo del parámetro,
     * obtenido a partir de los valores del mismo.
     * Según el parámetro que se esté calculando, el cálculo del valor óptimo
     * puede variar.
     */
    public abstract float valorRepresentativo();


    /**
     * Representación del objeto mediante caracteres
     */
    @Override
    public String toString(){

        String parametro= this.getNombreParametro() +" " + this.unidad + '\t';
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.setMaximumFractionDigits(3);
        int numeroValores = this.valoresParametro.length;
        float f;
        for(int i=0; i<numeroValores; i++) {
            f = this.valoresParametro[i];
            parametro += df.format(f) + '\t';
        }
        return parametro;
    }

    /**
     * Comprueba si dos parametros son iguales
     */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object parametroQoS){

        ParametroQoS pQ = (ParametroQoS) parametroQoS;
        boolean iguales = true;
        int numeroValores = this.valoresParametro.length;
        if ( numeroValores != pQ.getValoresParametro().length) {

            iguales = false;

        }else {

            for (int i=0; i<numeroValores; i++) {

                if(this.valoresParametro[i] != pQ.getValorParametro(i)) {
                    iguales = false;
                    break;
                }

            }
        }

        return iguales;
    }


  
}

