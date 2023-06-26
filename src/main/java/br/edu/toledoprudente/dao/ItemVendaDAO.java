package br.edu.toledoprudente.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.ItemVenda;
import jakarta.persistence.TypedQuery;

@Repository
public class ItemVendaDAO extends AbstractDao<ItemVenda, Integer> {

	public List<ItemVenda> findVenda(int id) {
		List<ItemVenda> lista = this.createQuery("select c from ItemVenda"
				+ " c where c.venda.id = ?1", id);
		return lista;
	}

	public List<Object[]> getProdutosMaisVendidos() {
		int quantidade = 5;
		String jpql = "select iv.id, iv.produto.nome, sum(iv.qtde) " +
				"FROM ItemVenda iv group by iv.produto.id";

		TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
		query.setMaxResults(quantidade);

		return query.getResultList();
	}

}
