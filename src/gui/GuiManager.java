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

