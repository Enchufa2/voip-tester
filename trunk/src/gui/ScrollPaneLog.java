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


package gui;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.Font;

import java.util.logging.Logger;
import java.util.logging.Level;


/**
 *
 * @author Antonio
 */
public class ScrollPaneLog extends JScrollPane {

    JTextArea textArealog =null;

    Logger logger;

    TextAreaHandler tah;

    Dimension dimensionArea = new Dimension(500,300);


    public ScrollPaneLog(Logger logger) {

        super();

        this.logger =  logger;
        logger.setLevel(Level.FINER);
        logger.setUseParentHandlers(true);
        this.textArealog = new JTextArea();
        Font fuente = new Font(Font.SANS_SERIF, Font.BOLD, 15);
        this.textArealog.setFont(fuente);
        tah = new TextAreaHandler(this.textArealog);
        tah.setLevel(Level.FINER);
        logger.addHandler(tah);
        
        super.getViewport().add(this.textArealog);
        super.setAutoscrolls(true);
       
    }

}

