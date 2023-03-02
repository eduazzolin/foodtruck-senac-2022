package model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.dto.HistoricoVendasDTO;
import model.dto.ResumoVendaDTO;
import model.dto.VendasCanceladasDTO;
import model.vo.VendaVO;

public class RelatorioDAO {

	public ArrayList<VendasCanceladasDTO> gerarRelatorioVendasCanceladasDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<VendasCanceladasDTO> vendasCanceladasDTO = new ArrayList<VendasCanceladasDTO>();
		
		String query = 
				"SELECT"
				+ " 	U.NOME AS NOME,"
				+ "		V.DATACANCELAMENTO AS DATA_CANCELAMENTO,"
				+ "		IFNULL((SELECT SUM(P.PRECO * ITV.QUANTIDADE) FROM ITEMVENDA ITV, PRODUTO P WHERE ITV.IDVENDA = V.IDVENDA AND ITV.IDPRODUTO = P.IDPRODUTO GROUP BY IDVENDA), 0) AS SUBTOTAL,"
				+ "		IFNULL(V.TAXAENTREGA,0) AS TAXA_ENTREGA,"
				+ "		IFNULL((V.TAXAENTREGA + (SELECT SUM(P.PRECO * ITV.QUANTIDADE) FROM ITEMVENDA ITV, PRODUTO P WHERE ITV.IDVENDA = V.IDVENDA AND ITV.IDPRODUTO = P.IDPRODUTO GROUP BY IDVENDA)),0) AS TOTAL"
				+ " FROM USUARIO U, VENDA V"
				+ " WHERE V.IDUSUARIO = U.IDUSUARIO AND V.DATACANCELAMENTO IS NOT NULL";
		try {
			resultado = stmt.executeQuery(query);
			while(resultado.next()) {
				VendasCanceladasDTO vendaCanceladaDTO = new VendasCanceladasDTO();
				vendaCanceladaDTO.setNome(resultado.getString(1));
				vendaCanceladaDTO.setDataCancelamento(LocalDateTime.parse(resultado.getString(2), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				vendaCanceladaDTO.setSubtotal(Double.parseDouble(resultado.getString(3)));
				vendaCanceladaDTO.setTaxaEntrega(Double.parseDouble(resultado.getString(4)));
				vendaCanceladaDTO.setTotal(Double.parseDouble(resultado.getString(5)));
				vendasCanceladasDTO.add(vendaCanceladaDTO);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar query do método gerarRelatorioVendasCanceladasDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		
		return vendasCanceladasDTO;
	}

	public ArrayList<ResumoVendaDTO> gerarRelatorioResumoVendaDAO(VendaVO vendaVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<ResumoVendaDTO> listaResumoVendaDTO = new ArrayList<ResumoVendaDTO>();
		String query = "SELECT IDPRODUTO, NOME, PRECO_UNIT, QUANTIDADE, TOTAL "
				+ "FROM VW_RESUMO_VENDA WHERE IDVENDA = " + vendaVO.getIdVenda();

		try {
			resultado = stmt.executeQuery(query);
			while(resultado.next()) {
				ResumoVendaDTO resumoVendaDTO = new ResumoVendaDTO();
				resumoVendaDTO.setIdVenda(vendaVO.getIdVenda());
				resumoVendaDTO.setIdProduto(Integer.parseInt(resultado.getString(1)));
				resumoVendaDTO.setNomeProduto(resultado.getString(2));
				resumoVendaDTO.setPrecoUnitario(Double.parseDouble(resultado.getString(3)));
				resumoVendaDTO.setQuantidade(Integer.parseInt(resultado.getString(4)));
				resumoVendaDTO.setPrecoTotal(Double.parseDouble(resultado.getString(5)));
				listaResumoVendaDTO.add(resumoVendaDTO);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar query do método gerarRelatorioResumoVendaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return listaResumoVendaDTO;
	}

	public double buscarValorTotalDaVendaDAO(int idVenda) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		double valorTotal = 0;
		String query = "SELECT VALOR_TOTAL FROM VW_HISTORICO_VENDA WHERE IDVENDA = " + idVenda;
		
		try {
			resultado = stmt.executeQuery(query);
			if(resultado.next()) {
				valorTotal = Double.parseDouble(resultado.getString(1));
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar query do método buscarValorTotalDaVendaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return valorTotal;
	}

	public HistoricoVendasDTO consultarInformacoesVendaPorIdVendaDAO(int idVenda) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		HistoricoVendasDTO informacoesVenda = new HistoricoVendasDTO();
		String query = "SELECT IDVENDA, "
				+ "NUMEROPEDIDO, "
				+ "CLIENTE, "
				+ "IDUSUARIO, "
				+ "VALOR_TOTAL, "
				+ "DATAVENDA,"
				+ "ENTREGA "
				+ "FROM VW_HISTORICO_VENDA WHERE IDVENDA = " + idVenda;
		try {
			resultado = stmt.executeQuery(query);
			if(resultado.next()) {
				informacoesVenda.setIdVenda(Integer.parseInt(resultado.getString(1)));
				informacoesVenda.setNumeroPedido(Integer.parseInt(resultado.getString(2)));
				informacoesVenda.setNomeUsuario(resultado.getString(3));
				informacoesVenda.setIdUsuario(Integer.parseInt(resultado.getString(4)));
				informacoesVenda.setValorTotal(Double.parseDouble(resultado.getString(5)));
				informacoesVenda.setDataVenda(LocalDateTime.parse(resultado.getString(6), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				informacoesVenda.setSituacaoEntrega(resultado.getString(7));
			} 
										
		} catch (SQLException erro) {
			System.out.println("Erro ao executar query do método consultarInformacoesVendaPorIdVendaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return informacoesVenda;
	}

}
