package model.bo;

import controller.UsuarioController;
import model.dao.EnderecoDAO;
import model.vo.EnderecoVO;
import model.vo.UsuarioVO;

public class EnderecoBO {

	public EnderecoVO cadastrarEnderecoBO(EnderecoVO enderecoVO) {
		// Em um caso real até teria várias validações eu imagino, tipo tem que ser dentro de uma região etc...
		// Aqui eu vou passar direto 
		EnderecoDAO enderecoDAO = new EnderecoDAO();
		return enderecoDAO.cadastrarEnderecoDAO(enderecoVO);
	}

	public EnderecoVO consultarEnderecoPorIdUsuarioBO(int idUsuario) {
		// verificando se o usuário possui endereço cadastrado
		UsuarioController usuarioController = new UsuarioController();
		UsuarioVO usuarioVO = new UsuarioVO();
		EnderecoDAO enderecoDAO = new EnderecoDAO();
		usuarioVO.setIdUsuario(idUsuario);
		usuarioVO = usuarioController.consultarUsuarioController(usuarioVO);
		if (usuarioVO.getIdEndereco() == 0) {
			return null;
		} else {
			return enderecoDAO.consultarEnderecoPorIdUsuarioDAO(idUsuario);
		}
	}

}
