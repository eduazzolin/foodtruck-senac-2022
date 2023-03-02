package model.vo;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProdutoVO {
	private int idProduto;
	private TipoProdutoVO TipoProduto;
	private String nome;
	private double preco;
	private LocalDateTime dataCadastro;
	private LocalDateTime dataExclusao;
	public int getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}
	public TipoProdutoVO getTipoProduto() {
		return TipoProduto;
	}
	public void setTipoProduto(TipoProdutoVO tipoProduto) {
		TipoProduto = tipoProduto;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public LocalDateTime getDataExclusao() {
		return dataExclusao;
	}
	public void setDataExclusao(LocalDateTime dataExclusao) {
		this.dataExclusao = dataExclusao;
	}
	public ProdutoVO() {
		super();
	}
	public void imprimir() {
		DecimalFormat deci = new DecimalFormat("0.00");
		System.out.printf("\n%3s  %-13s  %-20s  %-11s  %-20s  %-20s  ",
				this.getIdProduto(),
				this.getTipoProduto(),
				this.getNome(),
				"R$ " + deci.format(this.getPreco()),
				this.validarData(this.getDataCadastro()),
				this.validarData(this.getDataExclusao()));
		
	}
	private Object validarData(LocalDateTime data) {
		String resultado = "";
		if (data != null) {
			resultado = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		}
		return resultado;
	}

	
}
