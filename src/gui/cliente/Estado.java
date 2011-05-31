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
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *
 * @author Antonio
 */

public class Estado implements Serializable{

    private Estados estado;


    //private boolean activo = false;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );


    public Estado () {

        this.estado = Estados.INDEFINIDO;
        
    }


    public Estado( Estados estadoGlobal ) {
        
        this.estado = estadoGlobal;        
    }

    
    

    public synchronized void setEstado(Estados nuevoEstado) {

        /*switch(nuevoEstado) {

            case DESCONECTADO:
                if( this.estadoGlobal != Estados.CONFIGURAR_SOCKETS_PT
                        || this.estadoGlobal != Estados.OBTENIENDO_MEDIDAS_EXPERIMENTALES){

                    nuevoEstado = this.estadoGlobal;
                }
                break;

            case CONFIGURAR_SOCKETS_PT:

                 if( this.estadoGlobal != Estados.DESCONECTADO
                        || this.estadoGlobal != Estados.CONFIGURAR_PARAMETROS_ESTIMACION
                        || this.estadoGlobal != Estados.PARAMETROS_ESTIMACION_CONFIGURADOS){

                    nuevoEstado = this.estadoGlobal;
                }
                break;


        }*/
        
        Estados old = this.estado;
        this.estado = nuevoEstado;
        this.pcs.firePropertyChange( "estado", old, nuevoEstado);
    }

    public Estados getEstado() {

        return this.estado;
    }
    

    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        this.pcs.addPropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener )
    {
        this.pcs.removePropertyChangeListener( listener );
    }


    public enum Estados{

    DESCONECTADO,
    REGISTRADO,
    CONFIGURAR_SOCKETS_PT,
    SOCKETS_PT_CONFIGURADOS,
    CONFIGURAR_PARAMETROS_ESTIMACION,
    PARAMETROS_ESTIMACION_CONFIGURADOS,
    OBTENIENDO_MEDIDAS_EXPERIMENTALES,
    ESTIMACIONES_DISPONIBLES,
    INDEFINIDO
    ;
}



}



