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

