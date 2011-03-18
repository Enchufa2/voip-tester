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

