package model.bo;

import java.util.ArrayList;

import model.dao.UsuarioDAO;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class UsuarioBO {

	public UsuarioVO realizarLoginBO(UsuarioVO usuarioVO) {
		//não tem nenhuma regra de negócio então passa pra frente
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.realizarLoginDAO(usuarioVO); 
	}

	public ArrayList<TipoUsuarioVO> consultarTipoUsuarioBO() {
		//não tem nenhuma regra de negócio então passa pra frente
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.consultarTipoUsuarioDAO(); 
	}

	public UsuarioVO cadastrarUsuarioBO(UsuarioVO usuarioVO) {
		
		// 1ª regra: o cpf já existe?
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if(usuarioDAO.verificarExistenciaRegistroPorCpfDAO(usuarioVO)) {
			System.out.println("Usuário já cadastrado.");
		} else {
			usuarioVO = usuarioDAO.cadastrarUsuarioDAO(usuarioVO);
		}
		return usuarioVO;
	}

	public boolean excluirUsuarioBO(UsuarioVO usuarioVO) {
		
		// 1ª regra: o usuário existe?
		// 2ª regra: o usuário já foi desligado?
		
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if(usuarioDAO.verificarExistenciaRegistroPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
			if(usuarioDAO.verificarDesligamentoUsuarioPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
				System.out.println("\nUsuário já se encontra desligado no banco de dados.");
			} else {
				resultado = usuarioDAO.excluirUsuarioDAO(usuarioVO);
			}
		} else {
			System.out.println("\nUsuário não existe no banco de dados.");
		}
		return resultado;
	}

	public boolean atualizarUsuarioBO(UsuarioVO usuarioVO) {
		
		// 1ª regra: o usuário existe?
		// 2ª regra: o usuário já foi desligado?
		
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if(usuarioDAO.verificarExistenciaRegistroPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
			if(usuarioDAO.verificarDesligamentoUsuarioPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
				System.out.println("\nUsuário já se encontra desligado no banco de dados.");
			} else {
				resultado = usuarioDAO.atualizarUsuarioDAO(usuarioVO);
			}
		} else {
			System.out.println("\nUsuário não existe no banco de dados.");
		}
		return resultado;
	}

	public ArrayList<UsuarioVO> consultarTodosUsuariosBO() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		ArrayList<UsuarioVO> listaUsuarioVO = usuarioDAO.consultarTodosUsuariosDAO();
		if(listaUsuarioVO.isEmpty()) {System.out.println("\nNão há nenhum usuário no banco de dados.");}
		return listaUsuarioVO;
	}

	public UsuarioVO consultarUsuarioBO(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		UsuarioVO usuario = usuarioDAO.consultarUsuarioDAO(usuarioVO);
		if(usuario.getIdUsuario() == 0) {System.out.println("Usuário não localizado.");}
		return usuario;
	}

	public boolean atualizarEnderecoUsuarioBO(UsuarioVO usuarioVO) {
		// sem verificações
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.atualizarEnderecoUsuarioDAO(usuarioVO);
	}


}
