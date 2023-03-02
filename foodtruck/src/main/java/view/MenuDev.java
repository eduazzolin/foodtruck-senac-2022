package view;

import java.util.Scanner;

import model.dao.DevDAO;

public class MenuDev {

	private static final int OPCAO_MENU_DEV_QUERY = 1;
	private static final int OPCAO_MENU_DEV_VOLTAR = 9;
	
	Scanner teclado = new Scanner(System.in);
	
	public void apresentarMenuDev() {
		int opcao = this.apresentarOpcoesMenu();
		while (opcao != OPCAO_MENU_DEV_VOLTAR) {
			switch (opcao) {
				case OPCAO_MENU_DEV_QUERY:{
					System.out.print("DIGITE A QUERY: ");
					String query = teclado.nextLine();
					DevDAO devDAO = new DevDAO();
					devDAO.executarQuery(query);
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
			System.out.println("\n"
			+ "   _|                 |  |                      |    \r\n"
			+ "  |    _ \\   _ \\   _` |  __|   __| |   |   __|  |  / \r\n"
			+ "  __| (   | (   | (   |  |    |    |   |  (       <  \r\n"
			+ " _|  \\___/ \\___/ \\__,_| \\__| _|   \\__,_| \\___| _|\\_\\ \r\n"
			+ "                                               0.0.1  ");
			System.out.println("-> Menu Desenvolvedor");
			System.out.println(OPCAO_MENU_DEV_QUERY + " - Executar uma query");
			System.out.println(OPCAO_MENU_DEV_VOLTAR + " - Voltar");
			System.out.print("Digite uma opção: ");
			return Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
			return OPCAO_MENU_DEV_VOLTAR;
		}
	}
}
