/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente.charts;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 *
 * @author Antonio
 */
public class PanelCondiciones extends JPanel{

    JTextArea textAreaCondiciones;

    public PanelCondiciones() {

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("measurement conditions"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        //textarea
        this.textAreaCondiciones = new JTextArea();
        this.textAreaCondiciones.setOpaque(false);
        this.textAreaCondiciones.setEditable(false);

        //layout de los componentes
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.fill = GridBagConstraints.BOTH;
        super.add(this.textAreaCondiciones, gbc);


    }

}
