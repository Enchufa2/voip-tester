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



