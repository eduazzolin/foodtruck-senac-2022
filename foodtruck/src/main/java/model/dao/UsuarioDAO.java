package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class UsuarioDAO {

	public UsuarioVO realizarLoginDAO(UsuarioVO usuarioVO) {
		Connection con = Banco.getConnection();
		Statement stmt = Banco.getStatement(con);
		ResultSet resultado = null;
		
		String query = "SELECT u.idusuario, tipo.descricao, u.nome, u.cpf, u.email, "
				+ "u.telefone, u.datacadastro, u.dataexpiracao "
				+ "FROM usuario u, TIPOUSUARIO tipo "
				+ "WHERE u.login LIKE '" + usuarioVO.getLogin() + "' "
				+ " AND u.senha LIKE '" + usuarioVO.getSenha() + "' "
				+ " AND u.idtipousuario = tipo.idTipoUsuario";
		
		try {
			resultado = stmt.executeQuery(query);
			if(resultado.next()) {
				usuarioVO.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuarioVO.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuarioVO.setNome(resultado.getString(3));
				usuarioVO.setCpf(resultado.getString(4));
				usuarioVO.setEmail(resultado.getString(5));
				usuarioVO.setTelefone(resultado.getString(6));
				usuarioVO.setDataCadastro(LocalDateTime.parse(resultado.getString(7), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				if(resultado.getString(8) != null) { // validação porque se for nulo vai quebrar
					usuarioVO.setDataExpiracao(LocalDateTime.parse(resultado.getString(8), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método realizarLoginDAO()");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(con);
		}
		
		return usuarioVO;
	}

	public ArrayList<TipoUsuarioVO> consultarTipoUsuarioDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<TipoUsuarioVO> listaTipoUsuarioVO = new ArrayList<TipoUsuarioVO>();
		String query = "SELECT DESCRICAO FROM TIPOUSUARIO";
		try {
			resultado = stmt.executeQuery(query);
			while (resultado.next()) {
				TipoUsuarioVO tipoUsuarioVO = TipoUsuarioVO.valueOf(resultado.getString(1));
				listaTipoUsuarioVO.add(tipoUsuarioVO);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarTipoUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return listaTipoUsuarioVO;
	}

	public boolean verificarExistenciaRegistroPorCpfDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		
		String query = "SELECT cpf FROM USUARIO WHERE CPF = '" + usuarioVO.getCpf() + "'";
		try {
			resultado = stmt.executeQuery(query); 
			if (resultado.next()) { 
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarExistenciaRegistroPorCpfDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return retorno;
	}

	public UsuarioVO cadastrarUsuarioDAO(UsuarioVO usuarioVO) {
		String query = "INSERT INTO USUARIO (IDTIPOUSUARIO, NOME, CPF, EMAIL, TELEFONE, DATACADASTRO, LOGIN, SENHA, IDENDERECO) "
									+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);  // USA-SE ISSO PARA CRIAR MÁSCARAS ??" como as do try
		ResultSet resultado = null;														// AO INVÉS DE CONCATENAR GETS NA STRING do query
		try {
			pstmt.setInt(1, usuarioVO.getTipoUsuario().getValor());
			pstmt.setString(2, usuarioVO.getNome());
			pstmt.setString(3, usuarioVO.getCpf());
			pstmt.setString(4, usuarioVO.getEmail());
			pstmt.setString(5, usuarioVO.getTelefone());
			pstmt.setObject(6, usuarioVO.getDataCadastro()); // local date time é um objeto
			pstmt.setString(7, usuarioVO.getLogin());
			pstmt.setString(8, usuarioVO.getSenha());
			if (usuarioVO.getIdEndereco()>0) {pstmt.setInt(9, usuarioVO.getIdEndereco());}
			else {pstmt.setNull(9, 0);}
			
			
			
			pstmt.execute();
			resultado = pstmt.getGeneratedKeys();
			if(resultado.next()) {
				usuarioVO.setIdUsuario(Integer.parseInt(resultado.getString(1)));
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método cadastrarUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt); // ou closeStatement, não sei rs
			Banco.closeConnection(conn);
		}
		
		
		return usuarioVO;
	}

	public boolean excluirUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		
		// EXCLUSÃO FÍSICA: TIRA O REGISTRO COM DELETE
		// EXCLUSÃO LÓGICA: SÓ INVALIDA COM UMA INDICAÇÃO. NO CASO É COM A DATA DE EXPIRAÇÃO.
		// aqui vai ser usado a  lógica para manter o histórico.
		String query = "UPDATE USUARIO SET DATAEXPIRACAO = '" + usuarioVO.getDataExpiracao() + "' WHERE IDUSUARIO = " + usuarioVO.getIdUsuario();

		// NÃO É EXECUTE QUERY PQ VAI MOVIMENTAR O BANCO, DAÍ É UPDATE.
		// diferentemente do executeQuery, o executeUpdate retorna a quantidade de linhas afetadas, por isso.
		try{
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método excluirUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean verificarDesligamentoUsuarioPorIdUsuarioDAO(int idUsuario) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		
		String query = "SELECT DATAEXPIRACAO FROM USUARIO WHERE IDUSUARIO = " + idUsuario;
		try {
			resultado = stmt.executeQuery(query); 
			if (resultado.next()) { 
				String dataExpiracao = resultado.getString(1);
				if (dataExpiracao != null) {
					retorno = true;
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarDesligamentoUsuarioPorIdUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return retorno;
	}

	public boolean verificarExistenciaRegistroPorIdUsuarioDAO(int idUsuario) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		
		String query = "SELECT IDUSUARIO FROM USUARIO WHERE IDUSUARIO = " + idUsuario;
		try {
			resultado = stmt.executeQuery(query); 
			if (resultado.next()) { 
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarExistenciaRegistroPorIdUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return retorno;
	}

	public boolean atualizarUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query  = "UPDATE USUARIO SET IDTIPOUSUARIO = " + usuarioVO.getTipoUsuario().getValor();
		if (!usuarioVO.getNome().isEmpty()) {query	+= ", NOME = '" + usuarioVO.getNome() + "'";}
		if (!usuarioVO.getCpf().isEmpty()) {query += ", CPF = '" + usuarioVO.getCpf() + "'";}
		if (!usuarioVO.getEmail().isEmpty()) {query += ", EMAIL = '" + usuarioVO.getEmail() + "'";}
		if (!usuarioVO.getTelefone().isEmpty()) {query	+= ", TELEFONE = '" + usuarioVO.getTelefone() + "'";}
		query += ", DATACADASTRO = '" + usuarioVO.getDataCadastro() + "'";
		if (!usuarioVO.getLogin().isEmpty()) {query	+= ", LOGIN = '" + usuarioVO.getLogin() + "'";}
		if (!usuarioVO.getSenha().isEmpty()) {query	+= ", SENHA = '" + usuarioVO.getSenha() + "'";}
		query += " WHERE IDUSUARIO = "+ usuarioVO.getIdUsuario();

		try{
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método atualizarUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
		
	}

	public ArrayList<UsuarioVO> consultarTodosUsuariosDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<UsuarioVO> listaUsuarioVO = new ArrayList<UsuarioVO>();
		
		String query = 	"SELECT U.IDUSUARIO, TIPO.DESCRICAO, U.NOME, U.CPF, U.EMAIL, U.TELEFONE, "
						+ "U.DATACADASTRO, U.DATAEXPIRACAO, U.LOGIN, U.SENHA "
						+ "FROM USUARIO U, TIPOUSUARIO TIPO "
						+ "WHERE U.IDTIPOUSUARIO = TIPO.IDTIPOUSUARIO";
		
		try {
			resultado = stmt.executeQuery(query);
			while(resultado.next()) {
				UsuarioVO usuario = new UsuarioVO();
				usuario.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuario.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuario.setNome(resultado.getString(3));
				usuario.setCpf(resultado.getString(4));
				usuario.setEmail(resultado.getString(5));
				usuario.setTelefone(resultado.getString(6));
				usuario.setDataCadastro(LocalDateTime.parse(resultado.getString(7), 
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				if(resultado.getString(8) != null) { // evitando erro ao passar valor nulo
					usuario.setDataExpiracao(LocalDateTime.parse(resultado.getString(8), 
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				} 
				usuario.setLogin(resultado.getString(9));
				usuario.setSenha(resultado.getString(10));
				listaUsuarioVO.add(usuario);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarTodosUsuariosDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return listaUsuarioVO;
	}

	public UsuarioVO consultarUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		UsuarioVO usuario = new UsuarioVO();
		
		String query = 	"SELECT U.IDUSUARIO, TIPO.DESCRICAO, U.NOME, U.CPF, U.EMAIL, U.TELEFONE, "
						+ "U.DATACADASTRO, U.DATAEXPIRACAO, U.LOGIN, U.SENHA, U.IDENDERECO "
						+ "FROM USUARIO U, TIPOUSUARIO TIPO "
						+ "WHERE U.IDTIPOUSUARIO = TIPO.IDTIPOUSUARIO "
						+ "and u.idusuario = " + usuarioVO.getIdUsuario() ;
		
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				usuario.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuario.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuario.setNome(resultado.getString(3));
				usuario.setCpf(resultado.getString(4));
				usuario.setEmail(resultado.getString(5));
				usuario.setTelefone(resultado.getString(6));
				usuario.setDataCadastro(LocalDateTime.parse(resultado.getString(7), 
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				if(resultado.getString(8) != null) { // evitando erro ao passar valor nulo
					usuario.setDataExpiracao(LocalDateTime.parse(resultado.getString(8), 
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				} 
				usuario.setLogin(resultado.getString(9));
				usuario.setSenha(resultado.getString(10));
				if (resultado.getString(11) != null) {
					usuario.setIdEndereco(Integer.parseInt(resultado.getString(11)));
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return usuario;
	}

	public ArrayList<UsuarioVO> consultarListaEntregadores() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<UsuarioVO> listaEntregadores = new ArrayList<UsuarioVO>();
		
		String query = 	"SELECT U.IDUSUARIO, TIPO.DESCRICAO, U.NOME, U.CPF, U.EMAIL, U.TELEFONE, "
						+ "U.DATACADASTRO,U.LOGIN, U.SENHA "
						+ "FROM USUARIO U, TIPOUSUARIO TIPO "
						+ "WHERE U.IDTIPOUSUARIO = TIPO.IDTIPOUSUARIO "
						+ "AND U.IDTIPOUSUARIO = " + TipoUsuarioVO.ENTREGADOR.getValor()
						+ " AND U.DATAEXPIRACAO IS NULL";
		
		try {
			resultado = stmt.executeQuery(query);
			while(resultado.next()) {
				UsuarioVO usuario = new UsuarioVO();
				usuario.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuario.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuario.setNome(resultado.getString(3));
				usuario.setCpf(resultado.getString(4));
				usuario.setEmail(resultado.getString(5));
				usuario.setTelefone(resultado.getString(6));
				usuario.setDataCadastro(LocalDateTime.parse(resultado.getString(7), 
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				usuario.setLogin(resultado.getString(8));
				usuario.setSenha(resultado.getString(9));
				listaEntregadores.add(usuario);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarListaEntregadores");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return listaEntregadores;
	}

	public boolean atualizarEnderecoUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query  = "UPDATE USUARIO SET IDENDERECO = " + usuarioVO.getIdEndereco() + " WHERE IDUSUARIO = " + usuarioVO.getIdUsuario();
		
		try{
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método atualizarEnderecoUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	

}
