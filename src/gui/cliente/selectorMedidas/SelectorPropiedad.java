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


package gui.cliente.selectorMedidas;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


import gui.GuiManager;

/**
 *
 * @author Antonio
 */
public class SelectorPropiedad
        extends JPanel implements ChangeListener, ActionListener{

    //Componentes GUI
    final JSlider slider;
    final JFormattedTextField textFieldValor;
    final NumberFormat numberFormat;
    final NumberFormatter numberFormatter;
    final JLabel labelUnidades;
    final Dimension dimensionSelector = new Dimension(520, 130);

    public SelectorPropiedad(int min, int max) {

        super();

        //slider
        this.slider = new JSlider();
        this.slider.setMinimum(min);
        this.slider.setMaximum(max);
        this.slider.setValue(min);
        this.slider.setMinorTickSpacing(5);
        this.slider.setMajorTickSpacing(5);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);
        this.slider.addChangeListener(this);

        //textfield
        this.numberFormat = NumberFormat.getNumberInstance();
        this.numberFormat.setMaximumFractionDigits(3);
        this.numberFormat.setParseIntegerOnly(true);
        this.numberFormatter = new NumberFormatter(this.numberFormat);
        this.numberFormatter.setOverwriteMode(true);
        this.numberFormatter.setAllowsInvalid(true);
        this.numberFormatter.setMinimum(min);
        this.numberFormatter.setMaximum(max);
        this.textFieldValor = new JFormattedTextField(this.numberFormatter);
        this.textFieldValor.setColumns(5);
        this.textFieldValor.setText(numberFormat.format(this.slider.getValue()));
        this.textFieldValor.addActionListener(this);
       
        //label
        this.labelUnidades = new JLabel("");

        //posicionamiento de los componentes
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.insets.top = 20;
        gbc.insets.right = 20;
        gbc.insets.left = 20;
        super.add(this.textFieldValor, gbc);
        gbc.gridx = 1;
        gbc.insets.left = 0;
        super.add(this.labelUnidades, gbc);
        gbc.gridx = 2;
        gbc.insets.left = 0;
        gbc.anchor = GridBagConstraints.LINE_END;

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets.left = 20;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.ipadx = 220;
        gbc.insets.bottom = 20;
        super.add(this.slider, gbc);        
                
        
    }


    public void stateChanged(ChangeEvent changeEvent) {

        this.textFieldValor.setValue(this.slider.getValue());
        this.slider.requestFocusInWindow();
        
    }

    public void setLabelUnidades(String unidades) {

        this.labelUnidades.setText(unidades);
    }

    public void actionPerformed(ActionEvent e) {

        if( e.getSource() == this.textFieldValor) {
            Number value = (Number) this.textFieldValor.getValue();
            this.slider.setValue(value.intValue());
            this.textFieldValor.setValue(this.slider.getValue());
            this.slider.requestFocusInWindow();
        }
        
    }


    public Dimension getPreferredSize() {

        return this.dimensionSelector;
    }

    public Dimension getMaximumSize() {

        return this.getPreferredSize();

    }

    public Dimension getMinimumSize() {

        return this.getPreferredSize();

    }

    public void setPreferredSize(Dimension newDimension) {

        this.dimensionSelector.setSize(newDimension);
    }

    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled);
    }

}

