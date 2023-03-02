package model.vo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class VendaVO {
	
	private int idVenda;
	private int idUsuario;
	private int numeroPedido;
	private LocalDateTime dataVenda;
	private LocalDateTime dataCancelamento;
	private boolean flagEntrega;
	private double taxaEntrega;
	private ArrayList<ItemVendaVO> listaItemVendaVO;
	
	public int getIdVenda() {
		return idVenda;
	}
	public void setIdVenda(int idvenda) {
		this.idVenda = idvenda;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idusuario) {
		this.idUsuario = idusuario;
	}
	public int getNumeroPedido() {
		return numeroPedido;
	}
	public void setNumeroPedido(int numeropedido) {
		this.numeroPedido = numeropedido;
	}
	public LocalDateTime getDataVenda() {
		return dataVenda;
	}
	public void setDataVenda(LocalDateTime dataVenda) {
		this.dataVenda = dataVenda;
	}
	public LocalDateTime getDataCancelamento() {
		return dataCancelamento;
	}
	public void setDataCancelamento(LocalDateTime dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}
	public boolean isFlagEntrega() {
		return flagEntrega;
	}
	public void setFlagEntrega(boolean flagEntrega) {
		this.flagEntrega = flagEntrega;
	}
	public double getTaxaEntrega() {
		return taxaEntrega;
	}
	public void setTaxaEntrega(double taxaEntrega) {
		this.taxaEntrega = taxaEntrega;
	}
	public VendaVO(int idvenda, int idusuario, int numeropedido, LocalDateTime dataVenda,
			LocalDateTime dataCancelamento, boolean flagEntrega, double taxaEntrega) {
		super();
		this.idVenda = idvenda;
		this.idUsuario = idusuario;
		this.numeroPedido = numeropedido;
		this.dataVenda = dataVenda;
		this.dataCancelamento = dataCancelamento;
		this.flagEntrega = flagEntrega;
		this.taxaEntrega = taxaEntrega;
	}
	public VendaVO() {
		super();
	}
	public ArrayList<ItemVendaVO> getListaItemVendaVO() {
		return listaItemVendaVO;
	}
	public void setListaItemVendaVO(ArrayList<ItemVendaVO> listaItemVendaVO) {
		this.listaItemVendaVO = listaItemVendaVO;
	}

}
