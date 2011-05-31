/* 
Este fichero es parte de VoIPTester

    Copyright (C) 2009-2010 
    	      Antonio S�nchez Navarro (titosanxez@gmail.com)
	      Juan M. L�pez Soler (juanma@ugr.es)
	      Jorge Navarro Ortiz (jorgenavarro@ugr.es)
    

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los t�rminos de la Licencia P�blica General GNU publicada 
    por la Fundaci�n para el Software Libre, ya sea la versi�n 3 
    de la Licencia, o (a su elecci�n) cualquier versi�n posterior.

    Este programa se distribuye con la esperanza de que sea �til, pero 
    SIN GARANT�A ALGUNA; ni siquiera la garant�a impl�cita 
    MERCANTIL o de APTITUD PARA UN PROP�SITO DETERMINADO. 
    Consulte los detalles de la Licencia P�blica General GNU para obtener 
    una informaci�n m�s detallada. 

    Deber�a haber recibido una copia de la Licencia P�blica General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.

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

