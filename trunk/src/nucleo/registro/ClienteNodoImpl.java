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
    public ClienteNodoImpl(ControlNodos controlNodos)
            throws RemoteException{

        super();
        this.controlNodos = controlNodos;

    }

    /**
     * AsÃ­gna la referencia al nodo remoto
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

