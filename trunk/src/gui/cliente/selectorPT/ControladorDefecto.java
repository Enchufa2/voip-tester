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

