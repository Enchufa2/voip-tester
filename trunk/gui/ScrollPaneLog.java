/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
