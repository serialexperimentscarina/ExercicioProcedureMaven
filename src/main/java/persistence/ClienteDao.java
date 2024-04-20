package persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class ClienteDao implements IClienteDao{
	
	private GenericDao gDao;
	
	public ClienteDao(GenericDao gDao) {
		this.gDao = gDao;
	}

	@Override
	public String iudCliente(String acao, Cliente c) throws SQLException, ClassNotFoundException {
		Connection connection = gDao.getConnection();
		String sql = "{CALL sp_iud_cliente(?,?,?,?,?,?,?)}";
		CallableStatement cs = connection.prepareCall(sql);
		cs.setString(1, acao);
		cs.setString(2, c.getCpf());
		cs.setString(3, c.getNome());
		cs.setString(4, c.getEmail());
		cs.setDouble(5, c.getLimiteDeCredito());
		cs.setDate(6, c.getDtNascimento());
		cs.registerOutParameter(7, Types.VARCHAR);
		cs.execute();
		
		String saida = cs.getString(7);
		cs.close();
		connection.close();
		return saida;
	}

	@Override
	public Cliente consultar(Cliente c) throws SQLException, ClassNotFoundException {
		Connection connection = gDao.getConnection();
		String sql = "SELECT cpf, nome, email, limite_de_credito, dt_nascimento FROM cliente WHERE cpf = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, c.getCpf());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			c.setNome(rs.getString("nome"));
			c.setEmail(rs.getString("email"));
			c.setLimiteDeCredito(rs.getDouble("limite_de_credito"));
			c.setDtNascimento(rs.getDate("dt_nascimento"));
		}
		rs.close();
		ps.close();
		connection.close();
		return c;
	}

	@Override
	public List<Cliente> listar() throws SQLException, ClassNotFoundException {
		List<Cliente> clientes = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT cpf, nome, email, limite_de_credito, dt_nascimento FROM cliente";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Cliente cli = new Cliente();
			cli.setCpf(rs.getString("cpf"));
			cli.setNome(rs.getString("nome"));
			cli.setEmail(rs.getString("email"));
			cli.setLimiteDeCredito(rs.getDouble("limite_de_credito"));
			cli.setDtNascimento(rs.getDate("dt_nascimento"));
			
			clientes.add(cli);
		}
		rs.close();
		ps.close();
		c.close();
		return clientes;
	}
	
	

}
