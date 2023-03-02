package model.vo;

public enum SituacaoEntregaVO {

	/* sistema antigo
	ENTREGA_CANCELADA (1),
	PREPARANDO_PEDIDO (2),
	EM_ROTA_DE_ENTREGA (3),
	PEDIDO_ENTREGUE (4);
	*/
	
	// TODO ALTERAR PARA ORDEM (?)
	PEDIDO_REALIZADO (1),
	PREPARANDO_PEDIDO (2),
	EM_ROTA_DE_ENTREGA (3),
	PEDIDO_ENTREGUE (4),
	PEDIDO_CANCELADO (5),
	ENTREGA_CANCELADA (6);
	
	private int valor;

	private SituacaoEntregaVO(int valor) {
		this.valor = valor;
	}
	
	public int getValor() {
		return valor;
	}
	
	public static SituacaoEntregaVO getSituacaoEntregaPorValor(int valor) {
		SituacaoEntregaVO situacaoEntregaVO = null;
		for(SituacaoEntregaVO elemento : SituacaoEntregaVO.values()) {
			if(elemento.getValor() == valor) {
				situacaoEntregaVO = elemento;
			}
		}
		return situacaoEntregaVO;
	}

}
