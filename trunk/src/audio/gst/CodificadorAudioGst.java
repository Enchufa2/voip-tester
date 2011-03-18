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
import java.io.File;
import audio.gst.FormatoCodecGst.PropiedadesCodificador;
import java.util.concurrent.Semaphore;

import org.gstreamer.Buffer;
import org.gstreamer.Pipeline;
import org.gstreamer.Bus;
import org.gstreamer.Caps;
import org.gstreamer.Pad;
import org.gstreamer.GstObject;
import org.gstreamer.Gst;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.State;
import org.gstreamer.elements.AppSink;
import org.gstreamer.elements.AppSrc;
import org.gstreamer.elements.FileSrc;
import com.sun.jna.Pointer;

/**
 *
 * @author Antonio
 */
public class CodificadorAudioGst implements CodificadorAudio{

    private Pipeline pipeline;

    private AudioException audioException;

    private final Semaphore semaforo = new Semaphore(0);

    public CodificadorAudioGst() {

        this.pipeline = null;

    }


    /**
     *
     * Toma un archivo de audio en formato WAV, obtiene el raw audio y le aplica
     * la compresión de acuerdo a las características indicadas, devolviendo
     * el conjunto de buffers que contienen el audio comprimido.
     *
     * @param audioOriginal         Archivo de audio en formato WAV
     * @param formatoObjetivo       Propiedades del audio comprimido.
     *                              Debe ser de tipo <code>FormatoCodecGst</code>
     *
     * @return                      Los buffers con el audio comprimido
     * 
     * @throws AudioException       Si ocurre un error durante el procesamiento
     *
     * @see CodificadorAudio
     * @see FormatoCodecGst
     * @see Pipeline
     * @see Element
     */
    public synchronized BuffersAudio codificar(
            File audioOriginal,
            FormatoCodec formatoObjetivo
            )
            throws AudioException{

        FormatoCodecGst formatoObjetivoGst = (FormatoCodecGst) formatoObjetivo;
        FormatoRawAudioGst formatoRawAudioGst = (FormatoRawAudioGst) formatoObjetivoGst.getFormatoRawAudio();

        //El pipeline de gstreamer para codificación está formado por:
        //filesrc | wavparse | audioresample | audioconvert | capsfilter | element(codificador) | appsink
        this.pipeline = new Pipeline("pipeline");
        Bus bus = this.pipeline.getBus();
        this.audioException = null;

        //definición de elementos
        FileSrc filesrc = (FileSrc) ElementFactory.make("filesrc", "source");        
        filesrc.setLocation(audioOriginal);
        System.out.println(filesrc.get("location"));
        Element demuxer = ElementFactory.make("wavparse","fileDecoder");
        Element ident = ElementFactory.make("identity", "ident");
        Element audioconvert = ElementFactory.make("audioconvert","audio converter");
        Element audioresample = ElementFactory.make("audioresample", "audio resampler");
        Element capsfilterreq = ElementFactory.make("capsfilter", "capsfiltereq");

        capsfilterreq.setCaps(
                formatoObjetivoGst.getCapsNecesarios()
                );
        
        Element codaudio = ElementFactory.make(
                formatoObjetivoGst.getGstCodificador(),"codaudio"
                );

        //seleccion de propiedades de codec
        PropiedadesCodificador pc = formatoObjetivoGst.getPropiedadesCodec();
        int numeroPropiedades = pc.getNumeroPropiedades();
        
        String[] nombrePropiedades = pc.getNombrePropiedades();
        for (int i=0; i<numeroPropiedades; i++) {
            
            codaudio.set(
                    nombrePropiedades[i],
                    pc.getValorPropiedad(nombrePropiedades[i])
                    );
        }

        AppSink appsink = (AppSink) ElementFactory.make("appsink", "appsink");
        appsink.set("emit-signals", true);
        appsink.set("sync", false);

        //creación del pipeline
        this.pipeline.addMany(filesrc,  demuxer, ident, audioresample, audioconvert, capsfilterreq, codaudio, appsink);
        Element.linkMany(filesrc, demuxer);
        Element.linkMany(ident, audioresample ,audioconvert, capsfilterreq, codaudio, appsink);

        //añadir listeners
        //enlace dinámico para el decodificador del fichero
        CodificadorAudioGst.SignalPadAdded signalPadAdded= new CodificadorAudioGst.SignalPadAdded(ident);
        demuxer.connect(signalPadAdded);

        //manejo del flujo de bytes en el pipeline
        CodificadorAudioGst.NewBufferSignal newBufferSignal = new CodificadorAudioGst.NewBufferSignal(appsink);
        appsink.connect(newBufferSignal );
        
        //fin de stream
        bus.connect(new CodificadorAudioGst.SignalBusEos());
        //error en el procesamient
        bus.connect(new CodificadorAudioGst.SignalBusError());

        //inicio la codificación
        this.pipeline.play();

        //esperar el fin de la codificación
        this.semaforo.acquireUninterruptibly();
         
        formatoObjetivoGst.setCapsCodificacion(newBufferSignal.capsCodificacion);
        formatoRawAudioGst.setCapsRawAudio(signalPadAdded.caps);

        newBufferSignal.buffersAudio.setFormatoCodec(formatoObjetivoGst);
        
        this.pipeline.dispose();

        if (this.audioException !=null) throw this.audioException;

        return newBufferSignal.buffersAudio;
    }


    /**
     *
     * Toma un cojunto de buffers de audio y le aplica el proceso de descompresión
     * generando un archivo en formato WAV con el resultado de la descompresión.
     *
     * @param targetFile        El archivo de audio WAV que contendrá el audio descomprimido
     * @param audioCodificado   Los buffers de audio comprimido. Las propiedades de audio que
     *                          deben obtenerse mediante un objeto tipo  <code>FormatoCodecGst</code>
     *
     * @return                  El archivo de audio WAV que contendrá el audio descomprimido
     *
     * @throws AudioException       Si ocurre un error durante el procesamiento
     *
     * @see BuffersAudio
     * @see FormatoCodecGst
     * @see Pipeline
     * @see Element
     *
     */
    public synchronized File decodificar(
            File targetFile,
            BuffersAudio audioCodificado
            )
            throws AudioException {

        FormatoCodecGst formatoOrigenGst = (FormatoCodecGst) audioCodificado.getFormatoCodec();
        FormatoRawAudioGst formatoRawAudioGst = (FormatoRawAudioGst) formatoOrigenGst.getFormatoRawAudio();
         //El pipeline de gstreamer para decodificación está formado por:
         //appsrc | capsfilter | element(decodifcador) | audioconvert | audioresample | capsfilter | wavenc | filesink
        this.pipeline = new Pipeline("pipeline");
        Bus bus = this.pipeline.getBus();
        this.audioException = null;

        //definición de elementos
        AppSrc appsrc = (AppSrc) ElementFactory.make("appsrc", "appsrc");
        Element capsfilterin = ElementFactory.make("capsfilter", "capsfilterint");

        capsfilterin.setCaps(formatoOrigenGst.getCapsCodificacion());

        Element decodaudio = ElementFactory.make(
                formatoOrigenGst.getGstDecodificador(), "decodaudio"
                );
        
        Element audioconvert = ElementFactory.make("audioconvert", "audioconvert");
        Element audioresample = ElementFactory.make("audioresample", "audioresample");
        Element muxer = ElementFactory.make("wavenc", "fileEncoder");
        Element filesink = ElementFactory.make("filesink", "filesink");
        filesink.set("location", targetFile);
        Element capsfilterout = ElementFactory.make("capsfilter", "capsfilterout");
        capsfilterout.setCaps( formatoRawAudioGst.getCapsRawAudio());

        this.pipeline.addMany(appsrc, capsfilterin, decodaudio, audioconvert, audioresample, capsfilterout, muxer, filesink);
        Element.linkMany(appsrc, capsfilterin, decodaudio, audioconvert, audioresample, capsfilterout, muxer, filesink);

        //manejo del flujo de bytes en el pipeline
        CodificadorAudioGst.NeedDataSignal needDataSignal =
                new CodificadorAudioGst.NeedDataSignal(appsrc, audioCodificado);
        appsrc.connect(needDataSignal);

        //añadir listeners
        bus.connect(new CodificadorAudioGst.SignalBusEos());
        bus.connect(new CodificadorAudioGst.SignalBusError());

        //inicio de la decodificación
        this.pipeline.play();
        //esperar el fin de la decodificación
        this.semaforo.acquireUninterruptibly();
        
        this.pipeline.dispose();
        if (this.audioException != null) throw this.audioException;
        return targetFile;
    }



    /**
     * Implementación de la señal  <code>end of stream</code> (EOS) que indica que se ha
     * procesado el flujo de audio completo.
     *
     * @see Bus
     */
    class SignalBusEos implements Bus.EOS {

        public SignalBusEos() {
        }

        public void endOfStream(GstObject source) {
            
            CodificadorAudioGst.this.pipeline.setState(State.NULL);
            CodificadorAudioGst.this.semaforo.release();
            }
    }


    /**
     * Implmentación de la señal <code>error</code> que indica que se ha producido un error
     * durante el procesamiento del audio
     *
     * @see Bus
     */
    class SignalBusError implements Bus.ERROR {

        public SignalBusError() {
        }

        public void errorMessage(GstObject source, int code, String message) {
            //System.out.println("Error: code=" + code + " message=" + message);
            CodificadorAudioGst.this.pipeline.setState(State.NULL);
            CodificadorAudioGst.this.audioException = new AudioException(
                    "Error while processing audio",
                    message
                    );
            CodificadorAudioGst.this.semaforo.release();

        }
    }


    /**
     * Implementación de la señal <code>pad added</code>, correspondiente al enlace
     * dinámico entre elementos de gstreamer.
     *
     * @see Element
     */
    class SignalPadAdded implements Element.PAD_ADDED {

        private Element nextElement;
        private Caps caps;

        public SignalPadAdded(Element nextElement) {

             this.nextElement = nextElement;
         }


         public void padAdded(Element element, Pad pad) {

            this.caps = pad.getCaps();
            
            
            if (pad.isLinked()) {
                return;
            }

            pad.link(this.nextElement.getStaticPad("sink"));
            
        }
    }


   class ElementHandoffIdent implements Element.HANDOFF {

        private int bytesTotales=0;
        int rate;
        int sampleSize;
        int canales;
        //private double duracionAudio =0;

        public ElementHandoffIdent() {

        }

        public void handoff(Element element, Buffer buffer, Pad pad) {

            
            if (this.bytesTotales ==0) {
                this.rate = buffer.getCaps().getStructure(0).getInteger("rate");
                this.sampleSize = buffer.getCaps().getStructure(0).getInteger("width");
                this.canales = buffer.getCaps().getStructure(0).getInteger("channels");
            }
            bytesTotales += buffer.getSize();
        }

        public double duracionTotalEnMicrosegundos() {

            return 1000000 * (double) (8 * this.bytesTotales ) / (this.rate*this.canales * this.sampleSize);

        }

    }


   /**
    * Implmentación de la señal <code>new buffer</code> que permite obtener
    * los buffers de audio que se generan
    *
    * @see AppSink
    */
    class NewBufferSignal implements AppSink.NEW_BUFFER {

        private Caps capsCodificacion;
        private AppSink appSink;
        private BuffersAudio buffersAudio;
        private int bufferIndex = 0;

        public NewBufferSignal(AppSink appSink) {

            this.appSink = appSink;
            this.buffersAudio = new BuffersAudio();

        }

        public void newBuffer(Element element, Pointer pointer) {

            Buffer buffer = this.appSink.pullBuffer();
            System.out.println(this.capsCodificacion);

            if (this.bufferIndex == 0) {

                this.capsCodificacion = buffer.getCaps();

            }

            int bufferSize = buffer.getSize();

            if (bufferSize != 0) {

                this.buffersAudio.crearBufferAudio(this.bufferIndex, bufferSize);
                buffer.getByteBuffer().get(
                        this.buffersAudio.getBufferAudio(this.bufferIndex),
                        0,
                        bufferSize);
            }

            this.bufferIndex++;
        }

        public BuffersAudio getBuffersAudio() {

            return this.buffersAudio;
        }
    }


    /**
     * Implementación de la señal <code>need data</code> que permite introducir
     * buffers de audio para su procesamient
     *
     * @see AppSrc
     */
    class NeedDataSignal implements AppSrc.NEED_DATA{

        private AppSrc appSrc;
        private BuffersAudio buffersAudio;
        private int indexBuffer=0;

        public NeedDataSignal(AppSrc appSrc, BuffersAudio buffersAudio) {

            this.appSrc = appSrc;
            this.buffersAudio = buffersAudio;
        }

        public void needData(Element element, int length ,Pointer pointer){

            if (indexBuffer < this.buffersAudio.numeroDeBuffers()){

                int bufferSize =this.buffersAudio.longitudBuffer(indexBuffer);
                Buffer buffer = new Buffer(bufferSize);

                if (bufferSize !=0) {
                    buffer.getByteBuffer().put(
                            this.buffersAudio.getBufferAudio(indexBuffer)
                            );

                    this.appSrc.pushBuffer(buffer);
                }

                indexBuffer++;

            }else {
                this.appSrc.endOfStream();
            }

        }
    }

}

