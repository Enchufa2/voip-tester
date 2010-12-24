/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package audio;

import audio.gst.CodificadorAudioGst;
import audio.gst.FormatoCodecGst;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;

/**
 * Crea una correspondencia entre un tipo de codec y un objeto FormatoCodec que
 * lo representa
 * 
 * @author Antonio
 */
public class GestorCodecsAudio {

    /**
     * Correspondencia entre nombre del codec y objeto FormatoCodec que lo representa
     */
    Hashtable<String, FormatoCodec> codecs;


    
    /**
     * Crea un conjunto de correspondencias
     */
    public GestorCodecsAudio() {
         
        this.codecs = new Hashtable<String, FormatoCodec>();
        this.inspeccionarCodecs();
    }

    /**
     * Crea un conjunto de correspondencias de tamaño <code>numeroTipoCodecs</code>
     *
     * @param codecsDisponibles     Numero total de codecs que están disponibles y que son 
     *                              representables mediante el formato de audio correspondiente.
     */
    public GestorCodecsAudio(int codecsDisponibles) {

        this.codecs = new Hashtable<String, FormatoCodec>(codecsDisponibles);

    }


    /**
     * Devuelve el fichero correspondiente a un codec
     *
     * @param nombreCodec   El nombre del codec
     * @return              Las propiedads del codec correspondiente <code>nombreCodec</code>
     */
    public FormatoCodec getFormatoCodec(String nombreCodec) {

        return this.codecs.get(nombreCodec);
        
    }


    /**
     * Añade un fichero multimedia cuyo contenido esté comprimido con el codec
     * <code>nombreCodec</code>, si este no se encuentra ya en la correspondencia.
     *
     * @param nombreCodec   El nombre del codec
     * @param formatoCodec    Las propiedades del codec <code>nombreCodec</code>
     * @return              <code>false</code> Si el codec ya se encuentra en la correspondencia.
     */
    public boolean addFormatoCodec(String nombreCodec, FormatoCodec formatoCodec) {

        boolean existeCodec = this.codecs.containsKey(nombreCodec);

        if (!existeCodec ) {

            this.codecs.put(nombreCodec, formatoCodec);
        }
        
        return existeCodec;
    }

    /**
     * Devuelve el nombre de los codecs disponibles que se encuentran en las correspondencias.
     *
     * @return      El nombre de todos los codecs que se encuentran en las correspondencias.
     */
    public ArrayList<String> getNombreDeCodecsDisponible() {

        ArrayList<String> listaCodecs = new ArrayList<String>();
        Enumeration<String> keys = this.codecs.keys();
        String nombreCodec;
        while (keys.hasMoreElements()) {

            nombreCodec = keys.nextElement();
            if (this.codecs.get(nombreCodec).estaSoportado())
                listaCodecs.add(nombreCodec);

        }
        
        return listaCodecs;
    }


    /**
     * Crea un conjunto de codecs que pueden considerarse para la codificación
     * Se crea el objeto FormatoCodec correspondiente añadiendole las propiedades
     * que tiene el codificador correspondiente.
     *
     * La creación de los codecs se realiza según la información que proporciona
     * la salida del comando <code>gst-inspect</code>.
     *
     * @see FormatoCodec
     */
    public void inspeccionarCodecs() {

        FormatoCodecGst formatoCodec;
        String nombreCodec;

        //PCM Ley A
        nombreCodec = "PCM Ley A";
        formatoCodec = new FormatoCodecGst(nombreCodec, "alawenc", "alawdec");
        formatoCodec.setTasaDeDatos(64000);
        this.addFormatoCodec(nombreCodec, formatoCodec);

        //PCM Ley mu
        nombreCodec = "PCM Ley Mu";
        formatoCodec = new FormatoCodecGst(nombreCodec, "mulawenc", "mulawdec");
        formatoCodec.setTasaDeDatos(64000);
        this.addFormatoCodec(nombreCodec, formatoCodec);

        //ADPCM microsoft
        nombreCodec = "ADPCM MS";
        formatoCodec = new FormatoCodecGst(nombreCodec, "ffenc_adpcm_ms", "ffdec_adpcm_ms");
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "bitrate",
                new Integer[]{32000}
        );
        formatoCodec.setTasaDeDatos(32000);
        this.addFormatoCodec(nombreCodec, formatoCodec);

        //G.726
        nombreCodec = "G.726";
        formatoCodec = new FormatoCodecGst(nombreCodec, "ffenc_g726", "ffdec_g726");
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "bitrate",
                new Integer[]{16000, 24000, 32000, 40000}
        );        
        formatoCodec.setTasaDeDatos(16000);
        this.addFormatoCodec(nombreCodec, formatoCodec);


        //GSM-FR
        nombreCodec = "GSM-FR";
        formatoCodec = new FormatoCodecGst(nombreCodec, "gsmenc", "gsmdec");
        formatoCodec.setTasaDeDatos(13000);
        this.addFormatoCodec(nombreCodec, formatoCodec);

        //AMR-nb
        nombreCodec = "AMR-nb";
        formatoCodec = new FormatoCodecGst(nombreCodec, "amrnbenc", "amrnbdec");
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "band-mode",
                new Integer[]{0,1,2,3,4,5,6,7},
                "bps",
                new Integer[]{4750,5150,5900,6700,7400,7950,10200,12200}
        );
        formatoCodec.setTasaDeDatos(12200);
        this.addFormatoCodec(nombreCodec, formatoCodec);

        //WMA v1
        nombreCodec = "WMA v1";
        formatoCodec = new FormatoCodecGst(nombreCodec, "ffenc_wmav1", "ffdec_wmav1");
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "bitrate", new Integer[]{24000,28000,32000,36000,40000,44000,48000}
        );
        this.addFormatoCodec(nombreCodec, formatoCodec);

        //Siren 7
        nombreCodec ="Siren 7";
        formatoCodec = new FormatoCodecGst(nombreCodec, "sirenenc", "sirendec",16000,1);
        formatoCodec.setTasaDeDatos(16000);
        this.addFormatoCodec(nombreCodec, formatoCodec);

        //Speex
        nombreCodec="Speex";
        formatoCodec = new FormatoCodecGst(nombreCodec, "speexenc", "speexdec");
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "quality",
                new Float[]{0f,.5f,1f,1.5f,2f,2.5f,3f,3.5f,4f,4.5f,5f,5.5f,6f,6.5f,7f,7.5f,8f,8.5f,9f,9.5f,10f},
                "Calidad de codificación",
                null
        );
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "bitrate",
                new Integer[]{2500,4100,6000,8000,11000,15000,18500,24600}
                );
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "vad",
                new Boolean[]{false,true},
                "Detección de voz",
                new String[]{"Desactivado", "Activado"}
                );
        this.addFormatoCodec(nombreCodec, formatoCodec);

        //AAC
        nombreCodec="AAC";
        formatoCodec = new FormatoCodecGst(nombreCodec, "faac", "ffdec_aac");        
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "bitrate",
                new Integer[]{8000,9000,10000,11000,12000,13000}
        );
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "outputformat",
                new Integer[]{1},
                "Formato stream",
                new String[]{"ADTS"}
                );
        formatoCodec.getPropiedadesCodec().seleccionarValorPropiedad("outputformat", 0);
        this.addFormatoCodec(nombreCodec, formatoCodec);


         //CELT
        nombreCodec = "CELT";
        formatoCodec = new FormatoCodecGst(nombreCodec, "celtenc", "celtdec", 32000);
        Integer[] bitratesCelt = new Integer[38];
        for (int i=0; i<bitratesCelt.length; i++) {
            bitratesCelt[i]= 10 + i;
        }
        Integer[] bitratesCeltDesc = new Integer[38];
        for (int i=0; i<bitratesCeltDesc.length; i++) {
            bitratesCeltDesc[i]= bitratesCelt[i] * 1000;
        }
        formatoCodec.getPropiedadesCodec().addPropiedad(
                "bitrate",
                bitratesCelt,
                "bps",
                bitratesCeltDesc
        );
        formatoCodec.setTasaDeDatos(12200);
        this.addFormatoCodec(nombreCodec, formatoCodec);
        this.addFormatoCodec(nombreCodec, formatoCodec);

    }


    /**
     * Devuelve el codificador disponible para realizar las codificaciones
     * y decodificaciones del audio
     *
     * @return      El codificador que codifica/decodifica el audio
     */
    public static CodificadorAudio getCodificadorAudioDisponible(){

        return new CodificadorAudioGst();
    }
}
