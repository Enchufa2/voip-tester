/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.estimacionQoS.mos;
import java.lang.IllegalArgumentException;


/**
 *
 * @author Antonio
 */
public class ModeloPerdidasMarkov {


    /**
     * Contadores de transiciones entre estados
     */
    public int c11,c13,c14,c22,c23,c33;

    /**
     * Longitud en paquetes del gap
     */
    private int gmin;

    /**
     * Inicializa el objeto fijando un gap mínimo para analizar las ráfagas
     */
    public ModeloPerdidasMarkov(int gmin) {

        this.gmin = gmin;
    }   


    /**
     * Realiza el algoritmo de análisi de pérdidas según un modelo de Markov de cuatro
     * estados que tiene en cuenta las ráfagas.
     *
     * Este algoritmo se proporciona en el documento ETSI TS 101 329-5 V1.1.1:
     * Technology Compliance Specification; Part 5: Quality of Service (QoS) measurement methodologies
     */
     public void analizarPerdidas(long[] timestamps, int posicionInicial, int posicionFinal){

        if ((posicionFinal-posicionInicial) < this.gmin);
            //throw new IllegalArgumentException("El número de eventos de paquetes debe ser mayor que "+this.gmin);

        int pkt=0,lost=0;
        c11=0;c13=0;c14=0;c22=0;c23=0;c33=0;

        for (int i=posicionInicial; i < posicionFinal; i++) {

            if(timestamps[i]==0) {    //paquete perdido

                //c5 += pkt;
                if(pkt >= gmin) {

                    if(lost == 1){
                        c14 += 1;
                    }else{
                        c13 += 1;
                    }
                    lost = 1;
                    c11 += pkt;

                }else{
                    lost += 1;
                    //if (lost>8) c5=0;
                    if(pkt == 0) {
                        c33 += 1;
                    }else{
                        c23 += 1;
                        c22 += pkt;
                    }
                }
             pkt=0;
            }else{
                pkt +=1 ;
            }
        }
        this.c11 += this.c14;
    }// fin método



     /**
      * Calcula probabilidad de permanecer en el estado 1/4
      *
      * @return la probabilidad de permanecer en el estado 1/4
      */
     public float p11() {

         if ( (this.c11+this.c13)!=0) return ((float)this.c11)/(this.c11 + this.c13);
         else if( (this.c11+this.c13+this.c14+this.c22+this.c23+this.c33)==0) return 1f;
         return 0.f;
         
     }


     /**
      * Calcula probabilidad de transicion del estado 1/4 al 3
      *
      * @return la probabilidad de transicion del estado 1/4 al 3
      */
     public float p13() {

         return 1 - this.p11();
         
     }

     /**
      * Calcula probabilidad de transicion del estado 3 al 1/4
      *
      * @return la probabilidad de transicion del estado 3 al 1/4
      */
     public float p31() {

         if ( (this.c13+this.c23+this.c33)==0) return 0.f;
         return ((float) this.c13)/(this.c13+this.c23+this.c33);
         
     }


     /**
      * Calcula probabilidad de transicion del estado 3 al 2
      *
      * @return la probabilidad de transicion del estado 3 al 2
      */
     public float p32() {

         if ( (this.c23+this.c13+this.c33)==0) return 0.f;
         return ((float) this.c23)/(this.c13+this.c23+this.c33);
         

     }


     /**
      * Calcula probabilidad de permanecer en el estado 3
      *
      * @return la probabilidad de permanecer en el 3
      */
     public float p33() {

         return 1 - this.p31() - this.p32();
         
     }


     /**
      * Calcula probabilidad de permanecer en el estado 2
      *
      * @return la probabilidad de permanecer en el 2
      */
     public float p22() {

         if ( (this.c22+this.c23)==0) return 0.f;
         return ((float) this.c22)/(this.c22+this.c23);
         
     }


     /**
      * Calcula probabilidad de transicion del estado 2 al 3
      *
      * @return la probabilidad de transicion del estado 2 al 3
      */
     public float p23() {

         return 1 - this.p22();
         
     }

     /**
      * Calcula el valor d del algoritmo necesario para la obtención del resto
      * de probabilidades
      */
     private float parametro_d() {

         return this.p23()*this.p31() + this.p13()*this.p32() + this.p13()*this.p23();
         
     }


     /**
      * Calcula la probabilidad de ocurrencia del estado 1/4
      *
      * @return La probabilidad de ocurrencia del estado 1/4
      */
     public float p1(){

         float d = this.parametro_d();
         if (d==0) return 1;
         return (this.p31()*this.p23())/d;
     }

     /**
      * Calcula la probabilidad de ocurrencia del estado 2
      *
      * @return La probabilidad de ocurrencia del estado 2
      */
     public float p2(){

         float d = this.parametro_d();
         if (d==0) return d;
         return (this.p13()*this.p32())/d;
     }


     /**
      * Calcula la probabilidad de ocurrencia del estado 3
      *
      * @return La probabilidad de ocurrencia del estado 3
      */
     public float p3(){

         float d = this.parametro_d();
         if (d==0) return d;
         return (this.p13()*this.p23())/d;
     }




}

