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


package gui.cliente.selectorMedidas;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Hashtable;
import gui.GuiManager;
import gui.cliente.*;
/**
 *
 * @author Antonio
 */
public class PanelSeleccionMedidas 
        extends JPanel implements ItemListener {
        //implements ItemListener{

    final private CheckBoxMedida[] checkboxes;
    final private Dimension dimensionPanel;
    final Hashtable<NombreMedida, Boolean> seleccionesMedidas;

    private int numeroSelecciones=0;

    public PanelSeleccionMedidas() {

        super();
        super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        super.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        super.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("QoS measures"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.checkboxes = new CheckBoxMedida[NombreMedida.values().length];
        this.seleccionesMedidas = new Hashtable<NombreMedida, Boolean>();

        int height=0;
        int i;
        for(NombreMedida nm: NombreMedida.values()) {
            i = nm.ordinal();
            this.checkboxes[i] = new CheckBoxMedida(nm);
            this.checkboxes[i].addItemListener(this);
            this.seleccionesMedidas.put(nm, Boolean.FALSE);
            super.add(this.checkboxes[i]);
            height += this.checkboxes[i].getPreferredSize().height + 10;
        }
        int width= this.checkboxes[0].getPreferredSize().width;
        this.dimensionPanel = new Dimension(width, height);
        super.setSize(dimensionPanel);
        super.setMaximumSize(dimensionPanel);
        super.setMinimumSize(dimensionPanel);
        super.setPreferredSize(dimensionPanel);

        //Creación de propiedades
        this.getCheckBoxForMedida(NombreMedida.EMODEL).
                crearPropiedad(2, 20, 10 , "packets", "gmin");
        this.getCheckBoxForMedida(NombreMedida.DIST_PERDIDAS).
                crearPropiedad(2, 100, 50, "packets", "maximum burst");
        
    }


    public void itemStateChanged(ItemEvent e) {

        CheckBoxMedida cbm = (CheckBoxMedida) e.getSource();
        boolean seleccionado = false;
        if(e.getStateChange() == ItemEvent.SELECTED) {

            seleccionado = true;
            this.numeroSelecciones++;


        }else{
            
            this.numeroSelecciones--;
        }

        this.seleccionesMedidas.put(cbm.nombreMedida, seleccionado);

    }


    public String[] getNombreMedidas() {

        String[] nombreMedidas = new String[this.checkboxes.length];

        for (int i=0; i<this.checkboxes.length; i++) {

            nombreMedidas[i] = this.checkboxes[i].nombreMedida.toString();
        }

        return nombreMedidas;
    }

    public boolean medidaSeleccionada (NombreMedida nombreMedida) {

        return this.seleccionesMedidas.get(nombreMedida);
    }

    public boolean existeSeleccion() {

        boolean existeSeleccion = false;

        if(this.numeroSelecciones>0)
            existeSeleccion = true;
        
        return existeSeleccion;
    }

    public CheckBoxMedida getCheckBoxForMedida(NombreMedida nm) {

        for (int i=0; i<this.checkboxes.length; i++) {

            if(this.checkboxes[i].nombreMedida == nm){
                return this.checkboxes[i];
            }
        }

        return null;

    }

    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled);

    }

    }



