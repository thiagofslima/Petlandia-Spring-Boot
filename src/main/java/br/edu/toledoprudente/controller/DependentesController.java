package br.edu.toledoprudente.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.toledoprudente.dao.CategoriaDAO;
import br.edu.toledoprudente.dao.FuncionariosDAO;
import br.edu.toledoprudente.pojo.Funcionarios;

@Controller
@RequestMapping("/dependentes")
public class DependentesController {
	
	@Autowired
	FuncionariosDAO daofuncionarios;

	@Autowired
	CategoriaDAO daocategoria;
	
	
	@GetMapping(path = "/getFuncionarios/{nome}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFuncionarios(@PathVariable(value = "nome") String nome)
    {
		
		List<Funcionarios> lista =  daofuncionarios.findByName(nome);
        return new ResponseEntity<Object>( lista, HttpStatus.OK);
    }

	
	
	// @PostMapping(path = "/salvarFuncionario", produces=MediaType.APPLICATION_JSON_VALUE,
	// 		consumes = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<Object> salvar(@RequestBody Funcionarios funcionario) {
	// try {
	// 	funcionario.setCategoria(daocategoria.findById(
	// 			funcionario.getCategoria().getId()));
	// 	if(funcionario.getId()==null)
	// 		daofuncionarios.save(funcionario);
	// 	else
	// 		daofuncionarios.update(funcionario);
	// }
	// catch (Exception e) {
	// System.out.print(e.getMessage());
	// }
	// return new ResponseEntity<Object>( funcionario, HttpStatus.OK);
	// }

	
	@GetMapping(path = "/index")
	public String index() {
		
		return "/dependentes/index";
	}
	

}
