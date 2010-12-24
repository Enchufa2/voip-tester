/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;


/**
 *
 * @author Antonio
 */
public class BotonDobleEstado extends JButton{


    final Dimension dimensionSelector = new Dimension(120, 40);

    private boolean accionRealizada = false;

    String textoHacer="";

    String textoDeshacer="";

    public BotonDobleEstado(String textoHacer, String textoDeshacer) {

        super();
        this.textoHacer = textoHacer;
        this.textoDeshacer = textoDeshacer;
        this.setAccionRealizada(false);

    }


    public void setAccionRealizada(boolean accionRealizada) {

        if(accionRealizada) {

            this.accionRealizada = true;
            super.setText(this.textoDeshacer);

        }else{

            this.accionRealizada = false;
            super.setText(this.textoHacer);

        }
    }

    public boolean getAccionRealizada() {

        return this.accionRealizada;
    }


    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);

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
