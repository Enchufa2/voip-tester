package qos.protocolos.sincronizacion;


public class ParametrosSincronizacion {

    /**
     * Offset en nanosegundos del reloj local respecto a un equipo servidor
     * Si offsetLocal < 0 -> Reloj local adelantado respecto al reloj servidor
     * Si offsetLocal > 0 -> Reloj local retrasado respecto al reloj servidor
     */
    private long offsetLocal;

    /**
     * Tiempo de ida y vuelta en nanosegundos al servidor con el que se sincroniza
     */
    private long tiempoIdaVuelta;


    /**
     * Inicializa los parámetros a cero.
     */
    public ParametrosSincronizacion () {
    }

    /**
     * Devuelve el valor del offset local en nanosegundos
     *
     * @return El offset local en nanosegundos
     */
    public long getOffsetLocal () {
        return offsetLocal;
    }

    /**
     * Cambia el valor del offset local
     *
     * @param oL    El valor del offset en nanosegundos
     */
    public void setOffsetLocal (long oL) {
        this.offsetLocal = oL;
    }

    /**
     * Devuelve el tiempo de ida y vuelta en nanosegundos
     *
     * @return      El tiempo de ida y vuelta en nanosegundos
     */
    public long getTiempoIdaVuelta () {
        return tiempoIdaVuelta;
    }

    /**
     * Cambia el valor del tiempo de ida y vuelta
     *
     * @param tIV   El tiempo de ida y vuelta en nanosegundos
     */
    public void setTiempoIdaVuelta (long tIV) {
        this.tiempoIdaVuelta = tIV;
    }


    /**
     * Representación del objeto mediante caracteres
     *
     * <code>"RTT(ms): tiempoIdaVuelta
     *       "offset(ms): offsetLocal</code>
     *
     */
    @Override
    public String toString() {

        return "RTT(ms): "+this.tiempoIdaVuelta/1000000 + '\n'
                + "offset(ms): "+this.offsetLocal/1000000;
    }
    

}//fin de clase

