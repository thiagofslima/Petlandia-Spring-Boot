package br.edu.toledoprudente.pojo;

import org.springframework.boot.autoconfigure.domain.EntityScan;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@EntityScan
@Table(name="cliente")
public class Cliente 
extends AbstractEntity<Integer> {

	@NotBlank(message = "Informe um nome!")
	@Size(min = 3, max = 150, message = "O nome deve conter de 3 a 100 caracteres!")
	@Column(length = 150, nullable = false)
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
