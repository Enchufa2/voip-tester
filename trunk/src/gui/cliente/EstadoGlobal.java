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

