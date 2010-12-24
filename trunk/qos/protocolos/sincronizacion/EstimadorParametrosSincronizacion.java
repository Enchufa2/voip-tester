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

