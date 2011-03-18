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


package gui.cliente.charts;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.text.DecimalFormat;


/**
 *
 * @author Antonio
 */
public class PanelEstadisticas 
        extends JPanel implements ItemListener{
    
    JComboBox comboIntervalos;
    JLabel labelMax;
    JLabel labelMin;
    JLabel labelMed;
    DecimalFormat decimalFormatter;
    ChartMedidaQoS chartMedidaQoS;

    public PanelEstadisticas( ChartMedidaQoS chartMedidaQoS) {

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("statistics"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.chartMedidaQoS = chartMedidaQoS;
        
        this.labelMax = new JLabel();
        this.labelMin = new JLabel();
        this.labelMed = new JLabel();

        this.decimalFormatter = (DecimalFormat) DecimalFormat.getInstance();
        this.decimalFormatter.setMaximumFractionDigits(3);

        this.comboIntervalos = new JComboBox();
        int numeroIntervalos = this.chartMedidaQoS.medidaQoS.
                getParametrosTransmision().getNumeroIntervalos();
        for(int i=0; i<numeroIntervalos; i++) {

            this.comboIntervalos.addItem(new String("interval " + i));
        }
        
        this.comboIntervalos.addItemListener(this);
        this.comboIntervalos.setSelectedIndex(0);
        this.setTextForLabels(0);

        //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.gridx=0;
        gbc.gridy=0;
        gbc.insets.bottom=20;
        super.add(this.comboIntervalos, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridy++;
        gbc.insets.bottom=10;
        super.add(this.labelMax,gbc);

        gbc.gridy++;
        super.add(this.labelMin,gbc);

        gbc.gridy++;
        super.add(this.labelMed,gbc);        

    }


    private void setTextForLabels(int index) {

        this.labelMax.setText("max:  " + this.decimalFormatter.format(
                    this.chartMedidaQoS.maximoDeIntervalos[index])
                    + " "
                    + this.chartMedidaQoS.medidaQoS.unidadesEjeYtoString()
                    );

            this.labelMin.setText("min:  " + this.decimalFormatter.format(
                    this.chartMedidaQoS.minimoDeIntervalos[index])
                    + " "
                    + this.chartMedidaQoS.medidaQoS.unidadesEjeYtoString()
                    );

            this.labelMed.setText("mean:  " + this.decimalFormatter.format(
                    this.chartMedidaQoS.mediaDeIntervalos[index])
                    + " "
                    + this.chartMedidaQoS.medidaQoS.unidadesEjeYtoString()
                    );

    }

    public void itemStateChanged(ItemEvent itemEvent) {

        if(itemEvent.getSource() == this.comboIntervalos) {

            this.setTextForLabels(this.comboIntervalos.getSelectedIndex());            
        }
    }

}

