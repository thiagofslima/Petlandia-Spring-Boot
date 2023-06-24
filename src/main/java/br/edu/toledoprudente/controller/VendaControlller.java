package br.edu.toledoprudente.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.toledoprudente.dao.CategoriaDAO;
import br.edu.toledoprudente.dao.ClienteDAO;
import br.edu.toledoprudente.dao.FuncionariosDAO;
import br.edu.toledoprudente.dao.ItemVendaDAO;
import br.edu.toledoprudente.dao.ProdutoDAO;
import br.edu.toledoprudente.dao.UsersDAO;
import br.edu.toledoprudente.dao.VendaDAO;
import br.edu.toledoprudente.pojo.Categoria;
import br.edu.toledoprudente.pojo.Cliente;
import br.edu.toledoprudente.pojo.Funcionarios;
import br.edu.toledoprudente.pojo.ItemVenda;
import br.edu.toledoprudente.pojo.Produto;
import br.edu.toledoprudente.pojo.Venda;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Controller
@RequestMapping("/venda")
public class VendaControlller {

	@Autowired
	ClienteDAO daocliente;

	@Autowired
	CategoriaDAO daocategoria;

	@Autowired
	FuncionariosDAO daofuncionario;

	@Autowired
	ProdutoDAO daoproduto;

	@Autowired
	UsersDAO daoUser;

	@Autowired
	VendaDAO dao;

	@Autowired
	ItemVendaDAO daoitemvenda;

	@GetMapping("/novo")
	public String index(ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		return "/venda/index";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());

		List<Venda> vendas = dao.findAll();

		for (Venda venda : vendas) {
			List<ItemVenda> itens = daoitemvenda.findVenda(venda.getId());
			for (ItemVenda item : itens) {
				Produto produto = daoproduto.findById(item.getProduto().getId());
				item.setProduto(produto);
			}
			venda.setItens(itens);

			Cliente cliente = daocliente.findById(venda.getCliente().getId());
			venda.setCliente(cliente);
		}

		model.addAttribute("lista", vendas);
		return "/venda/listar";
	}

	@GetMapping("/excluir")
	public String excluir(@RequestParam(name = "id") int id,
			ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		try {
			dao.delete(id);
			model.addAttribute("mensagem",
					"Exclusão efetuada");
			model.addAttribute("retorno", true);
		} catch (Exception e) {
			model.addAttribute("mensagem",
					"Exclusão não pode ser efetuada!");
			model.addAttribute("retorno", false);
		}
		model.addAttribute("lista", dao.findAll());
		return "/venda/listar";
	}

	@ModelAttribute(name = "listacliente")
	public List<Cliente> listaCliente() {
		return daocliente.findAll();
	}

	@ModelAttribute(name = "listacategoria")
	public List<Categoria> listaCategoria() {
		return daocategoria.findAll();
	}

	@ModelAttribute(name = "listafuncionario")
	public List<Funcionarios> listaFuncionario() {
		return daofuncionario.findAll();
	}

	@ModelAttribute(name = "listaproduto")
	public List<Produto> listaProduto() {
		return daoproduto.findAll();
	}

	@PostMapping(path = "/salvarProduto", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> salvar(@RequestBody Categoria produto) {
		try {

			if (produto.getId() == null)
				daocategoria.save(produto);
			else
				daocategoria.update(produto);
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return new ResponseEntity<Object>(produto, HttpStatus.OK);
	}

	@PostMapping(path = "/salvar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> salvar(@RequestBody Venda venda) {
		try {
			Venda vendaI = new Venda();
			Cliente cliente = daocliente.findById(venda.getCliente().getId());
			Funcionarios funcionario = daofuncionario.findById(daoUser.getUsuarioLogado().getId());
			vendaI.setCliente(cliente);
			vendaI.setFuncionario(funcionario);
			vendaI.setDatavenda(LocalDate.now());
			vendaI.setValortotal(venda.getValortotal());

			dao.save(vendaI);

			List<ItemVenda> itemsVenda = venda.getItens();

			for (ItemVenda itemVenda : itemsVenda) {

				ItemVenda item = new ItemVenda();
				Produto produto = daoproduto.findById(itemVenda.getProduto().getId());
				item.setProduto(produto);
				item.setQtde(itemVenda.getQtde());
				item.setValorunitario(itemVenda.getValorunitario());
				item.setVenda(vendaI);
				daoitemvenda.save(item);

				produto.setEstoque(produto.getEstoque() - item.getQtde());
				daoproduto.save(produto);
			}

		} catch (Exception e) {
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>(venda, HttpStatus.OK);

	}

	// @GetMapping("/itens-venda/{idVenda}")
	// public String exibirItensVenda(@PathVariable("idVenda") Long idVenda,
	// ModelMap model) {
	// Venda venda = dao.findById(23); // Supondo que você tenha um método
	// 'findById' no seu DAO para buscar a
	// // venda pelo ID
	// if (venda == null) {
	// // Lógica para lidar com venda não encontrada
	// return "redirect:/venda/listar"; // Redireciona para a página de listagem de
	// vendas
	// }

	// model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
	// model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
	// model.addAttribute("itensVenda", venda.getItens()); // Supondo que a classe
	// Venda tenha um método 'getItens' que
	// // retorna os itens da venda

	// return "/venda/exibir-itens";
	// }

	// @GetMapping("/venda/{id}/itens")
	// public String exibirItensVenda(@PathVariable("id") Long vendaId, ModelMap
	// model) {
	// ItemVenda venda = daoitemvenda.findById(23); // Substitua por sua lógica para
	// obter a venda pelo ID
	// model.addAttribute("item", venda);
	// return "/venda/modal"; // Substitua pelo nome do seu fragmento HTML para
	// exibir os itens da venda
	// }

	List<ItemVenda> itemsVenda;

	@PostMapping(path = "/buscaritem", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> salvar(@RequestBody ItemVenda item, ModelMap model) {
		try {
			itemsVenda = daoitemvenda.findVenda(item.getId());
			// Venda teste = new Venda();
			// Cliente cliente = daocliente.findById(venda.getCliente().getId());
			// Funcionarios funcionario =
			// daofuncionario.findById(daoUser.getUsuarioLogado().getId());
			// teste.setCliente(cliente);
			// teste.setFuncionario(funcionario);
			// teste.setDatavenda(LocalDate.now());
			// teste.setValortotal(new BigDecimal(45.99));

			// dao.save(teste);

			// List<ItemVenda> itemsVenda = venda.getItens();

			// for (ItemVenda itemVenda : itemsVenda) {

			// ItemVenda item = new ItemVenda();
			// Produto produto = daoproduto.findById(itemVenda.getProduto().getId());
			// item.setProduto(produto);
			// item.setQtde(itemVenda.getQtde());
			// item.setValorunitario(itemVenda.getValorunitario());
			// item.setVenda(teste);
			// daoitemvenda.save(item);

			// produto.setEstoque(produto.getEstoque() - item.getQtde());
			// daoproduto.save(produto);
			// }

		} catch (Exception e) {
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>(itemsVenda, HttpStatus.OK);

	}

}
