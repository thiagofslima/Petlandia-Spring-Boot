package br.edu.toledoprudente.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.Categoria;

@Repository
public class CategoriaDAO extends AbstractDao<Categoria, Integer> {
	public List<Categoria> findByDescricao(String descricao) {
		List<Categoria> lista = this.createQuery("select c from Categoria"
				+ " c where c.descricao like ?1", descricao + "%") ;
		return lista;
	}
}
