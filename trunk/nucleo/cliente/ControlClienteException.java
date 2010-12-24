/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nucleo.cliente;

/**
 *
 * Excepciones generadas en las operaciones de la clase ControlCliente
 * 
 * @author Antonio
 */
public class ControlClienteException extends Exception {

    public ControlClienteException(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }

}
