
package nucleo.nodo;

/**
 *
 * Excepción generada por las operaciones de la clase Nodo relacionadas con el socket
 *
 * @see NodoRemoto
 * @see Nodo
 *
 * @author Antonio
 */
public class NodoSocketException extends NodoException {

    public NodoSocketException(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }

}
