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


package gui.servidor;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import gui.GuiManager;

/**
 *
 * @author Antonio
 */
public class BotonIniciar extends JButton{

    private Dimension dimensionSelector;

    private boolean conectado = false;

    ImageIcon iconIniciar, iconIniciarRoll, iconDetener, iconDetenerRoll;

    public BotonIniciar() {

        super();        
        this.iconIniciar = GuiManager.createImageIcon("servidor_iniciar_enabled.png");
        this.iconIniciarRoll = GuiManager.createImageIcon("servidor_iniciar_rollover.png");
        this.iconDetener = GuiManager.createImageIcon("servidor_detener_enabled.png");
        this.iconDetenerRoll = GuiManager.createImageIcon("servidor_detener_rollover.png");
        this.dimensionSelector = new Dimension(100,50);
        super.setBackground(Color.red);
        this.setConectado(false);

    }

    public void setConectado(boolean conectado) {

       this.conectado = conectado;
       this.setIcon(conectado);
       
    }

    public boolean getConectado() {

        return this.conectado;
    }

    public void setIcon(boolean conectado) {

        ImageIcon icon;
        if(conectado) {

            icon = this.iconDetener;
            super.setIcon(this.iconDetener);
            super.setRolloverIcon(this.iconDetenerRoll);
            super.setToolTipText("Stop server");

        }else{

            icon = this.iconIniciar;
            super.setIcon(this.iconIniciar);
            super.setRolloverIcon(this.iconIniciarRoll);
            super.setToolTipText("Start server");
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

            this.setConectado(this.conectado);

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

