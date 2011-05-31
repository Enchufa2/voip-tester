/* 
Este fichero es parte de VoIPTester

    Copyright (C) 2009-2010 
    	      Antonio Sánchez Navarro (titosanxez@gmail.com)
	      Juan M. López Soler (juanma@ugr.es)
	      Jorge Navarro Ortiz (jorgenavarro@ugr.es)
    

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los términos de la Licencia Pública General GNU publicada 
    por la Fundación para el Software Libre, ya sea la versión 3 
    de la Licencia, o (a su elección) cualquier versión posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
    MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. 
    Consulte los detalles de la Licencia Pública General GNU para obtener 
    una información más detallada. 

    Debería haber recibido una copia de la Licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.

*/
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
     * la compresiÃ³n de acuerdo a las caracterÃ­sticas indicadas, devolviendo
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
     * Toma un cojunto de buffers de audio y le aplica el proceso de descompresiÃ³n
     * generando un archivo en formato WAV con el resultado de la descompresiÃ³n.
     *
     * @param targetFile        El archivo de audio WAV que contendrÃ¡ el audio descomprimido
     * @param audioCodificado   Los buffers de audio comprimido
     *
     * @return                  El archivo de audio WAV que contendrÃ¡ el audio descomprimido
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
