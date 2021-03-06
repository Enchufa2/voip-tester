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


package gui.cliente.selectorPT;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import gui.GuiManager;
import audio.GestorCodecsAudio;
import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import gui.cliente.*;


/**
 *
 * @author Antonio
 */
public class PanelSelectores
        extends JPanel  {

    // un selector para cada parámetro de transmisión
    final SelectorParametro[] selectores = new SelectorParametro[4];
    // Un controlador para todos los selectores, de un tipo o de otro
    private ControladorParametro controladorParametro;

    //un botón para aceptar los parámetros seleccionados
    //JButton botonAceptar;
    final Dimension dimensionPanel;

    //Layout de los selectores
    final GridBagLayout layout =  new GridBagLayout();

    public PanelSelectores() {

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Transmission parameters"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        this.controladorParametro = new ControladorDefecto();

        super.setLayout(this.layout);
        this.selectores[0] = new SelectorParametro(
                NombreParametro.TIEMPO_ENTRE_ENVIOS,
                this.controladorParametro,
                new Unit("microseconds", 1)
                );

        this.selectores[1] = new SelectorParametro(
                NombreParametro.LONGITUD_MENSAJE,
                this.controladorParametro,
                new Unit("bytes", 1)
                );
        this.selectores[2] = new SelectorParametro(
                NombreParametro.MENSAJES_POR_INTERVALO,
                this.controladorParametro,
                new Unit("messages", 1)
                );

        this.selectores[3] = new SelectorParametro(
                NombreParametro.NUMERO_INTERVALOS,
                this.controladorParametro,
                new Unit("intervals", 1)
                );


        int width = (int) this.selectores[0].getPreferredSize().getWidth() + 80;
        int height=0;

        for (int i=0; i< this.selectores.length; i++) {
            height += this.selectores[i].getPreferredSize().getHeight() +40;
      
        }
        this.dimensionPanel = new Dimension(width, height);
        
        for (int i =0; i<this.selectores.length; i++)
            this.selectores[i].setListeners();  

        this.addComponentes();
        

    }

    void addComponentes() {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20,50,10,50);

        //selector tEE
        for (int i=0; i<this.selectores.length; i++) {
            gbc.gridy += i;
            if (i>0)
                gbc.insets.top = 10;
            if(i>2)
                gbc.insets.bottom = 20;
            
            super.add(this.selectores[i], gbc);
        }
        
    }

    public void setControladorParametro(ControladorParametro cp) {

        for (int i=0; i< this.selectores.length; i++) {
            this.selectores[i].setControladorParametro(cp);
        }
        this.controladorParametro = cp;
    }

    public ControladorParametro getControladorParametro() {

        return this.controladorParametro;
    }

 

    public ParametrosTransmision getParametrosTransmisionSeleccionados() {
        
        return this.controladorParametro.parametrosSeleccionados();
    }


    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled, true);
    }

    public Dimension getPreferredSize() {


        return new Dimension(this.dimensionPanel);
    }

    public Dimension getMaximumSize() {

        return this.getPreferredSize();
    }

    public Dimension getMinimumSize() {

        return this.getPreferredSize();
    }

}

 

