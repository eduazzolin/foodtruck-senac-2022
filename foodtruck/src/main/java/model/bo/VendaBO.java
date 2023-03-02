package model.bo;

import model.dao.EntregaDAO;
import model.dao.ProdutoDAO;
import model.dao.UsuarioDAO;
import model.dao.VendaDAO;
import model.vo.ItemVendaVO;
import model.vo.SituacaoEntregaVO;
import model.vo.VendaVO;

public class VendaBO {

	public VendaVO cadastrarVendaBO(VendaVO vendaVO) { // OK!
		// verificações:
		// 1. existência do usuário (redundante)
		// 2. existência do produto (redundante)
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		// verificar existência do usuário (redundante):
		if(usuarioDAO.verificarExistenciaRegistroPorIdUsuarioDAO(vendaVO.getIdUsuario())) {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			boolean listaItensValida = true;
			for (ItemVendaVO itemVendaVO : vendaVO.getListaItemVendaVO()) {
				// verificar existência do produto (redundante):
				if (!produtoDAO.verificarExistenciaRegistroPorIdProdutoDAO(itemVendaVO.getIdProduto())) {
					System.out.println("O produto de ID " + itemVendaVO.getIdItemVenda() + " não existe no banco de dados.");
				listaItensValida = false;
				}
			}
			// passando adiante caso esteja tudo certo: 
			if (listaItensValida) {
				VendaDAO vendaDAO = new VendaDAO();
				// cadastrar a venda no banco de dados:
				vendaVO = vendaDAO.cadastrarVendaDAO(vendaVO);
				// verificando se a venda foi registrada no banco
				if(vendaVO.getIdVenda() != 0) { 
					boolean resultado = vendaDAO.cadastrarItemVendaDAO(vendaVO); // registrando na tabela itemVenda
					if (!resultado) {
						System.out.println("Não foi possível incluir algum item do produto");
					}
					// registrando entrega caso haja:
					if(vendaVO.isFlagEntrega()) {
						EntregaBO entregaBO = new EntregaBO();
						resultado = entregaBO.cadastrarEntregaBO(vendaVO.getIdVenda());
						if (!resultado) {
							System.out.println("Não foi possível cadastrar entrega");
						}
					}
					// colocando o numero do pedido no VO:
					// o número é gerado automaticamento pelo banco de dados através de trigger
					vendaVO.setNumeroPedido(vendaDAO.retornarNumeroPedidoDAO(vendaVO.getIdVenda()));
				} else {
					System.out.println("\nNão foi possível cadastrar a venda.");
				}
			}
		} else { // caso o usuário não esteja no banco de dados:
			System.out.println("O usuário desta venda não existe no banco de dados.");
		}
		
		return vendaVO;
	}

	public boolean verificarVendaParaAtualizarSituacaoEntrega(VendaVO vendaVO) { // OK!
		// verificações:
		// se a venda existe (não foi implementado por que já foi verificado antes)
		// se a venda possui entrega
		// se a venda não foi cancelada
		// se a venda já foi entregue
		// verificar se a entrega foi cancelada
		
		VendaDAO vendaDAO = new VendaDAO();
		EntregaDAO entregaDAO = new EntregaDAO();
		boolean retorno = false;
		if(vendaDAO.verificarVendaPossuiEntregaDAO(vendaVO.getIdVenda())) {
			if (!vendaDAO.verificarCancelamentoPorIdVendaDAO(vendaVO.getIdVenda())) {
				if(entregaDAO.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda()).getDataEntrega() == null) {
					if(entregaDAO.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda()).getSituacaoEntrega().getValor() != 6) {
						retorno = true;
					} else {
						System.out.println("\nEntrega já foi cancelda.");
					}
				} else {
					System.out.println("\nVenda já foi entregue.");
				}
			} else {
				System.out.println("\nVenda já foi cancelada.");
			}
		} else {
			System.out.println("\nVenda não possui entrega.");
		}
		
		
		return retorno;
	}

	public boolean cancelarVendaBO(VendaVO vendaVO) { // OK!
		// verificações:
		// 1. já foi cancelada? 
		// 2. a data é > que a do cadastro?
		// 3. tá em rota ou entregue?
		// cancelar a entrega também
		
		VendaDAO vendaDAO = new VendaDAO();
		EntregaDAO entregaDAO = new EntregaDAO();
		boolean resultado = false;
		// verificando se a venda já está cancelada:
		if (!vendaDAO.verificarCancelamentoPorIdVendaDAO(vendaVO.getIdVenda())) {
			// verificando se a data de cancelamento é maior que a data de cadastro:
			if (vendaVO.getDataCancelamento().isAfter(vendaDAO.consultarDataCadastroPorIdVendaDAO(vendaVO.getIdVenda()))) {
				// verificando se a venda possui entrega:
				if (vendaDAO.verificarVendaPossuiEntregaDAO(vendaVO.getIdVenda())) {
					// verificando se a entrega está em rota ou concluída:
					if (!entregaDAO.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda()).getSituacaoEntrega().equals(SituacaoEntregaVO.EM_ROTA_DE_ENTREGA) &&
						!entregaDAO.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda()).getSituacaoEntrega().equals(SituacaoEntregaVO.PEDIDO_ENTREGUE)) {
						// cancelar a entrega:
						resultado = entregaDAO.atualizarSituacaoEntregaDAO(vendaVO, SituacaoEntregaVO.PEDIDO_CANCELADO);
						
						if (resultado) { // se atualizaou certinho a entrega, então cancela a venda
							// cancelar a venda:
							resultado = vendaDAO.cancelarVendaDAO(vendaVO); 
						}
					} else {
						System.out.println("Pedido está em rota de entrega ou já foi entregue.");
					}
				} else {// no caso de não ter entrega
					// só cancelar a venda:
					resultado = vendaDAO.cancelarVendaDAO(vendaVO); 
				}
			} else {
				System.out.println("Data de cancelamento não pode ser anterior à data de cadastro.");
			}
			
		} else {
			System.out.println("Venda já se encontra cancelada no banco de dados.");
		}
		return resultado;
	}

}
