package br.edu.toledoprudente.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.toledoprudente.dao.ClienteDAO;
import br.edu.toledoprudente.dao.ItemVendaDAO;
import br.edu.toledoprudente.dao.ProdutoDAO;
import br.edu.toledoprudente.dao.UsersDAO;
import br.edu.toledoprudente.dao.VendaDAO;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	UsersDAO dao;

	@Autowired
	VendaDAO daovenda;

	@Autowired
	ClienteDAO daocliente;

	@Autowired
	ProdutoDAO daoproduto;

	@Autowired
	ItemVendaDAO daoitemvenda;
	
	@GetMapping("/")
	public String home(ModelMap model) {
		model.addAttribute("nomeusuario", dao.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", dao.getUsuarioLogado().getImagem());
		model.addAttribute("count", daovenda.countVendas());
		model.addAttribute("sum", daovenda.sumVendas());
		model.addAttribute("countclientes", daocliente.countClientes());
		model.addAttribute("countprodutos", daoproduto.countProdutos());
		model.addAttribute("maisvendidos", daoitemvenda.getProdutosMaisVendidos());
		// model.addAttribute("imgusuario", dao.
		return "index";
	}
	
	@GetMapping("/login")
	public String login() {
	return "/login";
	}

	
	@GetMapping("/login-error")
	public String loginError(ModelMap model) {
	model.addAttribute("mensagem","Dados inválidos");
	return "/login";
	}

	
}
