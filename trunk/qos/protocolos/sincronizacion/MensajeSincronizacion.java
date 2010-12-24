/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.protocolos.sincronizacion;
import qos.protocolos.Mensaje;

/**
 *
 * @author Antonio
 */
public class MensajeSincronizacion extends Mensaje{

    /**
     * Longitud en bytes de los mensajes que se intercambian. Como el cliente
     * recibe dos tiemstamp de tipo long, se requieren 16 bytes.
     */
    public static final int LONGITUD_MENSAJE=16;

    /**
     * Posicion dentro del mensaje del timestamp T2 generado en el servidor.
     * Los timestamp ocupan 8 bytes.
     */
    public final int POSICION_T2=0;


    /**
     * Posicion dentro del mensaje del timestamp T3 generado en el servidor
     * Los timestamp ocupan 8 bytes.
     */
    public final int POSICION_T3=7;


    /**
     * Crea un mensaje sin datos
     */
     public MensajeSincronizacion() {

         super();

     }


     /**
     * Crea un mensaje con todos sus bytes a 0
     *
     * @param longitudMensaje número de bytes que contiene el mensaje
     */
    public MensajeSincronizacion(int longitudMensaje) {

        super(longitudMensaje);
    }


    /**
     * Crea un mensaje a partir de un conjunto de bytes
     *
     * @param b Conjunto de bytes
     */
    public MensajeSincronizacion(byte[] b) {

        super(b);
    }

    /**
     * Introduce el valor del timestamp T2 en la posición correspondiente
     * dentro del mensaje.
     *
     * @param T2    El timestamp T2
     */
    public void encapsularT2(long T2) {

        super.encapsularLong(this.POSICION_T2, T2);

    }

    /**
     * Devuelve el valor del timestamp T2 que contiene el mensaje
     *
     * @return  El timestamp T2
     */
    public long extraerT2() {

        return super.extraerLong(POSICION_T2);
    }


    /**
     * Introduce el valor del timestamp T3 en la posición correspondiente
     * dentro del mensaje.
     *
     * @param T3    El timestamp T3
     */
     public void encapsularT3(long T3) {

        super.encapsularLong(this.POSICION_T3, T3);

    }


    /**
     * Devuelve el valor del timestamp T3 que contiene el mensaje
     *
     * @return  El timestamp T3
     */
    public long extraerT3() {

        return super.extraerLong(POSICION_T3);
    }


}
