package qos.protocolos.timestamps;

import java.lang.Exception;

/**
 * Se lanza cuando el identificador de cabecera no es válido por dos razones:
 * - Es un número negativo.
 * - El identificador es mayor que el número de paquetes enviados.
 *
 * @author Antonio
 */
public final class IdCabeceraInvalidoException extends Exception{

    public IdCabeceraInvalidoException() {

        super("Invalid header identifier");
    }

}
