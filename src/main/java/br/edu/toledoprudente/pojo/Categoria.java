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
@Table(name="categoria")
public class Categoria 
extends AbstractEntity<Integer> {

	    @NotBlank(message = "Informe uma descrição!")
	    @Size(min = 3, max = 150, message = "A descrição deve conter de 3 a 150 caracteres!")
		@Column(length = 150, nullable = false)
		private String descricao;

		// @JsonIgnore
		// @OneToMany(mappedBy = "categoria")
		// private List<Funcionarios> funcionarios;
		
		// public List<Funcionarios> getFuncionarios() {
		// 	return funcionarios;
		// }

		// public void setFuncionarios(List<Funcionarios> funcionarios) {
		// 	this.funcionarios = funcionarios;
		// }

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		
		
		
	
}
