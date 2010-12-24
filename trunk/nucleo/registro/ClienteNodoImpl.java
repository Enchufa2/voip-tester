/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
