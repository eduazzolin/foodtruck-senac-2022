package model.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsuarioVO {
	
	private int idUsuario;
	private TipoUsuarioVO tipoUsuario;
	private String nome;
	private String cpf;
	private String email;
	private String telefone;
	private LocalDateTime dataCadastro;
	private LocalDateTime dataExpiracao;
	private String login;
	private String senha;
	private int idEndereco;
	
	
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public TipoUsuarioVO getTipoUsuario() {
		return tipoUsuario;
	}
	public void setTipoUsuario(TipoUsuarioVO tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public LocalDateTime getDataExpiracao() {
		return dataExpiracao;
	}
	public void setDataExpiracao(LocalDateTime dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	public UsuarioVO() {
		super();
	}
	public UsuarioVO(int idUsuario, TipoUsuarioVO tipoUsuario, String nome, String cpf, String email, String telefone,
			LocalDateTime dataCadastro, LocalDateTime dataExpiracao, String login, String senha, int idEndereco) {
		super();
		this.idUsuario = idUsuario;
		this.tipoUsuario = tipoUsuario;
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.telefone = telefone;
		this.dataCadastro = dataCadastro;
		this.dataExpiracao = dataExpiracao;
		this.login = login;
		this.senha = senha;
		this.idEndereco = idEndereco;
	}
	
	
	public void imprimir() {
		System.out.printf("\n%3d  %-13s  %-20s  %-11s  %-25s  %-13s  %-20s  %-20s  %-10s  %-10s  ",
							this.getIdUsuario(), 
							this.getTipoUsuario(),
							this.getNome(),
							this.getCpf(),
							this.getEmail(),
							this.getTelefone(),
							this.validarData(this.getDataCadastro()),
							this.validarData(this.getDataExpiracao()),
							this.getLogin(),
							this.getSenha());
		
	}
	private String validarData(LocalDateTime data) {
		String resultado = "";
		if(data != null) {
			resultado = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		}
		return resultado;
	}
	public int getIdEndereco() {
		return idEndereco;
	}
	public void setIdEndereco(int idEndereco) {
		this.idEndereco = idEndereco;
	}

	
}
