/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente.selectorPT;

import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import nucleo.cliente.GestorParametrosTransmision;

/**
 *
 * @author Antonio
 */
public interface ControladorParametro {

    public void ajustarValoresExtremos(SelectorParametro selectorParametro);

    public boolean estaAjustando();

    public ParametrosTransmision parametrosSeleccionados();

    public GestorParametrosTransmision getGPT();
    
}
