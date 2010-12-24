/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.net.URL;
import java.io.File;
/**
 *
 * @author Antonio
 */
public class GuiManager {


    public static void setEnabled(JPanel panel, boolean enabled) {

        Component[] componentes = panel.getComponents();
        for (int i=0; i<componentes.length; i++)
             componentes[i].setEnabled(enabled);            
    }

    public static ImageIcon createImageIcon(String nombreIcono) {
        
        URL imgURL = GuiManager.class.getResource("icons" + "/" + nombreIcono);
         
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.out.println("Couldn't find file: " + imgURL.getFile());
            return null;
        }

    }


    public static void setEnabled(JPanel panel, boolean enabled, boolean subPanel) {

        Component[] componentes = panel.getComponents();
        for (int i=0; i<componentes.length; i++)
            if(subPanel) 
                if (componentes[i] instanceof JPanel) {

                    GuiManager.setEnabled((JPanel) componentes[i], enabled, subPanel);
                } else {
                    componentes[i].setEnabled(enabled);
                }
            else
                 componentes[i].setEnabled(enabled);
    }



    public static JFrame crearYmostrarGUI(final Component componente, final boolean incluirPanel) {

        final JFrame frame = new JFrame();

        SwingUtilities.invokeLater(new Runnable() {


            public void run() {

                if (incluirPanel){
                    
                    JPanel panel = new JPanel();
                    panel.setLayout(new GridBagLayout());
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.fill = GridBagConstraints.BOTH;
                    panel.add(componente, gbc);
                    frame.getContentPane().add(panel);

                }else{

                    frame.getContentPane().add(componente);
                }

                frame.pack();
                frame.setVisible(true);
                //frame.setLocationByPlatform(true);
                frame.setLocationRelativeTo(null);
                //frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
            }
        });

        return frame;
    }




}
