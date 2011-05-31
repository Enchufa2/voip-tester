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

package audio.gst;

import audio.*;
import org.gstreamer.Caps;

/**
 * ImplementaciÃ³n de la clase <code>FormatoRawAudio</code> utilizando Gstreamer.
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
     * @param muestrasPorSegundo    El nÃºmero de muestras por segundo
     *
     */
    public void setMuestrasPorSegundo(int muestrasPorSegundo) {

        this.capsRawAudio.getStructure(0).setInteger("rate", muestrasPorSegundo);
    }


    /**
     * Ajusta el nÃºmero de canales de audio
     *
     * @param numeroDeCanales       El nÃºmero de canales de audio
     */
    public void setNumeroDeCanales(int numeroDeCanales) {

        this.capsRawAudio.getStructure(0).setInteger("channels", numeroDeCanales);
    }


    /**
     * Ajusta el tamaÃ±o de la muestra en bits
     *
     * @param bitsPorMuestra    El nÃºmero de bits por muestra
     */
    public void setLongituMuestraEnBits ( int bitsPorMuestra) {

        this.capsRawAudio.getStructure(0).setInteger("width", bitsPorMuestra);
    }


    /**
     * Devuelve la frecuencia de muestreo
     *
     * @return      El nÃºmero de muestras por segundo
     */
    public int getMuestrasPorSegundo(){

        return this.capsRawAudio.getStructure(0).getInteger("rate");
    }


    /**
     * Devuelve el nÃºmero de canales de audio
     *
     * @return      El nÃºmero de canales de audio
     */
    public int getNumeroDeCanales() {

        return this.capsRawAudio.getStructure(0).getInteger("channels");
    }


    /**
     * Devuelve el tamaÃ±o de la muestra en bits
     *
     * @return      El nÃºmero de bits por muestra
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

