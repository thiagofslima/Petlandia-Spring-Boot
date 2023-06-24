package br.edu.toledoprudente.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.edu.toledoprudente.dao.CategoriaDAO;
import br.edu.toledoprudente.dao.ProdutoDAO;
import br.edu.toledoprudente.dao.UsersDAO;
import br.edu.toledoprudente.pojo.Categoria;
import br.edu.toledoprudente.pojo.Produto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

	@Autowired
	private ProdutoDAO dao;

	@Autowired
	UsersDAO daoUser;

	@Autowired
	private CategoriaDAO daocategoria;

	@GetMapping("/novo")
	public String novo(ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		model.addAttribute("produto",
				new Produto());
		return "/produto/index";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		model.addAttribute("lista",
				dao.findAll());
		return "/produto/listar";
	}

	@GetMapping("/prealterar")
	public String preAlterar(
			@RequestParam(name = "id") int id,
			ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		model.addAttribute("produto",
				dao.findById(id));

		return "/produto/index";
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
		return "/produto/listar";
	}

	@PostMapping("/salvar")
	public String salvar(
			@ModelAttribute("produto") Produto pro, ModelMap model,
			@RequestParam("file") MultipartFile file) {
		try {
			model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
			model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
			Validator validator;
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			Set<ConstraintViolation<Produto>> constraintViolations = validator.validate(pro);
			String errors = "";
			for (ConstraintViolation<Produto> constraintViolation : constraintViolations) {
				errors = errors + constraintViolation.getMessage() + ". ";
			}
			if (errors != "") {
				// tem erros
				model.addAttribute("produto", pro);
				model.addAttribute("mensagem", errors);
				model.addAttribute("retorno", false);
				return "/produto/index";
			} else {

				Random random = new Random();
				String nomeArquivo = random.nextInt() +
						file.getOriginalFilename();
				pro.setImagem(nomeArquivo);

				// List<Produto> produtos = dao.findByDescricao(pro.getNome());

				// Produto produtoBusca = dao.findByProdutoNome(pro.getNome());
				// if (produtoBusca.getNome().isEmpty()) {
				// if(pro.getId() == null) {
				// dao.save(pro);
				// }
				// else {
				// dao.update(pro);
				// }
				// model.addAttribute("mensagem",
				// "Salvo com sucesso!");
				// }
				// else {
				// model.addAttribute("mensagem",
				// "Produto já cadastrado!");
				// }

				if (pro.getId() == null) {
					dao.save(pro);
				} else
					dao.update(pro);
				model.addAttribute("mensagem",
						"Salvo com sucesso!");

				// model.addAttribute("mensagem", teste);

				model.addAttribute("retorno",
						true);

				try {
					byte[] bytes = file.getBytes();
					Path path = Paths.get(System.getProperty("user.dir") +
							"\\src\\main\\resources\\static\\image\\" + nomeArquivo);
					Files.write(path, bytes);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			model.addAttribute("mensagem",
					"Erro ao salvar!"
							+ e.getMessage());
			model.addAttribute("retorno",
					false);
		}

		return "/produto/index";
	}

	@GetMapping(path = "/getProduto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getProduto(@PathVariable(value = "id") int id) {
		// produto
		Produto obj = dao.findById(id);
		return new ResponseEntity<Object>(obj, HttpStatus.OK);
	}

	/* método usado pra retornar dados para select html */
	@ModelAttribute(name = "listacategoria")
	public List<Categoria> listaCategoria() {
		return daocategoria.findAll();

	}

	@ResponseBody
	@RequestMapping(value = "/getimagem/{nome}", method = RequestMethod.GET)
	public HttpEntity<byte[]> download(@PathVariable(value = "nome") String nome) throws IOException {
		byte[] arquivo = Files.readAllBytes(
				Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\image\\" + nome));
		HttpHeaders httpHeaders = new HttpHeaders();
		switch (nome.substring(nome.lastIndexOf(".") + 1).toUpperCase()) {
			case "JPG":
				httpHeaders.setContentType(MediaType.IMAGE_JPEG);
				break;
			case "GIF":
				httpHeaders.setContentType(MediaType.IMAGE_GIF);
				break;
			case "PNG":
				httpHeaders.setContentType(MediaType.IMAGE_PNG);
				break;

			default:
				break;
		}
		httpHeaders.setContentLength(arquivo.length);
		HttpEntity<byte[]> entity = new HttpEntity<byte[]>(arquivo, httpHeaders);
		return entity;
	}

}
