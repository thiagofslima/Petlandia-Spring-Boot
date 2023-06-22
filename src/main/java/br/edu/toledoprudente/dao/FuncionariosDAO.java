package br.edu.toledoprudente.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.Funcionarios;
import br.edu.toledoprudente.pojo.Users;

@Repository
public class FuncionariosDAO extends AbstractDao<Funcionarios, Integer> {

	public List<Funcionarios> findByName(String name) {
		List<Funcionarios> lista = this.createQuery("select c from Funcionarios"
				+ " c where c.nome like ?1", name + "%") ;
		return lista;
	}
	
}
