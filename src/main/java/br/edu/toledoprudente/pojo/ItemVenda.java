package br.edu.toledoprudente.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "itemvenda")
public class ItemVenda extends AbstractEntity<Integer> {
	// valor total
	// data
	// cliente id
	
	// @NotNull(message = "Informe o valor total!")
	// @PositiveOrZero(message = "Valor deve ser maior ou igual a zero!")
	// @Column(name="valortotal", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
	// @NumberFormat(style = Style.CURRENCY, 
	// pattern = "#,##0.00")
	// private BigDecimal valortotal;

	@NotNull(message = "Informe um produto (ItemVenda)!")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="id_produto")
	private Produto produto;

	@NotNull(message = "Informe a venda (ItemVenda)!")
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="id_venda")
	private Venda venda;

	@NotNull(message = "Informe quantidade (ItemVenda)!")
	@Column(name="qtde", nullable = true)
	private int qtde;

	@NotNull(message = "Informe o valor (ItemVenda)!")
	@PositiveOrZero(message = "Valor deve ser maior ou igual a zero!")
	@Column(name="valorunitario", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
	@NumberFormat(style = Style.CURRENCY, 
	pattern = "#,##0.00")
	private BigDecimal valorunitario;



	// public BigDecimal getValortotal() {
	// 	return valortotal;
	// }

	// public void setValortotal(BigDecimal valortotal) {
	// 	this.valortotal = valortotal;
	// }

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public int getQtde() {
		return qtde;
	}

	public void setQtde(int qtde) {
		this.qtde = qtde;
	}

	public BigDecimal getValorunitario() {
		return valorunitario;
	}

	public void setValorunitario(BigDecimal valorunitario) {
		this.valorunitario = valorunitario;
	}

	
}
