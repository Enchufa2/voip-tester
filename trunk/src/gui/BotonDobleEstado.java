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

package gui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;


/**
 *
 * @author Antonio
 */
public class BotonDobleEstado extends JButton{


    final Dimension dimensionSelector = new Dimension(120, 40);

    private boolean accionRealizada = false;

    String textoHacer="";

    String textoDeshacer="";

    public BotonDobleEstado(String textoHacer, String textoDeshacer) {

        super();
        this.textoHacer = textoHacer;
        this.textoDeshacer = textoDeshacer;
        this.setAccionRealizada(false);

    }


    public void setAccionRealizada(boolean accionRealizada) {

        if(accionRealizada) {

            this.accionRealizada = true;
            super.setText(this.textoDeshacer);

        }else{

            this.accionRealizada = false;
            super.setText(this.textoHacer);

        }
    }

    public boolean getAccionRealizada() {

        return this.accionRealizada;
    }


    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);

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
}

