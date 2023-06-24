package br.edu.toledoprudente.controller;

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
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.toledoprudente.dao.CategoriaDAO;
import br.edu.toledoprudente.dao.UsersDAO;
import br.edu.toledoprudente.pojo.Categoria;
import br.edu.toledoprudente.pojo.Funcionarios;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

	@Autowired
	private CategoriaDAO dao;

	@Autowired
	UsersDAO daoUser;

	@GetMapping("/novo")
	public String novo(ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		model.addAttribute("categoria",
				new Categoria());
		return "/categoria/index";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		model.addAttribute("lista",
				dao.findAll());
		return "/categoria/listar";
	}

	@GetMapping("/prealterar")
	public String preAlterar(
			@RequestParam(name = "id") int id,
			ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		model.addAttribute("categoria",
				dao.findById(id));

		return "/categoria/index";
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
		return "/categoria/listar";
	}

	@PostMapping("/salvar")
	public String salvar(
			@ModelAttribute("categoria") Categoria cat, ModelMap model) {
		try {
			model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
			model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
			Validator validator;
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			Set<ConstraintViolation<Categoria>> constraintViolations = validator.validate(cat);
			String errors = "";
			for (ConstraintViolation<Categoria> constraintViolation : constraintViolations) {
				errors = errors + constraintViolation.getMessage() + ". ";
			}
			if (errors != "") {
				// tem erros
				model.addAttribute("categoria", cat);
				model.addAttribute("mensagem", errors);
				model.addAttribute("retorno", false);
				return "/categoria/index";
			} else {

				List<Categoria> categorias = dao.findByDescricao(cat.getDescricao());
				if (categorias.isEmpty()) {
					if (cat.getId() == null) {
						dao.save(cat);
					} else
						dao.update(cat);
					model.addAttribute("mensagem",
							"Salvo com sucesso!");
				} else {
					model.addAttribute("mensagem",
							"Descrição já cadastrada!");
				}

				model.addAttribute("retorno",
						true);

			}
		} catch (Exception e) {
			model.addAttribute("mensagem",
					"Erro ao salvar!"
							+ e.getMessage());
			model.addAttribute("retorno",
					false);
		}

		return "/categoria/index";
	}

	@GetMapping(path = "/getProduto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getProduto(@PathVariable(value = "id") int id) {
		// produto
		Categoria obj = dao.findById(id);
		return new ResponseEntity<Object>(obj, HttpStatus.OK);
	}

}
