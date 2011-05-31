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


package gui.servidor;

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
public class SelectorTiempoExpiracion 
        extends JPanel implements ChangeListener, ActionListener{

    //Componentes GUI
    final JSlider slider;
    final JFormattedTextField textFieldValor;
    final NumberFormat numberFormat;
    final NumberFormatter numberFormatter;
    final JLabel labelUnidades;
    final JButton botonAjustar;

    final Dimension dimensionSelector = new Dimension(520, 130);

    private static final int TIEMPO_MIN = 10;
    private static final int TIEMPO_MAX = 600;

    public SelectorTiempoExpiracion() {

        super();

        
        //textfield
        this.numberFormat = NumberFormat.getNumberInstance();
        this.numberFormat.setMaximumFractionDigits(3);
        this.numberFormat.setParseIntegerOnly(true);
        this.numberFormatter = new NumberFormatter(this.numberFormat);
        this.numberFormatter.setOverwriteMode(true);
        this.numberFormatter.setAllowsInvalid(true);
        this.numberFormatter.setMinimum(TIEMPO_MIN);
        this.numberFormatter.setMaximum(TIEMPO_MAX);
        this.textFieldValor = new JFormattedTextField(this.numberFormatter);
        this.textFieldValor.setColumns(5);
        this.textFieldValor.setText(String.valueOf(60));
        this.textFieldValor.addActionListener(this);

        //slider
        this.slider = new JSlider();
        this.slider.setMinimum(TIEMPO_MIN);
        this.slider.setMaximum(TIEMPO_MAX);
        this.slider.setMinorTickSpacing(10);
        this.slider.setMajorTickSpacing(60);
        this.slider.setPaintTicks(true);
        this.slider.addChangeListener(this);

        //label
        this.labelUnidades = new JLabel("seconds");

        //button
        this.botonAjustar = new JButton("Set");


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
        super.add(this.botonAjustar, gbc);
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

        if(changeEvent.getSource() == this.slider)
            this.textFieldValor.setValue(this.slider.getValue());

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

