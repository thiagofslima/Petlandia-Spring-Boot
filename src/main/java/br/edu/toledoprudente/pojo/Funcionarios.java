package br.edu.toledoprudente.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "funcionarios")
public class Funcionarios extends AbstractEntity<Integer> {
	
	@NotBlank(message = "Informe um nome!")
	@Size(min=3, max=150, message = "O nome deve ter entre 3 a 150 caracteres!")
	@Column(length = 150, nullable = false)
	private String nome;

	@NotNull(message = "Informe uma data!")
	@Column(name="datanascimento", nullable = false, columnDefinition = "DATE")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dataNascimento;
	
	// @NotNull(message = "Informe um rendimento!")
	// @PositiveOrZero(message = "Rendimento deve ser maior ou igual a zero!")
	// @Column(name="rendimento", nullable = true, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
	// @NumberFormat(style = Style.CURRENCY, 
	// pattern = "#,##0.00")
	// private BigDecimal rendimento;
	
	
	// @NotNull(message = "Informe uma categoria!")
	// @ManyToOne(cascade=CascadeType.PERSIST)
	// @JoinColumn(name="idcategoria")
	// private Categoria categoria;
	
	
	
	
	public Users getUsuario() {
		return usuario;
	}

	public void setUsuario(Users usuario) {
		this.usuario = usuario;
	}

	@JsonIgnore
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="idUsuario")
	private Users usuario;
	
	@Column(name="imagem", length = 300)
	private String imagem;
	
	
	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	// public Categoria getCategoria() {
	// 	return categoria;
	// }

	// public void setCategoria(Categoria categoria) {
	// 	this.categoria = categoria;
	// }

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	// public BigDecimal getRendimento() {
	// 	return rendimento;
	// }

	// public void setRendimento(BigDecimal rendimento) {
	// 	this.rendimento = rendimento;
	// }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
}
