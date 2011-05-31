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
    final private JFormattedTextField[] fieldsIPlocal = new JFormattedTextField[4];
    final private JFormattedTextField[] fieldsIPpublica = new JFormattedTextField[4];

    final private JFormattedTextField fieldPuerto;
    final private JLabel labelSocketLocal = new JLabel();
    final private JLabel labelSocketPublico = new JLabel();
    final private Dimension dimensionSelector = new Dimension();


    //elementos no visuales
    final private byte[] bytesIPlocal = new byte[4];
    final private byte[] bytesIPpublica = new byte[4];
    private int puerto;
    private boolean tieneFieldsIP = true;
    private boolean tieneIPpublica = true;
    static final private int FIRST_WELL_KNOWN_PORT=1;
    static final private int LAST_REGISTERED_PORT=65535;

    public CampoIP( boolean tieneFieldsIP, boolean tieneIPpublica ) {

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

        this.tieneFieldsIP = tieneFieldsIP;
        this.tieneIPpublica = tieneIPpublica;

        //Para IP local
        for (int i=0; i<4; i++) {
            this.fieldsIPlocal[i] = new JFormattedTextField(numberFormatterIP);
            this.fieldsIPlocal[i].setValue(1);
            this.fieldsIPlocal[i].setColumns(5);
            this.fieldsIPlocal[i].addPropertyChangeListener("value", this);
            this.bytesIPlocal[i] = ( (Integer) this.fieldsIPlocal[i].getValue() ).byteValue();
        }

        //Para IP pública
        if ( tieneIPpublica ) {
            for (int i=0; i<4; i++) {
                this.fieldsIPpublica[i] = new JFormattedTextField(numberFormatterIP);
                this.fieldsIPpublica[i].setValue(1);
                this.fieldsIPpublica[i].setColumns(5);
                this.fieldsIPpublica[i].addPropertyChangeListener("value", this);
                this.bytesIPpublica[i] = ( (Integer) this.fieldsIPpublica[i].getValue() ).byteValue();
            }
        }

        //Campo puerto
        NumberFormat numberFormatPuerto = NumberFormat.getNumberInstance();
        numberFormatPuerto.setParseIntegerOnly(true);
        NumberFormatter numberFormatterPuerto = new NumberFormatter(numberFormatPuerto);
        numberFormatterPuerto.setOverwriteMode(true);
        numberFormatterPuerto.setAllowsInvalid(true);
        numberFormatterPuerto.setCommitsOnValidEdit(true);
        numberFormatterPuerto.setMinimum(FIRST_WELL_KNOWN_PORT);
        numberFormatterPuerto.setMaximum(LAST_REGISTERED_PORT);
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
        gbc.insets.left = 10;
        gbc.insets.right = 20;
        gbc.insets.bottom = 20;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridy = 0;
        gbc.gridx = 0;

        if( tieneFieldsIP ){

            super.add(new JLabel("IP v4"), gbc);
            gbc.gridx++;
            gbc.insets.left = 0;
            gbc.insets.right = 0;

            for ( int i=0; i<4; i++) {
                
                if ( i == 3){
                     gbc.insets.right = 10;
                }
                super.add(this.fieldsIPlocal[i], gbc);
                gbc.gridx++;
                if ( i < 3 ){
                    super.add(new JLabel(" . "), gbc);
                    gbc.gridx++;
                }
            }

            gbc.insets.left = 10;
            gbc.insets.right = 10;
            gbc.insets.top = 0;
            gbc.gridy++;
            gbc.gridx=0;                        
            
            if ( tieneIPpublica ) {

                super.add(new JLabel("Public"), gbc);
                gbc.gridx++;
                gbc.insets.left = 0;
                gbc.insets.right = 0;

                for (int i = 0; i < 4; i++) {

                    if (i == 3) {
                        gbc.insets.right = 10;
                    }
                    super.add(this.fieldsIPpublica[i], gbc);
                    gbc.gridx++;
                    if ( i < 3 ){
                        super.add(new JLabel(" . "), gbc);
                        gbc.gridx++;
                    }
                }

                gbc.insets.left = 10;
                gbc.insets.right = 10;
                gbc.insets.top = 0;
                gbc.gridy++;
                gbc.gridx = 0;
                
            }
        }
        
        super.add(new JLabel("Port"), gbc);
        gbc.gridx++;
        gbc.insets.right = 0;
        gbc.insets.left = 0;
        super.add(this.fieldPuerto, gbc);

        gbc.gridx += 1;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        //gbc.weightx = 0.5;
        //gbc.insets.right = 20;
        if(tieneFieldsIP){           
            this.labelSocketLocal.setText( this.getLocalInetSocketAddress().toString() );
            if (tieneIPpublica){
                this.labelSocketPublico.setText( this.getPublicInetSocketAddress().toString() );
            }
        }else {
            this.labelSocketLocal.setText(""+this.getLocalInetSocketAddress().getPort());
        }
        super.add(this.labelSocketLocal, gbc);
        gbc.gridy++;
        super.add(this.labelSocketPublico, gbc);

    }

    public CampoIP(){
        this(true, true);
    }



    public void propertyChange(PropertyChangeEvent pce){


        if (pce.getSource() == this.fieldPuerto) {

            this.puerto = (Integer) pce.getNewValue();

        }else {

            for (int i = 0; i < this.fieldsIPlocal.length; i++) {

                if (pce.getSource() == this.fieldsIPlocal[i]) {
                    this.bytesIPlocal[i] = ((Integer) pce.getNewValue()).byteValue();
                    break;

                }else{
                    
                    if ( this.tieneIPpublica && pce.getSource() == this.fieldsIPpublica[i] ) {
                        this.bytesIPpublica[i] = ((Integer) pce.getNewValue()).byteValue();
                        break;
                    }
                }
            }
        }

       if(this.tieneFieldsIP){
            this.labelSocketLocal.setText(this.getLocalInetSocketAddress().toString());
            if (tieneIPpublica){
                this.labelSocketPublico.setText( this.getPublicInetSocketAddress().toString() );
            }
        }else {
            this.labelSocketLocal.setText(""+this.getLocalInetSocketAddress().getPort());
        }
        
    }


    public InetSocketAddress getLocalInetSocketAddress(){

        InetSocketAddress isa;
        try {

            isa = new InetSocketAddress(
                Inet4Address.getByAddress(this.bytesIPlocal),
                this.puerto
                );
            
        }catch(UnknownHostException uhe) {
            
            isa = null;
        }

        return isa;
    }

    public InetSocketAddress getPublicInetSocketAddress(){

        InetSocketAddress isa;
        try {

            isa = new InetSocketAddress(
                Inet4Address.getByAddress(this.bytesIPpublica),
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

        if (this.tieneFieldsIP){
            if(this.tieneIPpublica)
                this.dimensionSelector.setSize(430, 215);
            else
                this.dimensionSelector.setSize(430, 160);
        }else{
            this.dimensionSelector.setSize(430, 140);
        }
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


    public static void main(String args[]) {

        GuiManager.crearYmostrarGUI(new CampoIP(true, true), true);

    }
}


