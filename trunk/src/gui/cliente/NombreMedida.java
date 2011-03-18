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

/**
 *
 * @author Antonio
 */
public enum NombreMedida {

    RETARDO("Delay"),
    JITTER("Jitter"),
    PROB_PERDIDAS("Loss probability"),
    DIST_PERDIDAS("Loss distribution"),
    ANCHO_BANDA("Bandwidth"),
    EMODEL("E-Model"),
    PESQ("PESQ");

    String nombre;

    NombreMedida(String nombreMedida) {

        this.nombre = nombreMedida;
    }

    public String toString(){
        return this.nombre;
    }

}

