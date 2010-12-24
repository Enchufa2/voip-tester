/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.protocolos;

/**
 * Se lanza si la longitud del mensaje es negativa o superior a la máxima permitida,
 * cuyo valor depende de la longitud máxima de la carga UDP.
 * @author Antonio
 */
public final class LongitudInvalidaException extends IllegalArgumentException{

    public LongitudInvalidaException(String mensajeError) {

        super(mensajeError);

    }

}
