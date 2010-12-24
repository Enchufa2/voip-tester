
package nucleo.nodo;

/**
 *
 * Excepción generada por las operaciones de la clase Nodo
 *
 * @see NodoRemoto
 * @see Nodo
 *
 * @author Antonio
 */
public class NodoException extends Exception{

    public NodoException (String mensaje, Throwable causa) {

        super(mensaje, causa);
    }

}
