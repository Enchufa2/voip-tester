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


package gui.cliente.selectorPT;

import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.ChangeListener;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;


/**
 *
 * @author Antonio
 */
public class SelectorPTRangeModel implements BoundedRangeModel {


    protected ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();

    protected int maximum = 1000;
    protected int minimum = 1;
    protected int extent = 0;
    protected int value = 0;
    protected double step = 1;
    protected boolean isAdjusting = false;

    public SelectorPTRangeModel() {
    }

    public double getStep() {
        return step;
    }

    public void setStep(double newStep) {

        boolean change=false;


        if(newStep !=step){
        //deshacer el cambio
         int lastmin = (int) Math.ceil(minimum*step);
         int lastmax = (int) Math.ceil(maximum*step);
         if (lastmax-lastmin  >= newStep){

            //ajuste de la nueva escala
            int newMin = (int) Math.ceil(lastmin/newStep);
            int newMax = (int) (lastmax/newStep);

            if(newMax*newStep != lastmax
                    && newMin*newStep != lastmin)
                change = true;

            maximum = newMax;
            minimum = newMin;
            
        }
         
            step = newStep;
        }
        

    }


    public double getMaximoReal() {

        return maximum * step;
    }


    public void setMaximoReal(double newMaximo) {

        setRangeProperties(value, extent, minimum, (int) (newMaximo/step) , isAdjusting);

    }


    public double getMinimoReal() {

        return minimum * step;
    }


    public void setMinimoReal(double newMinimo) {


        setRangeProperties(value, extent, (int) Math.ceil(newMinimo/step), maximum, isAdjusting);
    }
  

    public double getValorReal() {

        return value * step;
    }


    public void setValorReal(double valor) {

        setRangeProperties((int) Math.round(valor/step), extent, minimum, maximum, isAdjusting);
    }


     public int getMaximum() {

        return maximum;
        
    }

    public void setMaximum(int newMaximum) {
        setRangeProperties(value, extent, minimum, newMaximum, isAdjusting);
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int newMinimum) {
        setRangeProperties(value, extent, newMinimum, maximum, isAdjusting);

    }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        setRangeProperties(newValue, extent, minimum, maximum, isAdjusting);
    }

    public double getDoubleValue() {
        return value;
    }


    public int getExtent() {
        return (int)extent;
    }

    public void setExtent(int newExtent) {

    }


    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    public void setValueIsAdjusting(boolean b) {
        setRangeProperties(value, extent, minimum, maximum, b);
    }

   
   public void setRangeProperties(int newValue,
                                   int newExtent,
                                   int newMin,
                                   int newMax,
                                   boolean newAdjusting) {

        if (newMax <= minimum*step) {
            //newMax = minimum + 1;//(int) Math.ceil(1/step);
        }

        if (Math.round(newValue) > newMax) { 
            newValue = newMax;
        }
        if (newMin >= maximum*step) {
            //newMin = maximum - 1;//(int) Math.ceil(1/step);
        }

        if (Math.round(newValue) < newMin ) {

            newValue = newMin;
        }

        boolean changeOccurred = false;
        if (newValue != value) {
            value = newValue;
            changeOccurred = true;
        }
        if (newMax != maximum) {
            maximum = newMax;
            changeOccurred = true;
        }
        if (newMin != minimum) {

            minimum = newMin;
            changeOccurred = true;
        }

        if (newAdjusting != isAdjusting) {
            maximum = newMax;
            isAdjusting = newAdjusting;
            changeOccurred = true;
        }

        if (changeOccurred) {
            fireStateChanged();
        }
        
    }


    /*
     * The rest of this is event handling code copied from
     * DefaultBoundedRangeModel.
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -=2 ) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }


}

