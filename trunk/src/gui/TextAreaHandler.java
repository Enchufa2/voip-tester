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

