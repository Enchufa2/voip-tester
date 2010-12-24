/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
