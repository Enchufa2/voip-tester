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
     * Longitud mÃ¡xima en bytes de los datos del mensaje
     * <code>Lmax= 2^16 - H_UDP - - H_IP - H_PT</code>
     */
    public static final int LONGITUD_DATOS_MAXIMA=65500;

    /**
     * Longitud mÃ¡xima en bytes de los datos del mensaje
     * considerando una MTU mÃ¡xima de 1500 bytes
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
     * Escribe el identificador en la posiciÃ³n correspondiente dentro del mensaje
     *
     * @param identificador     Valor del identificador de cabecera que asigan al mensaje que llama a este mÃ©todo
     */
    public void setIdentificadorCabecera(int identificador) {
        this.encapsularInt(POSICION_IDENTIFICADOR, identificador);
    }


    /**
     * Copia el contenido del array <code>datos</code> pasado como argumento, empezando por el principio de este,
     * al contenido del mensaje, empezando por la posiciÃ³n donde comienzan de los datos.
     *
     * @param datos     El array de bytes de los datos a copiar
     */
    public void setDatosMensaje (byte[] datos) {
        System.arraycopy(datos, 0, super.bytesMensaje, super.longitudCabecera, datos.length);
    }


    /**
     * Devuelve el valor del identificador de cabecera
     *
     * @return  El valor del identificador de cabecera del mensaje que llama a este mÃ©todo
     */
    public int identificadorCabecera() {

        return this.extraerInt(POSICION_IDENTIFICADOR);
    }

    /**
     * Devuelve el valor del identificador de cabecera.
     * En general el identificador no puede ser mayor que el nÃºmero de paquetes
     * que se envia. Si este identificador es menor que <code>idMasGrande</code> o es negativo
     * entonces se genera la excepciÃ³n de identificador invÃ¡lido.
     *
     * @param   idMasGrande
     *          Valor mÃ¡ximo que puede valer el identificador
     *
     * @throws IdCabeceraInvalidoException  Si el identificador de la cabecera de este mensaje
     *                                      es negativo o mayor del mÃ¡ximo permitido.
     *
     */
    public int identificadorCabecera(int idMasGrande) throws IdCabeceraInvalidoException {

        int identificadorCabecera = this.extraerInt(POSICION_IDENTIFICADOR);
        if ( (identificadorCabecera >= idMasGrande) || (identificadorCabecera < 0) )
            throw new IdCabeceraInvalidoException();

        return identificadorCabecera;
    }
 

}

