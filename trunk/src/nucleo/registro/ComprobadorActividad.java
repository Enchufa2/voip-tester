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