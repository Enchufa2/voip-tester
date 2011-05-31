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

package qos.protocolos;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;


/**
 * Proporciona funcionalidades a nivel de byte para el tratamiento de los datos
 * contenidos en los mensajes
 * 
 * @author Antonio
 */
public class GestorDatosMensaje {

    
    /**
     * Conjunto de mensajes a tratar
     *
     * @see Mensaje
     */
    protected Mensaje[] mensajes;


    /**
     * Crea el objeto a partir de un conjunto de mensajes
     *
     * @param mensajes  El conjunto de mensajes a tratar
     * @see Mensaje
     */
    public GestorDatosMensaje(Mensaje[] mensajes) {

        this.mensajes = mensajes;
    }


    /**
     * Obtiene los datos de <code>numeroDeMensajes</code> mensajes, empezando por
     * el mensaje con posicion <code>primerMensaje</code>, y los devuelve en un array de bytes.
     *
     * @param numeroDeMensajes  El nÃºmero de mensajes sobre que los que se extraen los datos.
     * @param primerMensaje     El primer mensaje a partir de cual empiezan a extraerse los datos
     * @return datosDeMensaje   El array de bytes de datos obtenidos.
     *
     * @throws IllegalArgumentException     Si se solicitan mensajes fuera del conjunto disponible
     */
    public byte[] deMensajesAbyteArray(int numeroDeMensajes, int primerMensaje) {

        //int numeroDeMensajes = this.mensajes.length;
        if ( (primerMensaje + numeroDeMensajes) > this.mensajes.length ){

            throw new IllegalArgumentException("Messages out of bounds");
        }

        int longitudDatosMensaje = this.mensajes[0].longitudDatosMensaje();
        
        byte[] datosDeMensaje = new byte[numeroDeMensajes*longitudDatosMensaje];
        
        for (int i=0; i < numeroDeMensajes; i++) {
    
            this.mensajes[primerMensaje+i].getDatosMensaje(datosDeMensaje, i*longitudDatosMensaje, longitudDatosMensaje);
        }

        return datosDeMensaje;
    }


    /**
     * Obtiene los datos de <code>numeroDeMensajes</code> mensajes, empezando por
     * el mensaje con posicion <code>primerMensaje</code>, y los copia en <code>bytesObtenidos</code>
     *
     * @param bytesObtenidos    Array donde se copian los datos que se obtienen de los mensajes
     *                          La longitud debe ser mayor o igual que <code>numeroDeMensajes</code>*<code>longitudDatos</code>
     * @param numeroDeMensajes  El nÃºmero de mensajes sobre que los que se extraen los datos.
     * @param primerMensaje     El primer mensaje a partir de cual empiezan a extraerse los datos
     * @return datosDeMensaje   El array de bytes de datos obtenidos.
     *
     * @throws IllegalArgumentException     Si se solicitan mensajes fuera del conjunto disponible
     */
    public byte[] deMensajesAbyteArray(byte[] bytesObtenidos, int numeroDeMensajes, int primerMensaje) {

        //int numeroDeMensajes = this.mensajes.length;
        if ( (primerMensaje + numeroDeMensajes) > this.mensajes.length ){

            throw new IllegalArgumentException("Messages out of bounds");
        }

        int longitudDatosMensaje = this.mensajes[0].longitudDatosMensaje();

        for (int i=0; i < numeroDeMensajes; i++) {

            this.mensajes[primerMensaje+i].getDatosMensaje(bytesObtenidos, i*longitudDatosMensaje, longitudDatosMensaje);
        }

        return bytesObtenidos;
    }


    /**
     * Copia el contenido del array de bytes <code>datos</code> en la parte de datos de
     * los mensajes. Copia <code>longitud</code> bytes de <code>datos</code> comenzando
     * por la posiciÃ³n <code>offset</code>, a partir del mensaje <code>primerMensaje</code>.
     * rellenando el campo de datos de este.
     * El nÃºmero de bytes <code>longitud</code> debe ser menor que
     *              <code> (mensajes.length - primerMensaje)*longitudDatosMensaje </code>
     *
     * @param datos          Las datos a mapear en los mensajes
     * @param offset         La posiciÃ³n dentro de <code>datos</code> a partir de la cual se empiezan a copiar los datos
     * @param longitud       El nÃºmero total de bytes a copiar
     * @param primerMensaje  Posicion del primer mensaje donde se empiezan a copiar los datos.
     *
     * @throws IllegalArgumentException     Si se solicitan mensajes fuera del conjunto disponible
     *
     * 
     */
    public void deByteArrayAmensajes(byte[] datos, int offset, int longitud, int primerMensaje){

        int longitudDatosMensaje = this.mensajes[0].longitudDatosMensaje();
        int numeroDeMensajes = longitud/longitudDatosMensaje;
        
        if ( (primerMensaje + numeroDeMensajes) > this.mensajes.length ){

            throw new IllegalArgumentException("Messages out of bounds: " + primerMensaje + numeroDeMensajes
                    + " Availables: " + this.mensajes.length);
        }
        
        int bytesRestantes = longitud % longitudDatosMensaje;
        int i;
        for (i=0; i<numeroDeMensajes; i++) {

            this.mensajes[primerMensaje+i].setDatosMensaje(datos, offset + i*longitudDatosMensaje, longitudDatosMensaje);

        }
        if (bytesRestantes!=0) {
            
            this.mensajes[primerMensaje+i].setDatosMensaje(datos, offset + i*longitudDatosMensaje, bytesRestantes);
        }
    }


    /**
     * Abre el fichero <code>origen</code> y copia el contenido de este, comenzando por <code>offset</code>, en
     * <code>numeroDeMensajes</code> mensajes, comenzando a partir del mensaje <code>primerMensaje</code>.
     * En total se mapearan <code> numeroDeMensajes * longitudDatosMensaje</code> bytes.
     *
     * @param origen                El fichero de origen de donde se leen los datos
     * @param offset                Primer byte del fichero a mapear
     * @param numeroDeMensajes      NÃºmero de mensajes que se utilizarÃ¡n para albergar los datos
     * @param primerMensaje         Primer mensaje donde se empiezan a copiar los datos
     *
     * @throws FileNotFoundException    Si el archivo origen no se encuentra
     * @throws IllegalArgumentException     Si se solicitan mensajes fuera del conjunto disponible
     * @throws IOException
     * 
     *
     */
    public void deFicheroAmensajes(File origen, int offset, int numeroDeMensajes, int primerMensaje)
            throws FileNotFoundException,IOException {

        int longitudDatosMensaje = this.mensajes[0].longitudDatosMensaje();
         
        if ( (primerMensaje + numeroDeMensajes) > this.mensajes.length ){

            throw new IllegalArgumentException("Messages out of bounds");
        
        }else if( (origen.length() - offset) < (numeroDeMensajes * longitudDatosMensaje)) {

           throw new IllegalArgumentException("Message data length bigger than total file length: "
                   + (origen.length() - offset) + " bytes");
        
        }

        FileInputStream fileInputStream = null;
        try{

            fileInputStream = new FileInputStream (origen);
            fileInputStream.skip(offset);
            int posicionDatos = this.mensajes[0].posicionDatos();
            for (int i=0; i<numeroDeMensajes; i++) {

                fileInputStream.read(this.mensajes[primerMensaje+i].getBytesMensaje(), posicionDatos, longitudDatosMensaje);
            }

        }finally {

            if (fileInputStream !=null){
                fileInputStream.close();
            }
        }

    }



    /**
     * Obtiene datos contenidos en <code>numeroDeMensajes</code> mensajes, comenzando
     * por el mensaje <code>primerMensaje</code>, y los escribre en el fichero <code>destino</code>.
     *
     * @param destino           El fichero sobre el que se escriben los datos
     * @param numeroDeMensajes  El numeroDeMensajes sobre los que se extraen los datos
     * @param primerMensaje     Primer mensaje donde se empiezan a leer los datos
     * @throws IllegalArgumentException     Si se solicitan mensajes fuera del conjunto disponible
     * @throws IOException
     *
     */
    public void deMensajesAFichero(File destino, int numeroDeMensajes, int primerMensaje) 
            throws IOException {

        if ( (primerMensaje + numeroDeMensajes) > this.mensajes.length ){

            throw new IllegalArgumentException("Messages out of bounds");

        }
        
        FileOutputStream fileOutputStream = null;
        try {

            fileOutputStream = new FileOutputStream(destino);
            int posicionDatos = this.mensajes[0].posicionDatos();
            int longitudDatosMensaje = this.mensajes[0].longitudDatosMensaje();

            for(int i=0; i<numeroDeMensajes; i++) {

                fileOutputStream.write(this.mensajes[primerMensaje + i].getBytesMensaje(), posicionDatos, longitudDatosMensaje);
            }

        }finally{

            if (fileOutputStream !=null)
            fileOutputStream.close();

        }
    }



    /**
     * Obtiene un numero de ficheros de audio de duracion <code>duracionAudioFichero</code>
     * obtenidos a partir de los datos de los mensajes. El nÃºmero de ficheros viene dado por:
     *   <code> (longitudDatosMensaje*numeroDeMensajes) / duracionIntervaloEnbytes</code>
     *
     * <code>directorioFicheros</code> debe indicar el directorio donde se crean
     * los ficheros. El nombre de los ficheros viene dado por <code>audio_x</code> donde
     * <code>x</code> es el orden de creaciÃ³n de los ficheros, y tendrÃ¡n extensiÃ³n <code>.wav</code>.
     *
     *
     * @param  directorioFicheros           El path donde se alojarÃ¡n los ficheros obtenidos
     * @param  longitudFichero              La longitud en bytes de cada fichero creado
     *
     * @throws IllegalArgumentException     Si la ruta indicada no es un directorio o no existe
     * @throws IOException                  Si ocurre un error de E/S al manipular los ficheros
     */
    public void deMensajesAsubficheros(File directorioFicheros, int longitudFichero)
            throws IOException{

        //int longitudIntervalo = this.formatoAudio.longitudIntervalo(duracionAudioFichero);
        int longitudDatos =this.mensajes[0].longitudDatosMensaje();
        int numeroFicheros = ( longitudDatos * this.mensajes.length) / longitudFichero;
        int mensajesPorIntervalo = longitudFichero / longitudDatos;
        File[] ficherosDatos =  new File[numeroFicheros];

        if( !directorioFicheros.isDirectory() || !directorioFicheros.exists()) {

            throw new IllegalArgumentException("Invalid directory path. " +
                    "Path is not a directory or not exists.");

        }

        String nombreBase = directorioFicheros.getPath() + File.separator + "datos" ;

        for (int i=0; i<numeroFicheros; i++) {

            ficherosDatos[i] = new File(nombreBase+"_"+i);
            this.deMensajesAFichero(ficherosDatos[i], mensajesPorIntervalo, i*mensajesPorIntervalo);

        }


    }



}

