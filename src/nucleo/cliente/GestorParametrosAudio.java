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

import qos.estimacionQoS.EstimadorPesq;
import audio.FormatoCodec;

/**
 * Clase encargada de gestionar la generación de parámetros de transmisión
 * de forma válida, tal que los valores se encuentren dentro de los límites establecidos
 * y teniendo en cuenta que se transmitirá audio/voz, por lo que deben de mantenerse las relaciones
 * entre los parámetros y las restricciones de duración
 *
 * @author Antonio
 */
public class GestorParametrosAudio extends GestorParametrosTransmision{


    /**
     * Formato de códec de audio
     *
     * @see FormatoCodec
     */
    private FormatoCodec formatoCodec;

    /**
     * Duración máxima en microsguendos de un intervalo
     */
    public static long duracion_intervalo_us_max = 10000000;

    /**
     * Duración mínima en microsguendos de un intervalo
     */
    public final long DURACION_INTERVALO_US_MIN = EstimadorPesq.DURACION_INTERVALO_MIN * 1000;


    /**
     * Crea el gestor de parámetros
     */
    public GestorParametrosAudio(FormatoCodec formatoCodec) {

        super();
        this.formatoCodec = formatoCodec;
        this.reiniciarValoresExtremos();
                
    }

    @Override
    protected void reiniciarValoresExtremos() {

        super.reiniciarValoresExtremos();
        
        this.ajustarParametrosMinimos();

        super.parametrosTransmision.setTiempoEntreEnvios(
                super.parametrosTransmisionMin.getTiempoEntreEnvios()
                );
        super.parametrosTransmision.setLongitudDatos(
                super.parametrosTransmisionMin.getLongitudDatos()
                );
        super.parametrosTransmision.setMensajesPorIntervalo(
                super.parametrosTransmisionMin.getMensajesPorIntervalo()
                );

        this.ajustarParametrosExtremos();

    }

    /**
     * Recalcula los límites inferior y superior
     */
    public void ajustarParametrosExtremos(){

        this.ajustarParametrosMinimos();
        this.ajustarParametrosMaximos();

    }

    /**
     * Recalcula el límite inferior
     */
    private void ajustarParametrosMinimos() {

        int tiempoEntreEnvios_min = (int) Math.floor(
                Math.max(
                    super.parametrosTransmisionMin.getTiempoEntreEnvios(),
                    this.formatoCodec.duracionEnMicrosegundos(super.parametrosTransmisionMin.getLongitudDatos())
                )
                );
        
        int longitudDatos_min = this.formatoCodec.longitudIntervalo(tiempoEntreEnvios_min + 1);

        int mensajesPorIntervalo_min= (int) Math.ceil(
                    this.DURACION_INTERVALO_US_MIN/
                    Math.max(tiempoEntreEnvios_min, super.parametrosTransmision.getTiempoEntreEnvios())
                )+1;
        if(mensajesPorIntervalo_min > this.parametrosTransmision.getMensajesPorIntervalo())
            this.parametrosTransmision.setMensajesPorIntervalo(mensajesPorIntervalo_min);

        
        super.parametrosTransmisionMin.setTiempoEntreEnvios(tiempoEntreEnvios_min);
        super.parametrosTransmisionMin.setLongitudDatos(longitudDatos_min);
        super.parametrosTransmisionMin.setMensajesPorIntervalo(mensajesPorIntervalo_min);
        
    }

    /**
     * Recalcula el límite superior
     */
    private void ajustarParametrosMaximos() {

        int tiempoEntreEnvios_max = (int) Math.min(
                this.formatoCodec.duracionEnMicrosegundos(GestorParametrosTransmision.LD_MAX),
                GestorParametrosAudio.TEE_MAX
                );

        int longitudDatos_max = this.formatoCodec.longitudIntervalo(tiempoEntreEnvios_max);

        int mensajesPorIntervalo_max = (int) GestorParametrosAudio.duracion_intervalo_us_max/
                Math.min(super.parametrosTransmision.getTiempoEntreEnvios(),tiempoEntreEnvios_max);
        if(mensajesPorIntervalo_max < this.parametrosTransmision.getMensajesPorIntervalo())
            this.parametrosTransmision.setMensajesPorIntervalo(mensajesPorIntervalo_max);

        super.parametrosTransmisionMax.setTiempoEntreEnvios(tiempoEntreEnvios_max);
        super.parametrosTransmisionMax.setLongitudDatos(longitudDatos_max);
        super.parametrosTransmisionMax.setMensajesPorIntervalo(mensajesPorIntervalo_max);

    }


    @Override
    public void setTiempoEntreEnvios(int tEE ){

        super.setTiempoEntreEnvios(tEE);
        this.parametrosTransmision.setLongitudDatos(
                this.formatoCodec.longitudIntervalo(tEE)
                );
        this.ajustarParametrosExtremos();
       
        
    }


    @Override
    public void setLongitudMensaje(int lM) {

        super.setLongitudMensaje(lM);
        this.parametrosTransmision.setTiempoEntreEnvios(
                (int) Math.ceil(this.formatoCodec.duracionEnMicrosegundos(lM))
                );
        this.ajustarParametrosExtremos();
    }


    @Override
    public void setMensajesPorIntervalo(int mPI){

        super.setMensajesPorIntervalo(mPI);
        this.ajustarParametrosExtremos();
        
    }


    @Override
    public void setNumeroIntervalos(int nI){

        super.setNumeroIntervalos(nI);
        this.ajustarParametrosExtremos();
        
    }


    /**
     * Devuelve la duración de un intervalo en milisegundos
     */
    public double getDuracionIntervaloEnMS() {

        return this.formatoCodec.duracionEnMicrosegundos(
                this.parametrosTransmision.numeroBytesPorIntervalo()
                )/1000.0;
    }


    /**
     * Devuelve la duración total en milisegundos
     */
    public double getDuracionTotalEnMS() {

        return this.formatoCodec.duracionEnMicrosegundos(
                this.parametrosTransmision.numeroDeMensajes()*this.parametrosTransmision.getLongitudDatos()
                )/1000.0;
    }


    /**
     * Ajusta la duración máxima de intervalo
     *
     * @param duracionIntervalo_us      duración en microsegundos
     */
    public static void setDuracionIntervaloMax(long duracionIntervalo_us) {
        
        GestorParametrosAudio.duracion_intervalo_us_max = duracionIntervalo_us;
    }


    /**
     * Ajusta el formato códec, recalculando los límites superior e inferior
     */
    public void setFormatoCodec(FormatoCodec fc) {

        this.formatoCodec = fc;
        this.reiniciarValoresExtremos();
       
    }


    /**
     * @return El formato códec considerado para la transmisión
     */
    public FormatoCodec getFormatoCodec() {

        return this.formatoCodec;
    }



    


    

}

