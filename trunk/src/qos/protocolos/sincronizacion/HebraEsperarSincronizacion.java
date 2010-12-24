/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qos.protocolos.sincronizacion;

import java.lang.Thread;
import java.lang.Exception;
import java.net.SocketTimeoutException;
import java.io.IOException;

/**
 *
 * @author Antonio
 */
public class HebraEsperarSincronizacion extends Thread{

    private ProtocoloSincronizacion protocoloSincronizacion;

    private Exception exception;

    public HebraEsperarSincronizacion(ProtocoloSincronizacion pS) {

        this.protocoloSincronizacion = pS;
    }

    @Override
    public void run() {

        try {

            this.protocoloSincronizacion.esperarPeticionSincronizacion();

        }catch(SocketTimeoutException ste) {

            this.exception = ste;

        }catch(IOException ioe) {

            this.exception = ioe;
      
        }
    }


    public Exception getException() {

        return this.exception;
    }



}
