

package nucleo.cliente;


/**
 *
 * Clase encargada de realizar la petición de generar los timestamps de emisor
 *
 * @see nucleo.nodo.Nodo
 * @see ControlNodos
 *
 * @author Antonio
 */
public class HebraTimestampsReceptor extends Thread{

    private ControlNodos.NodoServidorHandler nodoServidorHandler;

    private ControlNodos controlNodos;

    private long[] timestampsReceptor;

    int rtt_us;

    private NodoServidorException excepcionTimestamps;

    public HebraTimestampsReceptor(
            ControlNodos.NodoServidorHandler nodoServidorHandler,
            ControlNodos controlNodos,
            int rtt_us
            ) {

        this.setPriority(MIN_PRIORITY);
        this.nodoServidorHandler = nodoServidorHandler;
        this.controlNodos = controlNodos;
        this.rtt_us = rtt_us;
        this.excepcionTimestamps = null;
        super.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {

        String mensajeOperacion = "receiver timestamps request";
        try {

            this.timestampsReceptor = (long[]) this.nodoServidorHandler.procesarOperacion(
                    new Object[]{this.rtt_us},
                    mensajeOperacion
                    );

        }catch(NodoServidorException nse) {

            this.excepcionTimestamps = nse;

            try{

                this.controlNodos.cerrarSocketNodoLocal();

            } catch (NodoClienteException nce) {

                System.err.println("client socket closing operation failed");

            }
        }
    }


    /**
     * @return los timestamps obtenidos
     */
    public long[] getTimestampsReceptor() {

        return this.timestampsReceptor;
    }
   

    /**
     * Comnprueba si se produce una excepcióin durante la ejecución de la hebra
     * y la relanza en caso afirmativo.
     *
     * @throws NodoServidorException  Si se produce una excepción durante la generación
     *                                de los timestamps de receptor
     *
     * @see ControlNodos
     */
     public void comprobarExcepcionHebra()
             throws NodoServidorException {

        if (this.excepcionTimestamps !=null) {

            throw this.excepcionTimestamps;

        } 

    }

}
