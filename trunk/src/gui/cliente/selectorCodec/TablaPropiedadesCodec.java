/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente.selectorCodec;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.util.ArrayList;

import audio.gst.FormatoCodecGst;
import audio.gst.FormatoCodecGst.PropiedadesCodificador;
import java.awt.Color;

/**
 *
 * @author Antonio
 */
public class TablaPropiedadesCodec 
        extends JPanel implements ItemListener{


    private FormatoCodecGst formatoCodec;

    private final GridBagLayout gridLayout = new GridBagLayout();

    final Dimension dimensionPanel;

    private final Border bordeEtiquetas = BorderFactory.createLineBorder(Color.BLACK);

    private final Color colorEtiquetasProp = Color.lightGray;

    private String[] nombreColumnas = new String[]{
        "Prperties" ,
        "Property value",
        "Description",
        "Description value",
        "Sample rate (source)",
        "Channels (source)"
        };

    private final JLabel[] labelColumnas;

    private ArrayList<JLabel> columPropiedades;

    private ArrayList<JComboBox> columValoresProp;

    private ArrayList<JLabel> columDescripcion;

    private ArrayList<JLabel> columValoresDesc;

    private final JComboBox comboRateFuente;

    private final JComboBox comboCanalesFuente;

    private final JTextArea textAreaCodec;



    public TablaPropiedadesCodec(FormatoCodecGst fcg){

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Codec properties"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        super.setLayout(this.gridLayout);
        super.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        this.labelColumnas = new JLabel[this.nombreColumnas.length];
        //Dimension dimensionLabel = new Dimension(120,50);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 45;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0,0,0,0);
        //gbc.insets = new Insets(0,10,0,10);
        for (int i=0; i<this.nombreColumnas.length; i++) {

            this.labelColumnas[i] = new JLabel(this.nombreColumnas[i]);
            this.labelColumnas[i].setHorizontalAlignment(JLabel.CENTER);
            this.labelColumnas[i].setOpaque(true);
            this.labelColumnas[i].setBackground(Color.WHITE);
            this.labelColumnas[i].setBorder(this.bordeEtiquetas);

            if ( i<4){
                gbc.gridx = i;
                
            }else{
                gbc.ipadx = 10;
                gbc.gridy = 20;
                gbc.gridx = i - 4;
                gbc.insets.top = 40;
            }

            super.add(this.labelColumnas[i], gbc);
        }
        
        this.formatoCodec = fcg;
        
         //Elementos del audio fuente
        //combos de propiedades del audio fuente
        this.comboRateFuente = new JComboBox();
        this.comboRateFuente.addItem(8000);
        this.comboRateFuente.addItem(16000);
        this.comboRateFuente.setSelectedIndex(0);
        this.formatoCodec.getFormatoRawAudio().setMuestrasPorSegundo(
                (Integer) this.comboRateFuente.getItemAt(0)
                );

        this.comboCanalesFuente = new JComboBox();
        this.comboCanalesFuente.addItem(1);
        this.comboCanalesFuente.addItem(2);
        this.comboCanalesFuente.setSelectedIndex(0);
        this.formatoCodec.getFormatoRawAudio().setNumeroDeCanales(
                (Integer) this.comboCanalesFuente.getItemAt(0)
                );

        gbc.insets.top =0;
        gbc.gridy = gbc.gridy + 1;
        gbc.ipadx = 0;
        gbc.gridx = 0;
        super.add(this.comboRateFuente,gbc);
        this.comboRateFuente.addItemListener(this);
        gbc.gridx = 1;
        super.add(this.comboCanalesFuente, gbc);
        this.comboCanalesFuente.addItemListener(this);


        this.textAreaCodec = new JTextArea();
        this.textAreaCodec.setEditable(false);
        this.textAreaCodec.setText(this.formatoCodec.toString());
        gbc.insets.top = 10;
        gbc.insets.left = 10;
        gbc.gridx = 2;
        gbc.gridy = gbc.gridy - 1;
        gbc.gridheight = 4;
        gbc.gridwidth = 3;
        super.add(this.textAreaCodec, gbc);

        //etiquetas propiedades, valores, descripción y valores descripcion
        this.columPropiedades = new ArrayList<JLabel>();
        this.columValoresProp = new ArrayList<JComboBox>();
        this.columDescripcion = new ArrayList<JLabel>();
        this.columValoresDesc = new ArrayList<JLabel>();

        
        this.rellenarTabla();

        //Ajustar dimension del panel
        int height = 270;
        int numeroPropiedades = this.formatoCodec.getPropiedadesCodec().getNumeroPropiedades();
        for (int i=0; i<numeroPropiedades; i++)
            height += (i + 1) * 20;

        this.dimensionPanel = new Dimension(700, height);

    }

    public void rellenarTabla(){

        PropiedadesCodificador pc = this.formatoCodec.getPropiedadesCodec();
        JLabel propiedad;
        JComboBox valorPropiedad;
        JLabel descripcion;
        JLabel valorDescripcion;

        String[] nombrePropiedades = pc.getNombrePropiedades();
        Object[] valoresPropiedad;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        for (int i=0; i<nombrePropiedades.length; i++) {

            //nombre propiedades
            propiedad = new JLabel(nombrePropiedades[i], JLabel.CENTER);
            propiedad.setBorder(this.bordeEtiquetas);
            propiedad.setBackground(this.colorEtiquetasProp);
            propiedad.setOpaque(true);
            propiedad.setSize(this.labelColumnas[0].getSize());
            propiedad.setSize(new Dimension(100,50));
            this.columPropiedades.add(i,propiedad);
            gbc.gridy = i +1;
            gbc.gridx = 0;
            super.add(propiedad, gbc);

            //valoresPropiedad
            valoresPropiedad = pc.getValoresPropiedad(nombrePropiedades[i]);
            valorPropiedad = new JComboBox();
            this.columValoresProp.add(i,valorPropiedad);
            for (int j=0; j<valoresPropiedad.length; j++) {

                valorPropiedad.addItem(valoresPropiedad[j]);

            }
            valorPropiedad.setSelectedIndex(0);
            pc.seleccionarValorPropiedad(nombrePropiedades[i], 0);
            gbc.gridx = 1;
            super.add(valorPropiedad, gbc);
            valorPropiedad.addItemListener(this);
            
            //nombre descripcion
            descripcion = new JLabel("-", JLabel.CENTER);
            descripcion.setBackground(this.colorEtiquetasProp);
            descripcion.setOpaque(true);
            descripcion.setBorder(this.bordeEtiquetas);
            this.columDescripcion.add(i, descripcion);
            valorDescripcion = new JLabel("-", JLabel.CENTER);
            valorDescripcion.setBackground(this.colorEtiquetasProp);
            valorDescripcion.setOpaque(true);
            valorDescripcion.setBorder(this.bordeEtiquetas);
            this.columValoresDesc.add(i, valorDescripcion);

            if (pc.tieneDescripcion(nombrePropiedades[i])) {

                descripcion.setText(pc.getDescripcionPropiedad(nombrePropiedades[i]));
                this.setLabelsDescripcion(i, 0);

            }

            gbc.gridx = 2;
            super.add(descripcion, gbc);
            gbc.gridx = 3;
            super.add(valorDescripcion,gbc);
            
        }

    }


    public void setLabelsDescripcion(int indexLabel, int indexPropiedad) {

        PropiedadesCodificador pc = this.formatoCodec.getPropiedadesCodec();
        String propiedadSeleccionada = this.columPropiedades.get(indexLabel).getText();

        Object[] valoresDescripcion = pc.getValoresDescripcion(propiedadSeleccionada);
        if (pc.tieneValoresDescripcion(propiedadSeleccionada)) {
            if (valoresDescripcion.length > indexPropiedad && valoresDescripcion[indexPropiedad] != null) {

                this.columValoresDesc.get(indexLabel).setText(
                        valoresDescripcion[indexPropiedad].toString());
            }
        }
        

    }

    public void itemStateChanged(ItemEvent itemEvent) {

        JComboBox source = (JComboBox) itemEvent.getSource();

        //combos propiedades audio fuente
        if (source == this.comboRateFuente) {

            this.formatoCodec.getFormatoRawAudio().setMuestrasPorSegundo(
                    (Integer) source.getItemAt(source.getSelectedIndex())
                    );

        }else if(source == this.comboCanalesFuente) {

             this.formatoCodec.getFormatoRawAudio().setNumeroDeCanales(
                    (Integer) source.getItemAt(source.getSelectedIndex())
                    );

        }else{
            //combos propiedades del codificador
            PropiedadesCodificador propiedadesCodificador;
            String propiedadSeleccionada;
            int numeroCombosProp = this.columValoresProp.size();
            for (int i = 0; i < numeroCombosProp; i++) {

                if (source == this.columValoresProp.get(i)) {
                    propiedadesCodificador = this.formatoCodec.getPropiedadesCodec();
                    propiedadSeleccionada = this.columPropiedades.get(i).getText();
                    propiedadesCodificador.seleccionarValorPropiedad(
                            propiedadSeleccionada,
                            source.getSelectedIndex());

                    if (propiedadesCodificador.tieneDescripcion(propiedadSeleccionada)) {
                        this.setLabelsDescripcion(i, source.getSelectedIndex());
                    }

                    break;
                }
            }
        }

        this.textAreaCodec.setText(this.formatoCodec.toString());
        
    }




    public Dimension getPreferredSize() {


        return this.dimensionPanel;
    }

    public Dimension getMaximumSize() {

        return this.getPreferredSize();
    }

    public Dimension getMinimumSize() {

        return this.getPreferredSize();
    }


}
