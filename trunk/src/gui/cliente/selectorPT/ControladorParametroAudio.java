/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
