/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.servidor;

import javax.swing.JTabbedPane;
import gui.ScrollPaneLog;
import gui.GuiManager;


/**
 *
 * @author Antonio
 */
public class ServidorGUI 
        extends JTabbedPane{

    final PanelConfiguracionServidor configuracionServidor = new PanelConfiguracionServidor();

    final ScrollPaneLog logRegistro;

   
     
     public ServidorGUI() {

         super();
         

         this.logRegistro = new ScrollPaneLog(
                 this.configuracionServidor.controlServidor.registroLogger);

         super.addTab("Configuration", this.configuracionServidor);
         super.addTab("Log", this.logRegistro);
         super.setAutoscrolls(true);
         

       
     }

     public static void main(String args[]) {

        GuiManager.crearYmostrarGUI(new ServidorGUI(), false).setTitle("VoIPTester Server");
        
    }


}
