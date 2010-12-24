/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import javax.swing.JTextArea;

import java.util.logging.LogRecord;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Antonio
 */
public class TextAreaHandler extends Handler{

    JTextArea textAreaLog = null;

    //JPanel
    
    public TextAreaHandler(JTextArea textAreaLog) {

        super();
        super.setFormatter(new SimpleFormatter());

        this.textAreaLog = textAreaLog;
        this.textAreaLog.setAutoscrolls(true);
       
        
    }


    public synchronized void publish(LogRecord record) {
        String message = null;

        if (!super.isLoggable(record)) {
            return;
        }
        try {
            message = getFormatter().format(record);
        } catch (Exception e) {
            reportError(null, e, ErrorManager.FORMAT_FAILURE);
        }

        try {
            this.textAreaLog.append(message);
        } catch (Exception ex) {
            reportError(null, ex, ErrorManager.WRITE_FAILURE);
        }

    }

    public void close() {

    }

    public void flush() {
    }

     
}
