/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package audio;

/**
 * Excepción en las operaciones relacionadas con el procesamiento de audio
 * @author Antonio
 */
public class AudioException extends Exception {

    
    public AudioException(String mensaje, String mensajeCausa) {

        super(mensaje, new Throwable(mensajeCausa));
    }

    public AudioException(String mensaje, Throwable causa) {

        super(mensaje, causa);
    }

}
