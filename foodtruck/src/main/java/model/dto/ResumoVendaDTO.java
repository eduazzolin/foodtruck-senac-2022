package model.dto;

import java.text.DecimalFormat;

public class ResumoVendaDTO {
	
	private int idVenda;
	private int idProduto;
    private String nomeProduto;
    private double precoUnitario;
    private int  quantidade;
    private double precoTotal;
    
    
	public int getIdVenda() {
		return idVenda;
	}
	public void setIdVenda(int idVenda) {
		this.idVenda = idVenda;
	}
	public int getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public double getPrecoUnitario() {
		return precoUnitario;
	}
	public void setPrecoUnitario(double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public double getPrecoTotal() {
		return precoTotal;
	}
	public void setPrecoTotal(double precoTotal) {
		this.precoTotal = precoTotal;
	}
	public ResumoVendaDTO(int idVenda, int idProduto, String nomeProduto, double precoUnitario, int quantidade,
			double precoTotal) {
		super();
		this.idVenda = idVenda;
		this.idProduto = idProduto;
		this.nomeProduto = nomeProduto;
		this.precoUnitario = precoUnitario;
		this.quantidade = quantidade;
		this.precoTotal = precoTotal;
	}
	public ResumoVendaDTO() {
		super();
	}
	public void imprimir() {
//		System.out.printf("\n%3s  %-20s  %-11s  %-5s  %-11s", 
//				"ID", "NOME", "PREÇO UNIT", "QTD", "TOTAL");
		
		DecimalFormat deci = new DecimalFormat("0.00");
		System.out.printf("\n%3s  %-20s  %-11s  %-5s  %-11s", 
				this.getIdProduto(),
				this.getNomeProduto(),
				"R$ " + deci.format(this.getPrecoUnitario()),
				this.getQuantidade(),
				"R$ " + deci.format(this.getPrecoTotal()));
		
	}
    
}
