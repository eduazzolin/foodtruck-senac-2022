package controller;

import model.bo.EntregaBO;
import model.vo.SituacaoEntregaVO;
import model.vo.VendaVO;

public class EntregaController {

	public boolean atualizarSituacaoEntregaController(VendaVO vendaVO) {
		EntregaBO entregaBO = new EntregaBO();
		return entregaBO.atualizarSituacaoEntregaBO(vendaVO);
	}
	
	// SOBRECARGA: Parametrizando a situação
	public boolean atualizarSituacaoEntregaController(VendaVO vendaVO, SituacaoEntregaVO situacao) {
		EntregaBO entregaBO = new EntregaBO();
		return entregaBO.atualizarSituacaoEntregaBO(vendaVO, situacao);
	}

}
