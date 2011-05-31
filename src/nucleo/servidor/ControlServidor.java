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


package nucleo.servidor;

import nucleo.registro.ServidorRegistroImpl;
import nucleo.registro.RegistroException;
import nucleo.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.NoSuchObjectException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.rmi.RMISecurityManager;


/**
 *
 * @author Antonio
 */
public class ControlServidor {


    /**
     * Registro RMI donde se exportan los objetos remotos
     */
    private Registry registroRmi;


    /**
     * Objeto remoto que ofrece el servicio de registro de cliente para acceder
     * al Nodo remoto, necesario para realizar las estimaciones
     */
    private ServidorRegistroImpl servidorRegistro;


    /**
     * Objeto de log
     */
    public final Logger registroLogger;

    
    /**
     * Se inicia el objeto creando el objeto de servidor de registro, pero sin
     * exportarlo al registro RMI
     */
    public ControlServidor(){

        this.servidorRegistro = new ServidorRegistroImpl();

        this.registroLogger = Logger.getLogger("nucleo.registro");
        System.setProperty("java.rmi.server.disableHttp","true");

    }


    /**
     * Inicia el registro RMI en el puerto <code>puertoLocal</code>
     * y exporta el objeto de servidor de registro.
     *
     * @param   hostLocal       Dirección de host donde se exporta el servidor de registro
     * @param   puertoLocal     Puerto donde se inicia el registro RMI y donde
     *                          se exporta el servidor de registro
     *
     * @throws ControlServidorException     Si no se puede iniciar el registro RMI, o
     *                                      no se puede acceder a él.
     *                                      Si ocurre un error en la operación con el registro
     *
     * @see nucleo.registro.ServidorRegistroImpl
     * @see nucleo.registro.RegistroException
     *
     */
    public void registrarServidor(String hostLocal, int puertoLocal)
            throws ControlServidorException {
        
        try{

            this.registroRmi = LocateRegistry.createRegistry(puertoLocal);
            this.registroLogger.log(Level.FINER, "RMI register started and bound to: " + puertoLocal);

        }catch(RemoteException re) {

            String mensaje = "Error while starting RMI register";
            this.registroLogger.log(Level.FINER, mensaje, re);
            throw new ControlServidorException(mensaje, re);

        }

        try{

            this.servidorRegistro.registrarServidor(hostLocal, puertoLocal);
            this.registroLogger.log(
                    Level.FINER, "Register server started and exported to RMI registry. " +
                    "Bound to: " + hostLocal +":"+puertoLocal
                    );

        }catch (RemoteException re) {

            String mensaje = "Fail to exporting server to RMI registry";
            this.registroLogger.log(Level.FINER, mensaje, re);
            throw new ControlServidorException(mensaje, re);

        }catch (RegistroException rge) {

            String mensaje = "Error while starting the register server";
            this.registroLogger.log(Level.FINER, mensaje, rge);
            throw new ControlServidorException(mensaje, rge);
        }
      
    }


    /**
     * Elimina el servidor de registro previamente exportado del registro RMI,
     * a la vez que elimina este.
     *
     * @throws ControlServidorException     Si no se puede acceder al registro RMI
     *                                      Si hay un error en la operación con el registro
     *                                      Si el registro RMI no está iniciado
     *
     * @see nucleo.registro.ServidorRegistroImpl
     * @see nucleo.registro.RegistroException
     *
     */
    public void desconectarServidor()
            throws ControlServidorException {

        try{

            this.servidorRegistro.desconectarServidor();
            this.registroLogger.log(Level.FINER, "Register server disconnected. Unexported from RMI registry");

        }catch (RemoteException re) {

            String mensaje = "Fail to unexport from RMI registry";
            this.registroLogger.log(Level.FINER, mensaje, re);
            throw new ControlServidorException(mensaje, re);

        }catch (RegistroException rge) {

            String mensaje = "Error while disconnecting the register server";
            this.registroLogger.log(Level.FINER, mensaje, rge);
            throw new ControlServidorException(mensaje, rge);
        }

        try{

            UnicastRemoteObject.unexportObject(this.registroRmi, true);
            this.registroLogger.log(Level.FINER, "RMI registry deleted");

        }catch (NoSuchObjectException nsoe) {

            String mensaje ="RMI registry not started";
            this.registroLogger.log(Level.FINER, mensaje, nsoe);
            throw new ControlServidorException(mensaje, nsoe);

        }
        

    }


    /**
     * Comprueba si el registro RMI está iniciado y si el servidor de registro está
     * exportado en éste
     *
     * @param puertoLocal   El puerto donde está escucha el registro RMI
     * @return  true       Si el servicio está iniciado correctamente
     *
     * @throws ControlServidorException     Si no se puede acceder al registro RMI
     *                                      Si hay un error en la operación con el registro
     *                                      Si el registro RMI no está iniciado
     *
     * @see nucleo.registro.ServidorRegistroImpl
     * @see nucleo.registro.RegistroException
     *
     */
    public boolean servidorRegistrado( int puertoLocal)
            throws ControlServidorException{

        boolean registrado = false;

        try{
            
            registrado = this.registroRmi.equals(LocateRegistry.getRegistry(puertoLocal));            
            this.registroLogger.log(Level.FINER, "Register server active");

        }catch(RemoteException re){

            String mensaje = "Fail to access to RMI registry";
            this.registroLogger.log(Level.FINER, mensaje, re);
            throw new ControlServidorException(mensaje, re);
            
        }finally{

            return registrado;
        }

    }


    /**
     * Ajusta el tiempo de expiración o periodo de actividad de un cliente registrado
     * en el servidor de registro.
     *
     * @param tiempo_seg    El tiempo de expiración en segundos
     */
    public void ajustarPeriodoActividad(int tiempo_seg) {

        this.servidorRegistro.setTiempoExpiracion(tiempo_seg * 1000);
    }
    

    public Registry getRegistroRmi() {

        return this.registroRmi;
    }
}

