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

package nucleo.registro;
import nucleo.nodo.Nodo;
import nucleo.*;
import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * @author Antonio
 * @version 1.0
 * @created 02-sep-2010 13:30:56
 */
public class ComprobadorActividad extends Thread {

    
    private final ServidorRegistroImpl servidorRegistro;

    private int tiempoExpiracion_ms ;

    private Logger registroLogger;    

    
    /**
     *
     * @param servidorRegistro
     */
    public ComprobadorActividad(
            ServidorRegistroImpl servidorRegistro,
            int tiempoExpiracion_ms
            ) {

        this.servidorRegistro = servidorRegistro;
        this.tiempoExpiracion_ms = tiempoExpiracion_ms;
        this.registroLogger = Logger.getLogger("nucleo.registro");
        super.setPriority(Thread.MIN_PRIORITY);
        super.setDaemon(true);
    }

    @Override
    public void run() {

        Nodo nodoRemoto;
        ClienteNodo clienteNodo;
        
        while(this.servidorRegistro.existeClienteRegistrado()) {

            try {

                Thread.sleep(this.tiempoExpiracion_ms);

                synchronized (this.servidorRegistro) {
                    try {
                        
                        this.registroLogger.finer("Timer expired without notification from client");

                        if (this.servidorRegistro.existeClienteRegistrado()) {
                            nodoRemoto = this.servidorRegistro.getNodoRemoto();
                            clienteNodo = this.servidorRegistro.getClienteNodo();

                            synchronized (nodoRemoto) {
                                    clienteNodo.esPoseedorNodoRemoto(nodoRemoto);
                                    this.registroLogger.finer("Client still active");
                                }
                        }

                    } catch (RemoteException re) {

                        this.servidorRegistro.eliminarCliente();                        
                        this.registroLogger.finer("Client not active. Disconnection forced");
                    }
                }

            } catch (InterruptedException ie) {                
                this.registroLogger.finer("Notification from client");
            }

        }//Fin bucle principal

        this.registroLogger.finer("Activity checker stopped");

    }//fin run()


}
