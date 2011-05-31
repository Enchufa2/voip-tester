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

