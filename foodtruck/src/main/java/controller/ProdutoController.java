package controller;

import java.util.ArrayList;

import model.bo.ProdutoBO;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class ProdutoController {

	public ArrayList<TipoProdutoVO> consultarTipoProduto() {
		// Busca no banco todos os tipos de produto
		// e retorna um arrayList com seus respectivos VOs
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.consultarTipoProdutoBO();
	}

	public ProdutoVO cadastrarProdutoController(ProdutoVO produtoVO) {
		// Insere no banco e retorna o objeto com o ID preenchido
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.cadastrarProdutoBO(produtoVO);
	}

	public boolean excluirProdutoController(ProdutoVO produtoVO) {
		// Insere a data de exclusão no banco e retorna um boolean
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.excluirProdutoBO(produtoVO);
	}

	public ArrayList<ProdutoVO> ConsultarTodosProdutosController() {
		// Retorna um ArrayList com todos os VOs dos produtos
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.consultarTodosProdutosBO();
	}

	public ProdutoVO consultarProdutoController(ProdutoVO produtoVO) {
		// Retorna um ProdutoVO completo buscado no banco através do ID
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.consultarProdutoBO(produtoVO);
	}

	public boolean atualizarProdutoController(ProdutoVO produtoVO) {
		// Atualiza no banco de dados e retorna um booleano 
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.atualizarProdutoBO(produtoVO);
	}

	public ArrayList<ProdutoVO> consultarTodosProdutosVigentesController() {
		// Retorna um ArrayList com todos os VOs dos produtos sem data de exp
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.consultarTodosProdutosVigentesBO();
	}

	
}
