
package gui.cliente.selectorPT;

import gui.cliente.*;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import audio.FormatoCodec;
import audio.GestorCodecsAudio;
import gui.GuiManager;
import org.gstreamer.Gst;


/**
 *
 */
public class SelectorParametro extends JPanel implements
        ChangeListener,
        ActionListener {

    //Componentes GUI
    final SliderParametro slider;
    final JFormattedTextField textFieldValor;
    final DecimalFormat numberFormat;
    final NumberFormatter numberFormatter;
    final JLabel labelUnidades;
    final JLabel labelDuracion;
    final GridBagLayout layout = new GridBagLayout();
    final Dimension dimensionSelector = new Dimension(700, 110);
    
    //instancias para el funcionamiento
    ControladorParametro controladorParametro;
    public final NombreParametro nombreParametro;
    //SelectorPT selectorPT;

    public SelectorParametro(
            NombreParametro nombreParametro,
            ControladorParametro controladorParametro,
            Unit unidad) {

        super();
        super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        super.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(nombreParametro.toString()),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        //slider
        this.slider = new SliderParametro();
        this.slider.setUnit(unidad);
 
        //textfield
        this.numberFormat = (DecimalFormat) DecimalFormat.getNumberInstance();
        this.numberFormat.getDecimalFormatSymbols().setDecimalSeparator('.');
        this.numberFormat.setMaximumFractionDigits(3);
        this.numberFormat.setParseIntegerOnly(false);
        this.numberFormat.setGroupingUsed(false);
        this.numberFormatter = new NumberFormatter(this.numberFormat);
        this.numberFormatter.setOverwriteMode(true);
        this.numberFormatter.setAllowsInvalid(true);
        this.textFieldValor = new JFormattedTextField(this.numberFormatter);
        this.textFieldValor.setColumns(10);

        //unidades del selector
        this.labelUnidades = new JLabel(unidad.description);
        
        //etiqueta que muestra la duración equitavelente, si es necesario
        this.labelDuracion = new JLabel();
        
        this.controladorParametro = controladorParametro;
        this.nombreParametro = nombreParametro;
        
        this.addComponentes();

    }


    private void addComponentes() {

        //pnael slider + textfield + label unidades
        JPanel panelSlider = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        panelSlider.setLayout(this.layout);
        panelSlider.add(this.textFieldValor, gbc);
        gbc.gridx = 1;
        panelSlider.add(this.labelUnidades, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.ipadx = 350;
        panelSlider.add(this.slider, gbc);
        this.slider.setMinorTickSpacing(10);

        Dimension dimensionPanelSlider = new Dimension(550,50);
        panelSlider.setSize(dimensionPanelSlider);
        panelSlider.setPreferredSize(dimensionPanelSlider);
        panelSlider.setMinimumSize(dimensionPanelSlider);
        panelSlider.setMaximumSize(dimensionPanelSlider);
        
        //label duración
        JPanel panelDuracion = new JPanel(){
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
            public Dimension getPreferredSize() {
                return new Dimension(100,
                                     50);
            }
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        panelDuracion.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        panelDuracion.add(this.labelDuracion);

        //añadir paneles
        super.add(panelSlider);
        super.add(panelDuracion);

    }


    public void setListeners(){

        this.slider.addChangeListener(this);
        this.textFieldValor.addActionListener(this);
        this.controladorParametro.ajustarValoresExtremos(this);
    }

    
    public void setControladorParametro(ControladorParametro controladorParametro) {

        this.labelDuracion.setText("");
        this.slider.setStep(1);
        this.controladorParametro = controladorParametro;
        this.controladorParametro.ajustarValoresExtremos(this);
    }
    

    public void stateChanged(ChangeEvent changeEvent) {

        JSlider origen = (JSlider) changeEvent.getSource();
        int min = (int) this.slider.getMinimo();
        int max = (int) this.slider.getMaximo();
        NumberFormatter formatter = (NumberFormatter) this.textFieldValor.getFormatter();
        formatter.setMinimum(min);
        formatter.setMaximum(max);
        double value = this.slider.getValor();        
        this.textFieldValor.setValue(value);

        if (! (origen.getValueIsAdjusting() || controladorParametro.estaAjustando()) ) {

            controladorParametro.ajustarValoresExtremos(this);
        }

    }


    public void actionPerformed(ActionEvent e) {
 
        Number value = (Number) this.textFieldValor.getValue();
        this.slider.setValor(value.doubleValue());
        this.textFieldValor.setValue(this.slider.getValor());
        this.slider.requestFocusInWindow();

    }

    public void setTextLabelDuracion(double duracionEnMS) {

        this.labelDuracion.setText(
                String.valueOf(this.numberFormat.format(duracionEnMS) + " ms")
                );
    }

    public SliderParametro getSliderParametro() {

        return this.slider;
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

}
