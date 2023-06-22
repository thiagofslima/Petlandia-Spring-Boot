package br.edu.toledoprudente.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.Funcionarios;
import br.edu.toledoprudente.pojo.Users;

@Repository
public class UsersDAO extends AbstractDao<Users,Integer> implements UserDetailsService {
	
	
		@Autowired
		private FuncionariosDAO funcionariodao;
	
		public Funcionarios getUsuarioLogado() {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String nome; 
			if (principal instanceof UserDetails) {
				nome = ((UserDetails)principal).getUsername();
			} 
			else {
					nome = principal.toString();
				}
			return findByFuncionarioUserName(nome);
		}
		
		public Funcionarios findByFuncionarioUserName(String username) {
			List<Funcionarios> lista = funcionariodao.createQuery
			("select c from Users u inner join funcionarios c where u.username like ?1", username) ;
			return lista.isEmpty() ? null : lista.get(0);
		}
		
		public Users findByUserName(String username) {
			List<Users> lista = this.createQuery("select u from Users u where u.username like ?1", username) ;
			return lista.isEmpty() ? null : lista.get(0);
		}
		
		
		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			Users user = findByUserName(username);
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAppAuthorities());
		}
		


}



