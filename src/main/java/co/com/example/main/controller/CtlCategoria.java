package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Categoria;
import co.com.example.main.repository.RepoCategoria;

@Controller
public class CtlCategoria {

	@Autowired
	private RepoCategoria repoCategoria;
	
	@GetMapping("/registroCategoria")
	public String registroCategoria(Model model) {
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		return "registroCategoria";
	}
	
	@PostMapping("/guardarCategoria")
	public String guardarCategoria(Model model, Categoria categoria){
		
		repoCategoria.save(categoria);
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		return "redirect:/registroCategoria";
		
	}
	
	@GetMapping("/editarCategoria/{id}")
	public String editarCategoria(@PathVariable int id, Model model){
		Categoria c = repoCategoria.findById(id);
		model.addAttribute("categoria", c);
		return "editarCategoria";
	}
	
	@PostMapping("/modificarCategoria/{id}")
	public String editarCategoria(@PathVariable int id, Categoria categoria, Model model){
		categoria.setId(id);
		repoCategoria.save(categoria);
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		return "redirect:/registroCategoria";
	}
	
	@GetMapping("/eliminarCategoria/{id}")
	public String eliminarCategoria(@PathVariable int id, Model model){
		repoCategoria.deleteById(id);
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		return "redirect:/registroCategoria";
	}
	
}
