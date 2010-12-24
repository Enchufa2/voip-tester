/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente.selectorCodec;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import audio.GestorCodecsAudio;
import audio.gst.FormatoCodecGst;
import audio.FormatoCodec;
import audio.AudioException;
import audio.gst.AudioWavPropertiesGst;
import nucleo.cliente.GestorDirectorios;
import nucleo.cliente.GestorParametrosAudio;
import gui.GuiManager;
import java.awt.Color;
import java.util.ArrayList;


/**
 *
 * @author Antonio
 */
public class PanelSeleccionCodec 
        extends JPanel implements ListSelectionListener, ActionListener{


    GestorCodecsAudio gestorCodecsAudio;

    private final ArrayList<JScrollPane> panelesTablas;

    private final JList listaCodecs;

    private final JScrollPane panelListaCodecs;

    public final JButton botonSeleccionar;

    public final JButton botonAudioFile = new JButton();

    private final JSplitPane splitPaneCodecs;



    public PanelSeleccionCodec(GestorCodecsAudio gca ) {

        super();
        //super.setTitle("Selector codec");
        this.splitPaneCodecs = new JSplitPane();
        this.gestorCodecsAudio = gca;
        this.panelesTablas = new ArrayList<JScrollPane>();
        this.splitPaneCodecs.setPreferredSize(new Dimension(900,400));
        ArrayList<String> codecsDisponibles = this.gestorCodecsAudio.getNombreDeCodecsDisponible();
        this.listaCodecs = new JList(codecsDisponibles.toArray());
        this.listaCodecs.addListSelectionListener(this);
        this.listaCodecs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel subPanelTabla = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        for(int i=0; i<codecsDisponibles.size(); i++) {

            subPanelTabla = new JPanel();
            subPanelTabla.setLayout(layout);

            subPanelTabla.add(
                    new TablaPropiedadesCodec(
                        (FormatoCodecGst) this.gestorCodecsAudio.getFormatoCodec(codecsDisponibles.get(i))
                                ),
                                gbc
                            );

            this.panelesTablas.add(
                    i,
                    new JScrollPane(subPanelTabla)
                    );
            
        }

        this.listaCodecs.setSelectedIndex(0);        
        this.botonSeleccionar = new JButton("Select");
        this.botonAudioFile.setBorder(null);
        this.botonAudioFile.setIconTextGap(0);
        this.botonAudioFile.setOpaque(false);
        this.botonAudioFile.setBackground(Color.red);
        this.botonAudioFile.setToolTipText("select original audio");
        this.botonAudioFile.setIcon(GuiManager.createImageIcon("but_audioFile.png"));
        this.botonAudioFile.setRolloverIcon(GuiManager.createImageIcon("but_audioFile_roll.png"));
        this.botonAudioFile.addActionListener(this);

        this.panelListaCodecs = new JScrollPane(this.listaCodecs);
        this.panelListaCodecs.setMinimumSize(new Dimension(120,200));
        this.splitPaneCodecs.setLeftComponent(this.panelListaCodecs);
        this.splitPaneCodecs.setRightComponent(this.panelesTablas.get(0));

        super.setLayout(layout);
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        super.add(this.splitPaneCodecs, gbc);
        gbc.gridy=1;
        gbc.insets.top = 20;
        gbc.insets.bottom = 20;
        gbc.ipadx = 30;
        gbc.ipady = 10;
        super.add(this.botonSeleccionar, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.insets.left = 30;
        super.add(this.botonAudioFile, gbc);

        super.setMinimumSize(this.splitPaneCodecs.getPreferredSize());        
    }

    


    public void valueChanged(ListSelectionEvent listEvent) {

        this.splitPaneCodecs.setRightComponent(
                this.panelesTablas.get( this.listaCodecs.getSelectedIndex() )
                );

    }

    public void actionPerformed(ActionEvent actionEvent) {

       if(actionEvent.getSource() == this.botonAudioFile) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            int returnVal = fileChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {

                try{

                    AudioWavPropertiesGst awpg = new AudioWavPropertiesGst(fileChooser.getSelectedFile());
                    long duracion_us = awpg.getDuracionTotal_us();
                    if(duracion_us < 2000000) {
                        JOptionPane.showMessageDialog(
                                this,
                                "The audio file duration must be bigger than 2 seconds",
                                "audio event",
                                JOptionPane.INFORMATION_MESSAGE
                                );
                    }else{

                        //this.controladorParametroAudio.setDuracionIntervaloMax(duracion_us);
                        GestorParametrosAudio.setDuracionIntervaloMax(duracion_us);
                        GestorDirectorios.getInstance().setFicheroAudioOriginal(fileChooser.getSelectedFile());
                    }

                }catch(AudioException ae) {

                    JOptionPane.showMessageDialog(
                            this,
                            ae.getMessage() + '\n' +ae.getCause().getMessage(),
                            "audio event", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
    }

    public FormatoCodec getFormatoCodecSeleccionado() {

        return this.gestorCodecsAudio.getFormatoCodec(
                (String) this.listaCodecs.getSelectedValue()
                );
    }


}
