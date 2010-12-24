
package nucleo.cliente;


/**
 *
 * Excepción para las operaciones realizadas con el nodo cliente
 *
 * @see ControlNodos
 *
 * @author Antonio
 */
public class NodoClienteException extends Exception {

    public NodoClienteException(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }

}
