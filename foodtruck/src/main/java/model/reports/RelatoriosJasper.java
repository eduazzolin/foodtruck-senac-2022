package model.reports;

import java.io.IOException;
import java.util.HashMap;

import model.dao.Banco;
import model.vo.VendaVO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import view.MenuVenda;

public class RelatoriosJasper {

	public void gerarRelatorioVendasReports() {
        try {
            String currentPath = "";
            try {
                currentPath = new java.io.File(".").getCanonicalPath();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
            JasperRunManager.runReportToPdfFile(currentPath + "/report1.jasper", currentPath + "/REL01.pdf", null, Banco.getConnection());
            System.out.println("Relatorio gerado em " + currentPath + "/REL01.pdf");
        } catch (JRException ex) {
            System.out.println("Não foi possivel imprimir, por favor verifique o modelo de impressão");
        }
	}
	public void gerarRelatorioAcompanhamentoVendasReports(int idVenda) {
		try {
			String currentPath = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			// CONSTRUINDO O 'WHERE CLAUSE'
			String whereClause = "";
			if (idVenda > 0) {
				whereClause = "VENDA.IDVENDA = " + idVenda;
			} else {
				whereClause = "1=1";
			}
			map.put("whereClause", whereClause);
					
			
			try {
				currentPath = new java.io.File(".").getCanonicalPath();
			} catch (IOException ex) {
				System.out.println(ex.toString());
			}

			JasperRunManager.runReportToPdfFile(currentPath + "/report2.jasper", currentPath + "/REL02.pdf", map, Banco.getConnection());
			System.out.println("Relatorio gerado em " + currentPath + "/REL02.pdf");
		} catch (JRException ex) {
			System.out.println("Não foi possivel imprimir, por favor verifique o modelo de impressão");
		}
	}
	
	public void gerarRelatorioGuiaFreteReports() {
		try {
			String currentPath = "";
			try {
				currentPath = new java.io.File(".").getCanonicalPath();
			} catch (IOException ex) {
				System.out.println(ex.toString());
			}
			JasperRunManager.runReportToPdfFile(currentPath + "/report3.jasper", currentPath + "/REL03.pdf", null, Banco.getConnection());
			System.out.println("Relatorio gerado em " + currentPath + "/REL03.pdf");
		} catch (JRException ex) {
			System.out.println("Não foi possivel imprimir, por favor verifique o modelo de impressão");
		}
	}
}
