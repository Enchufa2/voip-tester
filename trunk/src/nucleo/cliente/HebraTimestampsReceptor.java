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
     * Comnprueba si se produce una excepci贸in durante la ejecuci贸n de la hebra
     * y la relanza en caso afirmativo.
     *
     * @throws NodoServidorException  Si se produce una excepci贸n durante la generaci贸n
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

