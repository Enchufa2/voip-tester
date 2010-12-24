/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente.labelsTab;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import gui.GuiManager;

/**
 *
 * @author Antonio
 */
public class LabelTab extends JLabel{

    private ImageIcon iconLabel;

    public LabelTab(String nombreIconTab) {

        super();

        this.iconLabel = GuiManager.createImageIcon(nombreIconTab);
        
        super.setIcon(this.iconLabel);
        super.setSize(this.iconLabel.getIconWidth(), this.iconLabel.getIconHeight());
        super.setBorder(null);
        super.setOpaque(false);
        super.setIconTextGap(0);
        super.setText(null);
        
    }



}
