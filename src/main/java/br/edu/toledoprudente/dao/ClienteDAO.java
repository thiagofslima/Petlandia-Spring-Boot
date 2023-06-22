package br.edu.toledoprudente.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.Cliente;

@Repository
public class ClienteDAO extends AbstractDao<Cliente, Integer> {
	public List<Cliente> findByNome(String nome) {
		List<Cliente> lista = this.createQuery("select c from Cliente"
				+ " c where c.nome like ?1", nome + "%") ;
		return lista;
	}
}
