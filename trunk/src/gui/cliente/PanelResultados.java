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

