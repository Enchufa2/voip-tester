package qos.estimacionQoS;

import qos.estimacionQoS.parametrosQoS.ParametroQoS;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import audio.FormatoCodec;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * @author Antonio
 * @version 1.0
 * @created 19-ago-2010 20:56:59
 */
public abstract class MedidaQoS 
        implements Serializable{


    /**
     * Propiedades de los datos de audio utilizados para obtener esta medida
     *
     * @see FormatoCodec
     */
    protected FormatoCodec formatoCodec;    


    /**
     * Parametros de transmisión utilizados para obtener esta medida.
     */
    protected ParametrosTransmision parametrosTransmision;


    /**
     * Estimaciones del parámetro correspondiente. Debe haber una estimación
     * del parámetro por cada intervalo
     */
    protected ParametroQoS[] parametrosQoS;


    /**
     * fecha de inicio/creación de medida
     */
    protected Date fechaDeMedida = new Date();


    /**
     * Se inicializa la  medida de acuerdo a las variables de transmisión de audio.
     * Existen medidas en las que es necesario disponer de datos del codec de audio para su
     * estimación.
     * Se reserva espacio para representar tantas estimaciones como número de
     * intervalos indiquen lso parámetros de transmisión.
     *
     * 
     * @param formatoCodec      Propiedades de los datos de audio utilizados para obtener esta medida
     * @param parametrosTransmision   Parametros de transmisión de audio utilizados para obtener esta medida
     */
    public MedidaQoS (
            FormatoCodec formatoCodec ,
            ParametrosTransmision parametrosTransmision
            ) {
        
        this.formatoCodec = formatoCodec;        
        this.parametrosTransmision = parametrosTransmision;
        this.parametrosQoS = 
                new ParametroQoS [ this.parametrosTransmision.getNumeroIntervalos() ];
               
    }


    /**
     * Inicializa la medida sólo a partir de los parámetros de transmisión. Existen medidas
     * en las que el contenido de los datos no tiene por qué ser audio.
     *
     * Se reserva espacio para representar tantas estimaciones como número de
     * intervalos indiquen los parámetros de transmisión.
     * 
     * @param parametrosTransmision Parametros de transmisión de audio utilizados para obtener esta medida
     */
    public MedidaQoS(
            ParametrosTransmision parametrosTransmision
            ) {

        this.formatoCodec = null;
        this.parametrosTransmision = parametrosTransmision;
        this.parametrosQoS = 
                new ParametroQoS [ this.parametrosTransmision.getNumeroIntervalos() ];

    }


    /**
     * Añade una estimación del parámetro correspondiente al intervalo indicado
     * por <code>numeroIntervalo</code>.
     *
     * @param numeroIntervalo   Selecciona la medida correspondiente al intervalo indicado por este argumento
     * @param parametroQoS      Los valores de la estimación del parámetro
     *
     */
    public void addParametroQoS(int numeroIntervalo,ParametroQoS parametroQoS){

        this.parametrosQoS[numeroIntervalo] = parametroQoS;
    }


    /**
     * Devuelve los valores del eje de abcisas para cada intervalo, que se corresponde
     * con los intantes temporales en milisegundos
     *
     * @return  la matriz con los valores donde cada fila representa los valores del eje
     */
    public float[][] valoresEjeX(){

        int numeroIntervalos = this.parametrosTransmision.getNumeroIntervalos();
        int mensajesPorIntervalo = this.parametrosTransmision.getMensajesPorIntervalo();        
        float tiempoEntreEnvios_ms = this.parametrosTransmision.getTiempoEntreEnvios()/1000f;        
        float instanteValorParametro_ms = 0;
        float[][] ejeX=null;
        
        if(this.parametrosQoS[0].numeroDeValores() == 1) {
            
            ejeX = new float[numeroIntervalos][1];
            for(int i=0; i<numeroIntervalos; i++) { 
                
                instanteValorParametro_ms = tiempoEntreEnvios_ms * mensajesPorIntervalo * (i+1);
                ejeX[i][0] = instanteValorParametro_ms;
            }
            
        }else{        
            
            ejeX = new float[numeroIntervalos][mensajesPorIntervalo];
            for (int i = 0; i < numeroIntervalos; i++) {

                instanteValorParametro_ms = tiempoEntreEnvios_ms * mensajesPorIntervalo * i;

                for (int j = 0; j < mensajesPorIntervalo; j++) {
                    ejeX[i][j] = instanteValorParametro_ms;
                    instanteValorParametro_ms += tiempoEntreEnvios_ms;
                }
            }
        }

        return ejeX;
    }

    /**
     * Devuelve los valores del eje de ordenadas, que se corresponde
     * con valores estimados del parámetro
     *
     * @return la matriz con los valores donde cada fila representa los valores del eje
     *         para un intervalo
     */
    public float[][] valoresEjeY(){

        int numeroIntervalos = this.parametrosTransmision.getNumeroIntervalos();
        int valoresPorIntervalo = this.parametrosQoS[0].numeroDeValores();
        
        float[][] ejeY = new float[numeroIntervalos][valoresPorIntervalo];
        for(int i=0; i<numeroIntervalos; i++) {

            for(int j=0; j<valoresPorIntervalo; j++) {

                ejeY[i][j] = this.parametrosQoS[i].getValorParametro(j);
            }
        }

        return ejeY;
    }


    /**
     * @return representación en <codeStringcode> de las  unidades de los valores del eje X
     */
    public String unidadesEjeXtoString() {
        
        return "ms";
    }

    /**
     * @return representación en <codeStringcode> del nombre del eje X
     */
    public String nombreEjeX() {

        return "time";
    }

    /**
     * @return representación en <codeStringcode> de las  unidades de los valores del eje Y
     */
    public String unidadesEjeYtoString() {
        
        return this.parametrosQoS[0].getUnidad().toString();
    }

    /**
     * @return representación en <codeStringcode> del nombre del eje Y
     */
    public String nombreEjeY() {

        return this.parametrosQoS[0].getNombreParametro();
    }

    /**
     * @return representación en <codeStringcode> del nombre del
     * parámetro de QoS que representa la  medida
     */
    public String nombreParametroQoS() {

        return this.parametrosQoS[0].getNombreParametro();
    }


    /**
     * @return representación en <codeStringcode> de las condiciones
     * en las que se realiza la medida
     */
    public String condicionesDeMedidaToString() {

        String condicionesToString="";
        String newLine = System.getProperty("line.separator");

        if(this.formatoCodec != null) {
            
            condicionesToString += this.formatoCodec.toString() + newLine + newLine;
        }
        
        condicionesToString += "interdeparture time (us): "+  this.parametrosTransmision.getTiempoEntreEnvios() +newLine
                + "message length (bytes): "+  this.parametrosTransmision.getLongitudDatos() +newLine
                + "messages per interval: "+  this.parametrosTransmision.getMensajesPorIntervalo() +newLine
                + "number of intervals: "+  this.parametrosTransmision.getNumeroIntervalos();

        return condicionesToString;
    }

    
    /**
     * Realiza un procesado de los valores estimados. La función o filtro
     * que se aplique dependerá del tipo de parámetro de QoS
     * 
     * To do
     *
     */
    public abstract float valorRepresentativo (int numeroIntervalo);

    
    /**
     * @return  Propiedades de audio utilizados para obtener esta medida
     */
    public FormatoCodec getFormatoCodec() {

        return this.formatoCodec;
    }   


    /**
     * @return   la fecha de generación de la medida
     */
    public Date getFechaDeMedida() {

        return this.fechaDeMedida;
    }

    /**
     * @return representación en <codeStringcode> de la fecha de creación
     * de la medida
     */
    public String fechaDeMedidaToString(){
        
        String fecha="";
        GregorianCalendar gregCal =  new GregorianCalendar();
        gregCal.setTime(this.fechaDeMedida);
        fecha +=gregCal.get(GregorianCalendar.DAY_OF_MONTH) +"."+ gregCal.get(GregorianCalendar.MONTH)
                + "." + gregCal.get(GregorianCalendar.YEAR)
                + "_" + gregCal.get(GregorianCalendar.HOUR) + "_" + gregCal.get(GregorianCalendar.MINUTE)
                + "_" + gregCal.get(GregorianCalendar.SECOND);
        
        return fecha;
    }

    /**
     * @return Parametros de transmisión utilizados para obtener esta medida
     */
    public ParametrosTransmision getParametrosTransmision() {

        return this.parametrosTransmision;
    }

    /**
     * @return el objeto <code>ParametroQoS</code> correspondiente
     * al intervalo indicado
     *
     * @param   numeroIntervalo   intervalo sobre el que desea conocer
     *                            el parámetro de QoS estimado
     */
    public ParametroQoS getParametroQoS(int numeroIntervalo) {

        return this.parametrosQoS[numeroIntervalo];
    }
   

}