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

/**
 * RepresentaciÃ³n del conjunto de propiedades de audio sin compresiÃ³n (raw audio),
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
     * @param muestrasPorSegundo    El nÃºmero de muestras por segundo
     *
     */
    public void setMuestrasPorSegundo(int muestrasPorSegundo);


    /**
     * Ajusta el nÃºmero de canales de audio
     *
     * @param numeroDeCanales       El nÃºmero de canales de audio
     */
    public void setNumeroDeCanales(int numeroDeCanales);


    /**
     * Ajusta el tamaÃ±o de la muestra en bits
     *
     * @param bitsPorMuestra    El nÃºmero de bits por muestra
     */
    public void setLongituMuestraEnBits ( int bitsPorMuestra);


    /**
     * Devuelve la frecuencia de muestreo
     *
     * @return      El nÃºmero de muestras por segundo
     */
    public int getMuestrasPorSegundo();


    /**
     * Devuelve el nÃºmero de canales de audio
     * 
     * @return      El nÃºmero de canales de audio
     */
    public int getNumeroDeCanales();


    /**
     * Devuelve el tamaÃ±o de la muestra en bits
     *
     * @return      El nÃºmero de bits por muestra
     */
    public int getLongitudMuestraEnBits();
}

