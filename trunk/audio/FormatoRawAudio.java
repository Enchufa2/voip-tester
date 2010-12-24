/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package audio;

/**
 * Representación del conjunto de propiedades de audio sin compresión (raw audio),
 * es decir, en formato PCM. Son necesarias para la clase <code>FormatoCodec</code>.
 *
 * A diferencia de  <code>FormatoCodec</code>, las propiedades de esta clase si pueden
 * ser ajustadas.
 *
 * @author Antonio
 */
public interface FormatoRawAudio {


    /**
     * Ajusta la frecuencia de muestreo
     *
     * @param muestrasPorSegundo    El número de muestras por segundo
     *
     */
    public void setMuestrasPorSegundo(int muestrasPorSegundo);


    /**
     * Ajusta el número de canales de audio
     *
     * @param numeroDeCanales       El número de canales de audio
     */
    public void setNumeroDeCanales(int numeroDeCanales);


    /**
     * Ajusta el tamaño de la muestra en bits
     *
     * @param bitsPorMuestra    El número de bits por muestra
     */
    public void setLongituMuestraEnBits ( int bitsPorMuestra);


    /**
     * Devuelve la frecuencia de muestreo
     *
     * @return      El número de muestras por segundo
     */
    public int getMuestrasPorSegundo();


    /**
     * Devuelve el número de canales de audio
     * 
     * @return      El número de canales de audio
     */
    public int getNumeroDeCanales();


    /**
     * Devuelve el tamaño de la muestra en bits
     *
     * @return      El número de bits por muestra
     */
    public int getLongitudMuestraEnBits();
}
