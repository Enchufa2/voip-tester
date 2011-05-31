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


package nucleo.cliente;

import qos.estimacionQoS.EstimadorPesq;
import audio.FormatoCodec;

/**
 * Clase encargada de gestionar la generaci贸n de par谩metros de transmisi贸n
 * de forma v谩lida, tal que los valores se encuentren dentro de los l铆mites establecidos
 * y teniendo en cuenta que se transmitir谩 audio/voz, por lo que deben de mantenerse las relaciones
 * entre los par谩metros y las restricciones de duraci贸n
 *
 * @author Antonio
 */
public class GestorParametrosAudio extends GestorParametrosTransmision{


    /**
     * Formato de c贸dec de audio
     *
     * @see FormatoCodec
     */
    private FormatoCodec formatoCodec;

    /**
     * Duraci贸n m谩xima en microsguendos de un intervalo
     */
    public static long duracion_intervalo_us_max = 10000000;

    /**
     * Duraci贸n m铆nima en microsguendos de un intervalo
     */
    public final long DURACION_INTERVALO_US_MIN = EstimadorPesq.DURACION_INTERVALO_MIN * 1000;


    /**
     * Crea el gestor de par谩metros
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
     * Recalcula los l铆mites inferior y superior
     */
    public void ajustarParametrosExtremos(){

        this.ajustarParametrosMinimos();
        this.ajustarParametrosMaximos();

    }

    /**
     * Recalcula el l铆mite inferior
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
     * Recalcula el l铆mite superior
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
     * Devuelve la duraci贸n de un intervalo en milisegundos
     */
    public double getDuracionIntervaloEnMS() {

        return this.formatoCodec.duracionEnMicrosegundos(
                this.parametrosTransmision.numeroBytesPorIntervalo()
                )/1000.0;
    }


    /**
     * Devuelve la duraci贸n total en milisegundos
     */
    public double getDuracionTotalEnMS() {

        return this.formatoCodec.duracionEnMicrosegundos(
                this.parametrosTransmision.numeroDeMensajes()*this.parametrosTransmision.getLongitudDatos()
                )/1000.0;
    }


    /**
     * Ajusta la duraci贸n m谩xima de intervalo
     *
     * @param duracionIntervalo_us      duraci贸n en microsegundos
     */
    public static void setDuracionIntervaloMax(long duracionIntervalo_us) {
        
        GestorParametrosAudio.duracion_intervalo_us_max = duracionIntervalo_us;
    }


    /**
     * Ajusta el formato c贸dec, recalculando los l铆mites superior e inferior
     */
    public void setFormatoCodec(FormatoCodec fc) {

        this.formatoCodec = fc;
        this.reiniciarValoresExtremos();
       
    }


    /**
     * @return El formato c贸dec considerado para la transmisi贸n
     */
    public FormatoCodec getFormatoCodec() {

        return this.formatoCodec;
    }



    


    

}

