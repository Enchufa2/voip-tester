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

