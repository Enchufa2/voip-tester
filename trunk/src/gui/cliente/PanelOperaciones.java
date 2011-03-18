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


package gui.cliente;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import gui.GuiManager;
import gui.BotonDobleEstado;
import gui.ProgressTask;
import gui.cliente.selectorPT.ControladorParametroAudio;

import nucleo.cliente.ControlAudio;
import nucleo.cliente.NodoClienteException;
import nucleo.cliente.NodoServidorException;
import nucleo.cliente.ControlNodos;

import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.estimacionQoS.*;;
import qos.protocolos.sincronizacion.ParametrosSincronizacion;
import audio.FormatoCodec;
import audio.AudioException;

/**
 *
 * @author Antonio
 */
public class PanelOperaciones 
        extends JPanel implements ActionListener, PropertyChangeListener{

    public final JTextArea textAreaOperaciones;

    public final BotonDobleEstado botonIniciar;

    public PanelConexiones  panelConexiones;

    public PanelConfiguracionPE panelConfiguracionPE;

    public PanelResultados panelResultados;
    
    private ProgressTask progressTask;

    private final JProgressBar progressBar;

    //elementos no grÃ¡ficos
    final ControlNodos controlNodos;

    final ControlAudio controlAudio;

    public final EstadoGlobal estadoGlobal;

    public ArrayList<MedidaQoS> medidasEstimadas;

    public PanelOperaciones(
            ControlNodos controlNodos,
            ControlAudio controlAudio,
            EstadoGlobal estadoGlobal,
            PanelConexiones panelConexiones,
            PanelConfiguracionPE panelConfiguracionPE,
            PanelResultados panelResultados
            ) {

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Operations Running"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));


        this.textAreaOperaciones = new JTextArea(10,50);
        this.textAreaOperaciones.setEditable(false);
        this.textAreaOperaciones.setFont(new Font(Font.DIALOG, Font.BOLD, 14));

        this.botonIniciar = new BotonDobleEstado("Start", "Stop");
        this.botonIniciar.addActionListener(this);

        this.progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        this.progressBar.setStringPainted(true);
               
        this.controlNodos = controlNodos;
        this.controlAudio = controlAudio;
        this.estadoGlobal = estadoGlobal;
        this.estadoGlobal.estadoSocketsConfigurados.addPropertyChangeListener(this);

        this.panelConexiones = panelConexiones;
        this.panelConfiguracionPE = panelConfiguracionPE;
        this.panelResultados = panelResultados;

        //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.insets.top = 20;
        gbc.insets.left = 20;
        
        gbc.gridy = 0;
        gbc.gridx =0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        super.add(this.botonIniciar, gbc);

        gbc.gridx++;
        gbc.ipadx=20;
        gbc.ipady=15;
        gbc.insets.right = 20;
        super.add(this.progressBar, gbc);
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(this.textAreaOperaciones);
        super.add(scrollPane, gbc);
    }



   public void comprobarConexionDS()
            throws NodoServidorException, NodoClienteException{

       
        if( ! this.controlNodos.socketsConectados()) {
            
            
            this.panelConexiones.conexionDS.botonConectar.setEstadoConexion(false);
            this.panelConexiones.conexionDS.botonConectar.setEnabled(true);
            this.panelConexiones.conexionDS.botonConectar.doClick();           
            this.panelConexiones.conexionDS.botonConectar.setEnabled(false);
            while(! this.panelConexiones.progressTask.isDone());
            
        }

    }

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
                    JOptionPane.showMessageDialog(
                        this,
                        e.getMessage(),
                        "Operation failed", JOptionPane.INFORMATION_MESSAGE
                        );
                }                
                
            }
        }

        return parametrosSincronizacion;
    }


   public void prepararNodosParaTransmision()
           throws NodoServidorException, NodoClienteException{

       this.controlNodos.ajustarParametrosTransmision(
               this.panelConfiguracionPE.selectoresPT.getParametrosTransmisionSeleccionados()
               );
       //Depende del sentido del enlace
       if(this.panelConfiguracionPE.seleccionEnlace.isUplink()) {

           this.controlNodos.crearMensajesEnlaceAscendente();

       }else{
           
           this.controlNodos.crearMensajesEnlaceDescendente();
       }
       
   }


   public File[] procesadoInicialDeVoz()
           throws NodoServidorException, NodoClienteException, AudioException {
       
       
       ControladorParametroAudio cpa =
               (ControladorParametroAudio) this.panelConfiguracionPE.selectoresPT.getControladorParametro();
       
       FormatoCodec formatoCodec = this.panelConfiguracionPE.ajusteCodec.getFormatoCodecSeleccionado();
       int duracionIntervaloUS = (int) (cpa.getGPT().getDuracionIntervaloEnMS() * 1000);

       //crear audio referencia          
       File[] originales = this.controlAudio.crearAudioReferencia(
               formatoCodec.getFormatoRawAudio(),
               duracionIntervaloUS,
               cpa.parametrosSeleccionados().getNumeroIntervalos()
               );
       
       //comprimir
       byte[] audioComprimido = this.controlAudio.comprimirAudio(formatoCodec, duracionIntervaloUS);

       //mapear segÃºn el enlace
        if(this.panelConfiguracionPE.seleccionEnlace.isUplink()) {

            this.controlNodos.mapearAudioNodoLocal(audioComprimido);

        }else{

           this.controlNodos.mapearAudioNodoRemoto(audioComprimido);
           
        }

       return originales;
   }


   public void obtenerTimestamps(ParametrosSincronizacion pS)
           throws NodoServidorException, NodoClienteException, InterruptedException {

       if(this.panelConfiguracionPE.seleccionEnlace.isUplink()) {

           this.controlNodos.obtenerTimestampsEnlaceAscendente( 
                   (int) (pS.getTiempoIdaVuelta()/1000)
                   );
           this.controlNodos.corregirTimestampsEmisor(
                   pS.getOffsetLocal()
                   );

       }else{

           this.controlNodos.obtenerTimestampsEnlaceDescente(
                   (int) (pS.getTiempoIdaVuelta()/1000)
                   );
           this.controlNodos.corregirTimestampsReceptor(
                   pS.getOffsetLocal())
                   ;
       }

   }


   public File[] procesadoFinalDeVoz()
           throws NodoServidorException, NodoClienteException, AudioException {

       byte[][] audioRecibido;

       if(this.panelConfiguracionPE.seleccionEnlace.isUplink()) {

            audioRecibido = this.controlNodos.audioRecibidoNodoRemoto();

        }else{

            audioRecibido = this.controlNodos.audioRecibidoNodoLocal();

        }

       File[] recibidos = this.controlAudio.descomprimirAudio(
               audioRecibido,
               this.panelConfiguracionPE.ajusteCodec.getFormatoCodecSeleccionado()
               );

       return recibidos;

   }


   public ArrayList<EstimadorQoS> crearEstimadores() {

       ArrayList<EstimadorQoS> estimadores = new ArrayList<EstimadorQoS>();
       ParametrosTransmision pT =
               this.panelConfiguracionPE.selectoresPT.getParametrosTransmisionSeleccionados();
       FormatoCodec fc = null;
       if (this.panelConfiguracionPE.ajusteCodec.codecEstaSeleccionado())
           fc = this.panelConfiguracionPE.ajusteCodec.getFormatoCodecSeleccionado();

       long[] timestampsPE = this.controlNodos.getTimestampsEmisor();
       long[] timestampsPR = this.controlNodos.getTimestampsReceptor();

       for (NombreMedida nm: NombreMedida.values()) {

           if(this.panelConfiguracionPE.seleccionMedidas.medidaSeleccionada(nm))
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

                   int rafaga_maxima = this.panelConfiguracionPE.seleccionMedidas.getCheckBoxForMedida(nm).getValorPropiedad(0);
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

                   if (fc != null) {
                       estimadores.add(new EstimadorEmodel(
                               fc,
                               pT,
                               timestampsPE,
                               timestampsPR,
                               this.panelConfiguracionPE.seleccionMedidas.getCheckBoxForMedida(nm).getValorPropiedad(0) )
                               );
                   } else {
                       estimadores.add(new EstimadorEmodel(
                               pT,
                               timestampsPE,
                               timestampsPR,
                               this.panelConfiguracionPE.seleccionMedidas.getCheckBoxForMedida(nm).getValorPropiedad(0) )
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


   public void realizarEstimaciones(ArrayList<EstimadorQoS> estimadores) {

       this.medidasEstimadas = ControlNodos.estimarParametrosQos(estimadores);
   }

   public void mostrarMedidas() {

       ParametrosTransmision pT = this.panelConfiguracionPE.selectoresPT.getParametrosTransmisionSeleccionados();
       for (int i=0; i<pT.getNumeroIntervalos(); i++) {
            this.textAreaOperaciones.append("Interval "+i + '\n');
            this.textAreaOperaciones.append("-------------" + '\n');
            for (int j=0; j<PanelOperaciones.this.medidasEstimadas.size(); j++) {
                    //System.out.println(this.medidasEstimadas.get(j));

                this.textAreaOperaciones.append(this.medidasEstimadas.get(j).getParametroQoS(i).toString() + '\n');
                }
            this.textAreaOperaciones.append("\n");
            //System.out.println('\n');
        }
   }

    public void programarOperaciones() {

        this.progressTask = new ProgressTask<Boolean, Void>(false) {

            public Boolean doInBackground()
                    throws NodoServidorException, NodoClienteException, AudioException, InterruptedException {
                
                Boolean exito=false;

                PanelOperaciones.this.textAreaOperaciones.setText(null);
                PanelOperaciones.this.estadoGlobal.setEstadoSocketsConfigurados(Estado.Estados.OBTENIENDO_MEDIDAS_EXPERIMENTALES);
                PanelOperaciones.this.textAreaOperaciones.append("Experimental measurement process started" + '\n'+'\n');

                PanelOperaciones.this.textAreaOperaciones.append("Checking connection...... ");
                PanelOperaciones.this.comprobarConexionDS();
                PanelOperaciones.this.textAreaOperaciones.append("OK" + '\n'+ '\n');
                this.setProgress(10);

                PanelOperaciones.this.textAreaOperaciones.append("Preparing nodes to transmission...... ");
                PanelOperaciones.this.prepararNodosParaTransmision();
                PanelOperaciones.this.textAreaOperaciones.append("OK" + '\n' +'\n');
                PanelOperaciones.this.textAreaOperaciones.append("Transmission parameters set: " + '\n' +
                                                                    PanelOperaciones.this.panelConfiguracionPE.selectoresPT.
                                                                    getParametrosTransmisionSeleccionados() + '\n'+ '\n'
                                                                );
                
                this.setProgress(20);

                
                if(PanelOperaciones.this.panelConfiguracionPE.ajusteCodec.codecEstaSeleccionado()) {
                    PanelOperaciones.this.textAreaOperaciones.append("Initial voice processing...... ");                    
                    PanelOperaciones.this.procesadoInicialDeVoz();
                    PanelOperaciones.this.textAreaOperaciones.append("OK" + '\n'+ '\n');
                    
                }
                this.setProgress(40);

                PanelOperaciones.this.textAreaOperaciones.append("Synchronizing nodes...... ");
                ParametrosSincronizacion pS = PanelOperaciones.this.sincronizar(2);
                if(pS.getOffsetLocal() != 0){
                    PanelOperaciones.this.textAreaOperaciones.append("OK" + '\n');
                    PanelOperaciones.this.textAreaOperaciones.append(pS.toString() + '\n' + '\n');
                }else
                    PanelOperaciones.this.textAreaOperaciones.append("fail" + '\n'+ '\n');

                this.setProgress(50);

                PanelOperaciones.this.progressBar.setIndeterminate(true);
                PanelOperaciones.this.textAreaOperaciones.append("Getting timestamps( " +
                        PanelOperaciones.this.panelConfiguracionPE.seleccionEnlace.getSentido() +" )" + "...... ");
                PanelOperaciones.this.obtenerTimestamps(
                        pS
                        );
                PanelOperaciones.this.progressBar.setIndeterminate(false);

                if(!super.isCancelled()){

                    PanelOperaciones.this.textAreaOperaciones.append("OK" + '\n'+ '\n');
                    this.setProgress(70);


                    if (PanelOperaciones.this.panelConfiguracionPE.ajusteCodec.codecEstaSeleccionado()) {
                        PanelOperaciones.this.textAreaOperaciones.append("Final voice processing...... ");
                        PanelOperaciones.this.procesadoFinalDeVoz();
                        PanelOperaciones.this.textAreaOperaciones.append("OK" + '\n'+ '\n');
                    }

                    PanelOperaciones.this.textAreaOperaciones.append("Experimental measurements got" + '\n'+ '\n');
                    this.setProgress(80);


                    PanelOperaciones.this.textAreaOperaciones.append("Doing estimations...... ");
                    PanelOperaciones.this.realizarEstimaciones(
                            PanelOperaciones.this.crearEstimadores());
                    PanelOperaciones.this.textAreaOperaciones.append("Ok" + '\n'+ '\n');
                    this.setProgress(90);

                    PanelOperaciones.this.textAreaOperaciones.append("Generating results......");
                    PanelOperaciones.this.panelResultados.setMedidasQoS(PanelOperaciones.this.medidasEstimadas);
                    PanelOperaciones.this.textAreaOperaciones.append("Ok" + '\n'+ '\n');
                    this.setProgress(100);
                    
                    exito = true;                
                }
                
                return exito;
            }

            public void doneAfter(Boolean exito) {

                if(exito!=null && exito){                    
                    PanelOperaciones.this.estadoGlobal.setEstadoSocketsConfigurados(
                            Estado.Estados.ESTIMACIONES_DISPONIBLES
                            );
                }else{
                    if(super.isCancelled())

                        PanelOperaciones.this.textAreaOperaciones.append("Operation cancel" + '\n');
                    else
                        PanelOperaciones.this.textAreaOperaciones.append("Operation error" + '\n');
                    
                    PanelOperaciones.this.estadoGlobal.setEstadoSocketsConfigurados(
                            Estado.Estados.PARAMETROS_ESTIMACION_CONFIGURADOS
                            );
                }

                PanelOperaciones.this.progressBar.setIndeterminate(false);
                PanelOperaciones.this.botonIniciar.setAccionRealizada(false);
                
            }
        };

        this.progressTask.addPropertyChangeListener(this);

    }



    public void actionPerformed(ActionEvent actionEvent) {

        if(actionEvent.getSource() == this.botonIniciar) {

            if(! this.botonIniciar.getAccionRealizada()) {
                this.programarOperaciones();
                this.progressTask.execute();
                this.botonIniciar.setAccionRealizada(true);                

            }else{
                this.progressTask.cancel(true);
                try{
                    this.controlNodos.cerrarSocketNodoLocal();
                }catch(NodoClienteException nce) {nce.printStackTrace();}

                try{
                    this.controlNodos.cerrarSocketNodoRemoto();
                }catch(NodoServidorException nse){nse.printStackTrace();}
                
                this.botonIniciar.setAccionRealizada(false);
            }
            
        }
    }

    public void propertyChange(PropertyChangeEvent pce) {

        if (pce.getPropertyName().equals("estado")){

            Estado.Estados nuevoEstado = (Estado.Estados) pce.getNewValue();
            this.ajustarComponentes(nuevoEstado);
            
        }

        if ("progress".equals(pce.getPropertyName())) {
            if(! this.progressBar.isIndeterminate()) {
                int progress = (Integer) pce.getNewValue();
                this.progressBar.setValue(progress);
            }
        }
    }

    public void ajustarComponentes(Estado.Estados nuevoEstado) {
        
        switch(nuevoEstado) {

                case OBTENIENDO_MEDIDAS_EXPERIMENTALES:

                    this.panelConexiones.setEnabled(false);
                    this.panelConfiguracionPE.setEnabled(false);
                    break;

                case PARAMETROS_ESTIMACION_CONFIGURADOS:

                   this.panelConexiones.setEnabled(true);
                   this.panelConfiguracionPE.setEnabled(true);                   
                   break;

                case ESTIMACIONES_DISPONIBLES:

                    this.panelConexiones.setEnabled(true);
                    this.panelConfiguracionPE.setEnabled(true);                    
                    this.panelConfiguracionPE.ajustarComponentes(Estado.Estados.PARAMETROS_ESTIMACION_CONFIGURADOS);
                    break;
            }

    }

}

