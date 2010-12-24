/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente;

import gui.BotonDobleEstado;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.cliente.selectorCodec.PanelAjusteCodec;
import gui.cliente.selectorMedidas.PanelSeleccionMedidas;
import gui.cliente.selectorMedidas.PanelSeleccionEnlace;
import gui.cliente.selectorPT.PanelSelectores;
import gui.cliente.selectorPT.ControladorDefecto;
import gui.cliente.selectorPT.ControladorParametroAudio;
import gui.GuiManager;

/**
 *
 * @author Antonio
 */
public class PanelConfiguracionPE
        extends JPanel implements 
                        ActionListener, PropertyChangeListener{

    //Componentes GUI
    public final PanelAjusteCodec ajusteCodec;

    public final PanelSeleccionMedidas seleccionMedidas;

    public final PanelSeleccionEnlace seleccionEnlace;

    public final PanelSelectores selectoresPT;

    public final BotonDobleEstado botonSeleccionar = new BotonDobleEstado("Select", "Change");

    private final EstadoGlobal estadoGlobal;

    private final ControladorDefecto controladorDefecto;

    private final ControladorParametroAudio controladorParametroAudio;

    public PanelConfiguracionPE(EstadoGlobal estadoGlobal) {

        super();        
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Estimation Parameters Configuration"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
       
        this.ajusteCodec = new PanelAjusteCodec();
        this.seleccionMedidas = new PanelSeleccionMedidas();
        this.seleccionEnlace = new PanelSeleccionEnlace();
        this.selectoresPT = new PanelSelectores();       


        this.estadoGlobal = estadoGlobal;
        this.estadoGlobal.estadoSocketsConfigurados.addPropertyChangeListener(this);
        this.controladorDefecto = (ControladorDefecto) this.selectoresPT.getControladorParametro();
        this.controladorParametroAudio = new ControladorParametroAudio(
                this.ajusteCodec.seleccionCodec.getFormatoCodecSeleccionado()
                );

        //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.top = 30;
        gbc.insets.left = 20;
        gbc.insets.right = 60;
        gbc.anchor = GridBagConstraints.LINE_START;
        //gbc.insets.right = 20;
        super.add(this.seleccionMedidas, gbc);

        gbc.gridx++;
        gbc.gridheight = 2;
        gbc.gridy = 0;
        gbc.insets.top = 10;
        gbc.insets.bottom = 0;
        gbc.insets.right = 20;
        gbc.anchor = GridBagConstraints.PAGE_START;
        super.add(this.selectoresPT, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets.top = 0;
        gbc.insets.right = 0;
        gbc.insets.bottom = 0;
        gbc.fill = GridBagConstraints.NONE;
        super.add(this.ajusteCodec, gbc);
        this.ajusteCodec.seleccionCodec.botonSeleccionar.addActionListener(this);
        this.ajusteCodec.botonQuitarCodec.addActionListener(this);

       
       
        gbc.gridx = 0;
        gbc.gridy++;        
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets.left = 0;
        gbc.insets.top = 10;
        gbc.insets.bottom = 10;
        gbc.insets.right = 20;
        gbc.gridwidth = 2;
        super.add(this.seleccionEnlace, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.ipadx = 50;
        gbc.ipady = 5;
        gbc.insets.right = 00;
        super.add(this.botonSeleccionar, gbc);
        this.botonSeleccionar.setAccionRealizada(false);
        this.botonSeleccionar.addActionListener(this);
        

    }

    public void actionPerformed(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();

        if(source == this.ajusteCodec.seleccionCodec.botonSeleccionar) {

            this.controladorParametroAudio.getGPT().setFormatoCodec(
                    this.ajusteCodec.seleccionCodec.getFormatoCodecSeleccionado()
                    );
            this.selectoresPT.setControladorParametro(this.controladorParametroAudio);

        }else if (source == this.ajusteCodec.botonQuitarCodec) {

            this.selectoresPT.setControladorParametro(this.controladorDefecto);

        }else if(source == this.botonSeleccionar) {

            if(! this.botonSeleccionar.getAccionRealizada()) {

                boolean pesqEstaSeleccionado = this.seleccionMedidas.medidaSeleccionada(NombreMedida.PESQ);
                boolean codecEstaSeleccionado = this.ajusteCodec.codecEstaSeleccionado();
                boolean existeSeleccion = this.seleccionMedidas.existeSeleccion();

                if ( (existeSeleccion && !pesqEstaSeleccionado) || (pesqEstaSeleccionado && codecEstaSeleccionado)) {

                    this.botonSeleccionar.setAccionRealizada(true);
                    this.estadoGlobal.setEstadoSocketsConfigurados(Estado.Estados.PARAMETROS_ESTIMACION_CONFIGURADOS);
                    

                } else {
                    
                    if(! existeSeleccion)
                        JOptionPane.showMessageDialog(
                                this,
                                "At least one QoS measure must be selected",
                                "Estimation parameters configuration",
                                JOptionPane.INFORMATION_MESSAGE);

                    else if(pesqEstaSeleccionado && !codecEstaSeleccionado)
                        JOptionPane.showMessageDialog(
                                this,
                                "At least one codec must be selected to get a PESQ measurement",
                                "Estimation parameters configuration",
                                JOptionPane.INFORMATION_MESSAGE);

                    this.estadoGlobal.setEstadoSocketsConfigurados(Estado.Estados.CONFIGURAR_PARAMETROS_ESTIMACION);
                }

            }else{

                 this.botonSeleccionar.setAccionRealizada(false);
                 this.estadoGlobal.setEstadoSocketsConfigurados(Estado.Estados.CONFIGURAR_PARAMETROS_ESTIMACION);
                 

            }            
        }
        
    }


    public void propertyChange(PropertyChangeEvent pce) {

        if (pce.getPropertyName().equals("estado")){

            Estado.Estados nuevoEstado = (Estado.Estados) pce.getNewValue();

            this.ajustarComponentes(nuevoEstado);
            
        }
    }


    public void ajustarComponentes(Estado.Estados nuevoEstado) {

            switch(nuevoEstado) {

                case CONFIGURAR_PARAMETROS_ESTIMACION:

                    this.seleccionEnlace.setEnabled(true);
                    this.seleccionMedidas.setEnabled(true);
                    this.selectoresPT.setEnabled(true);
                    this.ajusteCodec.setEnabled(true);
                    this.botonSeleccionar.setEnabled(true);
                    this.botonSeleccionar.setAccionRealizada(false);
                    break;

                case PARAMETROS_ESTIMACION_CONFIGURADOS:

                    this.seleccionEnlace.setEnabled(false);
                    this.seleccionMedidas.setEnabled(false);
                    this.selectoresPT.setEnabled(false);
                    this.ajusteCodec.setEnabled(false);
                    this.botonSeleccionar.setEnabled(true);
                    this.botonSeleccionar.setAccionRealizada(true);

                    break;
            }

    }

    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled);
        if(enabled) {
            this.ajustarComponentes(
                    this.estadoGlobal.estadoSocketsConfigurados.getEstado()
                    );
        }

    }

}
