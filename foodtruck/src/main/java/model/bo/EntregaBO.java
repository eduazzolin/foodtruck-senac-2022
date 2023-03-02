package model.bo;

import java.util.ArrayList;
import java.util.Random;

import model.dao.EntregaDAO;
import model.dao.UsuarioDAO;
import model.vo.EntregaVO;
import model.vo.SituacaoEntregaVO;
import model.vo.UsuarioVO;
import model.vo.VendaVO;

public class EntregaBO {

	public boolean cadastrarEntregaBO(int idVenda) { // OK!
		// verificar: se tem entregador no banco de dados
		boolean retorno = true;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		ArrayList<UsuarioVO> listaEntregadores = usuarioDAO.consultarListaEntregadores();
		if (listaEntregadores.isEmpty()) {
			System.out.println("Não há entregadores cadastrados.");
			retorno = false;
		} else {
			// escolhendo um entregador aleatoriamente:
			Random gerador = new Random();
			UsuarioVO entregador = listaEntregadores.get(gerador.nextInt(listaEntregadores.size()));
			EntregaVO entregaVO = new EntregaVO(0, idVenda, entregador.getIdUsuario(), SituacaoEntregaVO.PEDIDO_REALIZADO, null);
			EntregaDAO entregaDAO = new EntregaDAO();
			boolean resultado = entregaDAO.cadastrarEntregaDAO(entregaVO);
			if(!resultado) {
				System.out.println("Não foi possível cadastrar entrega.");
				retorno = false;
			}
		} 
		return retorno;
	}

	public boolean atualizarSituacaoEntregaBO(VendaVO vendaVO) { // OK!
		// as verificações foram feitas pelo BO de vendas: 
		// se a venda não foi cancelada
		// se a venda já foi entregue
		boolean retorno = false;
		EntregaDAO entregaDAO = new EntregaDAO();
		VendaBO vendaBO = new VendaBO();
		boolean resultado = vendaBO.verificarVendaParaAtualizarSituacaoEntrega(vendaVO);
		if (resultado) {
			retorno = entregaDAO.atualizarSituacaoEntregaDAO(vendaVO);
		}
		return retorno;
	}
	
	// SOBRECARGA: Parametrizando a situação
	public boolean atualizarSituacaoEntregaBO(VendaVO vendaVO, SituacaoEntregaVO situacao) { // OK!
		// as verificações foram feitas pelo BO de vendas: 
		// se a venda não foi cancelada
		// se a venda já foi entregue
		boolean retorno = false;
		EntregaDAO entregaDAO = new EntregaDAO();
		VendaBO vendaBO = new VendaBO();
		boolean resultado = vendaBO.verificarVendaParaAtualizarSituacaoEntrega(vendaVO);
		if (resultado) {
			retorno = entregaDAO.atualizarSituacaoEntregaDAO(vendaVO, situacao);
		}
		return retorno;
	}

}
