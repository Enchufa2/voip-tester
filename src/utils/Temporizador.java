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
     * Establece una marca de tiempo lo más exacta posible.
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
        /*Dado que los relojes de los computadores tienen una resolución
        es conveniente buscar el punto en el que el reloj cambia, reduciendo
        al máximo el error del timestamp devuelvo por el sistema*/
        while (this.tiempoInicio == tiempoReferencia) {
            this.tiempoInicio = System.currentTimeMillis();
            this.start = System.nanoTime();
        }
        this.tiempoInicio = this.tiempoInicio * 1000000;      //conversión a nanosegundos
        return this.tiempoInicio;
    }

    /**
     * Espera a que transcurria un tiempo determinado antes de continuar con la ejecución.
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

