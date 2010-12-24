package nucleo.cliente;

/**
 *
 * Clase encargada de realizar el proceso de espera de petición de sincronización
 * en una hebra.
 * 
 * @see nucleo.nodo.Nodo
 * @see ControlNodos
 *
 * @author Antonio
 */
public class HebraPeticionSincronizacion extends Thread{
   
    private ControlNodos.NodoServidorHandler nodoServidorHandler;

    private NodoServidorException excepcionSincronizacion;

    public HebraPeticionSincronizacion( ControlNodos.NodoServidorHandler nodoServidorHandler) {

        this.nodoServidorHandler = nodoServidorHandler;
        this.excepcionSincronizacion = null;
        super.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {

        String mensajeLog="waiting synchronization request";

        try{

            this.nodoServidorHandler.procesarOperacion(null, mensajeLog);

        }catch (NodoServidorException nse) {

            this.excepcionSincronizacion = nse;
        }
    }



    /**
     * Comnprueba si se produce una excepcióin durante la ejecución de la hebra
     * y la relanza en caso afirmativo.
     *
     * @throws NodoServidorException  Si se produce una excepción durante la ejecución de
     *                                la espera de sincronización
     *
     * @see ControlNodos
     */
    public void comprobarExcepcionSincronizacion()
            throws NodoServidorException {

        if (this.excepcionSincronizacion !=null) {

            throw this.excepcionSincronizacion;

        }
        
    }

}
