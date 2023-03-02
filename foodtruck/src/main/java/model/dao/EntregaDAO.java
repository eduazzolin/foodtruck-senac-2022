package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.vo.EntregaVO;
import model.vo.SituacaoEntregaVO;
import model.vo.VendaVO;

public class EntregaDAO {

	public boolean cadastrarEntregaDAO(EntregaVO entregaVO) { // OK!
		String query = "INSERT INTO ENTREGA (IDVENDA, IDENTREGADOR, IDSITUACAOENTREGA) "
				+ "VALUES (?, ?, ?)";
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		ResultSet resultado = null;
		boolean retorno = false;
		try {
			pstmt.setInt(1, entregaVO.getIdVenda());
			pstmt.setInt(2, entregaVO.getIdEntregador());
			pstmt.setInt(3, entregaVO.getSituacaoEntrega().getValor());
			pstmt.execute();
			resultado = pstmt.getGeneratedKeys();
			if(resultado.next()) {
				entregaVO.setIdEntrega(Integer.parseInt(resultado.getString(1)));
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método cadastrarEntregaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt); 
			Banco.closeConnection(conn);
		}
		
		return retorno;
	}

	public EntregaVO consultarEntregaPorIdVendaDAO(int idVenda) { // OK!
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		EntregaVO entregaVO = new EntregaVO();
		String query = "SELECT IDENTREGA, IDVENDA, IDENTREGADOR, SITUACAO_ENTREGA, DATAENTREGA"
				+ " FROM VW_ENTREGA_COMPLETA WHERE IDVENDA = " + idVenda;
		
		
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				entregaVO.setIdEntrega(Integer.parseInt(resultado.getString(1)));
				entregaVO.setIdVenda(Integer.parseInt(resultado.getString(2)));
				entregaVO.setIdEntregador(Integer.parseInt(resultado.getString(3)));
				entregaVO.setSituacaoEntrega(SituacaoEntregaVO.valueOf(resultado.getString(4)));
				if (resultado.getString(5) != null) { // evitando erro ao passar valor nulo
					entregaVO.setDataEntrega(LocalDateTime.parse(resultado.getString(5), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarEntregaPorIdVendaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return entregaVO;
	}

	public boolean atualizarSituacaoEntregaDAO(VendaVO vendaVO) { // OK!
		EntregaVO entregaVO = this.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda());
		if (entregaVO.getSituacaoEntrega().getValor() < SituacaoEntregaVO.PEDIDO_ENTREGUE.getValor()) {
			// se a entrega ainda não estiver como entregue, cancelada ou pedido cancelado:
			// avance uma etapa de entrega
			entregaVO.setSituacaoEntrega(SituacaoEntregaVO.getSituacaoEntregaPorValor(entregaVO.getSituacaoEntrega().getValor()+1));
			if (entregaVO.getSituacaoEntrega().getValor() == SituacaoEntregaVO.PEDIDO_ENTREGUE.getValor()) {
				// se a entrega foi mudada para entregue:
				// set a data da entrega
				entregaVO.setDataEntrega(LocalDateTime.now());
			}
		} 
			
		
		return this.atualizarEntregaDAO(entregaVO);
	}

	private boolean atualizarEntregaDAO(EntregaVO entregaVO) { // OK!
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "UPDATE ENTREGA SET IDSITUACAOENTREGA = " + entregaVO.getSituacaoEntrega().getValor();
		if (entregaVO.getDataEntrega() != null) {
			query += ", DATAENTREGA = '" + entregaVO.getDataEntrega() + "'";
		}
		query += " WHERE IDENTREGA = " + entregaVO.getIdEntrega();
		
		try {
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método atualizarEntregaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return retorno;
	}

	// SOBRECARGA PARAMETRIZANDO O TIPO DE ENTREGA:
		public boolean atualizarSituacaoEntregaDAO(VendaVO vendaVO, SituacaoEntregaVO situacao) {
			EntregaVO entregaVO = this.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda());
			entregaVO.setSituacaoEntrega(situacao);
			if (entregaVO.getSituacaoEntrega().getValor() == SituacaoEntregaVO.PEDIDO_ENTREGUE.getValor()) {
				entregaVO.setDataEntrega(LocalDateTime.now());
			}
			return this.atualizarEntregaDAO(entregaVO);
		}

	
	
	
}
