/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente;


import javax.swing.JTabbedPane;
import javax.swing.BorderFactory;
import gui.cliente.charts.*;
import java.util.ArrayList;
import qos.estimacionQoS.*;



/**
 *
 * @author Antonio
 */
public class PanelResultados 
        extends JTabbedPane {

    private PanelMedidaQoS[] panelMedidas;

    public PanelResultados() {

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Results"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    }

    public void setMedidasQoS(ArrayList<MedidaQoS> medidasQoS) {

        super.removeAll();


        int numeroMedidas = medidasQoS.size();
        this.panelMedidas = new PanelMedidaQoS[numeroMedidas];

        for (int i=0; i<numeroMedidas; i++) {

            this.panelMedidas[i] = new PanelMedidaQoS(
                    new ChartMedidaQoS(medidasQoS.get(i))
                    );
            super.addTab(medidasQoS.get(i).getParametroQoS(0).getNombreParametro(), this.panelMedidas[i] );
        }
    }

    
}
