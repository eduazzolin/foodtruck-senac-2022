package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.vo.ItemVendaVO;
import model.vo.VendaVO;

public class VendaDAO {

	public VendaVO cadastrarVendaDAO(VendaVO vendaVO) { // OK!
		String query = "INSERT INTO VENDA (IDUSUARIO, DATAVENDA, FLAGENTREGA";
		if (vendaVO.isFlagEntrega()) {
			query += ", TAXAENTREGA) VALUES (?, ?, ?, ?)";
		} else {
			query += ") VALUES (?, ?, ?)";
		}
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);  // USA-SE ISSO PARA CRIAR MÁSCARAS 
		ResultSet resultado = null;	
		try {
			pstmt.setInt(1, vendaVO.getIdUsuario());
			pstmt.setObject(2, vendaVO.getDataVenda());
			if(vendaVO.isFlagEntrega()) {
				pstmt.setInt(3, 1); // flag
				pstmt.setDouble(4, vendaVO.getTaxaEntrega());
			} else {
				pstmt.setInt(3, 0); // flag
			}
			pstmt.execute();
			resultado = pstmt.getGeneratedKeys();
			if(resultado.next()) {
				vendaVO.setIdVenda(resultado.getInt(1));
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método cadastrarVendaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt); 
			Banco.closeConnection(conn);
		}
		return vendaVO;
	}

	public boolean cadastrarItemVendaDAO(VendaVO vendaVO) { // OK!
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;   
		int contador = 0;
		boolean retorno = false;
		try {
			for (ItemVendaVO item : vendaVO.getListaItemVendaVO()) {
				String query = "INSERT INTO ITEMVENDA (IDVENDA, IDPRODUTO, QUANTIDADE) VALUES (?, ?, ?)";
				pstmt = Banco.getPreparedStatementWithPk(conn, query);
				pstmt.setInt(1, vendaVO.getIdVenda());
				pstmt.setInt(2, item.getIdProduto());
				pstmt.setInt(3, item.getQuantidade());
				pstmt.execute();
				ResultSet resultado = pstmt.getGeneratedKeys();
				if(resultado.next()) { // preenchendo os VOs de item venda com seus IDS
					item.setIdItemVenda(resultado.getInt(1));
					contador++;
				}
			}
			if (contador == vendaVO.getListaItemVendaVO().size()) { // isso significa que todos deram certo
				retorno = true;
			} else {
				System.out.println("Nem todos os produtos foram cadastrados.");
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método cadastrarItemVendaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closePreparedStatement(pstmt); 
			Banco.closeConnection(conn);
		}
		
		return retorno;
	}

	public int retornarNumeroPedidoDAO(int idVenda) { // OK!
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		String query = "SELECT NUMEROPEDIDO FROM VENDA WHERE IDVENDA = " + idVenda;
		ResultSet resultado = null;
		int numeroPedido = 0;
		
		try {
			resultado = stmt.executeQuery(query);
			if(resultado.next()) {
				numeroPedido = Integer.parseInt(resultado.getString(1));
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método retornarNumeroPedidoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt); 
			Banco.closeConnection(conn);
		}
		
		
		return numeroPedido;
	}

	public boolean verificarVendaPossuiEntregaDAO(int idVenda) { // OK!
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		String query = "SELECT FLAGENTREGA FROM VENDA WHERE IDVENDA = " + idVenda;
		ResultSet resultado = null;
		boolean retorno = false;
		
		try {
			resultado = stmt.executeQuery(query);
			if(resultado.next()) {
				if(resultado.getString(1).equals("1")) {
					retorno = true;
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarVendaPossuiEntregaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt); 
			Banco.closeConnection(conn);
		}
		
		return retorno;
	}

	public boolean verificarCancelamentoPorIdVendaDAO(int idVenda) { // OK!
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		String query = "SELECT DATACANCELAMENTO FROM VENDA WHERE IDVENDA = " + idVenda;
		ResultSet resultado = null;
		boolean retorno = false;
		
		try {
			resultado = stmt.executeQuery(query);
			if(resultado.next()) {
				if(resultado.getString(1) != null) {
					retorno = true;
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarCancelamentoPorIdVendaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt); 
			Banco.closeConnection(conn);
		}
		
		return retorno;
	}

	public LocalDateTime consultarDataCadastroPorIdVendaDAO(int idVenda) { // OK!
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		String query = "SELECT DATAVENDA FROM VENDA WHERE IDVENDA = " + idVenda;
		LocalDateTime dataVenda = null;
		
		try {
			resultado = stmt.executeQuery(query);
			if(resultado.next()) {
				dataVenda = LocalDateTime.parse(resultado.getString(1), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarDataCadastroPorIdVendaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt); 
			Banco.closeConnection(conn);
		}
		return dataVenda;
	}

	public boolean cancelarVendaDAO(VendaVO vendaVO) { // OK!
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		String query = "UPDATE VENDA SET DATACANCELAMENTO = '" + vendaVO.getDataCancelamento() 
			+ "' WHERE IDVENDA = " + vendaVO.getIdVenda();
		boolean retorno = false;
		
		try {
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método cancelarVendaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt); 
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	

	
}
