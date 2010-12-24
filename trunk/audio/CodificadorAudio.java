package audio;

import java.io.File;

/**
 * @author Antonio
 * @version 1.0
 * @created 17-ago-2010 14:07:13
 */
public interface CodificadorAudio {

    /**
     *
     * Toma un archivo de audio en formato WAV, obtiene el raw audio y le aplica
     * la compresión de acuerdo a las características indicadas, devolviendo
     * el conjunto de buffers que contienen el audio comprimido.
     *
     * @param audioOriginal         Archivo de audio en formato WAV
     * @param formatoObjetivo       Propiedades del audio comprimido
     *
     * @return                      Los buffers con el audio comprimido
     *
     * @throws AudioException       Si ocurre un error durante el procesamiento
     *
     * @see BuffersAudio
     * @see FormatoCodec
     */
    public BuffersAudio codificar(
            File audioOriginal,
            FormatoCodec formatoObjetivo) throws AudioException;

    /**
     *
     * Toma un cojunto de buffers de audio y le aplica el proceso de descompresión
     * generando un archivo en formato WAV con el resultado de la descompresión.
     *
     * @param targetFile        El archivo de audio WAV que contendrá el audio descomprimido
     * @param audioCodificado   Los buffers de audio comprimido
     *
     * @return                  El archivo de audio WAV que contendrá el audio descomprimido
     *
     * @throws AudioException       Si ocurre un error durante el procesamiento
     *
     * @see BuffersAudio
     *
     */
    public File decodificar(
            File targetFile,
            BuffersAudio audioCodificado) throws AudioException;

}