package br.edu.toledoprudente.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import br.edu.toledoprudente.dao.FuncionariosDAO;
import br.edu.toledoprudente.dao.UsersDAO;
import br.edu.toledoprudente.pojo.Categoria;
import br.edu.toledoprudente.pojo.Funcionarios;
import br.edu.toledoprudente.pojo.Users;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import br.edu.toledoprudente.pojo.AppAuthority;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

	@Autowired
	private FuncionariosDAO dao;

	// @Autowired
	// private CategoriaDAO daocategoria;

	@Autowired
	UsersDAO daoUser;

	@GetMapping("/novo")
	public String novo(ModelMap model) {
		Funcionarios cli = new Funcionarios();
		cli.setUsuario(new Users());
		try {
			model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
			model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		} catch (Exception ex) {
			model.addAttribute("mensagem",
					"Erro nome usuario!");
		}

		model.addAttribute("funcionario", cli);
		return "/funcionario/index";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
		model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());
		model.addAttribute("lista",
				dao.findAll());
		return "/funcionario/listar";
	}

	@GetMapping("/prealterar")
	public String preAlterar(
			@RequestParam(name = "id") int id,
			ModelMap model) {
		model.addAttribute("funcionario",
				dao.findById(id));

		return "/funcionario/index";
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
		return "/funcionario/listar";
	}

	@PostMapping("/salvar")
	public String salvar(
			@ModelAttribute("funcionario") Funcionarios cat, ModelMap model,
			@RequestParam("file") MultipartFile file) {
		try {
			model.addAttribute("nomeusuario", daoUser.getUsuarioLogado().getNome());
			model.addAttribute("imgusuario", daoUser.getUsuarioLogado().getImagem());

			Validator validator;
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			Set<ConstraintViolation<Funcionarios>> constraintViolations = validator.validate(cat);
			String errors = "";
			for (ConstraintViolation<Funcionarios> constraintViolation : constraintViolations) {
				errors = errors + constraintViolation.getMessage() + ". ";
			}

			// validar a imagem
			if (file.isEmpty()) {
				errors = errors + "Selecione uma imagem.";
			}

			if (errors != "") {
				// tem erros
				model.addAttribute("funcionario", cat);
				model.addAttribute("mensagem", errors);
				model.addAttribute("retorno", false);
				return "/funcionario/index";
			} else {

				Users usu = cat.getUsuario();// obter dados do usuario do form
				// criptografar a senha
				String senha = "{bcrypt}" + new BCryptPasswordEncoder()
						.encode(usu.getPassword());

				usu.setPassword(senha);

				usu.setEnabled(true);
				// usu.setAdmin(false);

				// setar a autorização
				Set<AppAuthority> appAuthorities = new HashSet<AppAuthority>();
				AppAuthority app = new AppAuthority();
				app.setAuthority("USER");
				app.setUsername(usu.getUsername());
				appAuthorities.add(app);
				usu.setAppAuthorities(appAuthorities);

				Random random = new Random();
				String nomeArquivo = random.nextInt() +
						file.getOriginalFilename();
				cat.setImagem(nomeArquivo);

				Users temUsuario = daoUser.findByUserName(usu.getUsername());
				boolean deuCerto = true;

				if (cat.getId() == null && temUsuario == null) {
					dao.save(cat);
					model.addAttribute("mensagem",
							"Salvo com sucesso!");

				} else if (cat.getId() != null && (temUsuario == null || temUsuario.getId() == cat.getId())) {
					dao.update(cat);
					model.addAttribute("mensagem",
							temUsuario);
				} else {
					deuCerto = false;
					model.addAttribute("mensagem",
							"Usuário já existe!");
				}

				model.addAttribute("retorno",
						deuCerto);

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

		return "/funcionario/index";
	}

	/* método usado pra retornar dados para select html */
	// @ModelAttribute(name = "listacategoria")
	// public List<Categoria> listaCategoria(){
	// return daocategoria.findAll();

	// }

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
