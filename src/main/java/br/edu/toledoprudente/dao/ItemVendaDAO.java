package br.edu.toledoprudente.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.ItemVenda;
import br.edu.toledoprudente.pojo.Produto;

@Repository
public class ItemVendaDAO extends AbstractDao<ItemVenda, Integer> {

    	public List<ItemVenda> findVenda(int id) {
		List<ItemVenda> lista = this.createQuery("select c from ItemVenda"
				+ " c where c.venda.id = ?1", id) ;
		return lista;
	}

}
