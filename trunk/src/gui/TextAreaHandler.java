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

