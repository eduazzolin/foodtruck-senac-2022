package model.bo;

import java.util.ArrayList;

import model.dao.ProdutoDAO;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class ProdutoBO {

	public ArrayList<TipoProdutoVO> consultarTipoProdutoBO() {
		// não tem regra, então passa adiante
		ProdutoDAO produtoDAO = new ProdutoDAO();
		return produtoDAO.consultarTipoProdutoDAO();
	}

	public ProdutoVO cadastrarProdutoBO(ProdutoVO produtoVO) {
		// não tem regra, então passa adiante
		ProdutoDAO produtoDAO = new ProdutoDAO();
		produtoVO = produtoDAO.cadastrarProdutoDAO(produtoVO);
		return produtoVO;
	}

	public boolean excluirProdutoBO(ProdutoVO produtoVO) {
		// regras:
		// 1º - produto existe;
		// 2º - não foi excluído ainda;
		boolean resultado = false;
		ProdutoDAO produtoDAO = new ProdutoDAO();
		if (produtoDAO.verificarExistenciaRegistroPorIdProdutoDAO(produtoVO.getIdProduto())) {
			if (produtoDAO.verificarExclusaoPorIdProdutoDAO(produtoVO.getIdProduto())) {
				System.out.println("Produto já se encontra excluído no banco de dados.");
			} else {
				resultado = produtoDAO.excluirProdutoDAO(produtoVO);
			}
		} else {
			System.out.println("Produto não existe no banco de dados.");
		}
		return resultado;
	}

	public ArrayList<ProdutoVO> consultarTodosProdutosBO() {
		// não tem regra, então passa adiante
		ProdutoDAO produtoDAO = new ProdutoDAO();
		ArrayList<ProdutoVO> listaProdutosVO = produtoDAO.consultarTodosProdutosDAO();
		if(listaProdutosVO.isEmpty()) {System.out.println("\nNão há nehum produto no banco de dados.");}
		return listaProdutosVO;
	}

	public ProdutoVO consultarProdutoBO(ProdutoVO produtoVO) {
		// já foi validado se o ID é ok
		ProdutoDAO produtoDAO = new ProdutoDAO();
		ProdutoVO produto = produtoDAO.consultarProdutoDAO(produtoVO);
		if (produto.getIdProduto() == 0) {System.out.println("Produto não localizado.");}
		return produto;
	}

	public ArrayList<ProdutoVO> consultarTodosProdutosVigentesBO() {
		// não tem regra, então passa adiante
		ProdutoDAO produtoDAO = new ProdutoDAO();
		ArrayList<ProdutoVO> listaProdutosVO = produtoDAO.consultarTodosProdutosVigentesDAO();
		if (listaProdutosVO.isEmpty()) {System.out.println("Lista de produtos está vazia");}
		return listaProdutosVO;
	}

	public boolean atualizarProdutoBO(ProdutoVO produtoVO) {
		// já foi validado que o produto existe,
		// validar se já não foi excluído 
		boolean resultado = false;
		ProdutoDAO produtoDAO = new ProdutoDAO();
		if (!produtoDAO.verificarExclusaoPorIdProdutoDAO(produtoVO.getIdProduto())) {
			resultado = produtoDAO.atualizarProdutoDAO(produtoVO);
		} else {
			System.out.println("Produto está excluído.");
		}
		
		return resultado;
	}

}
