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

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;


/**
 * Clase que representa los distintos buffers de audio que genera el codificador
 *
 * @author Antonio
 */
public class BuffersAudio {

    /**
     * Conjunto de buffers de audio, numerados en orden de generaci贸n.
     */
    private ArrayList<byte[]> buffersAudio;


    /**
     *  Numero total de bytes que forman el conjunto de todos los buffers
     */
    private int numeroTotalBytes=0;
    
    
    
    /**
     * Propiedades del audio de los buffers
     */
    private FormatoCodec formatoCodec;



    /**
     * Inicializa el objeto sin buffers
     */
    public BuffersAudio() {

        this.buffersAudio = new ArrayList<byte[]>();
    }


    /**
     * Crea el objeto a partir de otro
     */
    public BuffersAudio(BuffersAudio ba) {

        this.buffersAudio = (ArrayList<byte[]>) ba.buffersAudio.clone();
        this.formatoCodec = ba.formatoCodec;
        this.numeroTotalBytes = ba.numeroTotalBytes;
    }


    /**
     * Inicializa el objeto sin buffers, pero indicando las propiedades
     * del audio que contendr谩n.
     *
     * @param formatoCodec      Propiedades del audio de los buffers
     *
     * @see FormatoCodec
     */
    public BuffersAudio(FormatoCodec formatoCodec) {

        this.buffersAudio = new ArrayList<byte[]>();
        this.formatoCodec = formatoCodec;
        
    }
    

    /**
     * Inicializa el objeto a partir de un bytestream, las longitudes de los buffers
     * y las propiedades del audio que representa el bytestram
     *
     * @param byteArrayAudioStream          Bytes de audio
     * @param longitudesBuffer              Longitudes de los buffers de audio que se crean
     * @param formatoCodec                  Propiedades del audio
     * 
     
     */
    public BuffersAudio(
            byte[] byteArrayAudioStream,
            int[] longitudesBuffer,
            FormatoCodec formatoCodec) {

        int numeroBuffers = longitudesBuffer.length;               
        this.buffersAudio = new ArrayList<byte[]>();
        this.formatoCodec = formatoCodec;
        byte[] b;
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayAudioStream);
        for (int i=0; i<numeroBuffers; i++) {

            if(this.numeroTotalBytes + longitudesBuffer[i] > byteArrayAudioStream.length)
                break;
            else{
                b = new byte[ longitudesBuffer[i] ];
                bais.read(b, 0, longitudesBuffer[i]);
                this.buffersAudio.add(i,b);
                this.numeroTotalBytes += longitudesBuffer[i];
            }

        }

    }

   
    /**
     * Devuelve un buffer de audio en una posici贸n dada
     *
     * @param index     Posici贸n del buffer
     * @return          Los bytes del buffer
     *
     */
    public byte[] getBufferAudio(int index) {

        return this.buffersAudio.get(index);
    }


    /**
     * A帽ade un buffer de audio en una posici贸n dada
     *
     * @param index         Posici贸n del buffer
     * @param bytesAudio    Bytes del buffer
     */
    public void addBufferAudio(int index, byte[] bytesAudio){

        if (index < this.buffersAudio.size() && this.buffersAudio.get(index) !=null) {

            this.numeroTotalBytes -= this.buffersAudio.get(index).length;
            this.buffersAudio.remove(index);

        }

        this.buffersAudio.add(index, bytesAudio);
        this.numeroTotalBytes += bytesAudio.length;

    }


    /**
     * Crea un buffer en una posici贸n dada, con todos sus bytes a cero.
     *
     * @param index             Posici贸n del buffer
     * @param longitudBuffer    N煤mero de bytes del buffer
     */
    public void crearBufferAudio(int index, int longitudBuffer) {

        if (index <this.buffersAudio.size() && this.buffersAudio.get(index) != null) {

            this.numeroTotalBytes -= this.buffersAudio.get(index).length;
            this.buffersAudio.remove(index);

        }
        this.buffersAudio.add(index, new byte[longitudBuffer]);
        this.numeroTotalBytes += longitudBuffer;
        
    }


    /**
     * Devuelve el n煤mero de bytes que tiene un buffer de una posici贸n dada
     *
     * @param index     Posici贸n del buffer
     * @return          El n煤mero de bytes del buffer
     */
    public int longitudBuffer(int index) {

        return this.buffersAudio.get(index).length;
        
    }


    /**
     * Devuelve el n煤mero total de buffers
     *
     * @return      El n煤mero total de buffers
     */
    public int numeroDeBuffers() {

        return this.buffersAudio.size();

    }


    /**
     * Devuelve el n煤mero total de bytes de audio que forman todos los buffers
     *
     * @return      El n煤mero total de bytes que que forman todos los buffers
     */
    public int numeroTotalBytes() {

        return this.numeroTotalBytes;
        
    }


    /**
     * Devuelve los bytes de todos los buffers en forma de bytestream, de manera
     * ordenada.
     *
     * @return      El bytestream con los bytes de todos los buffers, de manera ordenada
     */
    public byte[] getByteArrayAudioStream() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b;

        for (int i=0; i<this.buffersAudio.size(); i++) {
            b = this.buffersAudio.get(i);
            baos.write(b, 0, b.length);
        }

        return baos.toByteArray();

    }


    /**
     *  Devuelve un subconjunto de buffers de audio de duraci贸n igual a la indicada
     *
     * @param duracionUS        Duraci贸n en microsegundos del subconjunto de buffers
     * @return                  El subconjunto de buffers de duraci贸n igual a la indicada
     *
     */
    public BuffersAudio getBuffersAudio(int duracionUS) {

        int bytesTotales = this.formatoCodec.longitudIntervalo(duracionUS);

        if (bytesTotales > this.numeroTotalBytes)
            return new BuffersAudio(this);

        BuffersAudio ba = new BuffersAudio();
        int bytesLeidos = 0;
        byte[] b;

        for (int i=0; i<this.buffersAudio.size(); i++) {

            b = this.buffersAudio.get(i);
            ba.addBufferAudio(i, b);
            bytesLeidos += b.length;
            if ( (bytesLeidos + b.length )> bytesTotales )
                break;
        }

        ba.setFormatoCodec(this.formatoCodec);
        return ba;
    }


    /**
     * Devuelve un vector donde el elemento de la posici贸n i es el n煤mero
     * de bytes que contiene el buffer i
     *
     * @return      Las longitudes del conjunto de vuffers
     */
    public int[] longitudesBuffer() {
        
        int numeroBuffers = this.buffersAudio.size();
        int[] longitudesBuffer = new int[numeroBuffers];
        
        for (int i=0; i<numeroBuffers; i++) {
            
            longitudesBuffer[i] = this.buffersAudio.get(i).length;
        }
        
        return longitudesBuffer;

    }


    /**
     * Devuelve los buffers de audio
     *
     * @return      Los buffers de aduio
     */
    public ArrayList<byte[]> getBuffersAudio() {

        return this.buffersAudio;
        
    }

    /**
     * Muestra por la salida est谩ndar las longitudes del conjunto de buffers
     */
    public void printLongitudesBuffer() {

        for (int i=0; i<this.buffersAudio.size(); i++) {
            System.out.print(this.buffersAudio.get(i).length +", ");
        }
        System.out.println();
    }


    /**
     * Asigna las propiedades del audio de los buffers
     *
     * @param formatoCodec      Las propiedades de audio de los buffers
     */
    public void setFormatoCodec(FormatoCodec formatoCodec) {

        this.formatoCodec = formatoCodec;

    }


    /**
     * Devuelve las propiedades del audio de los buffer
     *
     * @return      Las propiedades del audio de los buffer
     */
    public FormatoCodec getFormatoCodec() {

        return this.formatoCodec;
    }

}

