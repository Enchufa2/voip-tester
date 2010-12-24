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
     * Devuelve el convenio de interpretación de los bits. Big-endian
     * significa que el bit más significativo se encuentra a la izquierda
     *
     * @return <code>True</code>  Si el convenio es bigendian
     */

    public boolean bigEndian();


    /**
     * Devuelve el número de muestras por segundo, es decir,
     * la frecuencia de muestreo
     *
     * @return Número de muestras por segundo
     */
    public int muestrasPorSegundo();

    
    /**
     * Devuelve los bits que codifican cada muestra
     *
     * @return Tamaño de la muestra en bits
     */
    public double longitudMuestraEnBits();


    /*
     * Devuelve el número de bytes que tiene cada trama
     *
     * @return Número de bytes por trama
     */
    public int longitudTrama();


    /**
     * Devuelve el número de canales o pistas de audio
     *
     * @return Número de canales o pistas de audio
     */
    public int numeroDeCanales();



    /**
     * Calcula la tasa de datos, en bits/segundo, de un flujo de audio
     * con propiedades igual a la del objeto que llama este método.
     *
     * @return  La tasa de datos en bits/segundo
     */
    public int tasaDeDatos();


    /**
     * Realiza la conversión de bytes a microsegundos.
     *
     * @param longitudEnBytes Longitud en bytes a convertir.
     * @return La duración en microsegundos equivalente a una cantidad de bytes <code>longitudEnBytes</code>
     *
     */
    public double duracionEnMicrosegundos(long longitudEnBytes);

    /**
     * Realiza la conversión de microsegundos a bytes
     *
     * @param duracionIntervalo Duración en microsegundos a convertir.
     * @return El número de bytes equivalentes a una duración de <code>duracionIntervalo</code> milisegundos.
     */
    public int longitudIntervalo(double duracionIntervalo);


    /**
     * Realiza la conversión de tramas a microsegundos.
     *
     * @param numeroTramas Número de tramas a convertir.
     * @return La duración en microsegundos equivalente a una cantidad <code>numeroTramas</code> tramas
     *
     */
    public double duracionTramasEnMicrosegundos(long numeroTramas);


    /**
     * Devuelve el nombre del codec que representa el audio
     *
     * @return      El nombre de la codificación empleada en el audio
     */
    public String getNombreCodec();


    /**
     * Devuelve las propiedades del audio sin comprimir sobre el que se aplica
     * la técnica de codificación y el que se obtiene tras aplicar la decodificación
     *
     * @see FormatoRawAudio
     */
    public FormatoRawAudio getFormatoRawAudio();


     /**
     * Comprueba si el codec que representa el objeto se encuentra disponible
     * en el equipo que realiza el tratamiento del audio
     *
     * @return      <code>true</code> si el codec está disponible
     *
     */
    public boolean estaSoportado();


    
    
}