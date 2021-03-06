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
import gui.cliente.*;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;




/**
 *
 * @author Antonio
 */
public class CheckBoxMedida 
        extends JCheckBox{

    //medida que representa el checbox
    public final NombreMedida nombreMedida;

    //Parámetros de configuración relacionados con la medida
    ArrayList<SelectorPropiedad> selectoresPropiedad = new ArrayList<SelectorPropiedad>();
    ArrayList<String> nombrePropiedad = new ArrayList<String>();

    //Dialogo para mostrar el selector de propiedad
    JDialog dialogoSelector;


    //tamaño del check box
    final Dimension dimensionCheck = new Dimension(200,60);


    public CheckBoxMedida(NombreMedida nombreMedida){

        super(nombreMedida.toString());
        this.nombreMedida = nombreMedida;
        super.setSelected(false);
        super.setSize(this.dimensionCheck);
        super.setMaximumSize(this.dimensionCheck);
        super.setMinimumSize(this.dimensionCheck);
        super.setPreferredSize(this.dimensionCheck);
    }

    public void crearPropiedad(int min, int max, int defecto, String unidades, String nombrePropiedad) {

        final SelectorPropiedad sp = new SelectorPropiedad(min, max);
        sp.setLabelUnidades(unidades);


        if(this.selectoresPropiedad.size() ==0){
            MouseAdapter ma = new MouseAdapter() {

                public void mouseClicked(MouseEvent mouseEvent) {

                    if (mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.isControlDown()) {
                        CheckBoxMedida.super.setSelected(true);
                        for (int i = 0; i < CheckBoxMedida.this.selectoresPropiedad.size(); i++) {

                            JDialog jd = CheckBoxMedida.this.getDialogoSeleccionPropiedad(i);
                            jd.setVisible(true);

                        }                        
                    }                    
                }
            };
            super.addMouseListener(ma);
        }

        sp.slider.setValue(defecto);
        this.selectoresPropiedad.add(sp);
        this.nombrePropiedad.add(nombrePropiedad);
        super.setToolTipText("Ctrl + click to select " + nombrePropiedad);

    }


    public boolean tienePropiedades() {

        boolean tieneSubParametro = false;
        
        if(this.selectoresPropiedad.size()>0) {

            tieneSubParametro = true;
        }

        return tieneSubParametro;
    }

    public int getValorPropiedad(int index) {

        if(this.tienePropiedades())
            return this.selectoresPropiedad.get(index).slider.getValue();

        return Integer.MIN_VALUE;
    }    


    public JDialog getDialogoSeleccionPropiedad(int index) {

        Window window = SwingUtilities.windowForComponent(CheckBoxMedida.this);
         if (this.dialogoSelector == null) {
            this.dialogoSelector = new JDialog(
                    window,
                    JDialog.ModalityType.APPLICATION_MODAL);
            this.dialogoSelector.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            this.dialogoSelector.setResizable(false);
            this.dialogoSelector.setLocationRelativeTo(window);
        }

        this.dialogoSelector.getContentPane().add(
                this.selectoresPropiedad.get(index));        
        this.dialogoSelector.setTitle(this.nombrePropiedad.get(index));
        this.dialogoSelector.pack();
        this.selectoresPropiedad.get(index).slider.requestFocusInWindow();
        return this.dialogoSelector;

    }

}

