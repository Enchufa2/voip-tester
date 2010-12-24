/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
     * Conjunto de buffers de audio, numerados en orden de generación.
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
     * del audio que contendrán.
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
     * Devuelve un buffer de audio en una posición dada
     *
     * @param index     Posición del buffer
     * @return          Los bytes del buffer
     *
     */
    public byte[] getBufferAudio(int index) {

        return this.buffersAudio.get(index);
    }


    /**
     * Añade un buffer de audio en una posición dada
     *
     * @param index         Posición del buffer
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
     * Crea un buffer en una posición dada, con todos sus bytes a cero.
     *
     * @param index             Posición del buffer
     * @param longitudBuffer    Número de bytes del buffer
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
     * Devuelve el número de bytes que tiene un buffer de una posición dada
     *
     * @param index     Posición del buffer
     * @return          El número de bytes del buffer
     */
    public int longitudBuffer(int index) {

        return this.buffersAudio.get(index).length;
        
    }


    /**
     * Devuelve el número total de buffers
     *
     * @return      El número total de buffers
     */
    public int numeroDeBuffers() {

        return this.buffersAudio.size();

    }


    /**
     * Devuelve el número total de bytes de audio que forman todos los buffers
     *
     * @return      El número total de bytes que que forman todos los buffers
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
     *  Devuelve un subconjunto de buffers de audio de duración igual a la indicada
     *
     * @param duracionUS        Duración en microsegundos del subconjunto de buffers
     * @return                  El subconjunto de buffers de duración igual a la indicada
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
     * Devuelve un vector donde el elemento de la posición i es el número
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
     * Muestra por la salida estándar las longitudes del conjunto de buffers
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
