/* 
Este fichero es parte de VoIPTester

    Copyright (C) 2009-2010 
    	      Antonio Sánchez Navarro (titosanxez@gmail.com)
	      Juan M. López Soler (juanma@ugr.es)
	      Jorge Navarro Ortiz (jorgenavarro@ugr.es)
    

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los términos de la Licencia Pública General GNU publicada 
    por la Fundación para el Software Libre, ya sea la versión 3 
    de la Licencia, o (a su elección) cualquier versión posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
    MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. 
    Consulte los detalles de la Licencia Pública General GNU para obtener 
    una información más detallada. 

    Debería haber recibido una copia de la Licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.

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
     * @param longitudMensaje nÃºmero de bytes que contiene el mensaje
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
     * Introduce el valor del timestamp T2 en la posiciÃ³n correspondiente
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
     * Introduce el valor del timestamp T3 en la posiciÃ³n correspondiente
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

