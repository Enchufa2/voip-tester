/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente;

/**
 *
 * @author Antonio
 */
public enum NombreParametro {

    TIEMPO_ENTRE_ENVIOS("Interdeparture time"),
    LONGITUD_MENSAJE("Message length"),
    MENSAJES_POR_INTERVALO ("Messages per interval"),
    NUMERO_INTERVALOS ("Number of intervals");
   
    private String nombre;


    NombreParametro(String nombre) {

        this.nombre = nombre;
    }

    public String toString(){
        return this.nombre;
    }

    

}
