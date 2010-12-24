/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    final private JFormattedTextField fieldPuerto;
    final private JLabel labelSocket;
    final private Dimension dimensionSelector = new Dimension(520, 150);

    //elementos no gráficos
    private int puerto;
    final private int FIRST_WELL_KNOWN_PORT=1;
    final private int LAST_REGISTERED_PORT=65535;

    private ArrayList<NetworkInterface> networkInterfaces = new ArrayList<NetworkInterface>();


    public CampoInterfazRed() {

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

        this.labelSocket = new JLabel(
                this.getInetSocketAddress().toString()
                );


        GridBagLayout layout = new GridBagLayout();
        super.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.right = 20;
        gbc.insets.top = 20;
        gbc.anchor = GridBagConstraints.LINE_END;
        super.add(new JLabel("Interface"), gbc);
        gbc.gridx++;
        gbc.gridwidth = 4;
         gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        super.add(this.comboInterfaces, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.insets.bottom = 20;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        super.add(new JLabel("IP v4"), gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        super.add(this.comboInetAddress, gbc);
        gbc.gridx++;
        gbc.ipadx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        super.add(new JLabel("Port"), gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        super.add(this.fieldPuerto, gbc);

        gbc.gridx++;
        gbc.anchor = GridBagConstraints.CENTER;
        super.add(this.labelSocket,gbc);

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

        this.labelSocket.setText(
                    this.getInetSocketAddress().toString()
                    );
        
    }

    public void propertyChange(PropertyChangeEvent pce) {

        if(pce.getSource() == this.fieldPuerto) {

            this.puerto = (Integer) this.fieldPuerto.getValue();
            this.labelSocket.setText(
                    this.getInetSocketAddress().toString()
                    );
        }

    }
    

    public InetSocketAddress getInetSocketAddress(){

        return new InetSocketAddress(
                (Inet4Address) this.comboInetAddress.getSelectedItem(),
                this.puerto);

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
