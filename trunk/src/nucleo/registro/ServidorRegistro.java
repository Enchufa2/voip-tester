package nucleo.registro;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Antonio
 * @version 1.0
 * @created 30-ago-2010 17:19:15
 */
public interface ServidorRegistro extends Remote {

	/**
	 * 
	 * @param clienteNodo
	 */
	public void registrarCliente(ClienteNodo clienteNodo) throws RemoteException, RegistroException;

	/**
	 * 
	 * @param clienteNodo
	 */
	public void desconectarCliente(ClienteNodo clienteNodo) throws RemoteException, RegistroException;

        public void notificarActividad(ClienteNodo clienteNodo) throws RemoteException, RegistroException;

}