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


package gui.cliente.selectorMedidas;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import gui.GuiManager;

/**
 *
 * @author Antonio
 */
public class PanelSeleccionEnlace extends JPanel{

    final JRadioButton botonUplink = new JRadioButton("Uplink");

    final JRadioButton botonDownlink = new JRadioButton("Downlink");

    final ButtonGroup group = new ButtonGroup();



    public PanelSeleccionEnlace() {

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Direction"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        group.add(this.botonUplink);
        group.add(this.botonDownlink);
        this.botonUplink.setSelected(true);

        //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.top = 5;
        gbc.insets.left = 5;
        gbc.insets.bottom  = 5;

        super.add(this.botonUplink, gbc);

        gbc.gridx++;
        gbc.insets.right = 5;
        super.add(this.botonDownlink);
    }


    public boolean isUplink(){

        if(this.group.isSelected(this.botonUplink.getModel())) {
            return true;
        }

        return false;
    }

    public boolean isDownlink() {

        if(this.group.isSelected(this.botonDownlink.getModel())) {
            return true;
        }

        return false;

    }

    public String getSentido() {

        if(this.isUplink())
            return "UL";
        return "DL";
    }


    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled);
    }
}

