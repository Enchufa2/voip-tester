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

