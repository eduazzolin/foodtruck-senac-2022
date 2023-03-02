package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.vo.EnderecoVO;

public class EnderecoDAO {

	public EnderecoVO cadastrarEnderecoDAO(EnderecoVO enderecoVO) {
		String query = "INSERT INTO ENDERECO (CEP, RUA, NUMERO, COMPLEMENTO ) "
				+ "VALUES (?, ?, ?, ?)";
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);  
		ResultSet resultado = null;	
		try {
			pstmt.setString(1, enderecoVO.getCep());
			pstmt.setString(2, enderecoVO.getRua());
			pstmt.setString(3, enderecoVO.getNumero());
			pstmt.setString(4, enderecoVO.getComplemento());
			
			pstmt.execute();
			resultado = pstmt.getGeneratedKeys();
			if (resultado.next()) {
				enderecoVO.setIdEndereco(Integer.parseInt(resultado.getString(1)));
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método cadastrarEnderecoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}
		
		return enderecoVO;
	}

	public EnderecoVO consultarEnderecoPorIdUsuarioDAO(int idUsuario) {
		EnderecoVO enderecoVO = new EnderecoVO();
		String query = "SELECT * FROM ENDERECO WHERE IDENDERECO = (SELECT IDENDERECO FROM USUARIO WHERE IDUSUARIO = " + idUsuario + ")";
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;	
		
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				enderecoVO.setIdEndereco(Integer.parseInt(resultado.getString(1)));;
				enderecoVO.setCep(resultado.getString(2));
				enderecoVO.setRua(resultado.getString(3));
				enderecoVO.setNumero(resultado.getString(4));
				enderecoVO.setComplemento(resultado.getString(5));
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarEnderecoPorIdUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		
		return enderecoVO;
	}

}
