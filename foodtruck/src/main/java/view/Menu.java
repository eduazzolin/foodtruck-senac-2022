package view;

import java.util.Scanner;

import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class Menu {

	private static final int OPCAO_MENU_VENDA = 1;
	private static final int OPCAO_MENU_PRODUTO = 2;
	private static final int OPCAO_MENU_RELATORIO = 3;
	private static final int OPCAO_MENU_USUARIO = 4;
	private static final int OPCAO_MENU_VOLTAR = 9;
	
	Scanner teclado = new Scanner(System.in);
	
	public void apresentarMenu(UsuarioVO usuarioVO) {
		int opcao = this.apresentarOpcoesMenu(usuarioVO);
		while(opcao != OPCAO_MENU_VOLTAR) {
			switch(opcao) {
				case OPCAO_MENU_VENDA: {
					MenuVenda menuVenda = new MenuVenda();
					menuVenda.apresentarMenuVenda(usuarioVO);
					break;
				}
				case OPCAO_MENU_PRODUTO: {
					if(usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ADMINISTRADOR)
							|| usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.FUNCIONARIO)) {
						MenuProduto menuProduto = new MenuProduto();
						menuProduto.apresentarMenuProduto();
					}
					break;
				}
				case OPCAO_MENU_RELATORIO: {
					if(usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ADMINISTRADOR)
							|| usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.FUNCIONARIO)) {
						MenuRelatorio menuRelatorio = new MenuRelatorio();
						menuRelatorio.apresentarMenuRelatorio();
					}
					break;
				}
				case OPCAO_MENU_USUARIO: {
					if(usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ADMINISTRADOR)) {
						MenuUsuario menuUsuario = new MenuUsuario();
						menuUsuario.apresentarMenuUsuario();
					}
					break;
				}
				default: {
					System.err.println("Opção inválida.");
					break;
				}
			}
			opcao = this.apresentarOpcoesMenu(usuarioVO);
		}
		
	}

	private int apresentarOpcoesMenu(UsuarioVO usuarioVO) {
		try {
			//System.out.println("\n---------- Sistema foodtruck ----------");
			System.out.println("-> Menu Principal");
			System.out.println("\nOpções: ");
			System.out.println(OPCAO_MENU_VENDA + " - Menu de vendas");
			if(usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ADMINISTRADOR)
					|| usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.FUNCIONARIO)) {
				System.out.println(OPCAO_MENU_PRODUTO + " - Menu de produtos");
				System.out.println(OPCAO_MENU_RELATORIO + " - Menu de relatórios");
			}
			if(usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ADMINISTRADOR)) {
				System.out.println(OPCAO_MENU_USUARIO + " - Menu de usuários");
			}
			System.out.println(OPCAO_MENU_VOLTAR + " - Voltar");
			System.out.print("Digite uma opção: ");
			
			return Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
			return OPCAO_MENU_VOLTAR;
		}
	} 

}
