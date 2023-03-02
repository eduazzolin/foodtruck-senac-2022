package controller;

import java.util.ArrayList;

import model.bo.UsuarioBO;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class UsuarioController {

	public UsuarioVO realizarLoginController(UsuarioVO usuarioVO) {
		// vai devolver com todas as informações pegas do banco
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.realizarLoginBO(usuarioVO); 
	}

	public ArrayList<TipoUsuarioVO> consultarTipoUsuario() {
		// retorna uma lista dos tipos de usuario do banco
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.consultarTipoUsuarioBO(); 
	}

	public UsuarioVO cadastrarUsuarioController(UsuarioVO usuarioVO) {
		// vai inserir e devolver com todas as informações pegas do banco
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.cadastrarUsuarioBO(usuarioVO); 
	}

	public boolean excluirUsuarioController(UsuarioVO usuarioVO) {
		// insere a data de expiração e retorna um boolean
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.excluirUsuarioBO(usuarioVO); 
	}

	public boolean atualizarUsuarioController(UsuarioVO usuarioVO) {
		 // atualiza no banco e retorna um boolean
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.atualizarUsuarioBO(usuarioVO);
	}

	public ArrayList<UsuarioVO> consultarTodosUsuariosController() {
		// retorna um array list com todos os usuarios do banco
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.consultarTodosUsuariosBO();
	}

	public UsuarioVO consultarUsuarioController(UsuarioVO usuarioVO) {
		// vai devolver com todas as informações pegas do banco. igual ao login eu acho.
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.consultarUsuarioBO(usuarioVO);
	}

	public boolean atualizarEnderecoUsuarioController(UsuarioVO usuarioVO) {
		// vai atualizar no banco somente o ID endereço que estiver no usuario VO do parâmetro
		// só precisa que o usuarioVO tenha IDusuario
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.atualizarEnderecoUsuarioBO(usuarioVO);
	}


}
