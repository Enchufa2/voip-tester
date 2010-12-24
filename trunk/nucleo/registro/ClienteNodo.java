package nucleo.registro;
import nucleo.nodo.NodoRemoto;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Antonio
 * @version 1.0
 * @created 30-ago-2010 17:19:17
 */
public interface ClienteNodo extends Remote {

	/**
	 * 
	 * @param nodoRemoto
	 */
	public void poseerNodoRemoto(NodoRemoto nodoRemoto) throws RemoteException;

	public void liberarNodoRemoto() throws RemoteException;

        public boolean esPoseedorNodoRemoto( NodoRemoto nodoRemoto)
            throws RemoteException;
        

}