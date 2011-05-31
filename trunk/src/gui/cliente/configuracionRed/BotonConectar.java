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


    final Dimension dimensionSelector = new Dimension(120, 30);

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

