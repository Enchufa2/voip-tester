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
package utils;

/**
 *
 * @author Antonio
 */
public class Temporizador {

    /**
     * Marca de tiempo UTC en nanosegundos desde 1970.
     *
     * @see System
     */
    private long tiempoInicio;
    /**
     * Referencia temporal arbitraria de inicio en nanosegundos
     *
     * @see System
     */
    private long start;

    public Temporizador() {
    }

    /**
     * Establece una marca de tiempo lo mÃ¡s exacta posible.
     *
     * @return tiempoInicio Marca de tiempo UTC en nanosegundos desde 1970
     * @see System
     * @see #System.currentTimeMillis()
     * @see #System.nanoTime()
     */
    public long iniciar() {
        long tiempoReferencia = System.currentTimeMillis();
        this.tiempoInicio = System.currentTimeMillis();
        this.start = System.nanoTime();            //referencia arbitraria inicial, en nanosegundos
        /*Dado que los relojes de los computadores tienen una resoluciÃ³n
        es conveniente buscar el punto en el que el reloj cambia, reduciendo
        al mÃ¡ximo el error del timestamp devuelvo por el sistema*/
        while (this.tiempoInicio == tiempoReferencia) {
            this.tiempoInicio = System.currentTimeMillis();
            this.start = System.nanoTime();
        }
        this.tiempoInicio = this.tiempoInicio * 1000000;      //conversiÃ³n a nanosegundos
        return this.tiempoInicio;
    }

    /**
     * Espera a que transcurria un tiempo determinado antes de continuar con la ejecuciÃ³n.
     * Similiar a sleep(int milisegundos);
     *
     * @param nanosegundos  Tiempo de espera en nanosegundos
     */
    public void esperar(int nanosegundos) {

        long start = System.nanoTime();

        while ((System.nanoTime() - start) < nanosegundos);
    }

    /**
     * Genera una marca de tiempo UTC en nanosegundos desde Enero de 1970.
     *
     * @return marca de tiempo UTC en nanosegundos
     * @see System
     */
    public long timestamp() {
        
        return this.tiempoInicio + System.nanoTime() - this.start;
    }

    /**
     * Devuelve el tiempo en nanosegundos transcurrido desde que se inicio el temporizador
     *
     * @return tiempo transcurrido en nanosegundos de que se inicio el temporizador
     */
    public long tiempoTranscurrido() {


        return System.nanoTime() - this.start;
    }

    /**
     * Reinicia el temporizador poniendo a cero la referencia temporal de inicio
     */
    public void reiniciar() {
        this.tiempoInicio = 0;
        this.start = 0;
    }
}

