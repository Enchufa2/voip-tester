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


package nucleo.cliente;

import audio.FormatoCodec;
import audio.CodificadorAudio;
import audio.AudioException;
import audio.GestorCodecsAudio;
import audio.BuffersAudio;
import audio.FormatoRawAudio;

import java.util.logging.Logger;
import java.io.File;
import java.util.logging.Level;
import java.util.Arrays;

import audio.gst.FormatoCodecGst;
import org.gstreamer.Gst;


/**
 *
 * @author Antonio
 */
public class ControlAudio {


    private BuffersAudio audioComprimido;

    private BuffersAudio silencioComprimido;

    private GestorDirectorios gestorDirectorios;

    private CodificadorAudio codificadorAudio;

    private Logger logger;

    public ControlAudio(){

        this.logger = Logger.getLogger("nucleo.cliente");
        this.gestorDirectorios = GestorDirectorios.getInstance();
        this.codificadorAudio = GestorCodecsAudio.getCodificadorAudioDisponible();
        
    }

     /**
     * Comprime audio de referencia en formato wav, a un formato <code>formatoObjetivo</code>.
     * Se comprimen <code>duracionIntervaloEnUS</code> microsegundos de audio.
     *
     * @param formatoObjetivo           El formato de compresión de audio a utilizar
     * @param duracionIntervaloUS     Duración total de audio a comprimir
     *
     * @throws AudioException           Si ocurre un error en el proceso de codificación
     */
    public byte[] comprimirAudio(FormatoCodec formatoObjetivo, int duracionIntervaloUS)
            throws AudioException{

        this.logger.entering("ControlAudio", "comprimirAudio");

        File audioVozReferencia = this.gestorDirectorios.getDirectorioAudioVozReferencia().listFiles()[0];        
        File audioSilencioReferencia = this.gestorDirectorios.getDirectorioAudioSilencioReferencia().listFiles()[0];        
        this.audioComprimido = this.codificadorAudio.codificar(audioVozReferencia, formatoObjetivo);
        this.silencioComprimido = codificadorAudio.codificar(audioSilencioReferencia, formatoObjetivo);

        this.logger.exiting("ControlAudio", "comprimirAudio");

        return this.audioComprimido.getBuffersAudio(duracionIntervaloUS).getByteArrayAudioStream();

    }


    /**
      * Permite realizar la decodficación de audio comprimido con formato
      * <code>formatoCodec</code> y devolver el audio en un archivo WAV
      * Se realiza la descompresión tantas veces como intervalos se indiquen
      * en los parámetros de transmisión, obteniéndose un archivo WAV por cada
      * intervalo
      *
      * @param audioCodificado      bytes de audio codificado. Cada fila se corresponde con un intervalo
      * @param formatoCodec         Formato de compresión de los bytes de audio
      *
      * @throws AudioException      Si ocurre un error en el proceso de decodificación
      *
      */
     public File[] descomprimirAudio(byte[][] audioCodificado, FormatoCodec formatoCodec)
            throws AudioException {

        this.logger.entering("ControlAudio", "descomprimirAudio");

        File directorioRecibido = this.gestorDirectorios.
                crearDirectorioParaAudioRecibido(formatoCodec.getNombreCodec());
        
        int numeroIntervalos = audioCodificado.length;
        File[] audioDescomprimido = new File[numeroIntervalos];
        int[] longitudesBuffer = this.audioComprimido.longitudesBuffer();

        for (int i=0; i<numeroIntervalos; i++) {
            audioDescomprimido[i] = new File(
                    directorioRecibido , "recibido_"+ i +".wav"
                    );
            BuffersAudio buffersAudio = new BuffersAudio(
                    audioCodificado[i], longitudesBuffer, formatoCodec
                    );
            this.checkBuffersAudio(buffersAudio);
            this.codificadorAudio.decodificar(audioDescomprimido[i], buffersAudio);
        }

        this.logger.exiting("ControlAudio", "descomprimirAudio");

        return audioDescomprimido;
    }


     /**
      * Compara bit a bit el contenido de los buffers instancia de la clase con los
      * pasados como argumento. Los buffers argumento que no coincidan serán reemplazados
      * por buffers de silencio
      *
      * @param buffersAudio     Los buffers audio que pueden estar corruptos
      *
      */
     public void checkBuffersAudio(BuffersAudio buffersAudio) {

         int numeroBuffers = this.audioComprimido.numeroDeBuffers() > buffersAudio.numeroDeBuffers()
                 ? buffersAudio.numeroDeBuffers() : this.audioComprimido.numeroDeBuffers();
         
         boolean iguales=true;
         for (int i=0; i<numeroBuffers; i++) {
             
             iguales = Arrays.equals(
                     this.audioComprimido.getBufferAudio(i),
                     buffersAudio.getBufferAudio(i)
                     );

             if (! iguales){

                 buffersAudio.addBufferAudio(i, this.silencioComprimido.getBufferAudio(i));
             }
         }
         
     }


     /**
      * Crea el audio de referencia. Toma el archivo de audio original proporcionado
      * y genera un sub-archivo de audio con las características indicadas
      *
      * @param muestrasPorSegundo       Muestras por segundo de raw audio
      * @param numeroDeCanales          número de canales de raw audio
      * @param duracionIntervaloUS      duración de la señal de referencia en microsegundos
      * @param numeroIntervalos         Número de señales de referencia considerar
      *
      * @return     el archivo de audio referencia para cada intervalo
      *
      * @throws AudioException      Si ocurre un error durante el procesamiento
      */
     public File[] crearAudioReferencia(
             int muestrasPorSegundo,
             int numeroDeCanales,
             int duracionIntervaloUS,
             int numeroIntervalos
             )
             throws AudioException {

         this.logger.entering("ControlAudio", "crearAudioReferencia");
         
         FormatoCodecGst formatoCodec = new FormatoCodecGst("PCM", "identity", "identity");
        
         //CodificadorAudio ca = GestorCodecsAudio.getCodificadorAudioDisponible();
         BuffersAudio ba = this.codificadorAudio.codificar(
                 this.gestorDirectorios.getFicheroAudioOriginal(),
                 formatoCodec
                 );

         int tasaDeDatos = (int) (formatoCodec.muestrasPorSegundo() *formatoCodec.numeroDeCanales()*formatoCodec.longitudMuestraEnBits());
         formatoCodec.setTasaDeDatos(tasaDeDatos);
                 

         //Creación del sub-fichero de voz
         File vozReferencia = new File(this.gestorDirectorios.getDirectorioAudioVozReferencia(), "subAudioVoz.wav");
         BuffersAudio ba_intervalo = ba.getBuffersAudio(duracionIntervaloUS);
         ba.getFormatoCodec().getFormatoRawAudio().setMuestrasPorSegundo(muestrasPorSegundo);
         ba.getFormatoCodec().getFormatoRawAudio().setNumeroDeCanales(numeroDeCanales);
         
         this.codificadorAudio.decodificar(
                 vozReferencia,
                 ba_intervalo
                 );
         
         //Creación del sub-fichero de silencio
         File silencioReferencia = new File(this.gestorDirectorios.getDirectorioAudioSilencioReferencia(), "subAudioSilencio.wav");
         BuffersAudio ba_silencio = new BuffersAudio(
                 new byte[ba_intervalo.numeroTotalBytes()],
                 ba_intervalo.longitudesBuffer(),
                 ba_intervalo.getFormatoCodec()
                 );

         this.codificadorAudio.decodificar(
                 silencioReferencia,
                 ba_silencio
                 );

         this.logger.exiting("ControlAudio", "crearAudioReferencia");

         return this.getArchivosReferencia(numeroIntervalos);

     }


     /**
      * Crea el audio de referencia. Toma el archivo de audio original proporcionado
      * y genera un sub-archivo de audio con las características indicadas
      *
      * @param fra          Características del raw audio
      * @param duracionIntervaloUS      duración de la señal de referencia en microsegundos
      * @param numeroIntervalos         Número de señales de referencia considerar
      *
      * @return     el archivo de audio referencia para cada intervalo
      *
      * @throws AudioException      Si ocurre un error durante el procesamiento
      *
      * @see FormatoRawAudio
      * @see #crearAudioReferencia(int, int, int, int) 
      */
     public File[] crearAudioReferencia(
             FormatoRawAudio fra, int duracionIntervaloUS, int numeroIntervalos
             )throws AudioException {

         return this.crearAudioReferencia(
                 fra.getMuestrasPorSegundo(),
                 fra.getNumeroDeCanales(),
                 duracionIntervaloUS,
                 numeroIntervalos
                 );
     }


     /**
      * Devuelve los archivos de referencia creados
      *
      * @param  numeroIntervalos    El número de intervalos/ficheros de audio de referencia
      * 
      * @return el archivo de audio de referencia para cada intervalo
      */
     public File[] getArchivosReferencia(int numeroIntervalos) {

         File vozReferencia = this.gestorDirectorios.getDirectorioAudioVozReferencia().listFiles()[0];
         //return this.gestorDirectorios.getDirectorioAudioVozReferencia().listFiles();
         File[] ficherosReferencia = new File[numeroIntervalos];
         for (int i=0; i<numeroIntervalos; i++) {

             ficherosReferencia[i] = vozReferencia;
         }

         return ficherosReferencia;
     }


     /**
      * Devuelve los archivos de audio recibido
      *
      * @return el archivo de audio recibido en cada intervalo
      */
     public File[] getArchivosRecibidos() {

         return this.gestorDirectorios.getFicherosUltimoAudioRecibido();
     }


     /**
      *
      */
     public GestorDirectorios getGestorDirectorios() {

         return this.gestorDirectorios;
     }
         

}

