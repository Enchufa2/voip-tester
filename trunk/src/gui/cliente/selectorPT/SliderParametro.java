/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente.selectorPT;

import javax.swing.JSlider;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.util.Hashtable;
import java.text.DecimalFormat;
//import java.text.

/**
 *
 * @author Antonio
 */
public class SliderParametro  extends JSlider{
    

    Unit unit = new Unit("",1);

    private final SelectorPTRangeModel sliderModel =  new SelectorPTRangeModel();

    private Hashtable<Integer, JLabel> labels;

    private Integer[] keys = new Integer[2];

    JLabel labelMin;
    JLabel labelMax;

    DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getNumberInstance();

    public SliderParametro() {

        super();
        super.setModel(this.sliderModel);
        //this.nombreParametro = parametro;
        this.labels = new Hashtable<Integer, JLabel>(2);
        this.labelMin = new JLabel();
        this.labelMin.setPreferredSize(new Dimension(90,10));
        this.labelMax = new JLabel();
        this.labelMax.setPreferredSize(new Dimension(90,10));
        this.keys[0] = super.getMinimum();
        this.keys[1] = super.getMaximum();
        this.labels.put(
                this.keys[0],
                this.labelMin
                );
        this.labels.put(
                this.keys[1],
                this.labelMax
                );
        this.numberFormat.setMaximumFractionDigits(3);
        this.numberFormat.setGroupingUsed(false);
        super.setLabelTable(this.labels);
        super.setPaintLabels(true);

    }


   public void setMaximo(double newMax) {

       this.sliderModel.setMaximoReal(newMax);
       this.labels.remove(this.keys[1]);
       this.keys[1] = this.sliderModel.getMaximum();       
       this.labelMax.setText(this.numberFormat.format(this.getMaximo()));
       this.labels.put(this.keys[1], labelMax);

   }

   public void setMinimo(double newMin) {

       
       this.sliderModel.setMinimoReal(newMin);
       this.labels.remove(this.keys[0]);
       this.keys[0] = this.sliderModel.getMinimum();
       this.labelMin.setText(this.numberFormat.format(this.getMinimo()));
       this.labels.put(this.keys[0], labelMin);

   }


   public void setStep(double newStep) {

       this.sliderModel.setStep(newStep);
   }


   public double getMaximo() {

       return this.sliderModel.getMaximoReal();
   }


   public double getMinimo() {

       return this.sliderModel.getMinimoReal();
   }
  

   public double getValor() {

       return this.sliderModel.getValorReal();
   }

   public double getMultipliedValue() {

       return (this.getValor() * this.unit.multiplier);
   }

   public double getMultipliedMaximo() {

       return (this.getMaximo() * this.unit.multiplier);
   }

   public double getMultipliedMinimo() {

       return (this.getMinimo() * this.unit.multiplier);
   }


   public void setMultipliedMaximo(double value) {

       this.setMaximo(value/this.unit.multiplier);
   }

   public void setMultipliedMinimo(double value) {

       this.setMinimo(value/this.unit.multiplier);
   }

   
   public void setValor(double value) {

       this.sliderModel.setValorReal(value);
   }

   public void setMultipliedValor(double value) {

       this.setValor(value/this.unit.multiplier);
   }

   public void setUnit(Unit unit) {

       this.unit = unit;
       this.sliderModel.fireStateChanged();
       
   }

   public void resetToUnit() {

       this.setValoresExtremos(this.unit.min, this.unit.min);
   }
   

   public void setValoresExtremos(int min, int max) {

       this.setMultipliedMaximo(max);
       this.setMultipliedMinimo(min);
       
    }


}
