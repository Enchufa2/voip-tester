/* 
Este fichero es parte de VoIPTester

    Copyright (C) 2009-2010 
    	      Antonio S醤chez Navarro (titosanxez@gmail.com)
	      Juan M. L髉ez Soler (juanma@ugr.es)
	      Jorge Navarro Ortiz (jorgenavarro@ugr.es)
    

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los t閞minos de la Licencia P鷅lica General GNU publicada 
    por la Fundaci髇 para el Software Libre, ya sea la versi髇 3 
    de la Licencia, o (a su elecci髇) cualquier versi髇 posterior.

    Este programa se distribuye con la esperanza de que sea 鷗il, pero 
    SIN GARANT虯 ALGUNA; ni siquiera la garant韆 impl韈ita 
    MERCANTIL o de APTITUD PARA UN PROP覵ITO DETERMINADO. 
    Consulte los detalles de la Licencia P鷅lica General GNU para obtener 
    una informaci髇 m醩 detallada. 

    Deber韆 haber recibido una copia de la Licencia P鷅lica General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.

*/
package nucleo.cliente;

/**
 *
 * Clase encargada de realizar el proceso de espera de petici贸n de sincronizaci贸n
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
     * Comnprueba si se produce una excepci贸in durante la ejecuci贸n de la hebra
     * y la relanza en caso afirmativo.
     *
     * @throws NodoServidorException  Si se produce una excepci贸n durante la ejecuci贸n de
     *                                la espera de sincronizaci贸n
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

