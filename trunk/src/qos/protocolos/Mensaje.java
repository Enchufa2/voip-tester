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
package qos.protocolos;
import java.util.Arrays;

/**
 *
 * @author San
 */
public class Mensaje {

    /**
     * Bytes del mensaje
     */
     protected byte[] bytesMensaje;


     /**
      * Longitud cabecera
      */
     protected int longitudCabecera=0;

     /**
     * Constructor por defecto que inicializa el array a null
     */
     public Mensaje() {
     }
     

    /**
     * Crea un mensaje con todos sus bytes a 0
     *
     * @param longitudMensaje n煤mero de bytes que contiene el mensaje
     * @throws LongitudInvalidaException    Si la longitud del mensaje es menor o igual que 0
     */
    public Mensaje(int longitudMensaje) throws LongitudInvalidaException{

        if ( longitudMensaje <= 0 )
            throw new LongitudInvalidaException("Invalid message length");
        
        this.bytesMensaje = new byte[longitudMensaje];
    }


    /**
     * Crea un mensaje con todos sus bytes a 0
     *
     * @param longitudMensaje n煤mero de bytes que contiene el mensaje
     * @throws LongitudInvalidaException    Si la longitud del mensaje es menor o igual que 0
     *                                      Si la longitud de la cabecera es menor que 0
     */
    public Mensaje(int longitudMensaje, int longitudCabecera) throws LongitudInvalidaException{

       if ( longitudMensaje <= 0 )
            throw new LongitudInvalidaException("Invalid message length");

       if ( longitudCabecera < 0)
            throw new LongitudInvalidaException("Invalid header length: " + longitudCabecera);

        this.bytesMensaje = new byte[longitudMensaje + longitudCabecera];
        this.longitudCabecera = longitudCabecera;
    }


    /**
     * Crea un mensaje a partir de un conjunto de bytes
     *
     * @param b Conjunto de bytes
     */
    public Mensaje(byte[] b) {

        this.bytesMensaje=b;
    }


    /**
     * Encapsular un conjunto de bytes dentro del mensaje, siguiendo
     * el convenio bigEndian.
     *
     * @param index Posici贸n dentro del mensaje
     * @param bytes bytes a encapsular
     *
     * @see Byte
     */
    public void encapsularBytes(int index, byte[] bytes) {

        int numeroBytes=bytes.length;
        for (int i=0; i<numeroBytes; i++) {
            this.bytesMensaje[index+i]=bytes[i];
        }
    }


    /**
     * Extraer un conjunto de bytes del mensaje a partir de una posicion dada
     *
     * @param index Posici贸n dentro del mensaje
     * @param numeroBytes El n煤mero de bytes a extraer
     * @return bytes El conjunto de bytes extraidos
     */
    public byte[] extraerBytes(int index, int numeroBytes) {

        byte[] bytes = new byte[numeroBytes];
        for(int i=0; i<numeroBytes; i++){
            bytes[i]= this.bytesMensaje[i+index];
        }
        return bytes;
    }



    /**
     * Encapsula un tipo de dato Int dentro del mensaje, siguiendo
     * el convenio bigEndian.
     *
     * @param index Posici贸n dentro del mensaje
     * @param datoInt n煤mero de tipo Int a encapsular
     *
     * @see Integer
     */
    public void encapsularInt(int index, int datoInt) {

        for(int i=0; i <4; i++){
            this.bytesMensaje[i+index]=(byte) (datoInt >>> 8*(3-i));
        }
    }


    /**
     * Extrae un subconjunto de 4 bytes del mensaje y los convierte a int
     *
     * @param index Posici贸n dentro del mensaje
     * @return datoLong n煤mero de tipo int decodificado
     *
     */
    public int extraerInt(int index) {

        int datoInt=0;
        for(int i=0; i<4; i++){
            datoInt <<=8;
            datoInt^= (int) this.bytesMensaje[i+index] & 0xFF;
        }
        return datoInt;
    }

    /**
     * Encapsula un tipo de dato long dentro del mensaje, siguiendo
     * el convenio bigEndian.
     *
     * @param index Posici贸n dentro del mensaje
     * @param datoLong n煤mero de tipo long a encapsular
     *
     * @see Long
     */
    public void encapsularLong(int index,long datoLong) {

        for(int i=0; i <8; i++){
            this.bytesMensaje[i+index]=(byte) (datoLong >>> 8*(7-i));
        }
    }


    /**
     * Extrae un subconjunto de bytes del mensaje y los convierte a long
     *
     * @param index Posici贸n dentro del mensaje
     * @return datoLong n煤mero de tipo long decodificado
     *
     */
    public long extraerLong(int index) {

        long datoLong=0;
        for(int i=0; i <8; i++){
            datoLong <<=8;
            datoLong^= (long) this.bytesMensaje[i+index] & 0xFF;
        }
        return datoLong;
    }


    /**
     * Devuelve el conjunto de bytes que forman el mensaje
     *
     * @return bytesMensaje el conjunto de bytes que forman el mensaje
     */
    public byte[] getBytesMensaje() {

        return this.bytesMensaje;
    }

    /**
     * Asigna el conjunto de bytes que forman el mensaje, cambiando la referencia
     * del vector de bytes a otra nueva.
     *
     * @param   bytesMensaje    El conjunto de bytes asignados
     *
     */
    public void setBytesMensaje(byte[] bytesMensaje) {

        this.bytesMensaje = bytesMensaje;
    }



    /**
     * Pone todos los bytes del mensaje al valor <code>valor</code>
     *
     * @param valor     El valor que se asigna a los bytes del mensaje
     */
    public void setValorBytes(byte valor) {

        if(this.bytesMensaje !=null) {

            Arrays.fill(this.bytesMensaje, valor);
        }

    }


    /**
     * Devuelve el n煤mero de bytes que contiene el mensaje
     *
     * @return longitud Longitud en bytes del mensaje
     */
    public int longitudMensaje() {

        return this.bytesMensaje.length;
    }


     /**
     * Copia tantos bytes, indicados por longitud, de los datos del mensaje comenzando
     * por el principio de estos, al array pasado como argumento, comenzando por la
     * posici贸n indicada por offset.
     *
     * @param datos     El array de bytes destino donde se copiar谩n los datos del mensaje
     * @param offset    La posici贸n del array de bytes destino donde empiezan los datos
     * @param longitud  La longitud total en bytes a copiar
     */
    public void getDatosMensaje(byte[] datos, int offset, int longitud) {

        System.arraycopy(this.bytesMensaje, this.longitudCabecera, datos, offset, longitud);

    }


    /**
     * Copia tantos bytes, indicados por longitud, de los datos del array pasado
     * como argumento comenzando por el offset a los datos del mensaje, comenzando
     * por el principio de estos.
     *
     * @param datos     El array de bytes destino donde se copiar谩n los datos del mensaje
     * @param offset    La posici贸n del array de bytes destino donde empiezan los datos
     * @param longitud  La longitud total en bytes a copiar
     */
    public void setDatosMensaje(byte[] datos, int offset, int longitud) {

        System.arraycopy(datos, offset, this.bytesMensaje, this.longitudCabecera, longitud);

    }

    /**
     * Devuelve la longitud de los datos en bytes
     *
     */
    public int longitudDatosMensaje() {

        return this.bytesMensaje.length - this.longitudCabecera;
    }


    /**
     * Devuelve la longitud en bytes de la cabecera del mensaje
     */
    public int longitudDeCabecera() {

        return this.longitudCabecera;
    }

    /**
     * Devuelve la posici贸n del byte dentro del mensaje donde empiezan los datos
     */
    public int posicionDatos() {

        return this.longitudCabecera;
    }


}

