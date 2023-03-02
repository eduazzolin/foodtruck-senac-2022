package model.vo;

public class EnderecoVO {

	
	private int idEndereco;
	private String cep;
	private String rua;
	private String numero;
	private String complemento;
	
	public int getIdEndereco() {
		return idEndereco;
	}
	public void setIdEndereco(int idEndereco) {
		this.idEndereco = idEndereco;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public EnderecoVO(int idEndereco, String cep, String rua, String numero, String complemento) {
		super();
		this.idEndereco = idEndereco;
		this.cep = cep;
		this.rua = rua;
		this.numero = numero;
		this.complemento = complemento;
	}
	
	public EnderecoVO() {
		super();
	}
	public void imprimir() {
		System.out.println(
				"\nCEP:" + this.getCep() +
				"\nRUA:" + this.getRua() +
				"\nNÚMERO:" + this.getNumero() +
				"\nCOMPLEMENTO:" + this.getComplemento() + "\n");
	}
	
	
}
