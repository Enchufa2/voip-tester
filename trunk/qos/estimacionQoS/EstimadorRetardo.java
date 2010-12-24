package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.estimacionQoS.parametrosQoS.Retardo;
//import libreriaQosSinc.estimacionQoS.parametrosQoS.ParametrosAudio;
import audio.FormatoCodec;


/**
 * @author Antonio
 * @version 1.0
 * @updated 19-ago-2010 21:08:51
 */
public final class EstimadorRetardo extends
        EstimadorQoS {

    /**
     * Timestamps en nanosegundos de los paquetes enviados. Cada elemento i del vector
     * contiene el instante de envío del paquete i
     */
     private long timestampsPaquetesEnviados[];


    /**
     * Timestamps en nanosegundos de los paquetes recibidos. Cada elemento i del
     * vector contiene el instante de recepción del paquete i
     */
     private long timestampsPaquetesRecibidos[];


    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación.
     *
     * @param formatoCodec              Propiedades de los datos de audio utilizados para obtener la medida
     * @param parametrosTransmision     Parametros de transmisión de audio utilizados para obtener la medida
     * @param timestampsPE              Timestamps de los paquetes enviados asociados a los datos de transmisión
     * @param timestampsPR              Timestamps de los paquetes recibidos asociados a los datos de transmisión
     */
     public EstimadorRetardo(
            FormatoCodec formatoCodec,
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPE,
            long[] timestampsPR) {

        super(
                new MedidaRetardo(formatoCodec, parametrosTransmision)
                );

        this.timestampsPaquetesEnviados = timestampsPE;
        this.timestampsPaquetesRecibidos = timestampsPR;

     }

    /**
     * Crea el objeto a partir de los datos de transmisión y las medidas experimentales
     * necesarias para la estimación.
     * El retardo puede estimarse sin la necesidad de incluir datos de audio en los paquetes.
     *
     * @param parametrosTransmision     Los parámetros de transmisión utilizados para obtener la medida
     * @param timestampsPE      Timestamps de los paquetes enviados asociados a los datos de transmisión
     * @param timestampsPR      Timestamps de los paquetes recibidos asociados a los datos de transmisión
     */
     public EstimadorRetardo(
            ParametrosTransmision parametrosTransmision,
            long[] timestampsPE,
            long[] timestampsPR) {

        super(
                new MedidaRetardo(parametrosTransmision)
                );
        this.timestampsPaquetesEnviados = timestampsPE;
        this.timestampsPaquetesRecibidos = timestampsPR;        

     }

    /**
     * Estima el retardo para cada paquete n y en cada intervalo k,
     * haciendo uso de la expresion:
     * R(n)=Tr(n)-Te(n)
     * donde
     *      R(n): retardo estimado del paquete n
     *      Tr(n): tiempo de rececpión del paquete n
     *      Te(n): tiempo de envío del paquete n
     *
     * Las unidades de R(n) son ms
     * 
     * Hay que tener en cuenta que los timestamps de emisor se generan con un reloj
     * distinto a los timestamps del receptor, por lo que existirá un error debido al offset.
     * Por tanto este error debe corregirse eliminando dicho offset bien a los timestamps
     * bien a los valores de retardo.
     */
     public void estimarMedidaQoS() {

        int numeroIntervalos = super.medidaQoS.getParametrosTransmision().
                getNumeroIntervalos();
        int valoresPorIntervalo = super.medidaQoS.getParametrosTransmision().
                getMensajesPorIntervalo();

        Retardo retardo_k;
        long retardoPaquete_i;
        int limite_inferior;
        int limite_superior;

        for (int k = 0; k < numeroIntervalos; k++) {

            retardo_k = new Retardo(valoresPorIntervalo);
            limite_inferior = k * valoresPorIntervalo;
            limite_superior = (k + 1) * valoresPorIntervalo;
            for (int i = limite_inferior; i < limite_superior; i++) {

                if (this.timestampsPaquetesRecibidos[i] != 0) {

                    retardoPaquete_i = this.timestampsPaquetesRecibidos[i] - this.timestampsPaquetesEnviados[i];
                    retardo_k.setValorParametro(i - limite_inferior, retardoPaquete_i / 1000000f);       //conversion a milisegundos

                }
            }

            super.medidaQoS.addParametroQoS(k, retardo_k);
        }
    }


     /**
      * sólo para evaluación
      */
     private void checkTrafficProfile(){

         long idt = this.medidaQoS.parametrosTransmision.getTiempoEntreEnvios();
         long idtExp=0;
         long idtMedio=0;
         long idtMax=Long.MIN_VALUE;
         long idtMin=Long.MAX_VALUE;

         for(int i=1; i<this.timestampsPaquetesEnviados.length; i++) {

             idtExp = (this.timestampsPaquetesEnviados[i]-this.timestampsPaquetesEnviados[i-1])/1000;
             idtMedio += idtExp;
             if(idtExp > idtMax)
                 idtMax = idtExp;
             if(idtExp < idtMin)
                 idtMin = idtExp;
             System.out.println(this.timestampsPaquetesEnviados[i] + "\t" + this.timestampsPaquetesRecibidos[i]);
         }
         idtMedio = idtMedio / (this.medidaQoS.parametrosTransmision.numeroDeMensajes());
         System.out.println("IDT medio: " + idtMedio + " us");
         System.out.println("Desv max IDT: " + (idtMax - idt) + " us");
         System.out.println("Desv min IDT: " + (idtMin - idt) + " us");

     }
     

}

