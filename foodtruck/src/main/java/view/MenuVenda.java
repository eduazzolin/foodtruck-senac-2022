package view;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import controller.EnderecoController;
import controller.EntregaController;
import controller.ProdutoController;
import controller.RelatorioController;
import controller.UsuarioController;
import controller.VendaController;
import model.dto.HistoricoVendasDTO;
import model.dto.ResumoVendaDTO;
import model.vo.EnderecoVO;
import model.vo.ItemVendaVO;
import model.vo.ProdutoVO;
import model.vo.SituacaoEntregaVO;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;
import model.vo.VendaVO;

public class MenuVenda {

	private static final int OPCAO_MENU_CADASTRAR_VENDA = 1;
	private static final int OPCAO_MENU_CANCELAR_VENDA = 2;
	private static final int OPCAO_MENU_SITUACAO_ENTREGA = 3;
	private static final int OPCAO_MENU_CANCELAR_ENTREGA = 4;
	private static final int OPCAO_MENU_VENDA_VOLTAR = 9;

	
	private static double PERCENTUAL = 0.05; // PARA CALCULAR A TAXA DE ENTREGA
		
	Scanner teclado = new Scanner(System.in);
	DecimalFormat deci = new DecimalFormat("0.00");
	
	public void apresentarMenuVenda(UsuarioVO usuarioVO) { // OK!
		int opcao = this.apresentarOpcoesMenu(usuarioVO);
		while (opcao != OPCAO_MENU_VENDA_VOLTAR) {
			switch (opcao) {
				case OPCAO_MENU_CADASTRAR_VENDA:{
					if (!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ENTREGADOR)) {
						this.cadastrarVenda(usuarioVO);
					}
					break;
				}
				case OPCAO_MENU_CANCELAR_VENDA:{
					if (!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ENTREGADOR)) {
						this.cancelarVenda();
					}
					break;
				}
				case OPCAO_MENU_SITUACAO_ENTREGA:{
					if(!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.CLIENTE)) {
						this.atualizarSituacaoEntrega();
					}
					break;
				}
				case OPCAO_MENU_CANCELAR_ENTREGA:{
					if(!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.CLIENTE)) {
						this.atualizarSituacaoEntrega(SituacaoEntregaVO.ENTREGA_CANCELADA);
					}
					break;
				}
				default: {
					System.out.println("Opção inválida.");
				}
			}
			opcao = this.apresentarOpcoesMenu(usuarioVO);
		}
		
	}
	private int apresentarOpcoesMenu(UsuarioVO usuarioVO) { // OK!
		try {
			System.out.println("\n---------- Sistema FoodTruck ----------");
			System.out.println("-> Menu Vendas");
			System.out.println("\nOpções:");
			if (!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ENTREGADOR)) {
				System.out.println(OPCAO_MENU_CADASTRAR_VENDA + " - Cadastrar Venda");
				System.out.println(OPCAO_MENU_CANCELAR_VENDA + " - Cancelar Venda");
			}
			if(!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.CLIENTE)) {
				System.out.println(OPCAO_MENU_SITUACAO_ENTREGA + " - Situação da Entrega");
			}
			if(!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.CLIENTE)) {
				System.out.println(OPCAO_MENU_CANCELAR_ENTREGA + " - Cancelar Entrega");
			}
			System.out.println(OPCAO_MENU_VENDA_VOLTAR + " - Voltar");
			System.out.print("Digite uma opção: ");
			return Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
			// voltar se for opção inválida
			return OPCAO_MENU_VENDA_VOLTAR;
		}
	}

	private void cadastrarVenda(UsuarioVO usuarioVO) { // OK!
		VendaVO vendaVO = new VendaVO();
		VendaController vendaController = new VendaController();
		boolean tudoOkComUsuario = true;
		UsuarioVO usuarioExterno = new UsuarioVO();
		
		// pegando id do comprador:
		if(usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.CLIENTE)) {
			// caso o usuário seja cliente
			vendaVO.setIdUsuario(usuarioVO.getIdUsuario());
		} else {
			// caso o usuário não seja cliente e esteja registrando a compra de tericeiros
			UsuarioController usuarioController = new UsuarioController();
			do {
				tudoOkComUsuario = true;
				try {
					System.out.print("Informe o código do cliente: ");
					usuarioExterno.setIdUsuario(Integer.parseInt(teclado.nextLine()));
					usuarioExterno = usuarioController.consultarUsuarioController(usuarioExterno);
					if (usuarioExterno.getNome() != null) {
						System.out.println("Cliente: " + usuarioExterno.getNome());
						vendaVO.setIdUsuario(usuarioExterno.getIdUsuario());
					} else {
						System.out.println();
						tudoOkComUsuario = false;
					}
				} catch (NumberFormatException e) {
					tudoOkComUsuario = false;
					System.out.println("Opção inválida!");
				}
			} while (!tudoOkComUsuario);
		} 
		
		
		// incluindo ítens:
		if (tudoOkComUsuario) {
			
			// método que retorna um arrayList contendo os VOs dos produtos vigentes
			ArrayList<ProdutoVO> listaProdutosVO = this.listarProdutos(); 
			
			vendaVO.setDataVenda(LocalDateTime.now());
			double subtotal = 0;
			ArrayList<ItemVendaVO> listaItemVendaVO = new ArrayList<ItemVendaVO>();
			String continuar = "N";
			do {
				ItemVendaVO itemVendaVO = new ItemVendaVO();
				
				// método que recebe o id e a qtd do produto comprado
				// retorna o valor total dele 
				// e atualiza o array de listaItemVendaVO por referência
				subtotal += this.cadastrarItemVendaVO(itemVendaVO, listaProdutosVO);
				listaItemVendaVO.add(itemVendaVO);
				
				do {
					System.out.print("Deseja incluir mais um item? (S/N): ");
					continuar = teclado.nextLine();
					if (!continuar.equalsIgnoreCase("s") && !continuar.equalsIgnoreCase("n")) {
						System.out.println("Opção inválida!");
					}
				} while (!continuar.equalsIgnoreCase("s") && !continuar.equalsIgnoreCase("n"));
			} while(continuar.equalsIgnoreCase("S"));
			vendaVO.setListaItemVendaVO(listaItemVendaVO);
			
			
			// entrega:
			String opcaoEntrega = "";
			double taxaEntrega = 0;
			double totalConta = 0;
			do {
				System.out.print("Pedido é para entregar? (S/N): ");
				opcaoEntrega = teclado.nextLine();
				taxaEntrega = subtotal * PERCENTUAL;
				totalConta = subtotal;
				if(opcaoEntrega.equalsIgnoreCase("s")) {
					vendaVO.setFlagEntrega(true);
					vendaVO.setTaxaEntrega(taxaEntrega);
					totalConta += taxaEntrega;
					
					// verificando endereço:
					EnderecoController enderecoController = new EnderecoController();
					EnderecoVO enderecoVO = enderecoController.consultarEnderecoPorIdUsuarioController(vendaVO.getIdUsuario());
					
					// cadastrando um endereço caso o usuário não possua nenhum:
					if(enderecoVO == null) {
						MenuUsuario menuUsuario = new MenuUsuario(); // isso não parece estar certo rs... mas ok...
						UsuarioVO usuarioFinal = new UsuarioVO();
						usuarioFinal.setIdUsuario(vendaVO.getIdUsuario());
						menuUsuario.cadastrarEndereco(usuarioFinal);
					}
					
				}
				if(!opcaoEntrega.equalsIgnoreCase("s") && !opcaoEntrega.equalsIgnoreCase("n")) {
					System.out.println("Opção inválida");
				}
			} while (!opcaoEntrega.equalsIgnoreCase("s") && !opcaoEntrega.equalsIgnoreCase("n")); 
			
			
			// finalizando:
			// método que valida o ID do cliente e a lista de produtos da venda:
			if(this.validarCamposCadastro(vendaVO)) {
				
				// passando para o banco de dados e preenchendo com o id e com o número do pedido:
				// isso também cadastra na tabela itemVenda
				vendaVO = vendaController.cadastrarVendaController(vendaVO);
				this.imprimirResumoDaVenda(vendaVO, usuarioExterno, usuarioVO, taxaEntrega, totalConta);
				if(vendaVO.getIdVenda() != 0) {
					System.out.println("\n\nVenda cadastrada com sucesso!\n");
				} else {
					System.out.println("\n\nNão foi possível cadastrar a venda!");
				}
				
			}
		}
		
	}
	
	
	private void imprimirResumoDaVenda(VendaVO vendaVO, UsuarioVO usuarioExterno, UsuarioVO usuarioVO, double taxaEntrega, double totalConta) { // OK!
		System.out.println("\n---------- Resumo do Pedido ----------");
		System.out.println("Venda: " + vendaVO.getIdVenda());
		System.out.println("Pedido: " + vendaVO.getNumeroPedido());
		
		// informações do cliente:
		System.out.print("\nCliente: ");
		if (usuarioExterno.getNome() != null) {
			System.out.println(usuarioExterno.getNome() + " \nCPF: "  + usuarioExterno.getCpf());
		} else {
			System.out.println(usuarioVO.getNome() + " \nCPF: "  + usuarioVO.getCpf());
		}
		
		// informações dos produtos:
		System.out.printf("\n%3s  %-20s  %-11s  %-5s  %-11s", 
		"ID", "NOME", "PREÇO UNIT", "QTD", "TOTAL");
		RelatorioController relatorioController = new RelatorioController();
		ArrayList<ResumoVendaDTO> listaResumoVenda = relatorioController.gerarRelatorioResumoVendaController(vendaVO);
		for (ResumoVendaDTO resumoVendaDTO : listaResumoVenda) {
			resumoVendaDTO.imprimir();
		}
		
		// entrega e valor total:
		System.out.println("\n");
		if (vendaVO.isFlagEntrega()) {
			System.out.println("Total entrega: R$ " + deci.format(vendaVO.getTaxaEntrega()));
		}
		System.out.print("TOTAL DO PEDIDO: R$ " + deci.format(relatorioController.buscarValorTotalDaVendaController(vendaVO.getIdVenda())));
		
	}
	private boolean validarCamposCadastro(VendaVO vendaVO) { // OK!
		// método que valida o ID do cliente e a lista de produtos da venda;
		boolean resultado = true;
		System.out.println();
		if (vendaVO.getIdUsuario() == 0) {
			System.out.println("O campo código do cliente é obrigatório");
			resultado = false;
		}
		if (vendaVO.getListaItemVendaVO() == null || vendaVO.getListaItemVendaVO().isEmpty()) {
			System.out.println("O campo dos produtos vendidos é obrigatório.");
			resultado = false;
		}
		return resultado;
	}
	
	private double cadastrarItemVendaVO(ItemVendaVO itemVendaVO, ArrayList<ProdutoVO> listaProdutosVO) { // OK!
		// retorna por retorno o total e por referência o objeto preenchido
		boolean found = false;
		double valor = 0;
		do {
			try{
				// recebendo o id e a quantidade:
				System.out.print("Informe o código do produto: ");
				itemVendaVO.setIdProduto(Integer.parseInt(teclado.nextLine()));
				System.out.print("Informe a quantidade do produto: ");
				itemVendaVO.setQuantidade(Integer.parseInt(teclado.nextLine()));
				
				// percorrendo o array de produtos vigentes para ver se encontra o id digitado:
				for (ProdutoVO produto : listaProdutosVO) {
					if(produto.getIdProduto() == itemVendaVO.getIdProduto()) {
						valor = (produto.getPreco() * itemVendaVO.getQuantidade());
						found = true;
					}
				}
				
				// se não encontrou, entra no while novamente pq o 'found' ficou como falso.
				if (found == false) {System.out.println("Produto não encontrado.");}
			} catch (NumberFormatException e) {
				System.out.println("Opção inválida!");
				itemVendaVO.setIdProduto(0);
				itemVendaVO.setQuantidade(0);
			}
		} while (found == false);
		return valor;
	}

	
	private ArrayList<ProdutoVO> listarProdutos() { // OK!
		// método que lista os produtos vigentes e preenche um array list com os VOs deles
		ProdutoController produtoController = new ProdutoController();
		ArrayList<ProdutoVO> listaProdutosVO = produtoController.consultarTodosProdutosVigentesController();
		System.out.println("\n---------- Lista de Produtos ----------");
		System.out.printf("\n%3s  %-13s  %-20s  %-11s  %-20s  ", 
				"ID", "TIPO PRODUTO", "NOME", "PREÇO", "DATA CADASTRO");
		for (int i = 0; i < listaProdutosVO.size(); i++) {
			listaProdutosVO.get(i).imprimir();
		}
		System.out.println("\n");
		return listaProdutosVO;
	}
	private void atualizarSituacaoEntrega() { // OK!
		VendaVO vendaVO = new VendaVO();
		boolean confirmacao = this.confirmarVenda(vendaVO);
		if(confirmacao) {
			EntregaController entregaController = new EntregaController();
			boolean resultado = entregaController.atualizarSituacaoEntregaController(vendaVO);
			if (resultado) {System.out.println("\nSituação da entrega atualizada com sucesso!");}
			else {System.out.println("\nNão foi possível atualizar a situação da entrega.");}
		}
		
	}
	
	// SOBRECARGA: Parametrizando a situação
	private void atualizarSituacaoEntrega(SituacaoEntregaVO situacao) { // OK!
		VendaVO vendaVO = new VendaVO();
		boolean confirmacao = this.confirmarVenda(vendaVO);
		if(confirmacao) {
			EntregaController entregaController = new EntregaController();
			boolean resultado = entregaController.atualizarSituacaoEntregaController(vendaVO, situacao);
			if (resultado) {System.out.println("\nSituação da entrega atualizada com sucesso!");}
			else {System.out.println("\nNão foi possível atualizar a situação da entrega.");}
		}
		
	}
	private void cancelarVenda() { // OK!
		VendaVO vendaVO = new VendaVO();
		
		// pegando id da venda e confirmando:
		boolean confirmacao = this.confirmarVenda(vendaVO);
		
		if (confirmacao) {
			// recebendo a data de cancelamento:
			System.out.print("Digite a data do cancelamento no formato dd/MM/yyyy HH:mm:ss "
					+ "\nOu deixe em branco para pegar instante atual: ");
			String resposta = teclado.nextLine();
			if (resposta.isBlank()) {
				vendaVO.setDataCancelamento(LocalDateTime.now());
			} else {
				try {
					vendaVO.setDataCancelamento(LocalDateTime.parse(resposta, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
				} catch (DateTimeParseException e) {
					System.out.println("Data inválida!");
				}
			}
			
			// cancelando no banco de dados:
			if(vendaVO.getDataCancelamento() != null) {
				VendaController vendaController = new VendaController();
				boolean resultado = vendaController.cancelarVendaController(vendaVO);
				if (resultado) {System.out.println("\nVenda cancelada com sucesso!");}
				else {System.out.println("\nNão foi possível cancelar a venda.");}
			} else { 
				// caso a data esteja nula
				System.out.println("\nNão foi possível cancelar a venda.");
			}
		} else {
			System.out.println("\nNão foi possível cancelar a venda.");
		}
		
		
	}
	
	public boolean confirmarVenda(VendaVO vendaVO) { // OK!
		// método que pede o id da venda, apresenta informações sobre ela, 
		// pede confirmação do usuário e retorna booleano.
		RelatorioController relatorioController = new RelatorioController();
		boolean confirmacao = false;
		try {
			while (!confirmacao) {
				// recebendo o id:
				System.out.print("Digite o código da venda: ");
				vendaVO.setIdVenda(Integer.parseInt(teclado.nextLine()));
				
				// preenchendo com as informações retornadas do banco:
				HistoricoVendasDTO informacoesVenda = relatorioController.consultarInformacoesVendaPorIdVendaController(vendaVO.getIdVenda());
				
				// se o VO retornar sem ID, significa que a venda não foi encontrada. 
				// nesse caso a confirmação continua false e o while reinicia.
				if (informacoesVenda.getIdVenda() != 0) {
					System.out.printf("\n%7s  %6s  %-10s  %-9s  %-11s  %-19s  %-18s",
							"IDVENDA", "PEDIDO", "CLIENTE", "IDCLIENTE", "VALORTOTAL", "DATA", "ENTREGA"); 
					informacoesVenda.imprimir();
					System.out.print("\n\nConfirma (S/N)? ");
					if(teclado.nextLine().equalsIgnoreCase("s")) {
						confirmacao = true;
					}
				} else {
					System.out.println("Venda não encontrada.");
				}
			}
		} catch (NumberFormatException e){
			// vai retornar para o menu anterio
			confirmacao = false;
		}
		return confirmacao;
	}
	
}
