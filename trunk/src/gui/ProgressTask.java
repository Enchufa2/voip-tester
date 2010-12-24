/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JOptionPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;

/**
 *
 * @author Antonio
 */
public abstract class ProgressTask<T,V>
        extends SwingWorker<T,V> implements PropertyChangeListener, ActionListener{

    public final JProgressBar progressBar;

    public final JButton botonCancelar;

    public final JPanel panel = new JPanel();

    public JDialog dialog = new JDialog();

    public ProgressTask(boolean indeterminada) {

        super.addPropertyChangeListener(this);
        
        this.progressBar = new JProgressBar();
        this.progressBar.setIndeterminate(indeterminada);
        if(!indeterminada) {

            this.progressBar.setMinimum(0);
            this.progressBar.setMaximum(100);
            this.progressBar.setValue(0);
            this.progressBar.setStringPainted(true);
        }

        this.botonCancelar = new JButton("cancel");
        this.botonCancelar.addActionListener(this);

        this.panel.setOpaque(true);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.panel.setLayout(layout);
        gbc.ipady = 15;
        gbc.ipadx = 10;
        gbc.insets.bottom = 20;
        gbc.insets.top = 20;
        gbc.insets.left = 20;
        this.panel.add(this.progressBar, gbc);
        gbc.ipadx =0;
        gbc.ipady =0;
        gbc.insets.right = 20;
        this.panel.add(this.botonCancelar, gbc);
        
        this.dialog.add(this.panel);

        
    }


    public void done() {

        this.quitarDialogo();

        T t = null;

        try {

            t = this.get();

        } catch (CancellationException ce) {
        } catch (InterruptedException ie) {
        } catch (ExecutionException ee) {

            if(ee !=null){

                Throwable innerException = ee.getCause();
                Throwable innerCause = innerException.getCause();
                if(innerCause!=null) {

                    JOptionPane.showMessageDialog(
                            null,
                            innerException.getMessage() + '\n' + innerCause.getMessage(),
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);

                }else{

                    JOptionPane.showMessageDialog(
                            null,
                            ee.getMessage() +'\n' + innerException.getMessage(),
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);

                }
                
            }

        } finally {

            this.doneAfter(t);

        }
    }

    public void doneAfter(T t) {
    }


    public void ejecutarTarea(Component parentComponent, String nombreTarea) {

        this.mostrarDialogo(parentComponent, nombreTarea);
        super.execute();

    }

    public void ejecutarTarea(Component parentComponent, String nombreTarea, boolean cancel) {

        if(!cancel) {

            this.panel.remove(this.botonCancelar);
        }
        this.mostrarDialogo(parentComponent, nombreTarea);
        super.execute();

    }

    public void propertyChange(PropertyChangeEvent evt) {
      
        if ("progress".equals(evt.getPropertyName())) {
            if(! this.progressBar.isIndeterminate()) {
                int progress = (Integer) evt.getNewValue();
                this.progressBar.setValue(progress);
                this.progressBar.repaint();
            }
        }

        

    }


    public void actionPerformed(ActionEvent actionEvent){

        super.cancel(true);
        this.progressBar.setIndeterminate(false);
        this.progressBar.setValue(this.progressBar.getMinimum());

        if(this.dialog !=null)
            this.quitarDialogo();
        
    }

   

    public void mostrarDialogo(Component parentComponent , String titulo) {

        Window window = SwingUtilities.windowForComponent(parentComponent);
        this.dialog = new JDialog(window);
        this.dialog.setTitle(titulo);
        this.dialog.getContentPane().add(this.panel);
        this.dialog.pack();
        this.dialog.setLocationRelativeTo(window);
        this.dialog.setResizable(false);
        this.dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.dialog.setVisible(true);
        this.dialog.requestFocus();
    }

    public void quitarDialogo() {

        this.dialog.setVisible(false);
        this.dialog.dispose();
        
    }

    public JPanel getProgressPanel() {

        return this.panel;
    }


    


}
