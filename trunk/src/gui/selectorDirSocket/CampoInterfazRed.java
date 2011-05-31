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
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.ArrayList;
import java.net.SocketException;

import gui.GuiManager;

/**
 *
 * @author Antonio
 */
public class CampoInterfazRed extends JPanel
        implements ItemListener, PropertyChangeListener {

    //componentes GUI
    final private JComboBox comboInterfaces;
    final private JComboBox comboInetAddress;
    final private JFormattedTextField[] fieldsIPpublica = new JFormattedTextField[4];
    final private JFormattedTextField fieldPuerto;
    final private JLabel labelSocketPrivado;
    final private JLabel labelSocketPublico = new JLabel();
    final private Dimension dimensionSelector = new Dimension(450, 250);
    //elementos no gráficos
    private int puerto;
    final private byte[] bytesIPpublica = new byte[4];
    private boolean tieneIPpublica = true;
    final private int FIRST_WELL_KNOWN_PORT=1;
    final private int LAST_REGISTERED_PORT=65535;

    private ArrayList<NetworkInterface> networkInterfaces = new ArrayList<NetworkInterface>();


    public CampoInterfazRed( boolean tieneIPpublica) {

        super();
        this.comboInterfaces = new JComboBox();    
        this.comboInetAddress = new JComboBox();
        this.getInterfacesDisponibles();
        this.comboInterfaces.addItemListener(this);
        this.comboInetAddress.addItemListener(this);

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

        this.labelSocketPrivado = new JLabel(
                this.getLocalInetSocketAddress().toString()
                );

        if (tieneIPpublica) {
            dimensionSelector.setSize(500, 200);
            //Campo direccion IP
            NumberFormat numberFormatIP = NumberFormat.getNumberInstance();
            numberFormatIP.setParseIntegerOnly(true);
            NumberFormatter numberFormatterIP = new NumberFormatter(numberFormatIP);
            numberFormatterIP.setOverwriteMode(true);
            numberFormatterIP.setAllowsInvalid(true);
            numberFormatterIP.setCommitsOnValidEdit(true);
            numberFormatterIP.setMinimum(0);
            numberFormatterIP.setMaximum(255);

            for (int i = 0; i < 4; i++) {
                this.fieldsIPpublica[i] = new JFormattedTextField(numberFormatterIP);
                this.fieldsIPpublica[i].setValue(1);
                this.fieldsIPpublica[i].setColumns(5);
                this.fieldsIPpublica[i].addPropertyChangeListener("value", this);
                this.bytesIPpublica[i] = ((Integer) this.fieldsIPpublica[i].getValue()).byteValue();
            }

        }

        GridBagLayout layout = new GridBagLayout();
        super.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.right = 10;
        gbc.insets.top = 10;
        gbc.anchor = GridBagConstraints.LINE_END;
        super.add(new JLabel("Iface"), gbc);
        gbc.gridx++;
        gbc.gridwidth = 7;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        super.add(this.comboInterfaces, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.insets.bottom = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        super.add(new JLabel("IP v4"), gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        super.add(this.comboInetAddress, gbc);

        if (tieneIPpublica) {
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.ipadx = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            super.add(new JLabel("Public"), gbc);
            gbc.gridx++;
            gbc.insets.left = 0;
            gbc.insets.right = 0;

            for (int i = 0; i < 4; i++) {

                super.add(this.fieldsIPpublica[i], gbc);
                gbc.gridx++;
                if (i < 3) {
                    super.add(new JLabel(" . "), gbc);
                    gbc.gridx++;
                }
            }
        }
        gbc.gridy++;
        gbc.gridx=0;
        gbc.gridwidth = 1;
        gbc.insets.right = 10;
        gbc.anchor = GridBagConstraints.LINE_END;
        super.add(new JLabel("Port"), gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets.right = 0;
        super.add(this.fieldPuerto, gbc);

        gbc.gridx++;
        gbc.gridwidth =6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets.left = 20;
        //gbc.anchor = GridBagConstraints.CENTER;
        super.add(this.labelSocketPrivado, gbc );

        if (tieneIPpublica) {
            gbc.gridy++;
            this.labelSocketPublico.setText( this.getPublicInetSocketAddress().toString());
            super.add(this.labelSocketPublico,gbc);
        }

    }

    public CampoInterfazRed() {
        this(false);
    }

    public void getInterfacesDisponibles() {

        Enumeration<NetworkInterface> enumIfaces;
        NetworkInterface netIface;
        int numeroInterfaces = 0;
        try {

            enumIfaces = NetworkInterface.getNetworkInterfaces();
            while(enumIfaces.hasMoreElements()) {

                netIface = enumIfaces.nextElement();
                this.getIPdisponibles(netIface);
                if (this.comboInetAddress.getItemCount() > 0) {
                    
                    this.networkInterfaces.add(numeroInterfaces, netIface);
                    this.comboInterfaces.addItem(
                            this.networkInterfaces.get(numeroInterfaces).getDisplayName()
                            );
                    numeroInterfaces++;
                }
            }
            
        }catch(SocketException se) {

            try{

                this.comboInetAddress.addItem( (Inet4Address) Inet4Address.getLocalHost());

            }catch(UnknownHostException uhe) {

                uhe.printStackTrace();
                
            }

        }

        if(this.comboInterfaces.getItemCount() >0) {
            this.comboInterfaces.setSelectedIndex(0);
            this.getIPdisponibles(
                    (NetworkInterface) this.networkInterfaces.get(0)
                    );
        }else{
            
            if (this.comboInetAddress.getItemCount() >0) 
                 this.comboInetAddress.setSelectedIndex(0);
            
        }


    }

    public void getIPdisponibles(NetworkInterface networkInterface) {

        Enumeration<InetAddress> enumInet = networkInterface.getInetAddresses();
        ArrayList<Inet4Address> inet4addresses = new ArrayList<Inet4Address>();
        this.comboInetAddress.removeAllItems();


        while(enumInet.hasMoreElements()) {

            InetAddress ia = enumInet.nextElement();
            if (ia instanceof Inet4Address) {
                inet4addresses.add((Inet4Address) ia);
                this.comboInetAddress.addItem((Inet4Address) ia);
            }
        }

        if (this.comboInetAddress.getItemCount() > 0 )
            this.comboInetAddress.setSelectedIndex(0);


    }

    public void itemStateChanged(ItemEvent itemEvent) {

        if (itemEvent.getSource() == this.comboInterfaces) {

            this.getIPdisponibles(
                   this.networkInterfaces.get(
                        this.comboInterfaces.getSelectedIndex()
                        )
                 );
        }

        this.labelSocketPrivado.setText(
                    this.getLocalInetSocketAddress().toString()
                    );
        
    }

    public void propertyChange(PropertyChangeEvent pce) {

        if(pce.getSource() == this.fieldPuerto) {

            this.puerto = (Integer) this.fieldPuerto.getValue();
            
        }else {

            if (tieneIPpublica) {
                for (int i = 0; i < this.fieldsIPpublica.length; i++) {

                    if (pce.getSource() == this.fieldsIPpublica[i]) {
                        this.bytesIPpublica[i] = ((Integer) pce.getNewValue()).byteValue();
                        break;

                    }
                }
            }
        }
        this.labelSocketPrivado.setText(
                    this.getLocalInetSocketAddress().toString()
                    );
        if(tieneIPpublica) {
            this.labelSocketPublico.setText( this.getPublicInetSocketAddress().toString() );
        }

    }
    

    public InetSocketAddress getLocalInetSocketAddress(){

        return new InetSocketAddress(
                (Inet4Address) this.comboInetAddress.getSelectedItem(),
                this.puerto);

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


     public static void main( String args[] ){

         GuiManager.crearYmostrarGUI(new CampoInterfazRed(true) , false);

     }
}

