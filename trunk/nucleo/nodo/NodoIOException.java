
package nucleo.nodo;

/**
 *
 * Excepción generada por las operaciones de la clase Nodo relacionadas con E/S
 *
 * @see NodoRemoto
 * @see Nodo
 *
 * @author Antonio
 */
public class NodoIOException extends NodoException{

    public NodoIOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

}
