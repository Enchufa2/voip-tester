/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
