package model.bo;

import java.util.ArrayList;

import model.dao.RelatorioDAO;
import model.dto.ResumoVendaDTO;
import model.dto.VendasCanceladasDTO;
import model.vo.VendaVO;

public class RelatorioBO {

	public ArrayList<VendasCanceladasDTO> gerarRelatorioVendasCanceladasBO() {
		// nenhuma restrição, passa adiante
		RelatorioDAO relatorioDAO = new RelatorioDAO();
		return relatorioDAO.gerarRelatorioVendasCanceladasDAO();
	}

	public ArrayList<ResumoVendaDTO> gerarRelatorioResumoVendaBO(VendaVO vendaVO) {
		// nenhuma restrição, passa adiante
		RelatorioDAO relatorioDAO = new RelatorioDAO();
		return relatorioDAO.gerarRelatorioResumoVendaDAO(vendaVO);
	}

}
