package controller;

import model.bo.EnderecoBO;
import model.vo.EnderecoVO;

public class EnderecoController {

	public EnderecoVO cadastrarEnderecoController(EnderecoVO enderecoVO) {
		// cadastra no banco e retorna um VO completo com ID
		EnderecoBO enderecoBO = new EnderecoBO();
		return enderecoBO.cadastrarEnderecoBO(enderecoVO);
	}

	public EnderecoVO consultarEnderecoPorIdUsuarioController(int idUsuario) {
		// retorna o VO do endereco do id do cliente
		EnderecoBO enderecoBO = new EnderecoBO();
		return enderecoBO.consultarEnderecoPorIdUsuarioBO(idUsuario);
	}


}
