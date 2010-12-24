package qos.estimacionQoS.mos;

/**
 * Adaptación a Java del programa g107_4.
 * Esta versión está conforme con el algoritmo descrito en la Recomendación
 * G.107 (2003) de la ITU-T, para la comunicación de voz entre un extremo (S) y
 * otro (R).
 *
 * El modelo E proporciona uan estimacion de la calidad de la transmisión de voz,
 * boca-a-oreja, que se percibe en el lado (R). El parámetro que se calcula es un
 * factor de transmisión R que puede transformarse para estimar el MOS.
 *
 * Esta clase incluye un método <code>main</code> que cálcula y muestra
 * por la salida estándar el R con los valores por defecto. La clase permite
 * instanciar un objeto cuyos atributos son todos los parámetros del modelo, que
 * pueden modificarse.
 * 
 *
 * @author Antonio Sánchez Navarro. E.T.S. Telecomunicaciones. Universidad de Granada.
 *
 */

public class Emodel {

    /**índice de sonoridad en emision (dB)*/
    float slr=8.0f;
    /**índice de sonoridad en recepcion (dB)*/
    float rlr=2.0f;
    /**índice de enmascaramiento para el efecto local (dB)*/
    float stmr=15.0f;
    /**Valor D del teléfono, lado emisor.*/
    float ds=3.0f;
    /**valor D del teléfono, lado recepción*/
    float dr=3.0f;
    /**Índice de sonoridad del eco para el hablante(dB)*/
    float telr=65.0f;
    /**Pérdida de trayecto de eco ponderado (dB)*/
    float wepl=110.0f;
    /**Retardo medio en un sentido del trayecto de eco (ms)*/
    float t=0f;
    /**Retardo de ida y vuelta en un bucle a4 hilos (ms)*/
    float tr=0f;
    /**Retardo absoluto en conexiones sin eco (ms)*/
    float ta=0f;
    /**Número de unidades de distorsión decuantificación*/
    float qdu=1.0f;
    /**Factor de degradación de equipo*/
    float ie=0f;
    /**Factor de robustez contra pérdida de paquetes*/
    float bpl=1.0f;
    /**Probabilidad de pérdida de paquetes aleatoria (%)*/
    float ppl=0f;
    /**Relación de ráfaga*/
    float burstR=1.0f;
    /**Ruido de circuito referido al punto de 0 dBr (dBm0p)*/
    float nc=-70.0f;
    /**Nivel de ruido en el lado recepción (dBmp)*/
    float nfor=-64.0f;
    /**Ruido ambiente en el lado emisor (dB(A))*/
    float ps=35.0f;
    /**Ruido ambiente en el lado receptor (dB(A))*/
    float pr=35.0f;
    /**factor de mejora*/
    float A=0.0f;             

    public Emodel () {
    }

   /**
    * Obtiene el parámetro R de acuerdo a las fórmulas del apartado 3.1 de la
    * recomendación G.107-200503. Para los valores por defecto debe obtenerse
    * un valor de R=93.2.
    *
    * @return El parámetro de determinación de índices de transmisión (R)
    */
    public float calcularR() {
        float R=-1;
        /**Obtenemos la relación señal ruido básica (Ro) según el apartado 3.2*/
        //Suma de ruido. Fórmulas (3) a (7)
        float Nos= (float) ( ps - slr - ds - 100 + 0.004*Math.pow(ps - slr -rlr - ds - 14,2) ) ;
        // definición del índice de efecto local para el oyente (dB)
        float lstr=stmr+dr;
        float pre = (float) ( pr + 10*Math.log10(1+ Math.pow(10, (10 - lstr)/10)) );
        float Nor = (float) ( rlr - 121 + pre + 0.008*Math.pow(pre - 35, 2) );
        float nfo = nfor + rlr;
        float No = (float) ( 10*Math.log10(Math.pow(10, nc/10)+ Math.pow(10, Nos/10)
                + Math.pow(10, Nor/10) + Math.pow(10, nfo/10)) );
        //relacion señal ruido básica, fórmula (2)
        float Ro= (float) ( 15 - 1.5 * (slr + No) );

        /**Obtenemos el factor de degradaciones simultáneas según el apartado 3.3*/
        //iolr, fórmulas (9) y (10)
        float xolr = (float) ( slr + rlr + 0.2 * (64 + (No - rlr)) );
        float iolr = (float) ( 20 * ( Math.pow((1+ Math.pow(xolr/8,8.0)),1.0/8) - xolr/8) );
        //ist, fórmulas (11) y (12)
        float stmro = (float) ( -10 * Math.log10(Math.pow(10, -stmr/10) + ( Math.exp(-t/4)
                * Math.pow(10, -telr/10))) );
        float ist = (float) ( 12 * Math.pow( 1 + Math.pow((stmro-13)/6,8), 1.0/8 )
                -28 * Math.pow( 1 + Math.pow((stmro+1)/19.4,35), 1.0/35 )
                -13 * Math.pow( 1 + Math.pow((stmro-3)/33,13), 1.0/13 ) + 29 );

        //iq. formulas (13) a (17)
        float Q = (float) ( 37 - 15*Math.log10(qdu) );
        float G =(float) ( 1.07 + 0.258 * Q + 0.0602 * Math.pow(Q,2) );
        float iq =(float) ( 15*Math.log10(1 + Math.pow(10, (Ro - 100)/15 + 46/8.4 - G/9)
                + Math.pow(10, 46.0/30 - G/40)) );
        
        //is formula (8)
        float is= iolr + ist + iq;

        /**Obtenemos el factor de degradación por retardo según el apartado 3.4*/
        //TERV, formula (22)
        float terv =(float) ( telr + 6*Math.exp(-0.3*Math.pow(t, 2)) -
                40*Math.log10((1 + t/10)/(1 +t/150)) );
        //modificaciones para satisfacer fórmula (23)
        if (stmr < 9) {
            terv = terv + ist/2;
        }

        //Idte, fórmulas (19) a (21)
        float Re =(float) ( 80 + 2.5 * (terv -14) );
        float Roe =(float) ( -1.5 * (No - rlr));
        float xdt = (Roe -Re)/2;
        float idte = (float) (xdt + Math.sqrt(Math.pow(xdt, 2)+100) );
        idte = (float) ((idte -1.0) * (1.0 - Math.exp(-t)) );

        //modificaciones para satisfacer formula (24)
        if (stmr >20) {
            idte =(float) ( Math.sqrt(Math.pow(idte, 2)+Math.pow(ist, 2)) );
        }

        //idle, formulas (25) y (26)
        float Rle =(float) ( 10.5*(wepl +7)*Math.pow(tr+1, -0.25) );
        float xdl =(Ro - Rle)/2;
        float idle =(float) ( xdl + Math.sqrt(Math.pow(xdl, 2)+169) );

        //idd, formulas (27) y (28)
        float idd=0;
        if (ta > 100.0) {
            float X =(float) ( Math.log(ta/100)/Math.log(2) );
            idd = (float) ( 25 * (Math.pow(1 + Math.pow(X, 6), 1.0/6)
                    -3*Math.pow( 1+ Math.pow(X/3, 6), 1.0/6) + 2) );
        }

        // id
        float id = idte + idle + idd;

        /**Obtenemos el factor de pérdida de paquetes según el apartado 3.5*/
        // Inclusion de pérdida de paquetes, con ráfagas incluidas: Ieef, formula (29)
        float ieef = ie + (95 - ie)*(ppl / ( ppl/burstR +bpl));
        
        
        //R, formula (1)
        R = Ro - is - id - ieef + A;

        return R;
    }

    /** Obtiene el valor correspondiente de MOS a partir del parámetro
     * R estimado con el algoritmo del modelo E
     *
     * @param R     El parámetro de determinación de índices de transmisión (R)
     * @return      La nota media de opinión (MOS)
     */
    public static float calcularMOS(float R) {
        float mos=-1;
        if (R<0) {
            mos=1;
        }else if (R>100){
            mos=4.5f;
        }else {
            mos = (float) ( 1 + R* 0.035 + R*(R-60) * (100 - R) * 7 * Math.pow(10, -6) );
        }
        return mos;
    }

    /**Calcula el parámetro de determinación de índices de transmisión (R)
     * según la aproximación descrita por Juan Jose Ramos Muñoz y Juan Manuel López Soler
     * en la que sólo se considera como parámetros el retardo extremo-a-extremo en ms
     * y el factor de degradación del equipo (Ie).
     *
     * @return El parámetro de determinación de índices de transmisión (R
     */
    public float calcularRsimplificado() {
        float R=-1;
        float h=Float.NaN;
        //calculamos la función H(x) según (2)
        if ( (t-177.3f)<0 ) {
            h=0;
        }
        else{
            h=1;
        }
        
        R=94.2f-0.024f*t+0.11f*h-ie;
        return R;
    }

   /**
     * Fija el parámetro SLR. 
     * @param slr Se recomienda que esté dentro del rango [0,18]
     */
    public void setSLR (float slr) {
        this.slr=slr;
    }

   /**Fija el parámetro RLR. 
     * @param rlr Se recomienda que esté dentro del rango [-5,14]
     */
    public void setRLR (float rlr) {
        this.rlr=rlr;
    }

   /**Fija el parámetro STMR.
     * @param stmr Se recomienda que esté dentro del rango [10,20]
     */
    public void setSTMR (float stmr) {
        this.stmr=stmr;
    }

   /**Fija el parámetro Ds.
     * @param ds Se recomienda que esté dentro del rango [-3,+3]
     */
    public void setDs (float ds) {
        this.ds=ds;
    }

    /**Fija el parámetro Dr.
     * @param dr Se recomienda que esté dentro del rango [-3,+3]
     */
    public void setDr (float dr) {
        this.dr=dr;
    }

    /**Fija el parámetro TELR.
     * @param telr Se recomienda que esté dentro del rango [5,65]
     */
    public void setTELR (float telr) {
        this.telr=telr;
    }

    /**Fija el parámetro WEPL.
     * @param wepl Se recomienda que esté dentro del rango [5,110]
     */
    public void setWEPL (float wepl) {
        this.wepl=wepl;
    }

    /**Fija el parámetro T.
     * @param t Se recomienda que esté dentro del rango [0,500]
     */
    public void setT (float t) {
        this.t=t;
    }

    /**Fija el parámetro Tr.
     * @param tr Se recomienda que esté dentro del rango [0,1000]
     */
    public void setTr (float tr) {
        this.tr=tr;
    }

    /**Fija el parámetro Ta.
     * @param ta Se recomienda que esté dentro del rango [0,5000]
     */
    public void setTa (float ta) {
        this.ta=ta;
    }

    /**Fija el parámetro qdu.
     * @param qdu Se recomienda que esté dentro del rango [0,14]
     */
    public void setQdu (float qdu) {
        this.qdu=qdu;
    }

    /**Fija el parámetro Ie.
     * @param ie Se recomienda que esté dentro del rango [0,40]
     */
    public void setIe (float ie) {
        this.ie=ie;
    }

    /**Fija el parámetro Bpl.
     * @param bpl Se recomienda que esté dentro del rango [1,40]
     */
    public void setBpl (float bpl) {
        this.bpl=bpl;
    }

    /**Fija el parámetro Ppl.
     * @param ppl Se recomienda que esté dentro del rango [0,20]
     */
    public void setPpl (float ppl) {
        this.ppl=ppl;
    }

    /**Fija el parámetro BurstR.
     * @param burstR Se recomienda que esté dentro del rango [1,2]
     */
    public void setBurstR (float burstR) {
        this.burstR=burstR;
    }

    /**Fija el parámetro Nc.
     * @param nc Se recomienda que esté dentro del rango [-80,-40]
     */
    public void setNc (float nc) {
        this.nc=nc;
    }

    /**Fija el parámetro Nfor.
     * @param nfor Puede estar en cualquier rango.
     */
    public void setNfor (float nfor) {
        this.nfor=nfor;
    }

    /**Fija el parámetro Ps.
     * @param ps Se recomienda que esté dentro del rango [35,85]
     */
    public void setPs (float ps) {
        this.ps=ps;
    }

    /**Fija el parámetro Pr.
     * @param pr Se recomienda que esté dentro del rango [35,85]
     */
    public void setPr (float pr) {
        this.pr=pr;
    }

    /**Fija el parámetro A.
     * @param A Se recomienda que esté dentro del rango [0,20]
     */
    public void setA (float A) {
        this.A=A;
    }
    
    /*public static void main(String args[]) {
        Emodel emodel = new Emodel ();
        float R = emodel.calcularR();
        System.out.print("R= "+ R +"; ");
        System.out.println("MOS= "+emodel.calcularMOS(R));
        emodel.setT(250);
        emodel.setIe(20);
        R = emodel.calcularRmetodoRL();
        System.out.print("Método Ramos-López: ");
        System.out.print("Retardo = 250ms; Ie = 20; R = "+R+"; ");
        System.out.println("MOS= "+emodel.calcularMOS(R));
    }*/
}
