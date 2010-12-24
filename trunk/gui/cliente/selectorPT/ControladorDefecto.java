/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente.selectorPT;

import nucleo.cliente.GestorParametrosTransmision;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;

/**
 *
 * @author Antonio
 */
public class ControladorDefecto implements ControladorParametro{

    private GestorParametrosTransmision gpt;

    private boolean estaAjustando = false;
    
    public ControladorDefecto() {
        
        this.gpt = new GestorParametrosTransmision();
    }


    public void ajustarValoresExtremos(SelectorParametro selectorParametro){

        this.estaAjustando = true;

        ParametrosTransmision pTmax = this.gpt.getParametrosTransmisionMax();
        ParametrosTransmision pTmin = this.gpt.getParametrosTransmisionMin();
        SliderParametro sliderParametro = selectorParametro.getSliderParametro();
        
        switch(selectorParametro.nombreParametro) {
            
            case TIEMPO_ENTRE_ENVIOS:

                sliderParametro.setValoresExtremos(pTmin.getTiempoEntreEnvios(), pTmax.getTiempoEntreEnvios());
                this.gpt.setTiempoEntreEnvios((int) sliderParametro.getMultipliedValue());
                break;

            case LONGITUD_MENSAJE:

                sliderParametro.setValoresExtremos(pTmin.getLongitudDatos(), pTmax.getLongitudDatos());
                this.gpt.setLongitudMensaje((int) sliderParametro.getMultipliedValue());
                break;

            case MENSAJES_POR_INTERVALO:
                
                sliderParametro.setValoresExtremos(pTmin.getMensajesPorIntervalo(), pTmax.getMensajesPorIntervalo());
                this.gpt.setMensajesPorIntervalo((int) sliderParametro.getMultipliedValue());

                break;

            case NUMERO_INTERVALOS:
                
                sliderParametro.setValoresExtremos(pTmin.getNumeroIntervalos(), pTmax.getNumeroIntervalos());
                this.gpt.setNumeroIntervalos((int) sliderParametro.getMultipliedValue());
                break;
        }

        this.estaAjustando = false;
        
    }


    public boolean estaAjustando(){

        return this.estaAjustando;
    }

    public ParametrosTransmision parametrosSeleccionados(){

        return this.gpt.getParametrosTransmision();
    }

    public GestorParametrosTransmision getGPT() {

        return this.gpt;
    }

}
