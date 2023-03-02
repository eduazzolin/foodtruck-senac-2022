package controller;

import java.util.ArrayList;

import model.bo.RelatorioBO;
import model.dao.RelatorioDAO;
import model.dto.HistoricoVendasDTO;
import model.dto.ResumoVendaDTO;
import model.dto.VendasCanceladasDTO;
import model.reports.RelatoriosJasper;
import model.vo.VendaVO;

public class RelatorioController {

	public ArrayList<VendasCanceladasDTO> gerarRelatorioVendasCanceladasController() {
		RelatorioBO relatorioBO = new RelatorioBO();
		return relatorioBO.gerarRelatorioVendasCanceladasBO();
	}

	public ArrayList<ResumoVendaDTO> gerarRelatorioResumoVendaController(VendaVO vendaVO) {
		RelatorioBO relatorioBO = new RelatorioBO();
		return relatorioBO.gerarRelatorioResumoVendaBO(vendaVO);
	}

	public double buscarValorTotalDaVendaController(int idVenda) {
		RelatorioDAO relatorioDAO = new RelatorioDAO();
		return relatorioDAO.buscarValorTotalDaVendaDAO(idVenda);
	}

	public HistoricoVendasDTO consultarInformacoesVendaPorIdVendaController(int idVenda) {
		RelatorioDAO relatorioDAO = new RelatorioDAO();
		return relatorioDAO.consultarInformacoesVendaPorIdVendaDAO(idVenda);
	}

	public void gerarRelatorioVendasController() {
		RelatoriosJasper relatorioVendas = new RelatoriosJasper();
		relatorioVendas.gerarRelatorioVendasReports();
	}

	public void gerarRelatorioAcompanhamentoVendasController(int idVenda) {
		RelatoriosJasper relatorioVendas = new RelatoriosJasper();
		relatorioVendas.gerarRelatorioAcompanhamentoVendasReports(idVenda);
		
	}

	public void gerarRelatorioGuiaFreteController() {
		RelatoriosJasper relatorioVendas = new RelatoriosJasper();
		relatorioVendas.gerarRelatorioGuiaFreteReports();
	}



}
