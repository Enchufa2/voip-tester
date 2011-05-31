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
package audio;
import java.io.Serializable;

/**
 * @author Antonio
 * @version 1.0
 * @created 17-ago-2010 0:31:57
 */
public interface FormatoCodec
        extends Serializable{



    /**
     * Devuelve el convenio de interpretaci贸n de los bits. Big-endian
     * significa que el bit m谩s significativo se encuentra a la izquierda
     *
     * @return <code>True</code>  Si el convenio es bigendian
     */

    public boolean bigEndian();


    /**
     * Devuelve el n煤mero de muestras por segundo, es decir,
     * la frecuencia de muestreo
     *
     * @return N煤mero de muestras por segundo
     */
    public int muestrasPorSegundo();

    
    /**
     * Devuelve los bits que codifican cada muestra
     *
     * @return Tama帽o de la muestra en bits
     */
    public double longitudMuestraEnBits();


    /*
     * Devuelve el n煤mero de bytes que tiene cada trama
     *
     * @return N煤mero de bytes por trama
     */
    public int longitudTrama();


    /**
     * Devuelve el n煤mero de canales o pistas de audio
     *
     * @return N煤mero de canales o pistas de audio
     */
    public int numeroDeCanales();



    /**
     * Calcula la tasa de datos, en bits/segundo, de un flujo de audio
     * con propiedades igual a la del objeto que llama este m茅todo.
     *
     * @return  La tasa de datos en bits/segundo
     */
    public int tasaDeDatos();


    /**
     * Realiza la conversi贸n de bytes a microsegundos.
     *
     * @param longitudEnBytes Longitud en bytes a convertir.
     * @return La duraci贸n en microsegundos equivalente a una cantidad de bytes <code>longitudEnBytes</code>
     *
     */
    public double duracionEnMicrosegundos(long longitudEnBytes);

    /**
     * Realiza la conversi贸n de microsegundos a bytes
     *
     * @param duracionIntervalo Duraci贸n en microsegundos a convertir.
     * @return El n煤mero de bytes equivalentes a una duraci贸n de <code>duracionIntervalo</code> milisegundos.
     */
    public int longitudIntervalo(double duracionIntervalo);


    /**
     * Realiza la conversi贸n de tramas a microsegundos.
     *
     * @param numeroTramas N煤mero de tramas a convertir.
     * @return La duraci贸n en microsegundos equivalente a una cantidad <code>numeroTramas</code> tramas
     *
     */
    public double duracionTramasEnMicrosegundos(long numeroTramas);


    /**
     * Devuelve el nombre del codec que representa el audio
     *
     * @return      El nombre de la codificaci贸n empleada en el audio
     */
    public String getNombreCodec();


    /**
     * Devuelve las propiedades del audio sin comprimir sobre el que se aplica
     * la t茅cnica de codificaci贸n y el que se obtiene tras aplicar la decodificaci贸n
     *
     * @see FormatoRawAudio
     */
    public FormatoRawAudio getFormatoRawAudio();


     /**
     * Comprueba si el codec que representa el objeto se encuentra disponible
     * en el equipo que realiza el tratamiento del audio
     *
     * @return      <code>true</code> si el codec est谩 disponible
     *
     */
    public boolean estaSoportado();


    
    
}
