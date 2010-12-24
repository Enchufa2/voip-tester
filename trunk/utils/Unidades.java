/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

/**
 *
 * @author Antonio
 */
public class Unidades {

    private Unidad unidadNum;
    private Unidad unidadDem;

    public Unidades() {

        this.unidadNum = new Unidad();
        this.unidadDem = new Unidad();
    }
    
    public void setUnidadNum(String unidadNum) {

        this.unidadNum.setUnidad(unidadNum);
        
    }

    public void setUnidadDem(String unidadDem) {

        this.unidadDem.setUnidad(unidadDem);

    }

    public Unidad getUnidadNum() {

        return this.unidadNum;
    }

    public Unidad getUnidadDem() {

        return this.unidadDem;
    }


    public void setPrefijoUnidadNum(String prefijo) {

        this.unidadNum.setPrefijo(prefijo);
    }

     public void setPrefijoUnidadDem(String prefijo) {

        this.unidadDem.setPrefijo(prefijo);
    }

    public float getValorUnidadBasica(float valor) {

        return valor* this.unidadNum.getFactorEscala()/this.unidadDem.getFactorEscala();
    }

    public String toString() {

        String string="";
        if(this.unidadNum.unidad != TipoUnidad.NINGUNA)
            string += this.unidadNum.toString();

        if(this.unidadDem.unidad != TipoUnidad.NINGUNA) {
            if(this.unidadNum.unidad == TipoUnidad.NINGUNA)
                string += "1/";
            else
                string += "/" + this.unidadDem.toString();
        }

        return string;
    }


    public class Unidad {

        private TipoUnidad unidad = TipoUnidad.NINGUNA;
        private TipoPrefijo prefijo = TipoPrefijo.nulo;

        public Unidad() {
            
        }

        public void setPrefijo(String prefijo) {
            this.prefijo = TipoPrefijo.valueOf(prefijo);
        }

        public void setUnidad(String nombreUnidad) {

            this.unidad = TipoUnidad.valueOf(nombreUnidad);
        }

        public String toString() {

            return "" + this.prefijo.simbolo + this.unidad.simbolo;
        }

        public float getFactorEscala() {

            return ((float) Math.pow(
                    this.unidad.base,
                    this.prefijo.exponente * this.unidad.factorEscalaExponente
                    ));
        }
        
    }

    public enum TipoUnidad{

       NINGUNA(""),
       Byte("B", 2, 10),
       segundo("s"),
       paquete("packet");

        String simbolo;
        int base = 10;
        int factorEscalaExponente = 1;

        TipoUnidad(String nombre) {
            this.simbolo = nombre;
        }

        TipoUnidad(String nombre, int base, int factorEscalaExponente) {

            this.simbolo = nombre;
            this.base = base;
            this.factorEscalaExponente = factorEscalaExponente;
            
        }
        
    }

    public enum TipoPrefijo {
        peta("P", 15),
        tera("T", 12),
        giga("G", 9),
        mega("M", 6),
        kilo("K", 3),
        hecto("H", 2),
        deca("Da", 1),
        nulo("",0),
        deci("d",-1),
        ceti("c", -2),
        mili("m", -3),
        micro("u", -6),
        nano("n", -9),
        pico("p", -12),
        femto("f", -15);

        int exponente=1;
        String simbolo="";

        TipoPrefijo(String nombre, int exponente) {
            this.simbolo = nombre;
            this.exponente = exponente;
        }
    }



}
