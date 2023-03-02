package model.dto;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoricoVendasDTO {

	private int idVenda;
    private int numeroPedido;
	private String nomeUsuario;
    private int idUsuario;
    private double valorTotal;
    private LocalDateTime dataVenda;
    private String situacaoEntrega;
    
	public int getIdVenda() {
		return idVenda;
	}
	public void setIdVenda(int idVenda) {
		this.idVenda = idVenda;
	}
	public int getNumeroPedido() {
		return numeroPedido;
	}
	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public HistoricoVendasDTO() {
		super();
	}
	public HistoricoVendasDTO(int idVenda, int numeroPedido, String nomeUsuario, int idUsuario, double valorTotal,
			LocalDateTime dataVenda, String situacaoEntrega) {
		super();
		this.idVenda = idVenda;
		this.numeroPedido = numeroPedido;
		this.nomeUsuario = nomeUsuario;
		this.idUsuario = idUsuario;
		this.valorTotal = valorTotal;
		this.dataVenda = dataVenda;
		this.situacaoEntrega = situacaoEntrega;
	}
	public void imprimir() {
		DecimalFormat deci = new DecimalFormat("0.00");
		System.out.printf("\n%7d  %6d  %.10s  %-9d  %-11s  %-19s  %-18s", 
				this.getIdVenda(),
				this.getNumeroPedido(),
				this.getNomeUsuario() + "          ",
				this.getIdUsuario(),
				"R$ " + deci.format(this.getValorTotal()),
				this.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
				this.getSituacaoEntrega());
		
	}
	public LocalDateTime getDataVenda() {
		return dataVenda;
	}
	public void setDataVenda(LocalDateTime dataVenda) {
		this.dataVenda = dataVenda;
	}
	public String getSituacaoEntrega() {
		return situacaoEntrega;
	}
	public void setSituacaoEntrega(String situacaoEntrega) {
		this.situacaoEntrega = situacaoEntrega;
	}
    
}
