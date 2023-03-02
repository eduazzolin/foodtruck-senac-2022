package view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import controller.ProdutoController;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class MenuProduto {

	private static final int OPCAO_MENU_CADASTRAR_PRODUTO = 1;
	private static final int OPCAO_MENU_CONSULTAR_PRODUTO = 2;
	private static final int OPCAO_MENU_ATUALIZAR_PRODUTO = 3;
	private static final int OPCAO_MENU_EXCLUIR_PRODUTO = 4;
	private static final int OPCAO_MENU_PRODUTO_VOLTAR = 9;
	
	private static final int OPCAO_MENU_CONSULTAR_TODOS_PRODUTOS = 1;
	private static final int OPCAO_MENU_CONSULTAR_UM_PRODUTO = 2;
	private static final int OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR = 9;
	
	Scanner teclado = new Scanner(System.in);
	
	public void apresentarMenuProduto() {
		int opcao = this.apresentarOpcoesMenu();
		while (opcao != OPCAO_MENU_PRODUTO_VOLTAR) {
			switch (opcao) {
				case OPCAO_MENU_CADASTRAR_PRODUTO:{
					this.cadastrarProduto();
					break;
				}
				case OPCAO_MENU_CONSULTAR_PRODUTO:{
					this.consultarProduto();
					break;
				}
				case OPCAO_MENU_ATUALIZAR_PRODUTO:{
					this.atualizarProduto();
					break;
				}
				case OPCAO_MENU_EXCLUIR_PRODUTO:{
					this.excluirProduto();
					break;
				}
				default:{
					System.out.println("Opção inválida.");
				}
			}
			opcao = this.apresentarOpcoesMenu();
		}
		
	}

	
	private int apresentarOpcoesMenu() {
		try {
			System.out.println("\n---------- Sistema FoodTruck ----------");
			System.out.println("-> Menu produto");
			System.out.println("\nOpções:");
			System.out.println(OPCAO_MENU_CADASTRAR_PRODUTO + " - Cadastrar Produto");
			System.out.println(OPCAO_MENU_CONSULTAR_PRODUTO + " - Consultar Produto");
			System.out.println(OPCAO_MENU_ATUALIZAR_PRODUTO + " - Atualizar Produto");
			System.out.println(OPCAO_MENU_EXCLUIR_PRODUTO + " - Excluir Produto");
			System.out.println(OPCAO_MENU_PRODUTO_VOLTAR + " - Voltar");
			System.out.print("Digite uma opção: ");
			return Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
			return OPCAO_MENU_PRODUTO_VOLTAR;
		}
	}
	
	private void cadastrarProduto() { // OK!
		ProdutoVO produtoVO = new ProdutoVO();
		try { 
			// preenchendo os campos:
			produtoVO.setTipoProduto(TipoProdutoVO.getTipoProdutoVOPorValor(this.apresentarOpcoesTipoProduto()));
			produtoVO.setNome(this.inserirNomeValidoUpperCase());
			produtoVO.setPreco(this.inserirPrecoValido());
			produtoVO.setDataCadastro(LocalDateTime.now());
		
			// cadastrando no banco:
			ProdutoController produtoController = new ProdutoController();
			produtoVO = produtoController.cadastrarProdutoController(produtoVO);
			
			// verificando se deu tudo certo:
			if (produtoVO.getIdProduto() != 0) {
				System.out.println("Produto cadastrado com sucesso!");
			} else {
				System.out.println("Não foi possível cadastrar o produto.");
			}
		} catch (NumberFormatException e) {
			// voltar se deixar em branco a primeira pergunta
		}
		
	}
	
	private String inserirNomeValidoUpperCase() {  // OK!
		// método que garante que o nome seja inserido corretamente e os deixa em CAPS
		String nome = "";
		System.out.print("Digite o nome: ");
		nome = teclado.nextLine();
		if (nome.isBlank()) {
			System.out.println("O campo nome é obrigatório.");
			nome = this.inserirNomeValidoUpperCase();
		}
		return nome.toUpperCase();
	}

	private double inserirPrecoValido() { // OK!
		// método que garante que o preço seja inserido corretamente
		boolean ok = false;
		double valor = 0;
		do {
			try {
				System.out.print("Digite o preço: R$ ");
				valor = Double.parseDouble(teclado.nextLine());
				ok = true;
			} catch (NumberFormatException e){
				System.out.println("Valor em branco ou inválido.");
			}
		} while (ok == false);
		return valor;
	}


	private int apresentarOpcoesTipoProduto() { // OK!
		// método que apresenta as opções de tipo produto e recebe a opção desejada
		ProdutoController produtoController = new ProdutoController();
		ArrayList<TipoProdutoVO> listaTipoProdutoVO = produtoController.consultarTipoProduto();
		System.out.println("\n---- Tipos de Produtos ----");
		System.out.println("Opções: ");
		for (int i = 0; i < listaTipoProdutoVO.size(); i++) {
			System.out.println(listaTipoProdutoVO.get(i).getValor() + " - " + listaTipoProdutoVO.get(i));
		}
		System.out.print("\nDigite uma opção: ");
		return Integer.parseInt(teclado.nextLine());
	}

	private void excluirProduto() { // OK!
		ProdutoVO produtoVO = new ProdutoVO();
		ProdutoController produtoController = new ProdutoController();

		// apresentando confirmação:
		boolean confirmacao = this.confirmarProduto(produtoVO);
		
		// definindo a data atual como data de exclusão e atualizando no banco:
		if (confirmacao) {
			produtoVO.setDataExclusao(LocalDateTime.now());
			boolean resultado = produtoController.excluirProdutoController(produtoVO);
			if (resultado) {System.out.println("\nProduto excluído com sucesso!");} 
			else {System.out.println("Não foi possível excluir o produto.");}
		}
				
	}
	
	private void consultarProduto() { // OK!
		int opcao = this.apresentarOpcoesConsulta();
		ProdutoController produtoController = new ProdutoController();
		while (opcao != OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR) {
			switch (opcao) {
				case OPCAO_MENU_CONSULTAR_TODOS_PRODUTOS: {
					opcao = OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR;
					ArrayList<ProdutoVO> listaProdutoVO = produtoController.ConsultarTodosProdutosController();
					System.out.print("\n-> Resultado da consulta");
					System.out.printf("\n%3s  %-13s  %-20s  %-11s  %-20s  %-20s  ", 
							"ID", "TIPO PRODUTO", "NOME", "PREÇO", "DATA CADASTRO", "DATA EXPIRAÇÃO");
					for (int i = 0; i < listaProdutoVO.size(); i++) {
						listaProdutoVO.get(i).imprimir();
					}
					System.out.println();
					break;
				}
				case OPCAO_MENU_CONSULTAR_UM_PRODUTO: {
					try {
						opcao = OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR;
						ProdutoVO produtoVO = new ProdutoVO();
						System.out.print("\nInforme o código do produto: ");
						produtoVO.setIdProduto(Integer.parseInt(teclado.nextLine()));
						if (produtoVO.getIdProduto() > 0) {
							System.out.print("\n-> Resultado da consulta");
							ProdutoVO produto = produtoController.consultarProdutoController(produtoVO);
							if (produto.getIdProduto() != 0) {
								System.out.printf("\n%3s  %-13s  %-20s  %-11s  %-20s  %-20s  ", 
										"ID", "TIPO PRODUTO", "NOME", "PREÇO", "DATA CADASTRO", "DATA EXPIRAÇÃO");
								produto.imprimir();
							System.out.println();
							}
						} else {
							System.out.println("O campo código do produto é obrigatório.");
						}
					} catch (NumberFormatException e) {
						// esse try catch é só para voltar se deixar em branco
					}
					break;
				}
				default:{
					System.out.println("Opção inválida.");
					opcao = this.apresentarOpcoesConsulta();
					break;
				}
			}
		}
	}

	private int apresentarOpcoesConsulta() { // OK!
		try { 
			System.out.println("\nInforme o tipo de consulta a ser realizada: ");
			System.out.println(OPCAO_MENU_CONSULTAR_TODOS_PRODUTOS + " - Consultar todos os produtos");
			System.out.println(OPCAO_MENU_CONSULTAR_UM_PRODUTO + " - Consultar um produto específico");
			System.out.println(OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR + " - Voltar");
			System.out.print("\nDigite uma opção: ");
			return Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
			// voltar se deixar em branco a primeira pergunta
			return OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR;
		}
		
	}
	
	private boolean confirmarProduto (ProdutoVO produtoVO) { // OK!
		// método que pede o id do produto, apresenta informações sobre ele, 
		// pede confirmação do usuario e retorna booleano.
		ProdutoController produtoController = new ProdutoController();
		boolean confirmacao = false;
		try {
			while (!confirmacao) {
				// recebendo o id:
				System.out.print("Digite o código do produto: ");
				produtoVO.setIdProduto(Integer.parseInt(teclado.nextLine()));
				
				// preenchendo com as informações retornadas do banco:
				produtoVO = produtoController.consultarProdutoController(produtoVO);
				
				// se o VO retornar sem ID, significa que o produto não foi encontrado. 
				// nesse caso a confirmação continua false e o while reinicia.
				if (produtoVO.getIdProduto() != 0) {
					System.out.printf("\n%3s  %-13s  %-20s  %-11s  %-20s  %-20s  ", 
							"ID", "TIPO PRODUTO", "NOME", "PREÇO", "DATA CADASTRO", "DATA EXPIRAÇÃO");
					produtoVO.imprimir();
					System.out.print("\n\nConfirma (S/N)? ");
					if (teclado.nextLine().equalsIgnoreCase("s")) {
						confirmacao = true;
					}
				} 
			}
		} catch (NumberFormatException e){
			confirmacao = false;
			// vai retornar para o menu anterior
		}
		return confirmacao;
		
	}

	private void atualizarProduto() { // OK!
		ProdutoVO produtoVO = new ProdutoVO();
		ProdutoController produtoController = new ProdutoController();
		
		// apresentando confirmação:
		boolean confirmacao = this.confirmarProduto(produtoVO);
		
		try { 
			if (confirmacao) {
				
				// preenchendo campos:
				do {
					produtoVO.setTipoProduto(TipoProdutoVO.getTipoProdutoVOPorValor(this.apresentarOpcoesTipoProduto()));
				} while(produtoVO.getTipoProduto() == null);
				System.out.println("Para deixar como está, deixe em branco.");
				System.out.print("Digite o nome: ");
				produtoVO.setNome(teclado.nextLine());
				System.out.print("Digite o preço: R$ ");
				String preco = teclado.nextLine();
				if (!preco.isBlank()) {// só confirmando se o preço vai ser inserido corretamente:
					try {
						produtoVO.setPreco(Double.parseDouble(preco));
					} catch (NumberFormatException e) {
						System.out.println("Valor inválido. Preço não será atualizado.");
					}
				}
				produtoVO.setDataCadastro(LocalDateTime.now());
				
				// atualizando no banco de dados:
				boolean resultado = produtoController.atualizarProdutoController(produtoVO);
				if(resultado) {System.out.println("Produto atualizado com sucesso!");} 
				else {System.out.println("Não foi possível atualizar o produto.");}
			}					
				
		} catch (NumberFormatException e) {
			// voltar se deixar em branco a primeira pergunta
		}
	}

}
