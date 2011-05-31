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


