package view;

import java.util.ArrayList;
import java.util.Scanner;

import controller.RelatorioController;
import model.dto.VendasCanceladasDTO;
import model.vo.VendaVO;

public class MenuRelatorio {

	private static final int OPCAO_MENU_RELATORIO_LISTAR_VENDAS = 1;
	private static final int OPCAO_MENU_RELATORIO_ACOMPANHAMENTO_VENDAS = 2;
	private static final int OPCAO_MENU_RELATORIO_GUIA_FRETE = 3;
	
	private static final int OPCAO_MENU_RELATORIO_ACOMPANHAMENTO_VENDAS_ID = 1;
	private static final int OPCAO_MENU_RELATORIO_ACOMPANHAMENTO_VENDAS_ULTIMOS_10 = 2;
	
	private static final int OPCAO_MENU_RELATORIO_VENDAS_CANCELADAS = 8;
	private static final int OPCAO_MENU_RELATORIO_VOLTAR = 9;

	Scanner teclado = new Scanner(System.in);
	
	public void apresentarMenuRelatorio() {
		int opcao = this.apresentarOpcoesMenu();
		while(opcao != OPCAO_MENU_RELATORIO_VOLTAR) {
			switch (opcao) {
				case OPCAO_MENU_RELATORIO_VENDAS_CANCELADAS:{
					this.gerarRelatorioVendasCanceladas();
					break;
				}
				case OPCAO_MENU_RELATORIO_LISTAR_VENDAS:{
					this.gerarRelatorioVendas();
					break;
				}
				case OPCAO_MENU_RELATORIO_ACOMPANHAMENTO_VENDAS:{
					this.gerarRelatorioAcompanhamentoVendas();
					break;
				}
				case OPCAO_MENU_RELATORIO_GUIA_FRETE:{
					this.gerarRelatorioGuiaFrete();
					break;
				}
				default: {
					System.out.println("Opção inválida.");
				}
			}
			opcao = this.apresentarOpcoesMenu();
		}
	}

	private void gerarRelatorioGuiaFrete() {
		RelatorioController relatorioController = new RelatorioController();
		relatorioController.gerarRelatorioGuiaFreteController();
		
	}

	private int apresentarMenuRelatorioAcompanhamentoVendas() {
		try { 
			System.out.println("\nInforme o tipo de relatório a ser gerado: ");
			System.out.println(OPCAO_MENU_RELATORIO_ACOMPANHAMENTO_VENDAS_ID + " - Consultar por ID da venda");
			System.out.println(OPCAO_MENU_RELATORIO_ACOMPANHAMENTO_VENDAS_ULTIMOS_10 + " - Consultar últimos 10 pedidos"); 
			System.out.println(OPCAO_MENU_RELATORIO_VOLTAR + " - Voltar");
			System.out.print("\nDigite uma opção: ");
			return Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
			// voltar se deixar em branco a primeira pergunta
			return OPCAO_MENU_RELATORIO_VOLTAR;
		}
		
	}
	
	private void gerarRelatorioAcompanhamentoVendas() {
		RelatorioController relatorioController = new RelatorioController();
		int opcao = this.apresentarMenuRelatorioAcompanhamentoVendas();
		while(opcao != OPCAO_MENU_RELATORIO_VOLTAR) {
			switch (opcao) {
				case OPCAO_MENU_RELATORIO_ACOMPANHAMENTO_VENDAS_ID: {
					opcao = OPCAO_MENU_RELATORIO_VOLTAR;
					VendaVO vendaVO = new VendaVO();
					MenuVenda menuVenda = new MenuVenda();
					boolean confirmacao = menuVenda.confirmarVenda(vendaVO);
					if (confirmacao) {
						relatorioController.gerarRelatorioAcompanhamentoVendasController(vendaVO.getIdVenda());
					}
					break;
				}
				case OPCAO_MENU_RELATORIO_ACOMPANHAMENTO_VENDAS_ULTIMOS_10: {
					opcao = OPCAO_MENU_RELATORIO_VOLTAR;
					relatorioController.gerarRelatorioAcompanhamentoVendasController(0);
					break;
				}
				default: {
					System.out.println("Opção inválida.");
					opcao = this.apresentarMenuRelatorioAcompanhamentoVendas();
					break;
				}
			}
		
		
		}
		
	}

	private void gerarRelatorioVendas() {
		RelatorioController relatorioController = new RelatorioController();
		relatorioController.gerarRelatorioVendasController();
	}

	private void gerarRelatorioVendasCanceladas() {
		RelatorioController relatorioController = new RelatorioController();
		ArrayList<VendasCanceladasDTO> listaVendasCanceladasDTO = relatorioController.gerarRelatorioVendasCanceladasController();
		System.out.println("\n---------- RELATÓRIO ----------");
		System.out.println("-> VENDAS CANCELADAS:");
		System.out.printf("\n%-20s  %-24s  %12s  %12s  %12s  ", 
				"NOME", "DATA CANCELAMENTO", "SUBTOTAL", "TAXA ENTREGA", "TOTAL");
		for (VendasCanceladasDTO venda : listaVendasCanceladasDTO) {
			venda.imprimir();
		}
		System.out.println();
	}

	private int apresentarOpcoesMenu() {
		try {	
			System.out.println("\n---------- Sistema FoodTruck ----------");
			System.out.println("-> Menu Relatórios");
			System.out.println("\nOpções:");
			System.out.println(OPCAO_MENU_RELATORIO_LISTAR_VENDAS + " - Relatório 01:  Relatório de vendas");
			System.out.println(OPCAO_MENU_RELATORIO_ACOMPANHAMENTO_VENDAS + " - Relatório 02:  Relatório de acompanhamento de vendas");
			System.out.println(OPCAO_MENU_RELATORIO_GUIA_FRETE + " - Relatório 03:  Emissão de guia de frete");
			System.out.println(OPCAO_MENU_RELATORIO_VENDAS_CANCELADAS + " - Relatório de vendas canceladas");
			System.out.println(OPCAO_MENU_RELATORIO_VOLTAR + " - Voltar");
			System.out.print("Digite uma opção: ");
			return Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
			return OPCAO_MENU_RELATORIO_VOLTAR;
		}
	}
}
