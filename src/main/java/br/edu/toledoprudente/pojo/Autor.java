package br.edu.toledoprudente.pojo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@EntityScan
@Table(name="autor")
public class Autor 
extends AbstractEntity<Integer> {

	    @NotBlank(message = "Informe o nome!")
	    @Size(min = 3, max = 150, message = "O nome deve conter de 3 a 150 caracteres!")
		@Column(length = 150, nullable = false)
		private String nome;
		
		// @JsonIgnore
		// @OneToMany(mappedBy = "autor")
		// private List<Funcionarios> funcionarios;
		
		// public List<Funcionarios> getFuncionarios() {
		// 	return funcionarios;
		// }

		// public void setFuncionarios(List<Funcionarios> funcionarios) {
		// 	this.funcionarios = funcionarios;
		// }

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}
		
		
		
	
}
