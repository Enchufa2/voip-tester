package qos.estimacionQoS.parametrosQoS;

import java.io.Serializable;

public class ParametrosTransmision implements Serializable{

    /**
     * Intervalo de tiempo en microsegundos entre dos envios consecutivos de un
     * paquete. Permite fijar la tasa de envío en paquetes/segundo
     */
    private int tiempoEntreEnvios;


    /**
     * Longitud en bytes de los datos a enviar. Este parámetro junto con el
     * tiempo entre envíos permite fijar el bit rate de emision
     */
    private int longitudDatos;
    

    /**
     * Número de mensajes/paquetes a enviar en cada intervalo. Este campo
     * fija el número de mensajes involucrados en la obtención de un parámetro
     * en un intervalo.
     */
    private int mensajesPorIntervalo;
    
    
    /**
     * Numero de intervalos de <code>mensajesPorIntervalo</code> cada uno.
     * Fija, junto con  <code>mensajesPorIntervalo</code>, el número total
     * de mensajes a enviar.
     */
    private int numeroIntervalos;



    
    /**
     * Crea los parámetros de transmision
     *
     * @param tiempoEntreEnvios     Intervalo de tiempo en microsegundos entre
     *                              dos envios consecutivos
     * @param longitudDatos         Longitud en bytes de los datos a enviar
     * @param mensajesPorIntervalo  Número de mensajes que se envían en cada intervalo
     * @param numeroIntervalos      Número de intervalos en total.
     * 
     * 
     */
    public ParametrosTransmision (
            int tiempoEntreEnvios ,
            int longitudDatos,
            int mensajesPorIntervalo ,
            int numeroIntervalos
            ) {
        
        this.tiempoEntreEnvios = tiempoEntreEnvios;
        this.longitudDatos = longitudDatos;
        this.mensajesPorIntervalo = mensajesPorIntervalo;
        this.numeroIntervalos = numeroIntervalos;
        
    }


    /**
     * Inicializa lso parámetros de transmision a cero.
     */
    public ParametrosTransmision () {
    }

    /**
     * Constructor de copia
     *
     * @param pT    Los parámetros a copiar
     */
    public ParametrosTransmision(ParametrosTransmision pT) {

        this.tiempoEntreEnvios = pT.getTiempoEntreEnvios();
        this.longitudDatos = pT.getLongitudDatos();
        this.mensajesPorIntervalo = pT.getMensajesPorIntervalo();
        this.numeroIntervalos = pT.getNumeroIntervalos();
    }


    /**
     * modifica los parámetros de transmision
     *
     * @param tiempoEntreEnvios     Intervalo de tiempo en microsegundos entre
     *                              dos envios consecutivos
     * @param longitudDatos         Longitud en bytes de los datos a enviar
     * @param mensajesPorIntervalo  Número de mensajes que se envían en cada intervalo
     * @param numeroIntervalos      Número de intervalos en total.
     *
     *
     */
    public void setParametrosTransmision (
            int tiempoEntreEnvios ,
            int longitudDatos,
            int mensajesPorIntervalo ,
            int numeroIntervalos
            ) {

        this.tiempoEntreEnvios = tiempoEntreEnvios;
        this.longitudDatos = longitudDatos;
        this.mensajesPorIntervalo = mensajesPorIntervalo;
        this.numeroIntervalos = numeroIntervalos;

    }

    /**
     * Devuelve el intervalo de tiempo entre dos envios consecutivos
     *
     * @return      Intervalo de tiempo en microsegundos entre
     *              dos envios consecutivos de un paquete
     */
    public int getTiempoEntreEnvios () {
        return tiempoEntreEnvios;
    }


    /**
     * Fija el intervalo de tiempo entre dos envios consecutivos
     *
     * @param tiempoEntreEnvios     Intervalo de tiempo en microsegundos
     *                              entre dos envios consecutivos
     */
    public void setTiempoEntreEnvios (int tiempoEntreEnvios) {
        this.tiempoEntreEnvios = tiempoEntreEnvios;
    }

    

    

    /**
     * Devuelve la longitud de los datos del mensaje.
     *
     * @return longitudDatos  Longitud en bytes de los datos del mensaje
     */
    public int getLongitudDatos () {
        return longitudDatos;
    }

    /**
     * Fija la longitud de los datos del mensaje
     *
     * @param longitudDatos   Longitud en bytes de los datos del mensaje
     */
    public void setLongitudDatos (int longitudDatos) {
        this.longitudDatos = longitudDatos;
    }


    /**
     * Devuelve el número de mensajes por intervalo.
     *
     * @return   El número de mensajes por intervalo
     */
    public int getMensajesPorIntervalo () {
        return mensajesPorIntervalo;
    }


    /**
     * Fija el número de mensajes por intervalo
     *
     * @param mensajesPorIntervalo El número de mensajes por intervalo
     */
    public void setMensajesPorIntervalo (int mensajesPorIntervalo) {
        this.mensajesPorIntervalo = mensajesPorIntervalo;
    }


    /**
     * Devuelve el número de intervalos
     *
     * @return el número de intervalos
     */
    public int getNumeroIntervalos() {
        return this.numeroIntervalos;

    }

    /**
     * Fija el número de intervalos
     *
     * @param numeroIntervalos  El número de intervalos
     */
    public void setNumeroIntervalos(int numeroIntervalos) {
        this.numeroIntervalos = numeroIntervalos;
    }


    /**
     * Devuelve el número total de mensajes a enviar
     *
     * @return el número total de mensajes a enviar
     */
    public int numeroDeMensajes(){

        return this.numeroIntervalos * this.mensajesPorIntervalo;
    }


    /**
     * Devuelve el número total de bytes que se envían en cada intervalo
     *
     * @return el número total de bytes por intervalo
     *
     */
    public int numeroBytesPorIntervalo() {

        return this.longitudDatos * this.mensajesPorIntervalo;

    }


    /**
     * Representación del objeto mediante caracteres
     */
    @Override
    public String toString() {

        return "Interdeparture time(us): " + this.tiempoEntreEnvios + '\t'
                + "Message length(Bytes): " + this.longitudDatos + '\t'
                + "Messages per interval: " + this.mensajesPorIntervalo + '\t'
                + "Number of intervals: " + this.numeroIntervalos;
        
    }

}

