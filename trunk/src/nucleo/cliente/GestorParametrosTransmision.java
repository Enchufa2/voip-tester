package nucleo.cliente;

import qos.estimacionQoS.parametrosQoS.ParametrosTransmision;
import qos.protocolos.timestamps.MensajeProtoTimestamps;


/**
 *
 * Clase encargada de gestionar la generación de parámetros de transmisión
 * de forma válida, tal que los valores se encuentren dentro de los límites establecidos
 *
 * @see ParametrosTransmision
 *
 * @author Antonio
 */
public class GestorParametrosTransmision {


    /**
     * parámetros de transmisión a fijar
     */
    protected ParametrosTransmision parametrosTransmision;

    /**
     * límite superior de los parámetros de transmisión
     */
    protected ParametrosTransmision parametrosTransmisionMax;

    /**
     * límite inferior de los parámetros de transmisión
     */
    protected ParametrosTransmision parametrosTransmisionMin;

    public static final int TEE_MAX = 250000;
    public static final int LD_MAX = MensajeProtoTimestamps.LONGITUD_DATOS_MAXIMA;
    public static final int MPI_MAX = 100;
    public static final int NI_MAX = 40;


    /**
     * Crea el gestor
     */
    public GestorParametrosTransmision() {
        
        this.parametrosTransmision = new ParametrosTransmision(10,8,2,1);
        this.parametrosTransmisionMax = new ParametrosTransmision(TEE_MAX, LD_MAX, MPI_MAX, NI_MAX);
        this.parametrosTransmisionMin  = new ParametrosTransmision(10,8,2,1);
        //this.reiniciarValoresExtremos();
        
    }

    /**
     * Reestable los límites inferior y superior
     */
    protected void reiniciarValoresExtremos() {
        
        this.parametrosTransmision.setParametrosTransmision(10,8,1,1);
        this.parametrosTransmisionMax.setParametrosTransmision(TEE_MAX, LD_MAX, MPI_MAX, NI_MAX);
        this.parametrosTransmisionMin.setParametrosTransmision(10,8,1,1);

    }

    /**
     * asigna los parámetros de transmisión
     *
     * @param   Los nuevos parámetros de transmisión
     */
    private void setParametrosTranmision(ParametrosTransmision parametrosTransmision) {

        this.parametrosTransmision = parametrosTransmision;

    }

    /**
     * Ajusta el tiempo entre envíos
     *
     * @param tEE el tiempo entre envios en microsegundos
     *
     * @throws     Si el valor está fuera de los límites
     */
    public void setTiempoEntreEnvios(int tEE ){

        try{

            this.comprobarTiempoEntreEnvios(tEE);

        }catch(IllegalArgumentException  iae){
            System.out.println(iae.getMessage());
            return;
        }
        
        this.parametrosTransmision.setTiempoEntreEnvios(tEE);
        
    }


    /**
     * Ajusta la longitud de datos del mensaje
     *
     * @param lM  la longitud de datos del mensaje en bytes
     *
     */
    public void setLongitudMensaje(int lM){

        try{

            this.comprobarLongitudMensaje(lM);

        }catch(IllegalArgumentException  iae){
            System.out.println(iae.getMessage());
            return;
        }

        this.parametrosTransmision.setLongitudDatos(lM);
        
    }

     /**
     * Ajusta el número de mensajes por intervalo
     *
     * @param mPI   el número de mensajes por intervalo
     *
     */
    public void setMensajesPorIntervalo(int mPI){

        try{

            this.comprobarMensajesPorIntervalo(mPI);

        }catch(IllegalArgumentException  iae){
            System.out.println(iae.getMessage());
            return;
        }
        this.parametrosTransmision.setMensajesPorIntervalo(mPI);
        
    }


     /**
     * Ajusta el número de intervalos
     *
     * @param nI    el número de intervalos
     *
     */
    public void setNumeroIntervalos(int nI){

        try{

            this.comprobarNumeroIntervalos(nI);
            
        }catch(IllegalArgumentException  iae){
            System.out.println(iae.getMessage());
            return;
        }
        this.parametrosTransmision.setNumeroIntervalos(nI);
     
    }


    /**
     * Comprueba que el tiempo entre envíos esté dentro de los límites
     *
     * @throws IllegalArgumentException     Si el valor está fuera de los límites
     */
    public void comprobarTiempoEntreEnvios(int tEE)
            throws IllegalArgumentException {

        int tEE_max = this.parametrosTransmisionMax.getTiempoEntreEnvios();
        int tEE_min = this.parametrosTransmisionMin.getTiempoEntreEnvios();

        if(tEE < tEE_min) {

            throw new IllegalArgumentException("Interdeparture time must be bigger than "+tEE_min+" us" +
                     '\n' + "requested value: " + this.parametrosTransmision.getTiempoEntreEnvios());

        }else if(tEE > tEE_max) {

            throw new IllegalArgumentException("Interdeparture time must be smaller than "+tEE_max+" us" +
                    '\n' + "requested value: " + this.parametrosTransmision.getTiempoEntreEnvios());
        }
    }

    /**
     * Comprueba que la longitud de datos del mensaje esté dentro de los límites
     *  @throws IllegalArgumentException     Si el valor está fuera de los límites
     */
    public void comprobarLongitudMensaje(int longitudDatos)
            throws IllegalArgumentException {

        int longitudDatos_max = this.parametrosTransmisionMax.getLongitudDatos();
        int longitudDatos_min = this.parametrosTransmisionMin.getLongitudDatos();

        if( longitudDatos < longitudDatos_min) {

            throw new IllegalArgumentException("message length must be bigger than "+longitudDatos_min+" bytes" +
                    '\n' + "requested value: " + this.parametrosTransmision.getLongitudDatos());

        }else if( longitudDatos > longitudDatos_max ) {

            throw new IllegalArgumentException("message length must be smaller than "+longitudDatos_max+" bytes" +
                    '\n' + "requested value: " + this.parametrosTransmision.getLongitudDatos());
        }

    }


    /**
     * Comprueba que el número de mensajes por intervalo esté dentro de los límites
     *  @throws IllegalArgumentException     Si el valor está fuera de los límites
     */
    public void comprobarMensajesPorIntervalo(int mensajesPorIntervalo) throws IllegalArgumentException {

        int mensajesPorIntervalo_max = this.parametrosTransmisionMax.getMensajesPorIntervalo();
        int mensajesPorIntervalo_min = this.parametrosTransmisionMin.getMensajesPorIntervalo();

        if(mensajesPorIntervalo < mensajesPorIntervalo_min) {

            throw new IllegalArgumentException("message per interval must be bigger than "+mensajesPorIntervalo_min+" message(s)" +
                    '\n' + "requested value: " + this.parametrosTransmision.getMensajesPorIntervalo());

        }else if(mensajesPorIntervalo > mensajesPorIntervalo_max) {

            throw new IllegalArgumentException("message per interval must be smaller than: "+mensajesPorIntervalo_max+" message(s)" +
                     '\n' + "requested value: " + this.parametrosTransmision.getMensajesPorIntervalo());
        }

    }


    /**
     * Comprueba que el número de intervalos esté dentro de los límites
     *  @throws IllegalArgumentException     Si el valor está fuera de los límites
     */
    public void comprobarNumeroIntervalos(int numeroIntervalos) throws IllegalArgumentException {

        int numeroIntervalos_max = this.parametrosTransmisionMax.getNumeroIntervalos();
        int numeroIntervalos_min = this.parametrosTransmisionMin.getNumeroIntervalos();

        if(numeroIntervalos < numeroIntervalos_min) {

            throw new IllegalArgumentException("number of intervals must be bigger than "+numeroIntervalos_min+" interval(s)" +
                    '\n' + "requested value: " + this.parametrosTransmision.getNumeroIntervalos());

        }else if(numeroIntervalos > numeroIntervalos_max) {

            throw new IllegalArgumentException("number of intervals must be smaller than "+numeroIntervalos_max+" interval(s)" +
                    '\n' + "requested value: " + this.parametrosTransmision.getNumeroIntervalos());
        }

    }


    public void comprobarParametrosTransmision(ParametrosTransmision parametrosTransmision)
            throws IllegalArgumentException {
        
        this.comprobarTiempoEntreEnvios(parametrosTransmision.getTiempoEntreEnvios());
        this.comprobarMensajesPorIntervalo(parametrosTransmision.getMensajesPorIntervalo());
        this.comprobarLongitudMensaje(parametrosTransmision.getLongitudDatos());
        this.comprobarNumeroIntervalos(parametrosTransmision.getNumeroIntervalos());
        this.setParametrosTranmision(parametrosTransmision);
    }


    /**
     * @return el límite superior de los parámetros de transmisión
     */
    public ParametrosTransmision getParametrosTransmisionMax() {

        return this.parametrosTransmisionMax;
        
    }

    /**
     * @return el límite inferior de los parámetros de transmisión
     */
     public ParametrosTransmision getParametrosTransmisionMin() {

        return this.parametrosTransmisionMin;

    }

     /**
      * Ajusta el límite inferior
      *
      * @param tee  tiempo entre envíos
      * @param ld   longitud de datos
      * @param mpi  mensajes por intervalo
      * @param ni   número de intervalos
      */
     protected void setParametrosMinimos(int tee, int ld, int mpi, int ni) {

         this.parametrosTransmisionMin.setTiempoEntreEnvios(tee);
         this.parametrosTransmisionMin.setLongitudDatos(ld);
         this.parametrosTransmisionMin.setMensajesPorIntervalo(mpi);
         this.parametrosTransmisionMin.setNumeroIntervalos(ni);

     }


     /**
      * Ajusta el límite superior
      *
      * @param tee  tiempo entre envíos
      * @param ld   longitud de datos
      * @param mpi  mensajes por intervalo
      * @param ni   número de intervalos
      */
     protected void setParametrosMaximos(int tee, int ld, int mpi, int ni) {

         this.parametrosTransmisionMax.setTiempoEntreEnvios(tee);
         this.parametrosTransmisionMax.setLongitudDatos(ld);
         this.parametrosTransmisionMax.setMensajesPorIntervalo(mpi);
         this.parametrosTransmisionMax.setNumeroIntervalos(ni);

     }


     /**
      * Devuelve los parámetros de tranmsisión seleccionads/ajustados
      *
      * @return los parámetros de transmisión seleccionados
      */
     public ParametrosTransmision getParametrosTransmision() {
        
         return new ParametrosTransmision(this.parametrosTransmision);

    }


    
}
