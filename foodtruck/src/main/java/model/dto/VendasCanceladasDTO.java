package model.dto;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VendasCanceladasDTO {

	private String nome;
	private LocalDateTime dataCancelamento;
	private double subtotal;
	private double taxaEntrega;
	private double total;
	
	
	public void imprimir() {
		DecimalFormat deci = new DecimalFormat("0.00");
		System.out.printf("\n%-20s  %-24s  %12s  %12s  %12s  ", 
				this.getNome(),
				this.validarData(this.getDataCancelamento()),
				"R$ " + deci.format(this.getSubtotal()),
				"R$ " + deci.format(this.getTaxaEntrega()),
				"R$ " + deci.format(this.getTotal()));
	}
	
	
	private String validarData(LocalDateTime data) {
		String resultado = "";
		if (data != null) {
			resultado = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		}
		return resultado;
	}


	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LocalDateTime getDataCancelamento() {
		return dataCancelamento;
	}
	public void setDataCancelamento(LocalDateTime dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public double getTaxaEntrega() {
		return taxaEntrega;
	}
	public void setTaxaEntrega(double taxaEntrega) {
		this.taxaEntrega = taxaEntrega;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public VendasCanceladasDTO(String nome, LocalDateTime dataCancelamento, double subtotal, double taxaEntrega,
			double total) {
		super();
		this.nome = nome;
		this.dataCancelamento = dataCancelamento;
		this.subtotal = subtotal;
		this.taxaEntrega = taxaEntrega;
		this.total = total;
	}
	public VendasCanceladasDTO() {
		super();
	}
	
	
}
