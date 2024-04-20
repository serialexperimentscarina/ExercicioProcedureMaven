package persistence;

import java.sql.SQLException;
import java.util.List;

import model.Cliente;

public interface IClienteDao {
	
	public String iudCliente(String acao, Cliente c) throws SQLException, ClassNotFoundException;
	
	public Cliente consultar(Cliente c) throws SQLException, ClassNotFoundException;
	
	public List<Cliente> listar() throws SQLException, ClassNotFoundException;
}
