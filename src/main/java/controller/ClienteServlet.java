package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Cliente;
import persistence.ClienteDao;
import persistence.GenericDao;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ClienteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("cliente.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("botao");
		String cpf = request.getParameter("cpf");
		String nome = request.getParameter("nome");
		String email = request.getParameter("email");
		String limiteDeCredito = request.getParameter("limite");
		String dtNascimento = request.getParameter("dtNascimento");
		
		String saida = "";
		String erro = "";
		Cliente c = new Cliente();
		List<Cliente> clientes = new ArrayList();
		
		if(!cmd.contains("Listar")) {
			c.setCpf(cpf);
		}
		if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
			c.setNome(nome);
			c.setEmail(email);
			c.setLimiteDeCredito(Double.parseDouble(limiteDeCredito));
			c.setDtNascimento(Date.valueOf(dtNascimento));
		}
		
		try {
			if (cmd.contains("Cadastrar")) {
				saida = cadastrarCliente(c);
				c = null;
			}
			if (cmd.contains("Alterar")) {
				saida = alterarCliente(c);
				c = null;
			}
			if (cmd.contains("Excluir")) {
				saida = excluirCliente(c);
				c = null;
			}
			if (cmd.contains("Buscar")) {
				c = buscarCliente(c);
			}
			if (cmd.contains("Listar")) {
				clientes = listarClientes();
			}
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("cliente", c);
			request.setAttribute("clientes", clientes);
			
			RequestDispatcher rd = request.getRequestDispatcher("cliente.jsp");
			rd.forward(request, response);
		}
	}

	private List<Cliente> listarClientes() throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		List<Cliente> clientes = cDao.listar();
		return clientes;
	}

	private Cliente buscarCliente(Cliente c) throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		c = cDao.consultar(c);
		return c;
	}

	private String cadastrarCliente(Cliente c) throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		String saida = cDao.iudCliente("I", c);
		return saida;
	}
	
	private String alterarCliente(Cliente c) throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		String saida = cDao.iudCliente("U", c);
		return saida;
	}
	
	private String excluirCliente(Cliente c) throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		String saida = cDao.iudCliente("D", c);
		return saida;
	}
	
}
