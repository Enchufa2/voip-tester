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

