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

