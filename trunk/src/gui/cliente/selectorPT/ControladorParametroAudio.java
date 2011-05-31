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


package gui.cliente.selectorPT;

import gui.cliente.*;
import nucleo.cliente.GestorParametrosAudio;
import audio.FormatoCodec;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import java.util.Hashtable;

/**
 *
 * @author Antonio
 */
public class ControladorParametroAudio implements ControladorParametro{

    GestorParametrosAudio gpa;

    boolean estaAjustando = false;

    Hashtable<NombreParametro, SelectorParametro> selectores;


    public ControladorParametroAudio( FormatoCodec fc) {

        this.gpa = new GestorParametrosAudio(fc);
        this.selectores = new Hashtable<NombreParametro, SelectorParametro>();

    }

    public void ajustarValoresExtremos(SelectorParametro selectorParametro){

        this.estaAjustando = true;
        FormatoCodec fc = this.gpa.getFormatoCodec();
        ParametrosTransmision pTmin = this.gpa.getParametrosTransmisionMin();
        ParametrosTransmision pTmax = this.gpa.getParametrosTransmisionMax();
        SliderParametro sliderParametro = selectorParametro.getSliderParametro();
        this.addSelectorParametro(selectorParametro);

        switch(selectorParametro.nombreParametro) {

            case TIEMPO_ENTRE_ENVIOS:

                sliderParametro.setStep(fc.duracionTramasEnMicrosegundos(fc.longitudTrama())/sliderParametro.unit.multiplier);
                
                sliderParametro.setValoresExtremos(pTmin.getTiempoEntreEnvios(), pTmax.getTiempoEntreEnvios());
                this.gpa.setTiempoEntreEnvios(
                        (int) Math.ceil(sliderParametro.getMultipliedValue())
                        );
                
                if(this.selectores.containsKey(NombreParametro.LONGITUD_MENSAJE)) {
                
                    SelectorParametro nextSelector = this.selectores.get(
                            NombreParametro.LONGITUD_MENSAJE
                            );
                    nextSelector.getSliderParametro().
                            setMultipliedValor(this.gpa.getParametrosTransmision().getLongitudDatos());
                    
                }
                
                if(this.selectores.containsKey(NombreParametro.MENSAJES_POR_INTERVALO)) {
                
                    SelectorParametro nextSelector = this.selectores.get(
                            NombreParametro.MENSAJES_POR_INTERVALO
                            );
                    nextSelector.getSliderParametro().
                            setValoresExtremos(pTmin.getMensajesPorIntervalo(), pTmax.getMensajesPorIntervalo());
                    nextSelector.setTextLabelDuracion(this.gpa.getDuracionIntervaloEnMS());
                }

                if(this.selectores.containsKey(NombreParametro.NUMERO_INTERVALOS)) {

                    SelectorParametro nextSelector = this.selectores.get(
                            NombreParametro.NUMERO_INTERVALOS
                            );
                    nextSelector.setTextLabelDuracion(this.gpa.getDuracionTotalEnMS());
                }
                break;
            
            case LONGITUD_MENSAJE:

                sliderParametro.setStep((double) (fc.longitudTrama())/sliderParametro.unit.multiplier);
                sliderParametro.setValoresExtremos(pTmin.getLongitudDatos(), pTmax.getLongitudDatos());
                this.gpa.setLongitudMensaje(
                        (int) Math.ceil(sliderParametro.getMultipliedValue())
                        );

                if(this.selectores.containsKey(NombreParametro.TIEMPO_ENTRE_ENVIOS)) {

                    SelectorParametro nextSelector = this.selectores.get(
                            NombreParametro.TIEMPO_ENTRE_ENVIOS
                            );
                    nextSelector.getSliderParametro().
                            setMultipliedValor(this.gpa.getParametrosTransmision().getTiempoEntreEnvios());

                }
                
                if(this.selectores.containsKey(NombreParametro.MENSAJES_POR_INTERVALO)) {

                    SelectorParametro nextSelector = this.selectores.get(
                            NombreParametro.MENSAJES_POR_INTERVALO
                            );
                    nextSelector.getSliderParametro().
                            setValoresExtremos(pTmin.getMensajesPorIntervalo(), pTmax.getMensajesPorIntervalo());
                    nextSelector.setTextLabelDuracion(this.gpa.getDuracionIntervaloEnMS());
                }

                if(this.selectores.containsKey(NombreParametro.NUMERO_INTERVALOS)) {

                    SelectorParametro nextSelector = this.selectores.get(
                            NombreParametro.NUMERO_INTERVALOS
                            );
                    nextSelector.setTextLabelDuracion(this.gpa.getDuracionTotalEnMS());
                }
                
                break;

            case MENSAJES_POR_INTERVALO:

                
                sliderParametro.setValoresExtremos(pTmin.getMensajesPorIntervalo(), pTmax.getMensajesPorIntervalo());
                this.gpa.setMensajesPorIntervalo(
                        (int) Math.ceil(sliderParametro.getMultipliedValue())
                        );
                selectorParametro.setTextLabelDuracion(this.gpa.getDuracionIntervaloEnMS());
                
                if(this.selectores.containsKey(NombreParametro.NUMERO_INTERVALOS)) {

                    SelectorParametro nextSelector = this.selectores.get(
                            NombreParametro.NUMERO_INTERVALOS
                            );
                    nextSelector.setTextLabelDuracion(this.gpa.getDuracionTotalEnMS());
                }
                
                break;

            case NUMERO_INTERVALOS:
                
                sliderParametro.setValoresExtremos(pTmin.getNumeroIntervalos(), pTmax.getNumeroIntervalos());
                this.gpa.setNumeroIntervalos(
                        (int) Math.ceil(sliderParametro.getMultipliedValue())
                        );
                selectorParametro.setTextLabelDuracion(this.gpa.getDuracionTotalEnMS());
                break;
        }

        this.estaAjustando = false;
    }

    public boolean estaAjustando() {

        return this.estaAjustando;
    }


    public void setDuracionIntervaloMax(long duracion_us) {

        GestorParametrosAudio.setDuracionIntervaloMax(duracion_us);
        this.ajustarValoresExtremos(this.selectores.get(NombreParametro.MENSAJES_POR_INTERVALO));
    }

    public ParametrosTransmision parametrosSeleccionados() {

        return this.gpa.getParametrosTransmision();
    }

    public GestorParametrosAudio getGPT() {

        return this.gpa;
    }

    private void addSelectorParametro(SelectorParametro selectorParametro) {

        NombreParametro parametro = selectorParametro.nombreParametro;
        if(!this.selectores.contains(parametro))
            this.selectores.put(parametro, selectorParametro);
    }


}

