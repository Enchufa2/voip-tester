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


package gui.cliente.selectorCodec;

import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import audio.GestorCodecsAudio;
import audio.FormatoCodec;


/**
 *
 * @author Antonio
 */
public class PanelAjusteCodec 
        extends JPanel implements ActionListener{

    public PanelSeleccionCodec seleccionCodec;

    public JDialog dialogoSelCodec =null;

    public final JButton botonAjustar;

    public final JButton botonQuitarCodec;

    public final JLabel labelCodec = new JLabel();

    public Dimension dimensionPanel = new Dimension(200,150);

     //elementos no grÃ¡ficos
    public final GestorCodecsAudio gestorCodecsAudio = new GestorCodecsAudio();

    private boolean codecSeleccionado = false;

    public PanelAjusteCodec() {

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Codecs"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.seleccionCodec = new PanelSeleccionCodec(this.gestorCodecsAudio);
        this.seleccionCodec.botonSeleccionar.addActionListener(this);

        this.botonAjustar = new JButton("Select codec");
        this.botonQuitarCodec = new JButton("Deselect codec");
        this.botonQuitarCodec.setEnabled(false);

         //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.top = 10;
        gbc.insets.left = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        super.add(this.botonAjustar, gbc);
        this.botonAjustar.addActionListener(this);

        gbc.gridy++;        
        super.add(this.botonQuitarCodec, gbc);
        this.botonQuitarCodec.addActionListener(this);

        gbc.gridy++;
        gbc.insets.bottom = 10;
        gbc.insets.right  = 10;
        super.add(this.labelCodec, gbc);

    }   

    public void actionPerformed(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();

        if(source == this.botonAjustar) {

             this.seleccionCodec.gestorCodecsAudio.inspeccionarCodecs();
             Window window = SwingUtilities.windowForComponent(this);
               if (this.dialogoSelCodec == null) {
                   this.dialogoSelCodec = new JDialog(
                           window,
                           JDialog.ModalityType.APPLICATION_MODAL);
                   this.dialogoSelCodec.setTitle("Codec selector");
                   this.dialogoSelCodec.getContentPane().add(this.seleccionCodec);
                   this.dialogoSelCodec.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                   this.dialogoSelCodec.pack();
                   this.dialogoSelCodec.setResizable(false);
                   this.dialogoSelCodec.setLocationRelativeTo(window);
               }

               this.dialogoSelCodec.setVisible(true);

        }else if( source == this.botonQuitarCodec) {

            this.labelCodec.setText("");
            this.botonQuitarCodec.setEnabled(false);
            this.codecSeleccionado = false;

        }else if(source == this.seleccionCodec.botonSeleccionar) {

            this.labelCodec.setText(
                    this.seleccionCodec.getFormatoCodecSeleccionado().getNombreCodec()
                    );
            this.botonQuitarCodec.setEnabled(true);
            this.codecSeleccionado = true;
            this.dialogoSelCodec.setVisible(false);

        }
        
    }

    public boolean codecEstaSeleccionado() {

        return this.codecSeleccionado;
    }

    public FormatoCodec getFormatoCodecSeleccionado() {

        return this.seleccionCodec.getFormatoCodecSeleccionado();
    }

    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        this.botonAjustar.setEnabled(enabled);
        if(enabled)
            this.botonQuitarCodec.setEnabled(this.codecSeleccionado);
        else
            this.botonQuitarCodec.setEnabled(false);
    }

    public Dimension getPreferredSize() {


        return this.dimensionPanel;
    }

    public Dimension getMaximumSize() {

        return this.getPreferredSize();
    }

    public Dimension getMinimumSize() {

        return this.getPreferredSize();
    }


}

