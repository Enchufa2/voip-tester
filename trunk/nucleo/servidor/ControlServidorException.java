/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nucleo.servidor;

/**
 *
 * @author Antonio
 */
public class ControlServidorException extends Exception{

    public ControlServidorException(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }
}
