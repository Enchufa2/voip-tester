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


package gui.servidor;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import gui.selectorDirSocket.CampoInterfazRed;
import nucleo.servidor.ControlServidor;
import nucleo.servidor.ControlServidorException;



/**
 *
 * @author Antonio
 */
public class PanelConfiguracionServidor 
        extends JPanel implements ActionListener{

    //componentes gui
    public final CampoInterfazRed conexionLocal = new CampoInterfazRed();
    public final SelectorTiempoExpiracion selectorTE = new SelectorTiempoExpiracion();
    public final BotonIniciar botonIniciar = new BotonIniciar();

    //elementos no graficos
    final ControlServidor controlServidor;
    private InetSocketAddress isaLocal;

    public PanelConfiguracionServidor() {

        super();
        /*super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("ConfiguraciÃ³n Servidor de Registro"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));*/

        this.conexionLocal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Local network address"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.conexionLocal.setPreferredSize(new Dimension(700, 200));


        this.selectorTE.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Activity expiration time"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        this.selectorTE.slider.setValue(10);
        this.selectorTE.setEnabled(false);
        this.selectorTE.botonAjustar.addActionListener(this);

        this.controlServidor = new ControlServidor();
        this.botonIniciar.addActionListener(this);

         //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.top = 20;
        gbc.insets.left = 20;
        gbc.insets.right = 20;
        super.add(this.conexionLocal, gbc);

        gbc.gridy++;
        this.selectorTE.setPreferredSize(this.conexionLocal.getPreferredSize());
        super.add(this.selectorTE, gbc);

        gbc.gridy++;
        gbc.insets.bottom = 20;
        super.add(this.botonIniciar, gbc);


    }


    public void iniciarServidor() {


        try{
            this.isaLocal = this.conexionLocal.getInetSocketAddress();
            controlServidor.registrarServidor(
                    this.isaLocal.getAddress().getHostAddress(),
                    this.isaLocal.getPort()
                    );
            //System.setProperty("java.rmi.server.hostname","85.61.12.224");
            //System.setProperty("java.rmi.server.useLocalHostnameproperty", this.isaLocal.getAddress().getHostAddress());
            this.botonIniciar.setConectado(true);
            this.conexionLocal.setEnabled(false);
            this.selectorTE.setEnabled(true);

        }catch(ControlServidorException cse) {

            JOptionPane.showMessageDialog(
                    this,
                    cse.getMessage() + '\n' + cse.getCause().getMessage(),
                    "Service error",
                    JOptionPane.ERROR_MESSAGE
                    );

        }
    }

    public void detenerServidor() {


        try{
            if (this.controlServidor.servidorRegistrado(this.isaLocal.getPort())) {

                this.controlServidor.desconectarServidor();
                this.botonIniciar.setConectado(false);
                this.conexionLocal.setEnabled(true);
                this.selectorTE.setEnabled(false);

            }

        }catch(ControlServidorException cse) {

            JOptionPane.showMessageDialog(
                    this,
                    cse.getMessage() + '\n' + cse.getCause().getMessage(),
                    "Service error",
                    JOptionPane.ERROR_MESSAGE
                    );
        }

    }


    public void actionPerformed(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();

        if(source == this.botonIniciar) {

            if( ! this.botonIniciar.getConectado()) {

                this.iniciarServidor();

            }else{

                this.detenerServidor();
            }

        }else if(source == this.selectorTE.botonAjustar) {

            this.controlServidor.ajustarPeriodoActividad(
                    this.selectorTE.slider.getValue()
                    );

        }
    }

}

