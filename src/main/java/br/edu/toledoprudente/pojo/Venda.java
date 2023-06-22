package br.edu.toledoprudente.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.cglib.core.Local;
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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "venda")
public class Venda extends AbstractEntity<Integer> {

	@NotNull(message = "Informe o valor total!")
	@PositiveOrZero(message = "Valor deve ser maior ou igual a zero!")
	@Column(name="valortotal", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
	@NumberFormat(style = Style.CURRENCY, 
	pattern = "#,##0.00")
	private BigDecimal valortotal;

	@NotNull(message = "Informe data da venda!")
	@Column(name="datavenda", nullable = false, columnDefinition = "DATE")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate datavenda;

	// @NotNull(message = "Informe um cliente!")
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="id_funcionario")
	private Funcionarios funcionario;

	@Transient
	@OneToMany(mappedBy = "venda")
	public List<ItemVenda> itens;


	public BigDecimal getValortotal() {
		return valortotal;
	}

	public void setValortotal(BigDecimal valortotal) {
		this.valortotal = valortotal;
	}

	public LocalDate getDatavenda() {
		return datavenda;
	}

	public void setDatavenda(LocalDate datavenda) {
		this.datavenda = datavenda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Funcionarios getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionarios funcionario) {
		this.funcionario = funcionario;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}
	
}
