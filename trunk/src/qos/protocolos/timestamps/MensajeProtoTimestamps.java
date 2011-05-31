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

package qos.protocolos.timestamps;

import qos.protocolos.*;

/**
 *
 * @author Antonio
 */
public class MensajeProtoTimestamps extends Mensaje {

     /**
     * Posicion del mensaje donde se encuentra el identificador de mensaje.
     */
    public static final int POSICION_IDENTIFICADOR=0;

    /**
     * Longitud de la cabecera en bytes
     */
    public static final int LONGITUD_IDENTIFICADOR = 4;

    

    /**
     * Longitud máxima en bytes de los datos del mensaje
     * <code>Lmax= 2^16 - H_UDP - - H_IP - H_PT</code>
     */
    public static final int LONGITUD_DATOS_MAXIMA=65500;

    /**
     * Longitud máxima en bytes de los datos del mensaje
     * considerando una MTU máxima de 1500 bytes
     * <code>Lmax= 1500 - H_UDP - H_PT</code>
     */
    public static final int LONGITUD_DATOS_MAXIMA_MTU=1488;

    /**
     * Constructor por defecto que inicializa el array a null
     */
     public MensajeProtoTimestamps() {
         super();
         super.longitudCabecera = MensajeProtoTimestamps.LONGITUD_IDENTIFICADOR;
     }
    

    /**
     * Crea un mensaje de longitud total igual a <code>longitudDatos + LONGITUD_IDENTIFICADOR</code>
     *
     * @param longitudDatos  La longitud en bytes de los datos (sin la cabecera)
     * @throws LongitudInvalidaException
     */
    public MensajeProtoTimestamps(int longitudDatos) throws LongitudInvalidaException{
        
        super( longitudDatos, MensajeProtoTimestamps.LONGITUD_IDENTIFICADOR );
        
        if ( longitudDatos > MensajeProtoTimestamps.LONGITUD_DATOS_MAXIMA)
            throw new LongitudInvalidaException("Message length bigger than allowed maximum");
    }


    /**
     * Crea un mensaje a partir de un array de bytes dado.
     */
    public MensajeProtoTimestamps(byte[] b) {

        super(b);
        super.longitudCabecera = MensajeProtoTimestamps.LONGITUD_IDENTIFICADOR;
    }


    /**
     * Escribe el identificador en la posición correspondiente dentro del mensaje
     *
     * @param identificador     Valor del identificador de cabecera que asigan al mensaje que llama a este método
     */
    public void setIdentificadorCabecera(int identificador) {
        this.encapsularInt(POSICION_IDENTIFICADOR, identificador);
    }


    /**
     * Copia el contenido del array <code>datos</code> pasado como argumento, empezando por el principio de este,
     * al contenido del mensaje, empezando por la posición donde comienzan de los datos.
     *
     * @param datos     El array de bytes de los datos a copiar
     */
    public void setDatosMensaje (byte[] datos) {
        System.arraycopy(datos, 0, super.bytesMensaje, super.longitudCabecera, datos.length);
    }


    /**
     * Devuelve el valor del identificador de cabecera
     *
     * @return  El valor del identificador de cabecera del mensaje que llama a este método
     */
    public int identificadorCabecera() {

        return this.extraerInt(POSICION_IDENTIFICADOR);
    }

    /**
     * Devuelve el valor del identificador de cabecera.
     * En general el identificador no puede ser mayor que el número de paquetes
     * que se envia. Si este identificador es menor que <code>idMasGrande</code> o es negativo
     * entonces se genera la excepción de identificador inválido.
     *
     * @param   idMasGrande
     *          Valor máximo que puede valer el identificador
     *
     * @throws IdCabeceraInvalidoException  Si el identificador de la cabecera de este mensaje
     *                                      es negativo o mayor del máximo permitido.
     *
     */
    public int identificadorCabecera(int idMasGrande) throws IdCabeceraInvalidoException {

        int identificadorCabecera = this.extraerInt(POSICION_IDENTIFICADOR);
        if ( (identificadorCabecera >= idMasGrande) || (identificadorCabecera < 0) )
            throw new IdCabeceraInvalidoException();

        return identificadorCabecera;
    }
 

}

