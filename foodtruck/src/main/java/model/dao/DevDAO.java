package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DevDAO {

	public void executarQuery(String query) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		int resultado = 0;
		try {
			resultado = stmt.executeUpdate(query);
		} catch (SQLException erro) {
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		System.out.println("Linhas afetadas: " + resultado);
		
	}

}
