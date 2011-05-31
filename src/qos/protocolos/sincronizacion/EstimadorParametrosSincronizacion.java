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
package qos.protocolos.sincronizacion;


public class EstimadorParametrosSincronizacion {

    /**
     * Timestamps en nanosegundos necesarios para la estimaci贸n del offset
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
     * Crea una nueva medida de sincronizaci贸n a partir de los timestamps correspondientes.
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
     *      T1=instante en el que el cliente env铆a la petici贸n
     *      T2=instante en el que el servidor recibe la petici贸n
     *      T3=instante en el que el servidor env铆a la respuesta
     *      T4=instante en el que el cliente recibe la respuesta
     * </code>
     *
     *
     * Si el reloj local est谩 adelantado respecto del cliente: offset < 0
     * Si el reloj local est谩 retrasado respecto del servidor: offset > 0
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


