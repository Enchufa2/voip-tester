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
package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class SalidaComando extends Thread
{

    /**
     * Stream de entrada a la JVM, producida por un proceso o un fichero
     */
    private InputStream is;

    /**
     * Cadena de caracteres obtenida del stream
     */
    private String salidaDeComando;

    
    public SalidaComando(InputStream is) {
        this.is = is;
        this.salidaDeComando="";
    }

    /**
     * Obtiene la cadena de caracteres generada por el stream is
     */
    public void run()
    {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                this.salidaDeComando += line + '\n';
            }
            isr.close();
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String getSalidaDeComando() {
        return this.salidaDeComando;
    }
}


