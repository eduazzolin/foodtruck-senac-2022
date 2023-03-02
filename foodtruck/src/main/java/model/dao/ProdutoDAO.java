package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class ProdutoDAO {

	public ArrayList<TipoProdutoVO> consultarTipoProdutoDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<TipoProdutoVO> listaProdutoVO = new ArrayList<TipoProdutoVO>();
		String query = "SELECT DESCRICAO FROM TIPOPRODUTO";
		
		try {
			resultado = stmt.executeQuery(query);
			while (resultado.next()) {
				TipoProdutoVO tipoProdutoVO = TipoProdutoVO.valueOf(resultado.getString(1));
				listaProdutoVO.add(tipoProdutoVO);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarTipoProduto");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return listaProdutoVO;
	}

	public ProdutoVO cadastrarProdutoDAO(ProdutoVO produtoVO) {
		String query = "INSERT INTO PRODUTO (IDTIPOPRODUTO, NOME, PRECO, DATACADASTRO)"
				+ " VALUES (?, ?, ?, ?)";
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		ResultSet resultado = null;
		try {
			pstmt.setInt(1, produtoVO.getTipoProduto().getValor());
			pstmt.setString(2, produtoVO.getNome());
			pstmt.setDouble(3, produtoVO.getPreco());
			pstmt.setObject(4, produtoVO.getDataCadastro());
			
			pstmt.execute();
			resultado = pstmt.getGeneratedKeys();
			if(resultado.next()) {
				produtoVO.setIdProduto(Integer.parseInt(resultado.getString(1)));
			}
		} catch(SQLException erro){
			System.out.println("Erro ao executar a query do método cadastrarProdutoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}
		
		return produtoVO;
	}

	public boolean verificarExistenciaRegistroPorIdProdutoDAO(int idProduto) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		String query = "SELECT IDPRODUTO FROM PRODUTO WHERE IDPRODUTO = " + idProduto;
		boolean found = false;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				found = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarExistenciaRegistroPorIdProdutoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return found;
	}

	public boolean verificarExclusaoPorIdProdutoDAO(int idProduto) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		String query = "SELECT DATAEXCLUSAO FROM PRODUTO WHERE IDPRODUTO = " + idProduto;
		boolean found = false;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				String dataExclusao = resultado.getString(1);
				if(dataExclusao != null) {
					found = true;
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarExclusaoPorIdProdutoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return found;
	}

	public boolean excluirProdutoDAO(ProdutoVO produtoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		String query = "UPDATE PRODUTO SET DATAEXCLUSAO = '" + produtoVO.getDataExclusao() 
			+ "' WHERE IDPRODUTO = " + produtoVO.getIdProduto();
		boolean retorno = false;
		try {
			// NÃO É EXECUTE QUERY PQ VAI MOVIMENTAR O BANCO, DAÍ É UPDATE.
			// diferentemente do executeQuery, o executeUpdate retorna a quantidade de linhas afetadas, por isso.
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método excluirProdutoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public ArrayList<ProdutoVO> consultarTodosProdutosDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<ProdutoVO> listaProdutoVO = new ArrayList<ProdutoVO>();
		
		String query = "SELECT P.IDPRODUTO, TIPO.DESCRICAO, P.NOME, P.PRECO, P.DATACADASTRO, P.DATAEXCLUSAO"
				+ " FROM PRODUTO P, TIPOPRODUTO TIPO "
				+ "WHERE P.IDTIPOPRODUTO = TIPO.IDTIPOPRODUTO";
		
		try {
			resultado = stmt.executeQuery(query);
			while (resultado.next()) {
				ProdutoVO produtoVO = new ProdutoVO();
				produtoVO.setIdProduto(Integer.parseInt(resultado.getString(1)));
				produtoVO.setTipoProduto(TipoProdutoVO.valueOf(resultado.getString(2)));
				produtoVO.setNome(resultado.getString(3));
				produtoVO.setPreco(Double.parseDouble(resultado.getString(4)));
				produtoVO.setDataCadastro(LocalDateTime.parse(resultado.getString(5), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				if (resultado.getString(6) != null) { // evitando erro ao passar valor nulo
					produtoVO.setDataExclusao(LocalDateTime.parse(resultado.getString(6), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
				listaProdutoVO.add(produtoVO);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarTodosProdutosDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return listaProdutoVO;
	}

	public ProdutoVO consultarProdutoDAO(ProdutoVO produtoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ProdutoVO produto = new ProdutoVO();
		String query = "SELECT P.IDPRODUTO, TIPO.DESCRICAO, P.NOME, P.PRECO, P.DATACADASTRO, P.DATAEXCLUSAO"
				+ " FROM PRODUTO P, TIPOPRODUTO TIPO "
				+ "WHERE P.IDTIPOPRODUTO = TIPO.IDTIPOPRODUTO"
				+ " AND P.IDPRODUTO = " + produtoVO.getIdProduto();
		
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				produto.setIdProduto(Integer.parseInt(resultado.getString(1)));
				produto.setTipoProduto(TipoProdutoVO.valueOf(resultado.getString(2)));
				produto.setNome(resultado.getString(3));
				produto.setPreco(Double.parseDouble(resultado.getString(4)));
				produto.setDataCadastro(LocalDateTime.parse(resultado.getString(5), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				if (resultado.getString(6) != null) { // evitando erro ao passar valor nulo
					produto.setDataExclusao(LocalDateTime.parse(resultado.getString(6), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarProdutoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return produto;
	}

	public ArrayList<ProdutoVO> consultarTodosProdutosVigentesDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<ProdutoVO> listaProdutoVO = new ArrayList<ProdutoVO>();
		
		String query = "SELECT P.IDPRODUTO, TIPO.DESCRICAO, P.NOME, P.PRECO, P.DATACADASTRO"
				+ " FROM PRODUTO P, TIPOPRODUTO TIPO "
				+ "WHERE P.IDTIPOPRODUTO = TIPO.IDTIPOPRODUTO"
				+ " and p.dataexclusao is null";
		
		try {
			resultado = stmt.executeQuery(query);
			while (resultado.next()) {
				ProdutoVO produtoVO = new ProdutoVO();
				produtoVO.setIdProduto(Integer.parseInt(resultado.getString(1)));
				produtoVO.setTipoProduto(TipoProdutoVO.valueOf(resultado.getString(2)));
				produtoVO.setNome(resultado.getString(3));
				produtoVO.setPreco(Double.parseDouble(resultado.getString(4)));
				produtoVO.setDataCadastro(LocalDateTime.parse(resultado.getString(5), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				listaProdutoVO.add(produtoVO);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarTodosProdutosVigentesDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return listaProdutoVO;
	}

	public boolean atualizarProdutoDAO(ProdutoVO produtoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "UPDATE PRODUTO SET IDTIPOPRODUTO = " + produtoVO.getTipoProduto().getValor();
		if (!produtoVO.getNome().isEmpty()) {query += ", NOME = '" + produtoVO.getNome() + "'";}
		if (produtoVO.getPreco() != 0) {query += ", PRECO = " + produtoVO.getPreco();}
		query += ", DATACADASTRO = '" + produtoVO.getDataCadastro() + "' "
				+ "WHERE IDPRODUTO = " + produtoVO.getIdProduto();
		
		try {
			if(stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método atualizarProdutoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	

}
