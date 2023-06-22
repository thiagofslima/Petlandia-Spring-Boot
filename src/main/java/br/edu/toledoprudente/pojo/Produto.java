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
@Table(name = "produto")
public class Produto extends AbstractEntity<Integer> {
	
	@NotBlank(message = "Informe o nome do Produto!")
	@Size(min=3, max=150, message = "O nome deve ter entre 3 a 150 caracteres!")
	@Column(length = 150, nullable = false)
	private String nome;

	@Size(min=10, max=150, message = "A descrição deve ter 13 caracteres!")
	@Column(length = 150, nullable = true)
	private String descricao;

	@NotNull(message = "Informe o valor do produto!")
	@PositiveOrZero(message = "Valor deve ser maior ou igual a zero!")
	@Column(name="precovenda", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
	@NumberFormat(style = Style.CURRENCY, 
	pattern = "#,##0.00")
	private BigDecimal precovenda;

	// @NotNull(message = "Informe quantidade no estoque!")
	@Column(name="estoque", nullable = true)
	private int estoque;

	@NotNull(message = "Informe uma categoria!")
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="id_categoria")
	private Categoria categoria;

	@Column(name="imagem", length = 300)
	private String imagem;


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPrecovenda() {
		return precovenda;
	}

	public void setPrecovenda(BigDecimal precovenda) {
		this.precovenda = precovenda;
	}
	
	public int getEstoque() {
		return estoque;
	}

	public void setEstoque(int estoque) {
		this.estoque = estoque;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	
}
