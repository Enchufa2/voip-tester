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

package audio.gst;

import audio.*;
import org.gstreamer.Caps;

/**
 * Implementación de la clase <code>FormatoRawAudio</code> utilizando Gstreamer.
 *
 * @see FormatoRawAudio
 * @author Antonio
 */
public class FormatoRawAudioGst implements FormatoRawAudio{


    /**
     * Elemento de gstreamer que permite representar las propiedades de audio
     * requeridas
     *
     * @see Caps
     */
    private Caps capsRawAudio;


    /**
     * Inicializa el objeto con unas propiedades determinadas
     *
     * <code>muestrasPorSegundo = 16000</code>
     * <code>numeroDeCanales = 2</code>
     * <code>longitudMuestra = 16</code>
     *
     */
    public FormatoRawAudioGst() {

        this.capsRawAudio = Caps.fromString(
                "audio/x-raw-int, rate=16000, channels=2, endianness=1234, width=16, signed=true");

    }

    /**
     * Devuelve las caps del objeto
     *
     * @see Caps
     */
    public Caps getCapsRawAudio() {

        return new Caps(this.capsRawAudio);
    }

    /**
     * Ajusta las caps del objeto
     *
     * @param capsRawAudio  Caps que se asignan
     *
     * @see Caps
     */
    public void setCapsRawAudio(Caps capsRawAudio) {

        this.capsRawAudio = Caps.fromString(
                "audio/x-raw-int, rate=" + capsRawAudio.getStructure(0).getInteger("rate")
                + ", channels=" + capsRawAudio.getStructure(0).getInteger("channels")
                + ", endianness=1234, width="+ capsRawAudio.getStructure(0).getInteger("width")
                +", signed=true");

        //System.out.println(this.capsRawAudio.simplify());
    }


    /**
     * Ajusta la frecuencia de muestreo
     *
     * @param muestrasPorSegundo    El número de muestras por segundo
     *
     */
    public void setMuestrasPorSegundo(int muestrasPorSegundo) {

        this.capsRawAudio.getStructure(0).setInteger("rate", muestrasPorSegundo);
    }


    /**
     * Ajusta el número de canales de audio
     *
     * @param numeroDeCanales       El número de canales de audio
     */
    public void setNumeroDeCanales(int numeroDeCanales) {

        this.capsRawAudio.getStructure(0).setInteger("channels", numeroDeCanales);
    }


    /**
     * Ajusta el tamaño de la muestra en bits
     *
     * @param bitsPorMuestra    El número de bits por muestra
     */
    public void setLongituMuestraEnBits ( int bitsPorMuestra) {

        this.capsRawAudio.getStructure(0).setInteger("width", bitsPorMuestra);
    }


    /**
     * Devuelve la frecuencia de muestreo
     *
     * @return      El número de muestras por segundo
     */
    public int getMuestrasPorSegundo(){

        return this.capsRawAudio.getStructure(0).getInteger("rate");
    }


    /**
     * Devuelve el número de canales de audio
     *
     * @return      El número de canales de audio
     */
    public int getNumeroDeCanales() {

        return this.capsRawAudio.getStructure(0).getInteger("channels");
    }


    /**
     * Devuelve el tamaño de la muestra en bits
     *
     * @return      El número de bits por muestra
     */
    public int getLongitudMuestraEnBits() {

        return this.capsRawAudio.getStructure(0).getInteger("width");

    }


    @Override
    public String toString() {

        String newLine =System.getProperty("line.separator");

        return "Raw audio: " + newLine
                + '\t' + "Rate (Hz): " + this.getMuestrasPorSegundo() + newLine
                + '\t' + "Channels: " + this.getNumeroDeCanales() + newLine
                + '\t' + "sample size (bits): " + this.getLongitudMuestraEnBits();
    }
}

