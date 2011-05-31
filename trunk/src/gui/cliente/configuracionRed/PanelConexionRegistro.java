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


package gui.cliente.configuracionRed;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import gui.selectorDirSocket.CampoIP;
import gui.GuiManager;

/**
 *
 * @author Antonio
 */
public class PanelConexionRegistro extends JPanel{

    //componentes GUI
    public final CampoIP campoIPpServidor = new CampoIP(true, false);
    public final BotonConectar botonConectar = new BotonConectar("registro");
    public final CampoIP campoIpCliente = new CampoIP(false, false);


    //elementos no gráficos
    //Estado

    public PanelConexionRegistro() {

        super();
        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Register server connection"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        //JPanel panelDireccionesRed = new JPanel();
        this.campoIPpServidor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Server network address"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        //JPanel panelDireccionesRed = new JPanel();
        this.campoIpCliente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Client port"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);
        
        gbc.gridy = 0;
        gbc.gridx = 0;
        //this.campoIpCliente.removeIpDirFields();
        super.add(this.campoIpCliente, gbc);


        gbc.insets.top = 20;
        gbc.insets.left = 20;
        gbc.insets.right = 20;
        gbc.gridy++;
        super.add(this.campoIPpServidor, gbc);

        this.botonConectar.setEstadoConexion(false);
        gbc.gridy++;
        gbc.insets.bottom = 10;
        super.add(this.botonConectar, gbc);
    }


     public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled);
    }

}

