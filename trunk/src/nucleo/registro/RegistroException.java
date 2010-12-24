/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nucleo.registro;

/**
 * Excepción para las operaciones del registro
 *
 * @see ServidorRegistro
 * @see ClienteNodo
 *
 * @author Antonio
 */
public class RegistroException extends Exception {

    public RegistroException (String mensaje, Throwable causa) {
            super(mensaje, causa);
    }

}
