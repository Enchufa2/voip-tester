/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente.configuracionRed;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import gui.selectorDirSocket.*;
import gui.GuiManager;


/**
 *
 * @author Antonio
 */
public class PanelConexionDS extends JPanel{

    public final CampoIP conexionRemota = new CampoIP();
    public final CampoInterfazRed conexionLocal = new CampoInterfazRed();
    public final BotonConectar botonConectar = new BotonConectar("sockets");


    public PanelConexionDS() {

        super();

        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Timestamps protocol connection"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        this.conexionRemota.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Remote socket address"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.conexionLocal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Local socket address"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.conexionRemota.setPreferredSize(
                this.conexionLocal.getPreferredSize()
                );


         //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.gridy = 0;
        gbc.insets.top = 20;
        gbc.insets.left = 20;
        gbc.insets.right = 20;
        super.add(this.conexionRemota, gbc);

        gbc.gridy++;
        super.add(this.conexionLocal, gbc);


        this.botonConectar.setEstadoConexion(false);
        gbc.gridy++;
        gbc.insets.bottom = 20;
        super.add(this.botonConectar, gbc);

    }

    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled);

    }

}
