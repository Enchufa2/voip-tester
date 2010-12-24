

package nucleo.cliente;

/**
 *
 * Excepción para las operaciones realizadas con el nodo servidor
 *
 * @see ControlNodos
 *
 * @author Antonio
 */
public class NodoServidorException extends Exception{

    public NodoServidorException(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }

}
