/* 
Este fichero es parte de VoIPTester

    Copyright (C) 2009-2010 
    	      Antonio Sánchez Navarro (titosanxez@gmail.com)
	      Juan M. López Soler (juanma@ugr.es)
	      Jorge Navarro Ortiz (jorgenavarro@ugr.es)
    

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los términos de la Licencia Pública General GNU publicada 
    por la Fundación para el Software Libre, ya sea la versión 3 
    de la Licencia, o (a su elección) cualquier versión posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
    MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. 
    Consulte los detalles de la Licencia Pública General GNU para obtener 
    una información más detallada. 

    Debería haber recibido una copia de la Licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.

*/
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
     * Inicializa los parÃ¡metros a cero.
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
     * RepresentaciÃ³n del objeto mediante caracteres
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


