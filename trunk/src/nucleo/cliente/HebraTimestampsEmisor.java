

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
public class HebraTimestampsEmisor extends Thread{

    private ControlNodos.NodoServidorHandler nodoServidorHandler;

    private ControlNodos controlNodos;

    private long[] timestampsEmisor;

    private NodoServidorException excepcionTimestamps;

    public HebraTimestampsEmisor( 
            ControlNodos.NodoServidorHandler nodoServidorHandler,
            ControlNodos controlNodos
            ) {

        this.setPriority(MIN_PRIORITY);
        this.nodoServidorHandler = nodoServidorHandler;

        this.controlNodos = controlNodos;

        this.excepcionTimestamps = null;

        super.setPriority(Thread.MIN_PRIORITY);
        
    }

    @Override
    public void run() {

        String mensajeOperacion = "sender timestamps request";
        try {

            this.timestampsEmisor = (long[]) this.nodoServidorHandler.procesarOperacion(
                    null,
                    mensajeOperacion
                    );

        }catch(NodoServidorException nse) {
        
           this.excepcionTimestamps = nse;

           try{

            this.controlNodos.cerrarSocketNodoLocal();

           }catch(NodoClienteException nce) {

              nce.printStackTrace();
               
           }
        }

    }


    /**
     * @return  Los timestamps obtenidos
     */
    public long[] getTimestampsEmisor() {

        return this.timestampsEmisor;
    }


    /**
     * Comnprueba si se produce una excepcióin durante la ejecución de la hebra
     * y la relanza en caso afirmativo.
     *
     * @throws NodoServidorException  Si se produce una excepción durante la generación
     *                                de los timestamps de emisor
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

