package view;

import java.util.Scanner;

import controller.UsuarioController;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class Login {

	private static final int OPCAO_MENU_LOGIN = 1;
	private static final int OPCAO_MENU_CRIAR_CONTA = 2;
	private static final int OPCAO_MENU_DEV = 9879;
	private static final int OPCAO_MENU_SAIR = 9;
	
	Scanner teclado = new Scanner(System.in);
	
	public void apresentarMenuDeLogin() {
		int opcao = this.apresentarOpcoesMenu();
		while (opcao != OPCAO_MENU_SAIR) {
			switch(opcao) {
				case OPCAO_MENU_LOGIN: {
					UsuarioVO usuarioVO = this.realizarLogin();
					if (usuarioVO.getIdUsuario() != 0 && usuarioVO.getDataExpiracao() == null) {
						System.out.println("");
						apresentarLogoFoodtruck();
						System.out.println("\nUsuário logado: " + usuarioVO.getLogin());
						System.out.println("Perfil: " + usuarioVO.getTipoUsuario() + "\n");
						Menu menu = new Menu();
						menu.apresentarMenu(usuarioVO);
					}
					break;
				}
				case OPCAO_MENU_CRIAR_CONTA: {
					this.cadastrarNovoUsuario();
					break;
				}
				case OPCAO_MENU_DEV: {
					MenuDev menuDev = new MenuDev();
					menuDev.apresentarMenuDev();
					break;
				}
				default: {
					System.out.println("\nOpção inválida!");
				}
			}
			opcao = this.apresentarOpcoesMenu();
		}
	}



	private UsuarioVO realizarLogin() {
		UsuarioVO usuarioVO = new UsuarioVO();
		System.out.println("---------- Informações ----------");
		System.out.print("Login: ");
		usuarioVO.setLogin(teclado.nextLine());
		System.out.print("Senha: ");
		usuarioVO.setSenha(teclado.nextLine());
		
		// validação de que tudo foi preenchido:
		if (usuarioVO.getLogin().isEmpty() || usuarioVO.getSenha().isEmpty()) {
			System.out.println("Os campos de login e senha são obrigatórios.");
		} else {
			UsuarioController usuarioController = new UsuarioController();
			usuarioVO = usuarioController.realizarLoginController(usuarioVO);
			// validação de que o usuário foi encontrado:
			if (usuarioVO.getIdUsuario() == 0) {
				System.out.println("Usuário não encontrado.");
			}
			// validação de que o usuário não foi expirado:
			if (usuarioVO.getDataExpiracao() != null) {
				System.out.println("Usuário expirado.");
			}
		}
		return usuarioVO;
	}

	private int apresentarOpcoesMenu() {
		try {
			System.out.println("---------- Sistema FoodTruck ----------");

			System.out.println("\nOpções: ");
			System.out.println(OPCAO_MENU_LOGIN + " - Entrar");
			System.out.println(OPCAO_MENU_CRIAR_CONTA + " - Criar conta");
			System.out.println(OPCAO_MENU_SAIR + " - Sair");
			System.out.print("\nDigite uma opção: ");
			return Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("-----------------\nSistema Encerrado!");
			return OPCAO_MENU_SAIR;
		}
	}
	
	private void cadastrarNovoUsuario() {
		UsuarioVO usuarioVO = new UsuarioVO();
		usuarioVO.setTipoUsuario(TipoUsuarioVO.CLIENTE);
		MenuUsuario menuUsuario = new MenuUsuario();
		menuUsuario.cadastrarNovoUsuario(usuarioVO);
		// vai ser sempre cadastrado como cliente. 
		// para cadastrar outra classe é preciso que o administrador altere o perfil
		
	}
	
	public static void apresentarLogoFoodtruck() {
		System.out.println("\n"
				+ "   _|                 |  |                      |    \r\n"
				+ "  |    _ \\   _ \\   _` |  __|   __| |   |   __|  |  / \r\n"
				+ "  __| (   | (   | (   |  |    |    |   |  (       <  \r\n"
				+ " _|  \\___/ \\___/ \\__,_| \\__| _|   \\__,_| \\___| _|\\_\\ \r\n"
				+ "                                               1.0.0  ");
	}
	
}
