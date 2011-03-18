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


package gui.selectorDirSocket;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import gui.GuiManager;

/**
 *
 * @author Antonio
 */
public class CampoIP extends JPanel 
        implements PropertyChangeListener{


    //componentes GUI
    final private JFormattedTextField[] fieldsIP = new JFormattedTextField[4];
    final private JFormattedTextField fieldPuerto;
    final private JLabel labelSocket = new JLabel();
    final private Dimension dimensionSelector = new Dimension(430, 150);

    //elementos no visuales
    final private byte[] bytesIP = new byte[4];

    private int puerto;

    final private int FIRST_WELL_KNOWN_PORT=1;
    final private int LAST_REGISTERED_PORT=65535;


    public CampoIP() {

        super();

        //Campo direccion IP
        NumberFormat numberFormatIP = NumberFormat.getNumberInstance();
        numberFormatIP.setParseIntegerOnly(true);
        NumberFormatter numberFormatterIP = new NumberFormatter(numberFormatIP);
        numberFormatterIP.setOverwriteMode(true);
        numberFormatterIP.setAllowsInvalid(true);
        numberFormatterIP.setCommitsOnValidEdit(true);
        numberFormatterIP.setMinimum(0);
        numberFormatterIP.setMaximum(255);


        for (int i=0; i<4; i++) {

            this.fieldsIP[i] = new JFormattedTextField(numberFormatterIP);
            this.fieldsIP[i].setValue(1);
            this.fieldsIP[i].setColumns(5);
            this.fieldsIP[i].addPropertyChangeListener("value", this);
            this.bytesIP[i] = ( (Integer) this.fieldsIP[i].getValue() ).byteValue();
        }


        //Campo puerto
        NumberFormat numberFormatPuerto = NumberFormat.getNumberInstance();
        numberFormatPuerto.setParseIntegerOnly(true);
        NumberFormatter numberFormatterPuerto = new NumberFormatter(numberFormatPuerto);
        numberFormatterPuerto.setOverwriteMode(true);
        numberFormatterPuerto.setAllowsInvalid(true);
        numberFormatterPuerto.setCommitsOnValidEdit(true);
        numberFormatterPuerto.setMinimum(this.FIRST_WELL_KNOWN_PORT);
        numberFormatterPuerto.setMaximum(this.LAST_REGISTERED_PORT);
        this.fieldPuerto = new JFormattedTextField(numberFormatterPuerto);
        this.fieldPuerto.setColumns(5);
        this.fieldPuerto.setValue(4662);
        this.fieldPuerto.addPropertyChangeListener("value", this);
        this.puerto = 4662;

        //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.insets.top = 20;
        gbc.insets.left = 20;
        gbc.insets.right = 20;
        gbc.insets.bottom = 20;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridy = 0;
        gbc.gridx = 0;
        super.add(new JLabel("IP v4"), gbc);
        gbc.gridx++;
        gbc.insets.left = 0;
        gbc.insets.right = 0;
        super.add(this.fieldsIP[0], gbc);
        gbc.gridx++;
        super.add(new JLabel(" . "), gbc);
        gbc.gridx++;
        super.add(this.fieldsIP[1], gbc);
        gbc.gridx++;
        super.add(new JLabel(" . "), gbc);
        gbc.gridx++;
        super.add(this.fieldsIP[2], gbc);
        gbc.gridx++;
        super.add(new JLabel(" . "), gbc);
        gbc.gridx++;
        gbc.insets.right = 20;
        super.add(this.fieldsIP[3], gbc);

        gbc.insets.left = 20;
        gbc.insets.right = 20;
        gbc.insets.top = 0;
        gbc.gridy++;
        gbc.gridx =0;
        super.add(new JLabel("Port"), gbc);
        gbc.gridx++;
        gbc.insets.right = 0;
        gbc.insets.left = 0;
        super.add(this.fieldPuerto, gbc);
        gbc.gridx+=2;
        gbc.gridwidth = 5;
        gbc.insets.right = 20;
        this.labelSocket.setText(this.getInetSocketAddress().toString());
        super.add(this.labelSocket, gbc);

    }



    public void propertyChange(PropertyChangeEvent pce){


        if (pce.getSource() == this.fieldPuerto) {

            this.puerto = (Integer) pce.getNewValue();

        }else {

            for (int i = 0; i < this.fieldsIP.length; i++) {

                if (pce.getSource() == this.fieldsIP[i]) {
                    this.bytesIP[i] = ((Integer) pce.getNewValue()).byteValue();
                    break;
                }
            }
        }

       this.labelSocket.setText(this.getInetSocketAddress().toString());
        
    }


    public InetSocketAddress getInetSocketAddress(){

        InetSocketAddress isa;
        try {

            isa = new InetSocketAddress(
                Inet4Address.getByAddress(this.bytesIP),
                this.puerto
                );
            
        }catch(UnknownHostException uhe) {
            
            isa = null;
        }

        return isa;
    }


    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled);
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

}

