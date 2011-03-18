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


package nucleo.cliente;

import java.io.File;
import java.util.GregorianCalendar;
import java.lang.reflect.Field;


/**
 * Clase encargada de la gestión y localización de los distintos recursos
 * de archivos del sistema
 *
 * @see File
 * *
 * @author Antonio
 *
 */
public class GestorDirectorios {

    /**
     * directorio de recursos del sistema
     * <code>recursos/</code>
     */
    private File directorioRecursos = new File("recursos");

    /**
     * directorio donde se crean los ficheros de logs
     * <code>logs/</code>
     */
    private File directorioLogs = new File("logs");

    /**
     * directorio que contiene el archivo(s) de audio referencia
     * <code>recursos/audioReferencia/voz/</code>
     */
    private File directorioAudioVozReferencia = new File(
            this.directorioRecursos, "audioReferencia" + File.separator +"voz"
            );

    /**
     * directorio que contiene el archivo(s) de audio con silencio
     * <code>recursos/audioReferencia/silencio/</code>
     */
    private File directorioAudioSilencioReferencia = new File(
            this.directorioRecursos, "audioReferencia" + File.separator +"silencio"
            );

    /**
     * archivo de audio original
     *
     * <code>recursos/audioOriginal/male.wav</code>
     */
    //private File directorioAudioOriginal = new File(this.directorioRecursos, "audioOriginal");
    private File ficheroAudioOriginal = new File(
            this.directorioRecursos, "audioOriginal" + File.separator + "male.wav"
            );

    /**
     * Directorio donde se encuentran los archivos generados tras la generación de audio recibido
     *
     * <code>recursos/audioRecibido/</code>
     */
    private File directorioPadreAudioRecibido = new File(this.directorioRecursos, "audioRecibido");

    /**
     * Directorio corrspondiente a la última generación de audio recibido
     */
    private File directorioUltimoAudioRecibido;


    /**
     * Instancia permitida de esta clase*
     */
    private static GestorDirectorios instance=null;

    /**
     * Crea el gestor de directorios creando la estructura de directorios
     */
    private GestorDirectorios() {

        Field[] fields = this.getClass().getDeclaredFields();

        try {
            for (int i = 0; i < fields.length; i++) {

                Object datoMiembro = fields[i].get(this);
                
                if (datoMiembro instanceof File) {

                    File file = (File) datoMiembro;
                    if ( !file.exists() ) file.mkdirs();
        
                }
            }
        } catch (IllegalAccessException iae) {
        }
      
           
    }


    /**
     * @return el directorio de recursos
     */
    public File getDirectorioRecursos() {

        return this.directorioRecursos;
        
    }
    


    /**
     * @return la ruta donde se encuentra el ejecutable del test pesq
     *
     *
     */
    public File getRutaPesq(){

        File rutaPesq = new File(this.directorioRecursos, "comandoPesq" + File.separator + getComandoPesq());
        if ( !rutaPesq.exists() ) {

            return null;

        }
        return rutaPesq;
    }


    /**
     * @return el directorio que contiene los directorios de audio recibido
     */
    public File getDirectorioPadreAudioRecibido() {

        return this.directorioPadreAudioRecibido;
    }

    /**
     * @return el directorio de audio de referencia
     */
    public File getDirectorioAudioReferencia() {

        return this.directorioAudioVozReferencia.getParentFile();
    }

    /**
     * @return el directorio de voz referencia
     */
    public File getDirectorioAudioVozReferencia() {

        return this.directorioAudioVozReferencia;
    }

    /**
     * @return el directorio de silencio referencia
     */
    public File getDirectorioAudioSilencioReferencia() {

        return this.directorioAudioSilencioReferencia;
    }


    /**
     * @return la ruta del fichero de audio original
     */
    public File getFicheroAudioOriginal() {
        
        return this.ficheroAudioOriginal;

    }

    /**
     * Cambia el fichero de audio orginal
     */
    public void setFicheroAudioOriginal(File audioWavFile) {

        this.ficheroAudioOriginal = audioWavFile;
    }


    /**
     * crea un nuevo directorio que contiene la generación de audio recibido
     * El directorio tiene el nombre<code>nombreCodec_añomesdía_hora-minutos-segundos</code>
     *
     * @return el directorio creado
     */
    public File crearDirectorioParaAudioRecibido(String nombreCodec) {

        GregorianCalendar calendario = new GregorianCalendar();
        String rutaDirectorio = File.separator+ nombreCodec +"_"+calendario.get(GregorianCalendar.YEAR) +
                (calendario.get(GregorianCalendar.MONTH)+1) + calendario.get(GregorianCalendar.DAY_OF_MONTH)
                + "_" + calendario.get(GregorianCalendar.HOUR_OF_DAY) + "-" + calendario.get(GregorianCalendar.MINUTE)
                + "-" + calendario.get(GregorianCalendar.SECOND);//+File.separator +"degradadas";
        
 
        this.directorioUltimoAudioRecibido = new File(this.directorioPadreAudioRecibido, rutaDirectorio);
        this.directorioUltimoAudioRecibido.mkdirs();
        
        return  this.directorioUltimoAudioRecibido;
    }


    /**
     * @return el último directorio creado para el audio recibido
     */
    public File[] getFicherosUltimoAudioRecibido() {

        if (this.directorioUltimoAudioRecibido ==null) {
            File[] directoriosAudioRecibido = this.directorioPadreAudioRecibido.listFiles();
            File ultimoAudio = null;
            long ultimaMod = Long.MIN_VALUE;// = new long[directoriosAudioRecibido.length];
            long directorioMod;
            for (int i=0; i< directoriosAudioRecibido.length; i++) {
                directorioMod = directoriosAudioRecibido[i].lastModified();
                if ( directorioMod > ultimaMod) {
                    ultimaMod = directorioMod;
                    ultimoAudio = directoriosAudioRecibido[i];
                }
            }
            this.directorioUltimoAudioRecibido = ultimoAudio;
        }

        return this.directorioUltimoAudioRecibido.listFiles();
    }


    /**
     * elimina el contenido de todo audio recibido
     */
    public boolean eliminarFicherosPesq() {
        
        boolean eliminados = true;
        File[] files = this.directorioPadreAudioRecibido.listFiles();
        
        for (File f: files) {
            
            eliminados ^= f.delete();
            
        }
        
        return eliminados;
        
    }

    /**
     * @return el directorio de logs
     */
    public File getDirectorioLogs() {

        return this.directorioLogs;
    }

    public static GestorDirectorios getInstance() {

        if(GestorDirectorios.instance == null) {
            GestorDirectorios.instance = new GestorDirectorios();
        }

        return GestorDirectorios.instance;
    }

    /**
     * Devuelve el nombre del archivo ejecutable del Pesq
     * Este dependerá de si el programa se ejecuta en un sistema windows o
     * unix. Así, dependiendo del caso, el archivo debe tener un nombre concreto:
     *  - windows: pesq.exe
     *  - unix: pesq
     */
    private static String getComandoPesq() {

        String so=System.getProperty("os.name").toLowerCase();
        String comandoPesq="";
        if (so.indexOf("win")> -1) {
            comandoPesq="pesq.exe";
        }else if( (so.indexOf("unix")> -1) || (so.indexOf("linux")> -1)) {
            comandoPesq="pesq";
        }

        return comandoPesq;

    }
       


}

