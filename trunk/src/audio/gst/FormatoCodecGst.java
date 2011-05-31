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

package audio.gst;

import audio.*;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Structure.InvalidFieldException;
import java.util.Hashtable;
import java.util.Enumeration;



/**
 * @author Antonio
 * @version 1.0
 * @created 17-ago-2010 13:23:13
 */
public class FormatoCodecGst implements FormatoCodec {


    /**
     * Nombre representativo del codec
     */
    private String nombreCodificacion;

    /**
     * Nombre del elemento de gstreamer que proporciona
     * el mecanismo de codificación (encoding)
     */
    private String gstCodificador;

    /**
     * Nombre del elemento de gstreamer que proporciona
     * el mecanismo de decodificación (decoding).
     */
    private String gstDecodificador;

    
    /**
     * bit rate generado por el codificador. Dependiendo
     * del codificador de gstreamer es posible que este sea fijo
     * y que solo pueda obtenerse a partir del flujo de bytes que genere el codec.
     */
    private int bitRate;


    /**
     * Cuando el códec actúa como formato objetivo, este campo representa
     * las características de audio en formato PCM (raw audio) al que se le aplica
     * el proceso de codificación.
     * Cuando el códec actúa como formato origen, este campo representa las
     * características de audio en formato PCM (raw audio) que se obtienen tras
     * el proceso de decodificación.
     *
     * @see FormatoRawAudioGst
     *
     */
    private FormatoRawAudioGst formatoRawAudio;


    /**
     * Características de audio que requiere un elemento codificador a la entrada
     * (sink) para  poder realizar la codificación.
     *
     * @see Caps
     */
    private Caps capsNecesarios;

    

    /**
     * Características de audio asociadas al flujo de bytes que genera el codificador.
     * Estas dependen de las propiedades de cada codificador.
     *
     * @see Caps
     */
    private Caps capsCodificacion;


    /**
     * Posibles propiedades que se pueden ajustar en el codec. Estas dependen
     * del tipo de codificador.
     *
     * @see PropiedadesCodificador
     *
     */
    private PropiedadesCodificador propiedadesCodificador = new PropiedadesCodificador();


    
    /**
     * Crea el objeto a partir del nombre y de los nombres del codificador
     * y decodificador en Gstreamer.
     * 
     * El audio que entra al codificador contiene muestras de 16 bits a 8KHz
     *
     * @param nombreCodificacion    Nombre representativo del codec
     * @param gstCodificador        Nombre del elemento codificador
     * @param gstDecodificador      Nombre del elemento decodificador
     */
    public FormatoCodecGst(
            String nombreCodificacion,
            String gstCodificador,
            String gstDecodificador) {

        this.nombreCodificacion = nombreCodificacion;
        this.gstCodificador = gstCodificador;
        this.gstDecodificador = gstDecodificador;
        this.capsNecesarios = Caps.fromString(
                "audio/x-raw-int, endianness=1234, width=16, signed=true");
        this.formatoRawAudio = new FormatoRawAudioGst();


    }


    /**
     * Crea el objeto a partir del nombre y de los nombres del codificador
     * y decodificador en Gstreamer.
     *
     * El audio que entra al codificador contiene muestras de 16 bits a <code>freqMuestroNecesaria</code> Hz
     *
     * @param nombreCodificacion        Nombre representativo del codec
     * @param gstCodificador            Nombre del elemento codificador
     * @param gstDecodificador          Nombre del elemento decodificador
     * @param freqMuestreoNecesaria     Frecuencia de muestreo en Hz que requiere el codificador a la entrada
     */
    public FormatoCodecGst(
            String nombreCodificacion,
            String gstCodificador,
            String gstDecodificador,
            int freqMuestreoNecesaria) {

        this.nombreCodificacion = nombreCodificacion;
        this.gstCodificador = gstCodificador;
        this.gstDecodificador = gstDecodificador;
        this.capsNecesarios = Caps.fromString(
                "audio/x-raw-int, rate=" + freqMuestreoNecesaria + ", endianness=1234, width=16, signed=true");
        this.formatoRawAudio = new FormatoRawAudioGst();

    }


    /**
     * Crea el objeto a partir del nombre y de los nombres del codificador
     * y decodificador en Gstreamer.
     *
     * El audio que entra al codificador contiene muestras de <code>longitudMuestra</code> bits
     * a <code>freqMuestroNecesaria</code> Hz
     *
     * @param nombreCodificacion        Nombre representativo del codec
     * @param gstCodificador            Nombre del elemento codificador
     * @param gstDecodificador          Nombre del elemento decodificador
     * @param freqMuestreoNecesaria     Frecuencia de muestreo que requiere el codificador a la entrada
     * @param numeroCanales             Numero de canales de audio a codificar
     */
    public FormatoCodecGst(
            String nombreCodificacion,
            String gstCodificador,
            String gstDecodificador,
            int freqMuestreoNecesaria,
            int numeroCanales) {

        this.nombreCodificacion = nombreCodificacion;
        this.gstCodificador = gstCodificador;
        this.gstDecodificador = gstDecodificador;
        this.capsNecesarios = Caps.fromString(
                "audio/x-raw-int, rate=" + freqMuestreoNecesaria + ", channels=" +numeroCanales +", endianness=1234, width=16, signed=true"
                );
        this.formatoRawAudio = new FormatoRawAudioGst();

    }


    /**
     * Devuelve el convenio de interpretación de los bits. Big-endian
     * significa que el bit más significativo se encuentra a la izquierda
     *
     * @return <code>true</code>  Si el convenio es bigendian
     */
    public boolean bigEndian() {

        boolean bigEndian=true;
        
        if (this.capsNecesarios.getStructure(0).getInteger("endianness")==1234) {
            bigEndian = false;
        }
        return bigEndian;
    }


    /**
     * Devuelve la tasa de muestreo en Hz del audio que entra o sale del codificador
     *
     * @return La tasa o frecuencia de muestreo en Hz del audio que entra o sale del codificador
     */
    public int muestrasPorSegundo() {

        int rate;

        try{

            rate = this.capsNecesarios.getStructure(0).getInteger("rate");

        }catch(InvalidFieldException ife) {
            
            rate = this.formatoRawAudio.getMuestrasPorSegundo();
        }

        return rate;
    }


    /**
     * Fija la tasa de muestreo en Hz del audio que entra o sale del codificador
     *
     * @param   muestrasPorSegundo      La tasa o frecuencia de muestreo en Hz del
     *                                  audio que entra o sale del codificador
     */
    public void setMuestrasPorSegundo(int muestrasPorSegundo) {

        this.capsNecesarios.setInteger("rate", muestrasPorSegundo);
    }


    /**
     * Devuelve la longitud promedio de la muestra en bits
     *
     * @return El número promedio de bits por muestra
     */
    public double longitudMuestraEnBits() {

        double lmb = 0;

        try{

            lmb = this.capsNecesarios.getStructure(0).getInteger("width");

        }catch(InvalidFieldException ife) {

            int bitrate = this.tasaDeDatos();
            if (bitrate > 0) {
                lmb = ((double) bitrate) / this.muestrasPorSegundo();
            }
        }
        
        return lmb;
    }


    /*
     * Devuelve el número de bytes que tiene cada trama
     *
     * @return Número de bytes por trama
     */
    public int longitudTrama() {

        int longitudTrama = (this.bitRate / this.muestrasPorSegundo())/8;
        if ( longitudTrama == 0 )longitudTrama = 1;
        return longitudTrama * this.numeroDeCanales();
    }


    /**
     * Devuelve el número de canales de audio del stream
     *
     * 1->mono
     * 2->estéreo
     * ....
     *
     * @return El numero de canales de audio del stream
     */
    public int numeroDeCanales() {

        int numeroCanales;

        try{

            numeroCanales = this.capsNecesarios.getStructure(0).getInteger("channels");

        }catch(InvalidFieldException ife) {

            numeroCanales = this.formatoRawAudio.getNumeroDeCanales();
        }
         return numeroCanales;

    }


    /**
     * Fija el número de canales de audio del stream
     *
     * 1->mono
     * 2->estéreo
     * ....
     *
     * @param   numeroDeCanales     El numero de canales de audio del stream
     */
    public void setNumeroDeCanales(int numeroDeCanales) {

         if ( numeroDeCanales < 1 || numeroDeCanales > 2)
             throw new IllegalArgumentException("invalid number of channels: " + numeroDeCanales);

         this.capsNecesarios.setInteger("channels", new Integer(numeroDeCanales));

    }

    /**
     * Devuelve la tasa de datos del stream en bits por segundo
     *
     * @return      La tasa de datos del stream en bits por segundo
     */
    public int tasaDeDatos() {

        return this.bitRate;

    }


    /**
     * Ajusta la tasa de datos a partir <code>duracionEnMicrosegundos</code> y
     *  <code>bytesDatos</code>
     *
     *
     */
    public void setTasaDeDatos(double duracionEnMicrosegundos, long bytesDatos) {

        this.bitRate = (int) ( (bytesDatos * 8) /  (duracionEnMicrosegundos/1000000) );
    }

    /**
     * Ajusta la tasa de datos
     *
     * @param tasaDeDatos   Tasa de datos en bits por segundo
     * 
     */
    public void setTasaDeDatos (int tasaDeDatos) {

        this.bitRate = tasaDeDatos;
    }

    /**
     *
     * Devuelve la duración en microsegundos correspondiente a un número
     * de bytes  <code>longitudEnBytes</code>
     *
     * @param longitudEnBytes
     *
     * @return      La duración en microsegundos equivalente
     */
    public double duracionEnMicrosegundos(long longitudEnBytes) {

        if (this.bitRate == 0) {
            return -1;
        }

        return 1000000 * ( ( (double) longitudEnBytes * 8)
                / (this.tasaDeDatos()*this.numeroDeCanales()) );
    }

    /**
     * Devuelve la longitud en bytes correspondiente a un intervalo de duracion
     *
     *  <code>duracionEnMicrosegundos</code>
     *
     * @param duracionEnMicrosegundos
     *
     * @return      El número de bytes equivalente
     */
    public int longitudIntervalo(double duracionEnMicrosegundos) {
       
        if (this.bitRate == 0) {
            return -1;
        }
        
        return (int) (this.tasaDeDatos() *this.numeroDeCanales()* (duracionEnMicrosegundos/8000000));
    }


     /**
     * Realiza la conversión de tramas a microsegundos.
     *
     * @param numeroTramas Número de tramas a convertir.
     * @return La duración en microsegundos equivalente a una cantidad <code>numeroTramas</code> tramas
     *
     */
    public double duracionTramasEnMicrosegundos(long numeroTramas){

        return this.duracionEnMicrosegundos(
                numeroTramas*(this.longitudTrama()/this.numeroDeCanales())
                );
    }
   

    /**
     * @return  El nombre representativo del codec
     */
    public String getNombreCodec() {

        return this.nombreCodificacion;
    }


    /**
     *@return  El nombre del elemento codificador en Gstreamer
     *
     * @see Element
     */
    public String getGstCodificador() {

        return this.gstCodificador;
    }

    /**
     * @return  El nombre del elemento decodificador en Gstreamer
     *
     * @see Element
     */
    public String getGstDecodificador() {

        return this.gstDecodificador;
    }


    /**
     * Ajusta las características de audio asociadas al stream de audio codificado
     * con el codec que representa el objeto
     *
     * @param capsCodificacion      Las características del audio comprimido
     *
     * @see Caps
     */
     public void setCapsCodificacion(Caps capsCodificacion) {

        this.capsCodificacion = new Caps(capsCodificacion);

    }

    /**
     * Devuelve las características de audio asociadas al stream de audio
     * codificado con el codec que representa el objeto
     *
     * @return      Las caracteristis del audio comprimido
     *
     * @see Caps
     */
    public Caps getCapsCodificacion() {

        return this.capsCodificacion;
    }


    /**
     * Devuelve las características del audio descomprimido que se requieren a
     * la entrada del codificador.
     *
     * @return      Las características de audio necesarias a la entrada del codificador
     *
     * @see Caps
     */
    public Caps getCapsNecesarios() {

        return this.capsNecesarios;
    }

   
    /**
     * Devuelve las propiedades del audio sin comprimir sobre el que se aplica
     * la técnica de codificación y el que se obtiene tras aplicar la decodificación
     *
     * @return      las características de audio en formato PCM
     * @see         FormatoRawAudio
     *
     */
    public FormatoRawAudio getFormatoRawAudio() {

        return this.formatoRawAudio;
    }


    /**
     * Comprueba si el codec que representa el objeto se encuentra disponible
     * en el equipo que realiza el tratamiento del audio
     *
     * @return      <code>true</code> si el codec está disponible
     *
     * @see ElementFactory
     */
    public boolean estaSoportado() {
        
        boolean soportado = true;
        
        try {
            
            ElementFactory.make(this.gstCodificador, "codificador").dispose();
            
        }catch (IllegalArgumentException iae){

            soportado = false;
        }
        
        return soportado;
    }


    public String toString() {

        String newLine =System.getProperty("line.separator");

        return "Codec: " + this.nombreCodificacion + newLine
                + '\t' + "Bitrate (bps): " + this.bitRate + newLine
                + '\t' + "Channels: " + this.numeroDeCanales() + newLine
                + '\t' + "Rate (Hz): " + this.muestrasPorSegundo() + newLine
                + this.propiedadesCodificador.toString()
                + this.formatoRawAudio.toString();
    }
    

    /**
     * Devuelve las propiedades que se pueden ajustar en el codificador
     *
     * @return      Las propiedades que se pueden ajustar en el codificador
     * @see PropiedadesCodificador
     */
    public PropiedadesCodificador getPropiedadesCodec() {

        return this.propiedadesCodificador;
    }

    
    /**
     * Esta clase representa las distintas propiedades que disponen los codificadores
     * de Gstreamer y que pueden ajustarse para variar su comportamiento.
     * Cada propiedad tiene un posible conjunto de valores asociados, siendo estos
     * de distinto tipo y cantidad según la propiedad
     *
     * @see Element
     */
    public class PropiedadesCodificador {

        /**
         * Relación entre la propiedad y el nombre de la misma
         */
        Hashtable<String, Propiedad> propiedades;

        /**
         * Relación entre nombre de propiedad y el valor que se elige dentro
         * del conjunto de posibles valores que puede tomar. Así el valor entero
         * de la tabla representa el índice del vector de posibles valores.
         */
        Hashtable<String, Integer> valoresSeleccionados;


        /**
         * Inicializa el objeto con propiedades nulas
         */
        public PropiedadesCodificador() {

            this.propiedades = new Hashtable<String, Propiedad>();
            this.valoresSeleccionados = new Hashtable<String, Integer>();
        }


        /**
         * Devuelve el nombre de todas las propiedades que tiene el elemento
         */
        public String[] getNombrePropiedades() {

            Enumeration<String> nombrePropiedades = this.propiedades.keys();
            String[] prop = new String[this.propiedades.size()];
            int i = 0;
            while (nombrePropiedades.hasMoreElements()) {

                prop[i] = nombrePropiedades.nextElement();
                i++;
            }

            return prop;

        }

        /**
         * Devuelve los posibles valores que puede tomar una propiedad
         *
         * @param nombrePropiedad   El nombre de la propiedad que se consulta
         *
         * @return      El conjunto de posibles valores que puede tomar la propiedad
         */
        public Object[] getValoresPropiedad(String nombrePropiedad) {
            
            if (this.propiedades.containsKey( nombrePropiedad )){
                return this.propiedades.get(nombrePropiedad).valoresPropiedad;
            }
            return null;
        }

        /**
         * Añade una propiedad y su conjunto posible de valores
         *
         * @param nombrePropiedad   El nombre de la propiedad
         * @param valoresPropiedad  El conjunto de valores que puede tomar la propiedad
         */
        public void addPropiedad(String nombrePropiedad, Object[] valoresPropiedad) {

            if (!this.propiedades.containsKey(nombrePropiedad)) {

                this.propiedades.put(
                        nombrePropiedad, new Propiedad(nombrePropiedad, valoresPropiedad)
                        );
                this.seleccionarValorPropiedad(nombrePropiedad, 0);

            }
        }

        
        /**
         * Añade una propiedad y su conjunto posible de valores, indicando a demás
         * la descripciónd e la misma
         *
         * @param nombrePropiedad       El nombre de la propiedad
         * @param valoresPropiedad      El conjunto de valores que puede tomar la propiedad
         * @param descripcion           Descripcion de la propiedad
         * @param valoresDescripcion    Valores que toma la nombreDescripcion
         */
        public void addPropiedad(
                String nombrePropiedad,
                Object[] valoresPropiedad,
                String descripcion,
                Object[] valoresDescripcion) {

            if (!this.propiedades.containsKey(nombrePropiedad)) {

                Propiedad propiedad = new Propiedad(nombrePropiedad, valoresPropiedad);
                propiedad.setDescripcion(descripcion, valoresDescripcion);
                this.propiedades.put(
                        nombrePropiedad,
                        propiedad
                        );
                this.seleccionarValorPropiedad(nombrePropiedad, 0);

            }
        }

        /**
         * Devuelve el número de propiedades que posee el codificador
         *
         * @return      El número de propiedades que posee el codificador
         */
        public int getNumeroPropiedades() {

            return this.propiedades.size();
        }

        /**
         * selecciona un valor de entre los posible de una propiedad
         *
         * @param nombrePropiedad       El nombre de la propiedad
         * @param index                 El índice del vector de los posibles valores
         *                              que puede tomar la propiedad
         */
        public void seleccionarValorPropiedad(String nombrePropiedad, int index) {


            if (this.propiedades.containsKey(nombrePropiedad)) {

                this.valoresSeleccionados.put(nombrePropiedad, index);
                Propiedad propiedad = this.propiedades.get(nombrePropiedad);

                if(propiedad.tieneDescripcion() && 
                        (propiedad.descripcion.nombreDescripcion.equals("bps")
                        || propiedad.descripcion.nombreDescripcion.equals("bitrate") )) {

                    FormatoCodecGst.this.bitRate = (Integer) propiedad.descripcion.valoresDescripcion[index];
                    
                }else if (nombrePropiedad.equals("bitrate")) {

                    FormatoCodecGst.this.bitRate = (Integer) propiedad.valoresPropiedad[index];

                }
            }
        }

        /**
         * Devuelve el valor seleccionado para una propiedad
         *
         * @param nombrePropiedad    El nombre de la propiedad
         *
         * @return  El valor seleccionado para la propiedad. <code>null</code> si no se
         *          ha seleccionado ninguna propiedad
         */
        public Object getValorPropiedad(String nombrePropiedad) {

            if (this.propiedades.containsKey(nombrePropiedad) &&
                    this.valoresSeleccionados.containsKey(nombrePropiedad)) {

                return this.propiedades.get(nombrePropiedad).
                        valoresPropiedad[this.valoresSeleccionados.get(nombrePropiedad)];
            }

            return null;
        }


        /**
         *  Devuelve el nombre de la descripción de la propiedad
         *
         *  @return     El nombre de la descripcion de la propiedad.
         *              <code>null</code> si no existe descripción
         */
        public String getDescripcionPropiedad(String nombrePropiedad) {

            Propiedad propiedad = this.propiedades.get(nombrePropiedad);
            if (propiedad.tieneDescripcion())
                return propiedad.descripcion.nombreDescripcion;

            return null;
        }


        /**
         * Devuelve los valores de la descripcion
         *
         * @return      los valores de la descripción
         *              <code>null</code> si no existe descripcion
         */
        public Object[] getValoresDescripcion(String nombrePropiedad) {

            Propiedad propiedad = this.propiedades.get(nombrePropiedad);
            if (propiedad.tieneDescripcion())
                return propiedad.descripcion.valoresDescripcion;

            return null;
        }


        /**
         * Devuelve si una propiedad tiene descripcion
         *
         * @param nombrePropiedad       propiedad a consultar
         */
        public boolean tieneDescripcion(String nombrePropiedad) {

            return this.propiedades.get(nombrePropiedad).tieneDescripcion();
        }


        /**
         * Devuelve si, en caso de tener descripción, esta tiene valores
         *
         * @param nombrePropiedad       propiedad a consultar
         */
        public boolean tieneValoresDescripcion(String nombrePropiedad) {

            return this.propiedades.get(nombrePropiedad).tieneValoresDescripcion();
        }


        public String toString() {

            String newLine =System.getProperty("line.separator");
            
            String representacion = "Properties: " + newLine;
            Enumeration<String> keys = this.propiedades.keys();
            while(keys.hasMoreElements()) {
                Propiedad propiedad = this.propiedades.get(keys.nextElement());
                representacion += '\t' + propiedad.nombrePropiedad +": " +
                        this.getValorPropiedad(propiedad.nombrePropiedad) + newLine;
            }

            return representacion;

        }
        

        /**
         * Representa una propiedad del codificador, indicando el nombre de la misma,
         * el posible conjunto de valores que puede tomar, y la descripcion de la misma
         * (si existe).
         */
        class Propiedad{

            private String nombrePropiedad;
            private Object[] valoresPropiedad;
            private Descripcion descripcion;
            //private boolean esSeleccionable = true;

            public Propiedad(String nombrePropiedad, Object[] valoresPropiedad) {

                this.nombrePropiedad = nombrePropiedad;
                this.valoresPropiedad = valoresPropiedad;
            }

           /**
            * Indica si la propiedad contiene descripción
            *
            * @return <code>true</code> si tiene descripción
            */
            public boolean tieneDescripcion() {
                
                boolean tieneDescripcion=false;
                if (this.descripcion !=null) 
                    tieneDescripcion=true;
                
                return tieneDescripcion;
            }


           /**
            * Indica si la descripción contiene valores representativos
            *
            * @return <code>true</code> si contiene valores
            */
            public boolean tieneValoresDescripcion() {

                boolean tieneValores = false;

                if (this.tieneDescripcion()) {

                    if (this.descripcion.valoresDescripcion !=null)
                        tieneValores = true;
                }

                return tieneValores;
            }

            /**
             * Asigna una descripción a la propiedad
             *
             * @param descripcion       Nombre de la descripción
             * @param valoresDescripcion    Valores de la descripción
             */
            public void setDescripcion(String descripcion, Object[] valoresDescripcion) {
                
                this.descripcion = new Descripcion(descripcion, valoresDescripcion);
            }


                /**
                 * Representa la descripcion de una propiedad, indicando el significado de la misma
                 * y los valores descriptivos equivalentes a los valores de la propiedad
                 */
                class Descripcion{

                private String nombreDescripcion;
                private Object[] valoresDescripcion;

                public Descripcion(String descripcion, Object[] valoresDescripcion) {

                    this.nombreDescripcion = descripcion;
                    this.valoresDescripcion = valoresDescripcion;
                }
                

            }//fin Descripcion

        }//fin Propiedad




    }//fin PropiedadesCodificador
}

