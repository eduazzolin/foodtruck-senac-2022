package controller;

import model.bo.VendaBO;
import model.vo.VendaVO;

public class VendaController {

	public VendaVO cadastrarVendaController(VendaVO vendaVO) {
		// cadastra a venda no banco de dados e retorna um VO com id da venda
		VendaBO vendaBO = new VendaBO();
		return vendaBO.cadastrarVendaBO(vendaVO);
	}

	public boolean cancelarVendaController(VendaVO vendaVO) {
		// registra o cancelamento no banco de dados e retorna um booleano
		VendaBO vendaBO = new VendaBO();
		return vendaBO.cancelarVendaBO(vendaVO);
	}


}
