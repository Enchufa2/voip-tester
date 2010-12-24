/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Antonio
 */
public class EstadoGlobal {

    public Estado estadoGlobal;

    public Estado estadoRegistrado;

    public Estado estadoSocketsConfigurados;


    public EstadoGlobal() {

        this.estadoGlobal = new Estado();
        this.estadoRegistrado = new Estado();
        this.estadoSocketsConfigurados = new Estado();

    }

    public void setEstadoGlobal(Estado.Estados nuevoEstado) {

        /*switch(nuevoEstado) {

            case DESCONECTADO:

                this.estadoGlobal.setEstado(nuevoEstado);
                this.estadoRegistrado.setEstado(Estado.Estados.INDEFINIDO);
                this.estadoSocketsConfigurados.setEstado(Estado.Estados.INDEFINIDO);
                break;

            case REGISTRADO:

                this.estadoGlobal.setEstado(nuevoEstado);
                this.estadoRegistrado.setEstado(Estado.Estados.CONFIGURAR_SOCKETS_PT);
                break;

        }*/
         this.estadoGlobal.setEstado(nuevoEstado);
    }

    public void setEstadoRegistrado(Estado.Estados nuevoEstado) {

        /*switch(nuevoEstado) {

            case CONFIGURAR_SOCKETS_PT:

                this.estadoRegistrado.setEstado(nuevoEstado);
                this.estadoSocketsConfigurados.setEstado(Estado.Estados.INDEFINIDO);

                break;

            case SOCKETS_PT_CONFIGURADOS:

                this.estadoRegistrado.setEstado(nuevoEstado);
                this.estadoSocketsConfigurados.setEstado(Estado.Estados.CONFIGURAR_PARAMETROS_ESTIMACION);
                break;
        }*/
        this.estadoRegistrado.setEstado(nuevoEstado);
    }

    public void setEstadoSocketsConfigurados(Estado.Estados nuevoEstado) {

        this.estadoSocketsConfigurados.setEstado(nuevoEstado);
    }


    public Estado.Estados getEstadoGlobal() {

        return this.estadoGlobal.getEstado();
    }

    public Estado.Estados getEstadoRegistrado() {

        return this.estadoRegistrado.getEstado();
    }
    
   
    public Estado.Estados getEstadoSocketsConfigurados() {

        return this.estadoSocketsConfigurados.getEstado();
   }

}
