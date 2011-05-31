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
package qos.protocolos.sincronizacion;


public class EstimadorParametrosSincronizacion {

    /**
     * Timestamps en nanosegundos necesarios para la estimación del offset
     *
     * El contenido del array debe ser:
     * <code>
     *                      timestampsSincronizacion[0]==T1
     *                      timestampsSincronizacion[1]==T2
     *                      timestmaps[2]==T3
     *                      timestampsSincronizacion[3]==T4
     * </code>
     */
    private long[] timestampsSincronizacion;


    /**
     * Constructor por defecto. Inicia el array de timestamps a null.
     */
    public EstimadorParametrosSincronizacion(){
    }

    
    /**
     * Crea una nueva medida de sincronización a partir de los timestamps correspondientes.
     */
    public EstimadorParametrosSincronizacion( long[] timestampsSincronizacion) {
        this.timestampsSincronizacion=timestampsSincronizacion;
    }


    /**
     * Devuelve los timestamps generados por el protocolo de sincronizacion
     *
     * @return timestampsSincronizacion
     */
    public long[] getTimestampsSincronizacion() {
        return this.timestampsSincronizacion;
    }


    /**
     * Asigna nuevos timestamps de sincronizacion
     */
    public void setTimestampsSincronizacion(long[] timestampsSincronizacion) {
        this.timestampsSincronizacion=timestampsSincronizacion;
    }


    /**
     * Calcula el offset del reloj local del cliente respecto del servidor y
     * el tiempo de ida y vuelta, empleando las siguientes expresiones:
     * <code>
     *      offset = ( tiempoIda - tiempoVuelta )/2
     *      tiempoIdaYvuelta=  tiempoIda + tiempoVuelta
     * </code>
     * donde
     * <code>
     *      tiempoIda = T2-T1 - offset
     *      tiempoVuelta = T4 + offset - T3
     *      T1=instante en el que el cliente envía la petición
     *      T2=instante en el que el servidor recibe la petición
     *      T3=instante en el que el servidor envía la respuesta
     *      T4=instante en el que el cliente recibe la respuesta
     * </code>
     *
     *
     * Si el reloj local está adelantado respecto del cliente: offset < 0
     * Si el reloj local está retrasado respecto del servidor: offset > 0
     *
     *
     */
    public ParametrosSincronizacion estimarParametrosSincronizacion () {

        long tiempoIda,tiempoVuelta;
        ParametrosSincronizacion parametrosSinc = new ParametrosSincronizacion();

        tiempoIda=this.timestampsSincronizacion[1]-this.timestampsSincronizacion[0];
        tiempoVuelta=this.timestampsSincronizacion[3]-this.timestampsSincronizacion[2];
        parametrosSinc.setOffsetLocal((tiempoIda-tiempoVuelta)/2);
        parametrosSinc.setTiempoIdaVuelta(tiempoIda+tiempoVuelta);

        return parametrosSinc;
    }
}


