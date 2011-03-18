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
import gui.selectorDirSocket.*;
import gui.GuiManager;


/**
 *
 * @author Antonio
 */
public class PanelConexionDS extends JPanel{

    public final CampoIP conexionRemota = new CampoIP();
    public final CampoInterfazRed conexionLocal = new CampoInterfazRed();
    public final BotonConectar botonConectar = new BotonConectar("sockets");


    public PanelConexionDS() {

        super();

        super.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Timestamps protocol connection"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        this.conexionRemota.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Remote socket address"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.conexionLocal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Local socket address"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        this.conexionRemota.setPreferredSize(
                this.conexionLocal.getPreferredSize()
                );


         //posicionamiento de los campos
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(layout);

        gbc.gridy = 0;
        gbc.insets.top = 20;
        gbc.insets.left = 20;
        gbc.insets.right = 20;
        super.add(this.conexionRemota, gbc);

        gbc.gridy++;
        super.add(this.conexionLocal, gbc);


        this.botonConectar.setEstadoConexion(false);
        gbc.gridy++;
        gbc.insets.bottom = 20;
        super.add(this.botonConectar, gbc);

    }

    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        GuiManager.setEnabled(this, enabled);

    }

}

