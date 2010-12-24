package qos.estimacionQoS.mos;

import utils.SalidaComando;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Antonio
 */
public class Pesq {

    /**
     * Array de cadena de caracteres que representa el comando a ejecutar
     * y los argumentos del mismo.
     *
     * comando[0] == nombre del comando ejecutable
     * comando[1] == argumento 1: frecuencia de muestreo
     * comando[2] == argumento 2: path de señal original en formato wav
     * comando[3] == argumento 3: path de señal degradada en formato wav
     */
    private String[] comando = new String[4];

    /**
     * Salida de error producida por la ejecución del pesq
     */
    private String salidaError="";

    /**
     * Salida estándar producida por la ejecución del pesq
     */
    private String salidaEstandar="";


    /**
     * Localiza y obtiene el archivo ejecutable que realiza el test Pesq, que tomará
     * para realizar el test.
     *
     * @param  rutaPesq                 La ruta donde se encuentra el ejecutablePesq
     */
    public Pesq(File rutaPesq) {
       
        this.comando[0] = rutaPesq.getPath();
    }

    /*
     Llama al ejecutable correspondiente y obtiene las salidas generadas por el mismo.
     * Es responsabilidad del usuario gestionar las salidas para comprobar si el proceso ha finalizado
     * con éxito.
     *
     * @param original                  Descriptor de archivo de la señal original
     * @param degradada                 Descriptor de archivo de la señal degradada/recibida
     * @param muestrasPorSegundo        Frecuencia de muestreo de las señales de audio
     *                                  Solo se permiten 8000 y 16000 Hz
     *
     * @throws PesqException            Si ocurre un error durante la ejecución del comando
     * 
     */
    public void ejecutarPesq(File original, File degradada, int muestrasPorSegundo)
            throws PesqException{

        if ( (muestrasPorSegundo != 8000) && (muestrasPorSegundo != 16000) ) {

            throw new PesqException("Invalid sample rate: "+muestrasPorSegundo +" Hz"+
                     '\n' +"Must be 8000 or 16000 Hz");
        }
        this.comando[1] = "+" + muestrasPorSegundo;
        this.comando[2] = original.getAbsolutePath();
        this.comando[3] = degradada.getAbsolutePath();

        Process ejecutablePesq = null;
        try{
            
            ejecutablePesq = Runtime.getRuntime().exec(this.comando);

        }catch(IOException ioe) {

            throw new PesqException ("Error while running test PESQ", ioe);
        }
        
        SalidaComando stderr = new SalidaComando (ejecutablePesq.getErrorStream());
        SalidaComando stdout = new SalidaComando (ejecutablePesq.getInputStream());

        stderr.start();
        stdout.start();

        try{

            ejecutablePesq.waitFor();
            stderr.join();
            stdout.join();

        }catch (InterruptedException ie) {

            throw new PesqException("PESQ execution interrumped", ie);

        }
        
        this.salidaError = stderr.getSalidaDeComando();
        this.salidaEstandar = stdout.getSalidaDeComando();

    }

    /**
     * Obtiene el valor numérico del Mos a partir de la cadena de caracteres
     * generada en la salida estándar por el comando que ejecuta el Pesq.
     *
     * @return mosPesq El valor numérico como Float del MOS-LQO
     */
    public float salidaDeComandoAValorMos() {

        float mosPesq = Float.NaN;
        int posicionSignoIgualdad;
        if ((posicionSignoIgualdad = this.salidaEstandar.toLowerCase().indexOf("=")) > 0) {
            String valorDevuelto = this.salidaEstandar.substring(posicionSignoIgualdad + 8, posicionSignoIgualdad + 14);
            if (valorDevuelto.indexOf("1.#QO")==0) {
                mosPesq = 1;
            }else {
                mosPesq = Float.valueOf(valorDevuelto);
            }
        }

        return mosPesq;
    }

    /**
     * Devuelve el contenido de salida estándar generada por el comando que ejecuta el pesq
     * 
     * @return La cadena de caracteres generada en la salida estándar por el comando que ejecuta el pesq
     */
    public String getSalidaEstandar () {

        return this.salidaEstandar;
    }


    /**
     * Devuelve el contenido de salida de error generada por el comando que ejecuta el pesq
     *
     * @return La cadena de caracteres generada en la salida de error por el comando que ejecuta el pesq
     */
    public String getSalidaDeError() {
        return this.salidaError;
    }

    /**
     * Devuelve el path donde se encuentra el comando pesq
     *
     * @return El path donde se encuentra el comando Pesq
     */
    public String getRutaComandoPesq() {

        return this.comando[0];
    }


    

}
