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
 * Clase encargada de realizar la petici贸n de generar los timestamps de emisor
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
     * Comnprueba si se produce una excepci贸in durante la ejecuci贸n de la hebra
     * y la relanza en caso afirmativo.
     *
     * @throws NodoServidorException  Si se produce una excepci贸n durante la generaci贸n
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


