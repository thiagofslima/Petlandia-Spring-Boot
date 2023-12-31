package br.edu.toledoprudente.pojo;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@EntityScan
@Table(name = "Users")
public class Users
		extends AbstractEntity<Integer> implements UserDetails {

	@Column(length = 150, nullable = false)
	private String username;

	@Column(length = 350, nullable = false)
	private String password;

	@Column(nullable = false)
	private Boolean enabled;

	@Column(name = "isAdmin")
	private boolean isAdmin;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.PERSIST)
	private List<Funcionarios> funcionarios;

	public List<Funcionarios> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionarios> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public Set<AppAuthority> getAppAuthorities() {
		return appAuthorities;
	}

	public void setAppAuthorities(Set<AppAuthority> appAuthorities) {
		this.appAuthorities = appAuthorities;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "appUser")
	private Set<AppAuthority> appAuthorities;

	public Users(
			String username,
			String password,
			boolean enabled,
			boolean isAdmin,
			boolean accountNonExpired,
			boolean credentialsNonExpired,
			boolean accountNonLocked,

			Collection<? extends AppAuthority> authorities// ,
	// PersonalInformation personalInformation
	) {
		if (((username == null) || "".equals(username)) || (password == null)) {
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.isAdmin = isAdmin;
		// this.accountNonExpired = accountNonExpired;
		// this.credentialsNonExpired = credentialsNonExpired;
		// this.accountNonLocked = accountNonLocked;
		// this.appAuthorities =
		// Collections.unmodifiableSet(sortAuthorities(authorities));
		// this.personalInformation = personalInformation;
	}

	public Users() {
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
