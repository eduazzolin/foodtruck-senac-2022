package view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import controller.EnderecoController;
import controller.UsuarioController;
import model.vo.EnderecoVO;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class MenuUsuario {

	private static final int OPCAO_MENU_CADASTRAR_USUARIO = 1;
	private static final int OPCAO_MENU_CONSULTAR_USUARIO = 2;
	private static final int OPCAO_MENU_ATUALIZAR_USUARIO = 3;
	private static final int OPCAO_MENU_EXCLUIR_USUARIO = 4;
	private static final int OPCAO_MENU_CADASTRAR_ENDERECO = 5;
	private static final int OPCAO_MENU_CONSULTAR_ENDERECO = 6;
	private static final int OPCAO_MENU_USUARIO_VOLTAR = 9;
	
	private static final int OPCAO_MENU_CONSULTAR_TODOS_USUARIOS = 1;
	private static final int OPCAO_MENU_CONSULTAR_UM_USUARIO = 2;
	private static final int OPCAO_MENU_CONSULTAR_USUARIO_VOLTAR = 9;

	
	Scanner teclado = new Scanner(System.in);
	
	public void apresentarMenuUsuario() { // OK!
		int opcao = this.apresentarOpcoesMenu();
		while (opcao != OPCAO_MENU_USUARIO_VOLTAR) {
			switch (opcao) {
				case OPCAO_MENU_CADASTRAR_USUARIO:{
					UsuarioVO usuarioVO = new UsuarioVO();
					this.cadastrarUsuario(usuarioVO);
					break;
				}
				case OPCAO_MENU_CONSULTAR_USUARIO:{
					this.consultarUsuario();
					break;
				}
				case OPCAO_MENU_ATUALIZAR_USUARIO:{
					this.atualizarUsuario();
					break;
				}
				case OPCAO_MENU_EXCLUIR_USUARIO:{
					this.excluirUsuario();
					break;
				}
				case OPCAO_MENU_CADASTRAR_ENDERECO:{
					this.cadastrarEndereco();
					break;
				}
				case OPCAO_MENU_CONSULTAR_ENDERECO:{
					this.consultarEndereco();
					break;
				}
				default:{
					System.out.println("Opção inválida.");
				}
			}
			opcao = this.apresentarOpcoesMenu();
		}
	}


	private void consultarEndereco() {
		EnderecoController enderecoController = new EnderecoController();
		UsuarioController usuarioController = new UsuarioController();
		
		// definindo o usuário:
		UsuarioVO usuarioVO = new UsuarioVO();
		boolean confirmacao = this.confirmarUsuario(usuarioVO);
		
		if (confirmacao) {
			
			// vendo se o usuário tem endereço cadastrado:
			usuarioVO = usuarioController.consultarUsuarioController(usuarioVO);
			if (usuarioVO.getIdEndereco() == 0) {
				System.out.println("Usuário não possui endereço cadastrado.");
			} else {
				
				// buscando o endereço caso tenha e imprimindo:
				EnderecoVO enderecoVO = enderecoController.consultarEnderecoPorIdUsuarioController(usuarioVO.getIdUsuario());
				enderecoVO.imprimir();
			}
			
			
		}
		
	}


	public void cadastrarEndereco() {
		EnderecoController enderecoController = new EnderecoController();
						
		// definindo o usuário:
		UsuarioVO usuarioVO = new UsuarioVO();
		boolean confirmacao = this.confirmarUsuario(usuarioVO);
		
		if (confirmacao) {
			
			// cadastrando endereço no banco
			EnderecoVO enderecoVO = this.preencherEndereco();
			enderecoVO = enderecoController.cadastrarEnderecoController(enderecoVO);
			if (enderecoVO.getIdEndereco() != 0) {
				System.out.println("Endereço cadastrado com sucesso!");
			} else {
				System.err.println("Não foi possível cadastrar o endereço.");
			}
			usuarioVO.setIdEndereco(enderecoVO.getIdEndereco());
			
			// inserindo o id do endereço no usuário
			UsuarioController usuarioController = new UsuarioController();
			boolean resultado = usuarioController.atualizarEnderecoUsuarioController(usuarioVO); 
			if (resultado) {
				System.out.println("Endereço atribuído ao usuário com sucesso!");
				// o endereço antigo do usuário será mantido no bd porque pode estar no histórico de alguma entrega
				}
			else {
				System.err.println("Erro ao atribuir endereço ao usuário.");
			}
		}
	}
	
	public void cadastrarEndereco(UsuarioVO usuarioVO) {
		EnderecoController enderecoController = new EnderecoController();
		
		// cadastrando endereço no banco
		EnderecoVO enderecoVO = this.preencherEndereco();
		enderecoVO = enderecoController.cadastrarEnderecoController(enderecoVO);
		if (enderecoVO.getIdEndereco() != 0) {
			System.out.println("Endereço cadastrado com sucesso!");
		} else {
			System.err.println("Não foi possível cadastrar o endereço.");
		}
		
		// inserindo o id do endereço no usuário
		usuarioVO.setIdEndereco(enderecoVO.getIdEndereco());
		UsuarioController usuarioController = new UsuarioController();
		boolean resultado = usuarioController.atualizarEnderecoUsuarioController(usuarioVO); 
		if (resultado) {
			System.out.println("Endereço atribuído ao usuário com sucesso!");
			// o endereço antigo do usuário será mantido no bd porque pode estar no histórico de alguma entrega
		}
		else {
			System.err.println("Erro ao atribuir endereço ao usuário.");
		}
	
	}


	private void atualizarUsuario() { // OK!
		UsuarioVO usuarioVO = new UsuarioVO();
		
		// recendo id e confirmando:
		boolean confirmacao = this.confirmarUsuario(usuarioVO);
		
		
		try { 
			if (confirmacao) {
				
				// preenchendo campos:
				do {
					usuarioVO.setTipoUsuario(TipoUsuarioVO.getTipoUsuarioVOPorValor(this.apresentarOpcoesTipoUsuario()));
				} while(usuarioVO.getTipoUsuario() == null);
				System.out.println("Para deixar como está, deixe em branco.");
				System.out.print("Digite o nome: ");
				usuarioVO.setNome(teclado.nextLine());
				System.out.print("Digite o CPF: ");
				usuarioVO.setCpf(teclado.nextLine());
				System.out.print("Digite o e-mail: ");
				usuarioVO.setEmail(teclado.nextLine());
				System.out.print("Digite o telefone: ");
				usuarioVO.setTelefone(teclado.nextLine());
				usuarioVO.setDataCadastro(LocalDateTime.now());
				System.out.print("Digite o login: ");
				usuarioVO.setLogin(teclado.nextLine());
				System.out.print("Digite a senha: ");
				usuarioVO.setSenha(teclado.nextLine());
				
				// perguntando se quer atualizar o endereço:
				String opcaoEndereco = "";
				while (!opcaoEndereco.equalsIgnoreCase("s") && !opcaoEndereco.equalsIgnoreCase("n")) {
					System.out.print("Deseja atualizar o endereço? (S/N): ");
					opcaoEndereco = teclado.nextLine();
				}
				if (opcaoEndereco.equalsIgnoreCase("s")) {
					this.cadastrarEndereco(usuarioVO);
				}

				
				// atualizando no banco de dados:
				UsuarioController usuarioController = new UsuarioController();
				boolean resultado = usuarioController.atualizarUsuarioController(usuarioVO);
				if(resultado) {System.out.println("Usuário atualizado com sucesso!");}
				else {System.out.println("Não foi possível atualizar o usuário.");}
			}
		} catch (NumberFormatException e) {
			// voltar se deixar em branco a primeira pergunta
		}
	}


	private int apresentarOpcoesMenu() { // OK!
		try {
			System.out.println("\n---------- Sistema FoodTruck ----------");
			System.out.println("-> Menu usuário");
			System.out.println("\nOpções:");
			System.out.println(OPCAO_MENU_CADASTRAR_USUARIO + " - Cadastrar Usuário");
			System.out.println(OPCAO_MENU_CONSULTAR_USUARIO + " - Consultar Usuário");
			System.out.println(OPCAO_MENU_ATUALIZAR_USUARIO + " - Atualizar Usuário");
			System.out.println(OPCAO_MENU_EXCLUIR_USUARIO + " - Excluir Usuário");
			System.out.println(OPCAO_MENU_CADASTRAR_ENDERECO + " - Cadastrar/Modificar Endereço");
			System.out.println(OPCAO_MENU_CONSULTAR_ENDERECO + " - Consultar Endereço");
			System.out.println(OPCAO_MENU_USUARIO_VOLTAR + " - Voltar");
			System.out.print("Digite uma opção: ");
			return Integer.parseInt(teclado.nextLine());
		} catch (NumberFormatException e) {
			return OPCAO_MENU_USUARIO_VOLTAR;
		}
	}

	public void cadastrarNovoUsuario(UsuarioVO usuarioVO) { // OK!
		// método que cadastra usuário externo. 
		// é chamado pelo usuário que está se cadastrando na tela de login.
		this.cadastrarUsuario(usuarioVO);
	}	
	
	private void cadastrarUsuario(UsuarioVO usuarioVO) { // OK!
		try { 
			
			// preenchendo campos:
			if(usuarioVO.getTipoUsuario() == null) {
				
				// verificando se o usuário vêm da tela de login ou do menu usuário
				// através do tipoUsuario
				do {
					usuarioVO.setTipoUsuario(TipoUsuarioVO.getTipoUsuarioVOPorValor(this.apresentarOpcoesTipoUsuario()));
				} while(usuarioVO.getTipoUsuario() == null);
			}
			System.out.print("Digite o nome: ");
			usuarioVO.setNome(teclado.nextLine());
			System.out.print("Digite o CPF: ");
			usuarioVO.setCpf(teclado.nextLine());
			System.out.print("Digite o e-mail: ");
			usuarioVO.setEmail(teclado.nextLine());
			System.out.print("Digite o telefone: ");
			usuarioVO.setTelefone(teclado.nextLine());
			usuarioVO.setDataCadastro(LocalDateTime.now());
			System.out.print("Digite o login: ");
			usuarioVO.setLogin(teclado.nextLine());
			System.out.print("Digite a senha: ");
			usuarioVO.setSenha(teclado.nextLine());
			usuarioVO.setIdEndereco(-1); // para não barrar no validarCamposCadastro(

			
			// endereço:
			// perguntando se quer adicionar:
			String opcaoEndereco = "";
			while (!opcaoEndereco.equalsIgnoreCase("s") && !opcaoEndereco.equalsIgnoreCase("n")) {
				System.out.print("Deseja cadastrar um endereço? (S/N): ");
				opcaoEndereco = teclado.nextLine();
			}
			if (opcaoEndereco.equalsIgnoreCase("s")) {
				EnderecoVO enderecoVO = this.preencherEndereco();
				EnderecoController enderecoController = new EnderecoController();
				enderecoVO = enderecoController.cadastrarEnderecoController(enderecoVO);
				if (enderecoVO.getIdEndereco() != 0) {
					System.out.println("Endereço cadastrado com sucesso!");
				} else {
					System.err.println("Não foi possível cadastrar o endereço.");
				}
				usuarioVO.setIdEndereco(enderecoVO.getIdEndereco());
			}
			
			
			// validando se tudo foi preenchido:
			if(this.validarCamposCadastro(usuarioVO)) {
				UsuarioController usuarioController = new UsuarioController();
				usuarioVO = usuarioController.cadastrarUsuarioController(usuarioVO);
				if(usuarioVO.getIdUsuario()!= 0) {
					System.out.println("Usuário cadastrado com sucesso!\n");
				} else {
					System.err.println("Não foi possível cadastrar o usuário.");
				}
			}
		} catch (NumberFormatException e) {
			// voltar se deixar em branco a primeira pergunta
		}
	}
	
	public EnderecoVO preencherEndereco() {
		EnderecoVO enderecoVO = new EnderecoVO();
		do {
			System.out.print("Digite o CEP: ");
			enderecoVO.setCep(teclado.nextLine());
			System.out.print("Digite a rua: ");
			enderecoVO.setRua(teclado.nextLine());
			System.out.print("Digite o número da residência: ");
			enderecoVO.setNumero(teclado.nextLine());
			System.out.print("Digite o complemento se houver: ");
			enderecoVO.setComplemento(teclado.nextLine());
		} while (!this.validarCamposEndereco(enderecoVO));
		return enderecoVO;
	}


	public boolean validarCamposEndereco(EnderecoVO enderecoVO) {
		// verifica se os atributos do produto estão nulos (== null)
		// ou se estão vazios (.isEmpty [que é a mesma coisa que .equals("")])
		boolean resultado = true;
		System.out.println();
		if(enderecoVO.getCep() == null || enderecoVO.getCep().isEmpty()) {
			System.out.println("O campo CEP é obrigatório.");
			resultado = false;
		}
		if(enderecoVO.getRua() == null || enderecoVO.getRua().isEmpty()) {
			System.out.println("O campo rua é obrigatório.");
			resultado = false;
		}
		if(enderecoVO.getNumero() == null || enderecoVO.getNumero().isEmpty()) {
			System.out.println("O campo número é obrigatório.");
			resultado = false;
		}
		return resultado;
	}


	private int apresentarOpcoesTipoUsuario() { // OK!
		UsuarioController usuarioController = new UsuarioController();
		ArrayList<TipoUsuarioVO> listaTipoUsuarioVO = usuarioController.consultarTipoUsuario();
		System.out.println("\n---- Tipos de Usuários ----");
		System.out.println("Opções: ");
		for (int i = 0; i< listaTipoUsuarioVO.size(); i++) {
			System.out.println(listaTipoUsuarioVO.get(i).getValor() + " - " + listaTipoUsuarioVO.get(i));
		}
		System.out.print("\nDigite uma opção: ");
		return Integer.parseInt(teclado.nextLine());
	}

	private boolean validarCamposCadastro(UsuarioVO usuarioVO) { // OK!
		// verifica se os atributos do produto estão nulos (== null)
		// ou se estão vazios (.isEmpty [que é a mesma coisa que .equals("")])
		boolean resultado = true;
		System.out.println();
		if(usuarioVO.getNome() == null || usuarioVO.getNome().isEmpty()) {
			System.out.println("O campo nome é obrigatório.");
			resultado = false;
		}
		if(usuarioVO.getCpf() == null || usuarioVO.getCpf().isEmpty()) {
			System.out.println("O campo CPF é obrigatório.");
			resultado = false;
		}
		if(usuarioVO.getEmail() == null || usuarioVO.getEmail().isEmpty()) {
			System.out.println("O campo email é obrigatório.");
			resultado = false;
		}
		if(usuarioVO.getTelefone() == null || usuarioVO.getTelefone().isEmpty()) {
			System.out.println("O campo telefone é obrigatório.");
			resultado = false;
		}
		if(usuarioVO.getDataCadastro() == null) {
			System.out.println("O campo data cadastro é obrigatório.");
			resultado = false;
		}
		if(usuarioVO.getLogin() == null || usuarioVO.getLogin().isEmpty()) {
			System.out.println("O campo login é obrigatório.");
			resultado = false;
		}
		if(usuarioVO.getSenha() == null || usuarioVO.getSenha().isEmpty()) {
			System.out.println("O campo senha é obrigatório.");
			resultado = false;
		}
		if(usuarioVO.getIdEndereco() == 0) {
			System.out.println("O campo endereço é obrigatório.");
			resultado = false;
		}
		return resultado;
	}

	private void consultarUsuario() { // OK!
		int opcao = this.apresentarOpcoesConsulta();
		UsuarioController usuarioController = new UsuarioController();
		while(opcao != OPCAO_MENU_CONSULTAR_USUARIO_VOLTAR) {
			switch(opcao) {
				case OPCAO_MENU_CONSULTAR_TODOS_USUARIOS: {
					opcao = OPCAO_MENU_CONSULTAR_USUARIO_VOLTAR;
					ArrayList<UsuarioVO> listaUsuarioVO = usuarioController.consultarTodosUsuariosController();
					System.out.print("\n-> Resultado da consulta");
					System.out.printf(	"\n%3s  %-13s  %-20s  %-11s  %-25s  %-13s  %-20s  %-20s  %-10s  %-10s  ", 
										"ID", "TIPO USUÁRIO", "NOME", "CPF", "E-MAIL", "TELEFONE", "DATA CADASTRO", 
										"DATA EXPIRAÇÃO", "LOGIN", "SENHA");
					for (int i = 0; i < listaUsuarioVO.size(); i++) {
						listaUsuarioVO.get(i).imprimir();
					}
					System.out.println();
					break;
				}
				case OPCAO_MENU_CONSULTAR_UM_USUARIO: {
					try { // esse try catch é só para voltar se deixar em branco
						opcao = OPCAO_MENU_CONSULTAR_USUARIO_VOLTAR;
						UsuarioVO usuarioVO = new UsuarioVO();
						System.out.print("\nInforme o código do usuário: ");
						usuarioVO.setIdUsuario(Integer.parseInt(teclado.nextLine()));
						if(usuarioVO.getIdUsuario() != 0) {
							System.out.print("\n-> Resultado da consulta");
							UsuarioVO usuario = usuarioController.consultarUsuarioController(usuarioVO);
							if (usuario.getIdUsuario() != 0) {
								System.out.printf(	"\n%3s  %-13s  %-20s  %-11s  %-25s  %-13s  %-20s  %-20s  %-10s  %-10s  ", 
										"ID", "TIPO USUÁRIO", "NOME", "CPF", "E-MAIL", "TELEFONE", "DATA CADASTRO", 
										"DATA EXPIRAÇÃO", "LOGIN", "SENHA");
								usuario.imprimir();
								System.out.println();
							}
						} else {
							System.out.println("O campo código do usuário é obrigatório.");
						}
					} catch (NumberFormatException e) {
						// voltar se deixar em branco
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
		System.out.println("\nInforme o tipo de consulta a ser realizada: ");
		System.out.println(OPCAO_MENU_CONSULTAR_TODOS_USUARIOS + " - Consultar todos os usuários");
		System.out.println(OPCAO_MENU_CONSULTAR_UM_USUARIO + " - Consultar um usuário específico");
		System.out.println(OPCAO_MENU_CONSULTAR_USUARIO_VOLTAR + " - Voltar");
		System.out.print("\nDigite uma opção: ");
		return Integer.parseInt(teclado.nextLine());
	}


	private boolean confirmarUsuario(UsuarioVO usuarioVO) { // OK!
		// método que pede o id do usuário, apresenta informações sobre ele, 
		// pede confirmação do usuario e retorna booleano.
		UsuarioController usuarioController = new UsuarioController();
		boolean confirmacao = false;
		try {
			while (!confirmacao) {
				// recebendo o id:
				System.out.print("Digite o código do usuário: ");
				usuarioVO.setIdUsuario(Integer.parseInt(teclado.nextLine()));
				
				// preenchendo com as informações retornadas do banco:
				usuarioVO = usuarioController.consultarUsuarioController(usuarioVO);
				
				// se o VO retornar sem ID, significa que o produto não foi encontrado. 
				// nesse caso a confirmação continua false e o while reinicia.
				if (usuarioVO.getIdUsuario() != 0) {
					System.out.printf(	"\n%3s  %-13s  %-20s  %-11s  %-25s  %-13s  %-20s  %-20s  %-10s  %-10s  ", 
							"ID", "TIPO USUÁRIO", "NOME", "CPF", "E-MAIL", "TELEFONE", "DATA CADASTRO", 
							"DATA EXPIRAÇÃO", "LOGIN", "SENHA");
					usuarioVO.imprimir();
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


	private void excluirUsuario() { // OK!
		UsuarioVO usuarioVO = new UsuarioVO();
		
		// apresentando confirmação:
		boolean confirmacao = this.confirmarUsuario(usuarioVO);
		
		if (confirmacao) {
			
			// recebendo a data de exclusão:
			System.out.print("Digite a data de exclusão no formato dd/MM/yyyy HH:mm:ss "
					+ "\nOu deixe em branco para pegar instante atual: ");
			String resposta = teclado.nextLine();
			if (resposta.isBlank()) {
				usuarioVO.setDataExpiracao(LocalDateTime.now());
			} else {
				try {
					usuarioVO.setDataExpiracao(LocalDateTime.parse(resposta, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
				} catch (DateTimeParseException e) {
					System.out.println("Data inválida!");
				}
			}
			
			// conferindo data e excluindo no banco de dados:
			if (usuarioVO.getDataExpiracao() != null) {
				UsuarioController usuarioController = new UsuarioController();
				boolean resultado = usuarioController.excluirUsuarioController(usuarioVO);
				if (resultado) {System.out.println("\nUsuário excluído com sucesso!");}
				else {System.out.println("Não foi possível excluir o usuário.");}
			} else {
				// caso a data esteja nula:
				System.out.println("\nNão foi possível excluir o usuário.");
			}
		} else {
			System.out.println("\nNão foi possível excluir o usuário.");
		}
	}
	
}
