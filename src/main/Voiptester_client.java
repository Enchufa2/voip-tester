
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

package main;

import nucleo.cliente.ControlCliente;
import nucleo.cliente.ControlNodos;
import nucleo.cliente.ControlAudio;
import nucleo.cliente.GestorDirectorios;
import nucleo.cliente.GestorParametrosAudio;
import nucleo.cliente.GestorParametrosTransmision;
import qos.protocolos.sincronizacion.ParametrosSincronizacion;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.estimacionQoS.*;
import audio.FormatoCodec;
import audio.gst.FormatoCodecGst;
import audio.GestorCodecsAudio;
import argparser.*;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import org.gstreamer.Gst;

/**
 *
 */
public class Voiptester_client {

    private ControlCliente controlCliente;
    private ControlNodos controlNodos;
    private ControlAudio controlAudio;
    private Boolean downlink;

    /**
     *
     */
    public Voiptester_client() throws Exception{

        this.controlNodos = new ControlNodos(
                GestorDirectorios.getInstance().getDirectorioLogs());
        this.controlCliente = new ControlCliente(controlNodos);

    }

    /**
     *
     */
    public void registrarCliente(String hostRegistro, int puertoRegistro, int puertoCliente)
            throws Exception {

        this.controlCliente.conectarConServidor(hostRegistro, puertoRegistro, puertoCliente);
    }

    /**
     *
     */
    public void desconectarCliente() throws Exception {

        this.controlCliente.desconectarConServidor();
    }

    /**
     *
     */
    public void conectarSockets(
            String hostLocal,
            String hostLocalPublic,
            int puertoLocal,
            String hostRemoto,
            String hostRemotoPublic,
            int puertoRemoto
            )throws Exception{

        controlNodos.cerrarSocketNodoRemoto();
        controlNodos.cerrarSocketNodoLocal();
        controlNodos.configurarSocketNodoRemoto(hostRemoto, puertoRemoto);
        controlNodos.configurarSocketNodoLocal(hostLocal, puertoLocal);
        controlNodos.conectarSockets( new InetSocketAddress( hostLocalPublic, puertoLocal ),
                                      new InetSocketAddress( hostRemotoPublic, puertoRemoto ));
    }


    /**
     *
     */
    public ParametrosSincronizacion sincronizar(int numeroIntentos){

        ParametrosSincronizacion parametrosSincronizacion = null;

        for (int i=1; i<=numeroIntentos; i++) {
            try {
                this.controlNodos.sincronizarConNodoRemoto();
                parametrosSincronizacion = this.controlNodos.estimarParametrosSincronizacion();
                break;

            }catch(Exception e){

                if(i==numeroIntentos){
                    parametrosSincronizacion = new ParametrosSincronizacion();
                    parametrosSincronizacion.setTiempoIdaVuelta(10000000000L);
                    System.out.println(e.getMessage());
                }

            }
        }

        return parametrosSincronizacion;
    }


    /**
     *
     */
    public void prepararNodosParaTransmision(
            ParametrosTransmision parametrosTransmision) throws Exception {

        this.controlNodos.ajustarParametrosTransmision(
                parametrosTransmision);

        //Depende del sentido del enlace
        if (!this.downlink) {

            this.controlNodos.crearMensajesEnlaceAscendente();

        } else {

            this.controlNodos.crearMensajesEnlaceDescendente();
        }
    }

    /**
     *
     */
    public File[] procesadoInicialDeVoz(FormatoCodec formatoCodec, int duracionIntervaloUS, int numeroIntervalos)
            throws Exception {

        //crear audio referencia
        this.controlAudio = new ControlAudio();
        File[] originales = this.controlAudio.crearAudioReferencia(
                formatoCodec.getFormatoRawAudio(),
                duracionIntervaloUS,
                numeroIntervalos);

        //comprimir
        byte[] audioComprimido = this.controlAudio.comprimirAudio(formatoCodec, duracionIntervaloUS);

        //mapear segÃºn el enlace
        if (!this.downlink) {

            this.controlNodos.mapearAudioNodoLocal(audioComprimido);

        } else {

            this.controlNodos.mapearAudioNodoRemoto(audioComprimido);

        }

        return originales;
    }


    /**
     *
     */
    public void obtenerTimestamps(ParametrosSincronizacion pS)
            throws Exception {

        if (!this.downlink) {

            this.controlNodos.obtenerTimestampsEnlaceAscendente(
                    (int) (pS.getTiempoIdaVuelta() / 1000));
            this.controlNodos.corregirTimestampsEmisor(
                    pS.getOffsetLocal());

        } else {

            this.controlNodos.obtenerTimestampsEnlaceDescente(
                    (int) (pS.getTiempoIdaVuelta() / 1000));
            this.controlNodos.corregirTimestampsReceptor(
                    pS.getOffsetLocal());
        }
    }

    /**
     *
     */
    public File[] procesadoFinalDeVoz(FormatoCodec formatoCodec)
            throws Exception {

        byte[][] audioRecibido;

        if (!this.downlink) {

            audioRecibido = this.controlNodos.audioRecibidoNodoRemoto();

        } else {

            audioRecibido = this.controlNodos.audioRecibidoNodoLocal();

        }

        File[] recibidos = this.controlAudio.descomprimirAudio(
                audioRecibido,
                formatoCodec);

        return recibidos;

    }


    /**
     *
     */
    public ArrayList<EstimadorQoS> crearEstimadores(
            ArrayList<NombreMedida> medidasArealizar,
            ParametrosTransmision pT,
            FormatoCodec fc
            ) {

       ArrayList<EstimadorQoS> estimadores = new ArrayList<EstimadorQoS>();

       long[] timestampsPE = this.controlNodos.getTimestampsEmisor();
       long[] timestampsPR = this.controlNodos.getTimestampsReceptor();

       for (NombreMedida nm: medidasArealizar) {
          
           switch(nm) {

               case RETARDO:

                   if (fc != null) {
                       estimadores.add(new EstimadorRetardo(
                               fc,
                               pT,
                               timestampsPE,
                               timestampsPR));
                   } else {
                       estimadores.add(new EstimadorRetardo(
                               pT,
                               timestampsPE,
                               timestampsPR));
                   }
                   break;

               case JITTER:

                   if (fc != null) {
                       estimadores.add(new EstimadorJitter(
                               fc,
                               pT,
                               timestampsPE,
                               timestampsPR));
                   } else {
                       estimadores.add(new EstimadorJitter(
                               pT,
                               timestampsPE,
                               timestampsPR));
                   }

                   break;

               case PROB_PERDIDAS:

                   if (fc != null) {
                       estimadores.add(new EstimadorProbabilidadPerdidas(
                               fc,
                               pT,
                               timestampsPR));
                   } else {
                       estimadores.add(new EstimadorProbabilidadPerdidas(
                               pT,
                               timestampsPR));
                   }

                   break;

               case DIST_PERDIDAS:

                   int rafaga_maxima = 50;
                   if (fc != null) {
                       estimadores.add(new EstimadorDistribucionPerdidas(
                               fc,
                               pT,
                               this.controlNodos.getTimestampsReceptor(),
                               rafaga_maxima )
                               );
                   } else {
                       estimadores.add(new EstimadorDistribucionPerdidas(
                               pT,
                               this.controlNodos.getTimestampsReceptor(),
                               rafaga_maxima )
                               );
                   }

                   break;

               case ANCHO_BANDA:

                   if (fc != null) {
                       estimadores.add(new EstimadorAnchoBanda(
                               fc,
                               pT,
                               timestampsPR));
                   } else {
                       estimadores.add(new EstimadorAnchoBanda(
                               pT,
                               timestampsPR));
                   }


                   break;

               case EMODEL:

                   int gmin = 15;
                   if (fc != null) {
                       estimadores.add(new EstimadorEmodel(
                               fc,
                               pT,
                               timestampsPE,
                               timestampsPR,
                               gmin)
                               );
                   } else {
                       estimadores.add(new EstimadorEmodel(
                               pT,
                               timestampsPE,
                               timestampsPR,
                               gmin )
                               );
                   }


                   break;

               case PESQ:

                   if (fc != null)
                       estimadores.add(new EstimadorPesq(
                               fc,
                               pT,
                               this.controlAudio.getArchivosReferencia(pT.getNumeroIntervalos()),
                               this.controlAudio.getArchivosRecibidos(),
                               this.controlAudio.getGestorDirectorios().getRutaPesq() )
                               );

                   break;
           }
       }

       return estimadores;

   }


    /**
     *
     */
    public ArrayList<MedidaQoS> realizarEstimaciones(ArrayList<EstimadorQoS> estimadores) {

        return ControlNodos.estimarParametrosQos(estimadores);
    }


    /**
     *
     */
    public static ParametrosTransmision getParametrosTransmision(
            int tEE,
            int lM,
            int mPI,
            int nI,
            FormatoCodec fc
            ){

        GestorParametrosTransmision gpt;

        if(fc!=null) {

            gpt = new GestorParametrosAudio(fc);
            tEE = (int) ( ( (int) (tEE/fc.duracionTramasEnMicrosegundos(1)) )*fc.duracionTramasEnMicrosegundos(1));
            gpt.setTiempoEntreEnvios(tEE);

        }else{
            gpt = new GestorParametrosTransmision();
            gpt.setTiempoEntreEnvios(tEE);
            gpt.setLongitudMensaje(lM);
        }

        gpt.setMensajesPorIntervalo(mPI);
        gpt.setNumeroIntervalos(nI);

        return gpt.getParametrosTransmision();

    }
    
    
    /**
     * 
     */
    public String getDirectionToString(){
        if(this.downlink)
            return "DL";
        
        return "UL";
    }



    /**
     * 
     */
    public static void main(String args[]){
       
        
        Voiptester_client cliente;
        ArgumentosEntrada argumentosEntrada;
        FormatoCodec formatoCodec = null;
        GestorCodecsAudio gestorCodecs;
        //Boolean incluirAudio = Boolean.FALSE;
        ArrayList<MedidaQoS> medidasQoS;
        ArrayList<NombreMedida> medidasArealizar=null;

        try {

            cliente = new Voiptester_client();
            argumentosEntrada = cliente.new ArgumentosEntrada();
            
            //parsear comando
            argumentosEntrada.parser.matchAllArgs(args);
            try{
                medidasArealizar = argumentosEntrada.comprobarValoresIntroducidos();
                 //obtener propiedades de codec
                if(argumentosEntrada.incluirAudio) {

                    Gst.init();
                    gestorCodecs = new GestorCodecsAudio();                    
                    formatoCodec = argumentosEntrada.comprobarOptionGst(gestorCodecs);
            }
            }catch(Exception e) {
                System.out.println(e.getMessage());
                System.out.println(argumentosEntrada.parser.getSynopsisString());
                System.exit(2);
            }
            cliente.downlink = argumentosEntrada.downLink.value;
            
            //comprobar parametros de transmision introducidos
            ParametrosTransmision parametrosTransmision = Voiptester_client.getParametrosTransmision(
                    argumentosEntrada.tParameters[0],
                    argumentosEntrada.tParameters[1],
                    argumentosEntrada.tParameters[2],
                    argumentosEntrada.tParameters[3],
                    formatoCodec);
           

            //registro
            System.out.print("Register process initiated......");
            //System.setProperty("java.rmi.server.hostname", argumentosEntrada.clientPort.value);
            cliente.registrarCliente(
                    argumentosEntrada.serverHost.value,
                    argumentosEntrada.serverPort.value,
                    argumentosEntrada.clientPort.value);
            System.out.println("OK");

            //conexión de sockets
            System.out.print("Connecting timestamps protocol sockets......");
            if (argumentosEntrada.publicLocalHost.value==null){
                argumentosEntrada.publicLocalHost.value = argumentosEntrada.localHost.value;
            }
            if ( argumentosEntrada.publicRemoteHost.value==null){
               argumentosEntrada.publicRemoteHost.value = argumentosEntrada.remoteHost.value;
            }
            cliente.conectarSockets(
                    argumentosEntrada.localHost.value,
                    argumentosEntrada.publicLocalHost.value,
                    argumentosEntrada.localPort.value,
                    argumentosEntrada.remoteHost.value,
                    argumentosEntrada.publicRemoteHost.value,
                    argumentosEntrada.remotePort.value);
            System.out.println("OK");

            //Ajuste de parámetros de transmision
            System.out.print("Preparing nodes to transmission...... ");

            cliente.prepararNodosParaTransmision(parametrosTransmision);
            System.out.println("OK");
            System.out.println("Transmission parameters set: " + parametrosTransmision);

            //Procesado inicial de voz
            if (argumentosEntrada.incluirAudio) {

                System.out.print("Initial voice processing...... ");
                cliente.procesadoInicialDeVoz(
                        formatoCodec,
                        (int) formatoCodec.duracionEnMicrosegundos(parametrosTransmision.numeroBytesPorIntervalo()),
                        parametrosTransmision.getNumeroIntervalos());
            }
            System.out.println("OK");

            //sincronizar
            System.out.print("Synchronizing nodes......");
            ParametrosSincronizacion pS = cliente.sincronizar(3);
            if (pS.getOffsetLocal() != 0) {
                System.out.println("OK");
                System.out.println(pS);
            } else {
                System.out.println("fail");
            }

            //Obtener timestamps
            System.out.print("Getting timestamps( " + cliente.getDirectionToString() + " )......");
            cliente.obtenerTimestamps(pS);
            System.out.println("OK");

            //Procesado final de voz
            if (argumentosEntrada.incluirAudio) {
                System.out.print("Final voice processing...... ");
                cliente.procesadoFinalDeVoz(formatoCodec);
                System.out.println("OK");
            }

            //realizar estimaciones
            System.out.print("Doing estimations...... ");
            medidasQoS = cliente.realizarEstimaciones(
                    cliente.crearEstimadores(medidasArealizar, parametrosTransmision, formatoCodec));
            System.out.println("OK");

            //Generar resultados
            System.out.print("Generating results......");
            File outputDir = new File(argumentosEntrada.outputDir.value);
            if ( ! outputDir.exists()) {

                outputDir.mkdirs();
            }
            UtilidadesCSV.exportarMedidaQoStoCSV(medidasQoS, outputDir);            
            System.out.println("OK");

            //desconectar
            cliente.desconectarCliente();

        } catch (Exception e) {

            String mensaje = e.getMessage();
            if(e.getCause()!=null)
                mensaje+= ": " + e.getCause().getMessage();
            System.out.println(mensaje);
            System.exit(-1);

        }

    }


    public class ArgumentosEntrada{

        //elementos para parser de linea
        ArgParser parser = new ArgParser("voiptester [{-cP, --clientPort} clientPort]" +
                "[{-sH, --serverHost} serverHost] [{-sP, --serverPort} port] " +
                "[{-lH, --localHost} localhost] [{-plH --publicLocalHost} publicLocalHost] [{-lP, --localPort} localport]" +
                "[{-rH, --rHost} remoteHost] [{-prH --publicRemoteHost} publicRemoteHost] [{-rP, --remotePort} remotePort] " +
                "[{-t, --tParameters} idt ml mpi ni] [{-e, --estimators} djplbeq] [{-dL, --downlink}]" +
                "[{-g, --gst} cN rate channels bN] [{-o, --outputDir} output_dir] [{-?, -help}]");

        IntHolder clientPort = new IntHolder(0);
        StringHolder serverHost = new StringHolder(null);
        IntHolder serverPort = new IntHolder(-1);
        StringHolder localHost = new StringHolder(null);
        StringHolder publicLocalHost = new StringHolder(null); //
        IntHolder localPort = new IntHolder(-1);
        StringHolder remoteHost = new StringHolder(null);
        StringHolder publicRemoteHost = new StringHolder(null); //
        IntHolder remotePort = new IntHolder(-1);
        int [] tParameters = new int[4];
        StringHolder estimators = new StringHolder(null);
        BooleanHolder downLink = new BooleanHolder(false);
        int[] gst = new int[4];
        StringHolder outputDir = new StringHolder("results");
        //flag para indicar si hay que incluir audio en los mensajes
        boolean incluirAudio = false;

        /**
         *
         */
        public ArgumentosEntrada(){

            this.parser.addOption("-cP,--clientPort %d #Set the client port which receives callbacks from the server (optional)", this.clientPort);
            this.parser.addOption("-sH,--serverHost %s #The register server domain name or IPv4 (mandatory)", this.serverHost);
            this.parser.addOption("-sP, --serverPort %d #The register server port (mandatory)", this.serverPort);
            this.parser.addOption("-lH,--localHost %s #The local socket domain name or IPv4 (mandatory)", this.localHost);
            this.parser.addOption("-plH,--publicLocalHost %s #The public local socket domain name or IPv4 (optional)", this.publicLocalHost);
            this.parser.addOption("-lP, --localPort %d #The local socket port (mandatory)", this.localPort);
            this.parser.addOption("-rH,--remoteHost %s #The remote socket domain name or IPv4 (mandatory)", this.remoteHost);
            this.parser.addOption("-prH,--publicRemoteHost %s #The public remote socket domain name or IPv4 (optional)", this.publicRemoteHost);
            this.parser.addOption("-rP, --remotePort %d #The remote socket port (mandatory)", this.remotePort);
            this.parser.addOption("-t,--tParameters %dX4 #The transmission parameters (mandatory): \n"
                    + "\t\t\t\t" + "idt - interdeparture time in microseconds\n"
                    + "\t\t\t\t" + "tml - message lenght in bytes\n"
                    + "\t\t\t\t" + "mpe - messages per interval\n"
                    + "\t\t\t\t" + "ni - number of intervals\n",
                    this.tParameters
                    );
            this.parser.addOption("-e,--estimators %s #Select the QoS parameters to be measured (mandatory at least one):\n"
                    + "\t\t\t\t" + "d - delay in miliseconds\n"
                    + "\t\t\t\t" + "j - jitter in miliseconds\n"
                    + "\t\t\t\t" + "p - loss probability\n"
                    + "\t\t\t\t" + "l - loss distribucion (frequency lossess vs burst lenght\n"
                    + "\t\t\t\t" + "b - bandwidth in KB/s\n"
                    + "\t\t\t" + "e - R factor from E-Model\n" + "q - MOS from PESQ",
                    this.estimators
                    );
            this.parser.addOption("-dL, --downLink %v #Set the transmission direction from server to client (default is uplink)", this.downLink);

            this.gst[0] = Integer.MAX_VALUE;
            this.gst[3] = Integer.MIN_VALUE;
            this.parser.addOption("-g, --gst %dX4 #Set to include compressed audio into messages (optional or mandatory if PESQ is set). "
                    + "This option is specified by four integers, which meaning:\n"
                    + "\t\t\t\t" + "cN - Codec Number. Se the voice coding technique\n"
                    + "\t\t\t\t" + "rate - The raw audio sample rate in Hz\n"
                    + "\t\t\t\t" + "channels - The number of channels of raw audio\n"
                    + "\t\t\t\t" + "bN - Bitrate number. Set the bitrate of audio coded. It is a " +
                    "a integer that corresponds with a bitrate value\n"
                    + "See codec_list.txt for the supported codecs and option values",
                    this.gst
                    );
            this.parser.addOption("-o,--outputDir %s #Output directory where results will be saved."
                    + "By default the output directory is <current_directory/results", this.outputDir);
        }

        /**
         *
         */
        public ArrayList<NombreMedida> comprobarValoresIntroducidos()
                throws Exception {

            ArrayList<NombreMedida> medidasArealizar = new ArrayList<NombreMedida>();

            if (this.serverHost.value == null || this.serverPort.value == -1) {
                throw new Exception("You must specify a server host and port");
            }

            if (this.localHost.value == null || this.localPort.value == -1 ||
                    this.remoteHost.value == null || this.remotePort.value == -1) {
                throw new Exception("Invalid sockets directions");
            }

            int[] estimatorCounter = new int[7];
            for (int i = 0; i < this.estimators.value.length(); i++) {
                char c = this.estimators.value.charAt(i);
                switch (c) {
                    case 'd':
                        if (estimatorCounter[0] == 0) {
                            medidasArealizar.add(NombreMedida.RETARDO);
                            estimatorCounter[0]++;
                        }
                        break;

                    case 'j':
                        if (estimatorCounter[1] == 0) {
                            medidasArealizar.add(NombreMedida.JITTER);
                            estimatorCounter[1]++;
                        }
                        break;

                    case 'p':
                        if (estimatorCounter[2] == 0) {
                            medidasArealizar.add(NombreMedida.PROB_PERDIDAS);
                            estimatorCounter[2]++;
                        }
                        break;

                    case 'l':
                        if (estimatorCounter[3] == 0) {
                            medidasArealizar.add(NombreMedida.DIST_PERDIDAS);
                            estimatorCounter[3]++;
                        }
                        break;

                    case 'b':
                        if (estimatorCounter[4] == 0) {
                            medidasArealizar.add(NombreMedida.ANCHO_BANDA);
                            estimatorCounter[4]++;
                        }
                        break;

                    case 'e':
                        if (estimatorCounter[5] == 0) {
                            medidasArealizar.add(NombreMedida.EMODEL);
                            estimatorCounter[5]++;
                        }
                        break;

                    case 'q':
                        if (estimatorCounter[6] == 0) {
                            medidasArealizar.add(NombreMedida.PESQ);
                            this.incluirAudio = true;
                            estimatorCounter[6]++;
                        }
                        break;
                }
            }
            if (medidasArealizar.size() == 0) {
                throw new Exception("You must specify one measuremente to perform at least");
            }

            if (this.gst[0] != Integer.MAX_VALUE){
                this.incluirAudio = true;
            }

            File output = new File(this.outputDir.value);
            if(! output.exists()){

                if(! output.mkdirs())
                    throw new Exception("Invalid output directory. It does not exist and it was not possible to be created");
            }

            return medidasArealizar;
        }

        private FormatoCodec comprobarOptionGst(GestorCodecsAudio gca)
                throws Exception{

            String[] codecsAelegir = {
                "A Law",
                "Mu Law",
                "ADPCM MS",
                "G.726",
                "GSM-FR",
                "WMA v1",
                "Siren 7" ,
                "Speex" ,
                "AAC",
                "CELT"
                };
            FormatoCodecGst fc = null;
            
            if (this.gst[0] == Integer.MAX_VALUE)
                    throw new Exception("Measuring PESQ requires to specify a codec");
            else if(this.gst[0] <0 || this.gst[0]>9)
                throw new Exception("Invalid codec number");
            

            if( this.gst[1]!=8000 && this.gst[1]!=16000)
                throw new Exception("Rate must be 8000 or 16000 Hz " + this.gst[1]);

            if(this.gst[2]!=1 && this.gst[2]!=2)
                throw new Exception("Channels must be 1 or 2");
            
            fc = (FormatoCodecGst) gca.getFormatoCodec( codecsAelegir[this.gst[0]] );            
            if(! fc.estaSoportado())
                throw new Exception("Codec not available in current GStreamer Framework");

            fc.getFormatoRawAudio().setMuestrasPorSegundo(this.gst[1]);
            fc.getFormatoRawAudio().setNumeroDeCanales(this.gst[2]);

            Object[] propValues = fc.getPropiedadesCodec().getValoresPropiedad("bitrate");

            if( propValues != null ) {

                if(this.gst[3]<0 || this.gst[3]>= propValues.length)
                    throw new Exception("Invalid bitrate number for " + fc.getNombreCodec());

                fc.getPropiedadesCodec().seleccionarValorPropiedad("bitrate", this.gst[3]);

            }else{
                if(this.gst[3]!=-1)
                    throw new Exception("Invalid bitrate number for" + fc.getNombreCodec());
            }

            return fc;

        }
    }

}
