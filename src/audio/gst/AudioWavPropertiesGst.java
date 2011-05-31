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

import org.gstreamer.Buffer;
import org.gstreamer.Pipeline;
import org.gstreamer.Bus;
import org.gstreamer.Caps;
import org.gstreamer.Pad;
import org.gstreamer.GstObject;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.State;
import org.gstreamer.elements.FileSrc;
import audio.AudioException;
import java.io.File;
import java.util.concurrent.Semaphore;


/**
 *
 * Clase encargada de obtener las propiedades de un archivo de audio
 *
 * @author Antonio
 */
public class AudioWavPropertiesGst {

    private Pipeline pipeline;

    private File audioFile;

    private long duracionTotal_us=0;

    private Caps capsWav;

    private final Semaphore semaforo = new Semaphore(0);

    private AudioException audioException=null;




    public AudioWavPropertiesGst(File audioFile)
            throws AudioException{

        this.pipeline = new Pipeline("pipeline");
        this.audioFile = audioFile;

        FileSrc filesrc = (FileSrc) ElementFactory.make("filesrc", "source");
        filesrc.setLocation(audioFile);
        Element demuxer = ElementFactory.make("wavparse","fileDecoder");
        Element fakesink = ElementFactory.make("fakesink", "fakesink");
        fakesink.set("signal-handoffs", true);
        this.pipeline.addMany(filesrc, demuxer, fakesink);
        Element.linkMany(filesrc, demuxer);        

        SignalPadAdded spa = new SignalPadAdded(fakesink);
        demuxer.connect(spa);
        ElementHandoff eh = new ElementHandoff();
        fakesink.connect(eh);
        SignalBusEos sbe = new SignalBusEos();
        SignalBusError sberr = new SignalBusError();               
        this.pipeline.getBus().connect(sbe);
        this.pipeline.getBus().connect(sberr);
              
        this.pipeline.play();

        this.semaforo.acquireUninterruptibly();

        if(this.audioException != null)
            throw this.audioException;

        this.duracionTotal_us = (long) eh.duracionTotalEnMicrosegundos();

        this.pipeline.dispose();
    }

   public Caps getCapsWav() {

       return this.capsWav;
   }

   public long getDuracionTotal_us() {

       return this.duracionTotal_us;
   }


   class ElementHandoff implements Element.HANDOFF {

        private int bytesTotales=0;
        int rate;
        int sampleSize;
        int canales;
        //private double duracionAudio =0;

        public ElementHandoff() {

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
     * ImplementaciÃ³n de la seÃ±al  <code>end of stream</code> (EOS) que indica que se ha
     * procesado el flujo de audio completo.
     *
     * @see Bus
     */
    class SignalBusEos implements Bus.EOS {

        public SignalBusEos() {
        }

        public void endOfStream(GstObject source) {

            AudioWavPropertiesGst.this.pipeline.setState(State.NULL);
            AudioWavPropertiesGst.this.semaforo.release();
            }
    }


    /**
     * ImplmentaciÃ³n de la seÃ±al <code>error</code> que indica que se ha producido un error
     * durante el procesamiento del audio
     *
     * @see Bus
     */
    class SignalBusError implements Bus.ERROR {

        public SignalBusError() {
        }

        public void errorMessage(GstObject source, int code, String message) {
            AudioWavPropertiesGst.this.pipeline.setState(State.NULL);
            if(message.indexOf("type")>0)
                AudioWavPropertiesGst.this.audioException = new AudioException(
                    "audio file processing failed: " + AudioWavPropertiesGst.this.audioFile.getName(),
                    "The file must be an audio WAV file"
                    );
            else
                AudioWavPropertiesGst.this.audioException = new AudioException(
                        "audio file processing failed: " + AudioWavPropertiesGst.this.audioFile.getName(),
                        message);

            AudioWavPropertiesGst.this.semaforo.release();

        }
    }


    /**
     * ImplementaciÃ³n de la seÃ±al <code>pad added</code>, correspondiente al enlace
     * dinÃ¡mico entre elementos de gstreamer.
     *
     * @see Element
     */
    class SignalPadAdded implements Element.PAD_ADDED {

        private Element nextElement;

        public SignalPadAdded(Element nextElement) {

             this.nextElement = nextElement;
         }


         public void padAdded(Element element, Pad pad) {

            //this.caps = pad.getCaps();
            AudioWavPropertiesGst.this.capsWav = pad.getCaps();

            if (pad.isLinked()) {
                return;
            }

            pad.link(this.nextElement.getStaticPad("sink"));

        }
    }   

}

