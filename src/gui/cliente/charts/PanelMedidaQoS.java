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


package gui.cliente.charts;

import javax.swing.BorderFactory;
import info.monitorenter.gui.chart.views.ChartPanel;
import info.monitorenter.gui.chart.controls.LayoutFactory;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import gui.GuiManager;
import java.io.IOException;


/**
 *
 * @author Antonio
 */
public class PanelMedidaQoS 
        extends JPanel implements ActionListener{

    private ChartPanel chartPanel;

    private PanelCondiciones panelCondiciones;

    private PanelEstadisticas panelEstadisticas;

    private JButton botonExportar;

    private JFileChooser fileChooser;

    public PanelMedidaQoS(ChartMedidaQoS cmq) {

        super();

        //configuraciÃ³n opciones popup menu chart
        LayoutFactory factory = LayoutFactory.getInstance();
        factory.setShowAxisXRangePolicyMenu(false);
        factory.setShowAxisYRangePolicyMenu(false);
        factory.setShowAxisXTitleMenu(false);
        factory.setShowAxisYTitleMenu(false);
        factory.setShowHighlightMenu(false);
        factory.setShowRemoveTraceMenu(false);
        factory.setShowPhysicalUnitsMenu(false);
        factory.setShowSaveEpsMenu(false);
        factory.setShowTraceNameMenu(false);
        factory.setShowRemoveTraceMenu(false);
        factory.setShowErrorBarWizardMenu(false);
        factory.setShowAnnotationMenu(false);       
        this.chartPanel = new ChartPanel(cmq);
               
        this.panelCondiciones = new PanelCondiciones();
        this.panelCondiciones.textAreaCondiciones.append(
                cmq.medidaQoS.condicionesDeMedidaToString()
                );

        //estadÃ­sticas
        this.panelEstadisticas = new PanelEstadisticas(cmq);

        //botÃ³n exportar
        this.botonExportar = new JButton();
        this.botonExportar.addActionListener(this);
        this.botonExportar.setBorder(null);
        this.botonExportar.setIconTextGap(0);
        this.botonExportar.setOpaque(false);
        this.botonExportar.setBackground(Color.red);
        this.botonExportar.setIcon(GuiManager.createImageIcon("but_export.png"));
        this.botonExportar.setRolloverIcon(GuiManager.createImageIcon("but_export_roll.png"));
        this.botonExportar.setToolTipText("export to txt");

        //file chooser
        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.fileChooser.setMultiSelectionEnabled(false);

        //layout de los componentes
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);
        
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.insets.bottom = 30;
        gbc.insets.top = 30;              
        gbc.gridx++;       
        super.add(this.panelCondiciones, gbc);

        gbc.gridy++;
        gbc.insets.bottom = 0;
        gbc.insets.top = 0;
        super.add(this.panelEstadisticas, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets.top = 20;
        gbc.insets.bottom = 20;
        super.add(this.botonExportar, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridheight = 3;
        gbc.weightx = 1;
        gbc.weighty =1;
        gbc.insets.top = 0;
        gbc.insets.bottom = 0;
        super.add(this.chartPanel, gbc);

    }


    public void actionPerformed(ActionEvent actionEvent) {

        int returnVal = Integer.MIN_VALUE;

        if(actionEvent.getSource() == this.botonExportar) {

            returnVal = this.fileChooser.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {

                ChartMedidaQoS cmq = (ChartMedidaQoS) this.chartPanel.getChart();
                try{

                    cmq.exportarAtxt(this.fileChooser.getSelectedFile());

                }catch(IOException ioe) {

                    JOptionPane.showMessageDialog(this, "Couldn't create .txt files" + '\n' + ioe.getMessage()
                            , "Operation failed", JOptionPane.ERROR_MESSAGE);

                }
            }
        }
        
    }
   
}

