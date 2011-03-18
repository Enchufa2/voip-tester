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


package gui.cliente.configuracionRed;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import gui.GuiManager;
import javax.swing.ImageIcon;


/**
 *
 * @author Antonio
 */
public class BotonConectar extends JButton{


    final Dimension dimensionSelector = new Dimension(120, 40);

    private boolean conectado = false;

    private ImageIcon iconConectar, iconConectarRoll, iconDesconectar, iconDesconectarRoll;

    public BotonConectar(String nombreBaseIcon) {

        super();

        this.iconConectar = GuiManager.createImageIcon(nombreBaseIcon + "_conectar_enabled.png");
        this.iconConectarRoll = GuiManager.createImageIcon(nombreBaseIcon + "_conectar_rollover.png");
        this.iconDesconectar = GuiManager.createImageIcon(nombreBaseIcon + "_desconectar_enabled.png");
        this.iconDesconectarRoll = GuiManager.createImageIcon(nombreBaseIcon + "_desconectar_rollover.png");
        super.setBackground(Color.red);
        this.setEstadoConexion(false);


    }

    public void setEstadoConexion(boolean conectado) {

        this.conectado = conectado;
        this.setIcon(conectado);
    }

    public boolean getEstadoConexion() {

        return this.conectado;
    }

    public void setIcon(boolean conectado) {

        ImageIcon icon;
        if(conectado) {

            icon = this.iconDesconectar;
            super.setIcon(this.iconDesconectar);
            super.setRolloverIcon(this.iconDesconectarRoll);
            super.setToolTipText("Disconnect");

        }else{

            icon = this.iconConectar;
            super.setIcon(this.iconConectar);
            super.setRolloverIcon(this.iconConectarRoll);
            super.setToolTipText("Connect");
        }

        this.dimensionSelector.setSize(
                icon.getIconWidth(),
                icon.getIconHeight());
        super.setBorderPainted(false);
        super.setMargin(new Insets(0,0,0,0));
        super.setBorder(null);
        super.setOpaque(false);
        super.setIconTextGap(0);
        super.setText(null);


    }


    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);

        if(enabled) {

            this.setEstadoConexion(this.conectado);

        }else{

            super.setBackground(Color.gray);
        }

    }

    public Dimension getPreferredSize() {

        return this.dimensionSelector;
    }

    public Dimension getMaximumSize() {

        return this.getPreferredSize();

    }

    public Dimension getMinimumSize() {

        return this.getPreferredSize();

    }
}

