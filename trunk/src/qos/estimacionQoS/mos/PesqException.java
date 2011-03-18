/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.estimacionQoS.mos;

/**
 *
 * @author Antonio
 */
public class PesqException extends Exception{

    public PesqException(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }

    public PesqException(String mensaje) {

        super(mensaje);
    }

}
