package br.edu.toledoprudente.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.Produto;
import jakarta.persistence.TypedQuery;

@Repository
public class ProdutoDAO extends AbstractDao<Produto, Integer> {

	public List<Produto> findByDescricao(String nome) {
		List<Produto> lista = this.createQuery("select c from Produto"
				+ " c where c.nome like ?1", nome + "%") ;
		return lista;
	}

		public long countProdutos() {
		TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Produto p",
				Long.class);
		return query.getSingleResult();
	}

	// public Produto findByProdutoNome(String nome) {
	// 	List<Produto> lista = produtodao.createQuery
	// 	("select c from Produto where nome like ?1", nome) ;
	// 	return lista.isEmpty() ? null : lista.get(0);
	// }

}
