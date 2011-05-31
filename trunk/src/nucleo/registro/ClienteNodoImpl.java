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


package nucleo.registro;

import nucleo.cliente.ControlNodos;
import nucleo.nodo.NodoRemoto;
import nucleo.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Antonio
 */
public class ClienteNodoImpl extends UnicastRemoteObject implements ClienteNodo{


    /**
     *  Programa cliente que contiene y utiliza la referencia remota al nodo remoto
     */
    private ControlNodos controlNodos;


    /**
     * Crea el cliente a partir del control del nodo local y remoto
     */
    public ClienteNodoImpl(int puertoCliente, ControlNodos controlNodos)
            throws RemoteException{

        super(puertoCliente);
        this.controlNodos = controlNodos;

    }

    /**
     * Asígna la referencia al nodo remoto
     *
     * @param nodoRemoto    Referencia remota al nodo remoto
     */
    public synchronized void poseerNodoRemoto(NodoRemoto nodoRemoto) 
            throws RemoteException{

        this.controlNodos.setNodoRemoto(nodoRemoto);

    }

    /**
     * Elimina la referencia remota al nodo remoto
     */
    public synchronized void liberarNodoRemoto()
            throws RemoteException{

        this.controlNodos.setNodoRemoto(null);

    }


    /**
     * Comprueba si el control del nodo posee el nodo remoto <code>nodoRemoto</code>
     *
     * @return <code>true</code>    Si el control del nodo posee el nodo <code>nodoRemoto</code>
     */
    public synchronized boolean esPoseedorNodoRemoto( NodoRemoto nodoRemoto)
            throws RemoteException{

        if (this.controlNodos.getNodoRemoto() == nodoRemoto)
            return true;

        return false;

    }


    
    
}

