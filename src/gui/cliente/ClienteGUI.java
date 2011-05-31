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


package gui.cliente;


import javax.swing.JTabbedPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import gui.GuiManager;
import gui.cliente.labelsTab.LabelTab;
import nucleo.cliente.ControlAudio;
import nucleo.cliente.ControlNodos;
import nucleo.cliente.NodoClienteException;
import nucleo.cliente.ControlCliente;
import nucleo.cliente.GestorDirectorios;
import org.gstreamer.Gst;


/**
 *
 * @author Antonio
 */
public class ClienteGUI 
        extends JTabbedPane implements PropertyChangeListener{

    public final PanelConexiones panelConexiones;

    public final PanelConfiguracionPE panelConfiguracionPE;

    public final PanelOperaciones panelOperaciones;

    public final PanelResultados panelResultados;
   
    public final ControlCliente controlCliente;

    public final GestorDirectorios gestorDirectorios;

    public final ControlAudio controlAudio;

    public final EstadoGlobal estadoGlobal;


    public ClienteGUI() 
            throws NodoClienteException{

        super();

        this.gestorDirectorios = GestorDirectorios.getInstance();

        this.controlCliente = new ControlCliente(new ControlNodos(
                this.gestorDirectorios.getDirectorioLogs())
                );

        this.controlAudio = new ControlAudio();

        this.estadoGlobal = new EstadoGlobal();
        this.estadoGlobal.estadoGlobal.addPropertyChangeListener(this);
        this.estadoGlobal.estadoRegistrado.addPropertyChangeListener(this);
        this.estadoGlobal.estadoSocketsConfigurados.addPropertyChangeListener(this);

        this.panelConexiones = new PanelConexiones(
                this.controlCliente,
                this.estadoGlobal
                );

        this.panelConfiguracionPE = new PanelConfiguracionPE(this.estadoGlobal);

        this.panelResultados = new PanelResultados();

        this.panelOperaciones = new PanelOperaciones(
                this.controlCliente.getControlNodos(),
                this.controlAudio,
                this.estadoGlobal,
                this.panelConexiones,
                this.panelConfiguracionPE,
                this.panelResultados
                );

        
        super.addTab("Connections", new JScrollPane(this.panelConexiones));
        LabelTab tab_pc = new LabelTab("tab_pc.png");
        super.setTabComponentAt(0, tab_pc);

        super.addTab("Parameters", new JScrollPane(this.panelConfiguracionPE));
        LabelTab tab_pe = new LabelTab("tab_pe.png");
        super.setTabComponentAt(1,  tab_pe);

        super.addTab("Operations", new JScrollPane(this.panelOperaciones));
        LabelTab tab_po = new  LabelTab("tab_po.png");
        super.setTabComponentAt(2,  tab_po);
        
        super.addTab("Results", new JScrollPane(this.panelResultados));
        LabelTab tab_pr = new  LabelTab("tab_pr.png");
        super.setTabComponentAt(3, tab_pr);

        super.setTabPlacement(JTabbedPane.LEFT);

        this.estadoGlobal.setEstadoGlobal(Estado.Estados.DESCONECTADO);

    }





    public void propertyChange(PropertyChangeEvent pce) {

        if (pce.getPropertyName().equals("estado")){

            Estado.Estados nuevoEstado = (Estado.Estados) pce.getNewValue();

            switch(nuevoEstado) {

                case DESCONECTADO:

                    this.setEnabledAt(1, false);
                    this.setEnabledAt(2, false);
                    this.setEnabledAt(3, false);                    
                    break;

                case REGISTRADO:

                    this.setEnabledAt(1, false);
                    this.setEnabledAt(2, false);
                    this.setEnabledAt(3, false);                    
                    break;

                case CONFIGURAR_SOCKETS_PT:

                    this.setEnabledAt(1, false);
                    this.setEnabledAt(2, false);
                    this.setEnabledAt(3, false);
                    break;

                case SOCKETS_PT_CONFIGURADOS:

                    this.setEnabledAt(1, true);
                    if(this.estadoGlobal.estadoSocketsConfigurados.getEstado() == Estado.Estados.PARAMETROS_ESTIMACION_CONFIGURADOS
                       || this.estadoGlobal.estadoSocketsConfigurados.getEstado() == Estado.Estados.ESTIMACIONES_DISPONIBLES
                       )
                        this.setEnabledAt(2,true);
                    else
                        this.setEnabledAt(2, false);
                    
                    this.setEnabledAt(3, false);                   
                   break;

                case CONFIGURAR_PARAMETROS_ESTIMACION:

                    this.setEnabledAt(1, true);
                    this.setEnabledAt(2, false);
                    break;

                case PARAMETROS_ESTIMACION_CONFIGURADOS:

                    this.setEnabledAt(1, true);
                    this.setEnabledAt(2, true);
                    break;
                            
                case OBTENIENDO_MEDIDAS_EXPERIMENTALES:

                    this.setEnabledAt(3, false);
                    break;

                case ESTIMACIONES_DISPONIBLES:

                    this.setEnabledAt(3, true);                    
                    break;
            }
        }
    }

    public void setEnabledAt(int i, boolean enabled) {

        super.setEnabledAt(i, enabled);
        super.getTabComponentAt(i).setEnabled(enabled);
        
    }

       

    public static void main(String args[]) throws Exception{

        Gst.init();
        ClienteGUI clienteGUI = new ClienteGUI();
        JFrame frame = GuiManager.crearYmostrarGUI(clienteGUI, false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setTitle("Cliente VOIPTester");
        
    }

}

