package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Categoria;
import co.com.example.main.domain.Subcategoria;
import co.com.example.main.repository.RepoCategoria;
import co.com.example.main.repository.RepoSubcategoria;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlSubcategoria {

	@Autowired
	private RepoSubcategoria repoSubcategoria;
	
	@Autowired
	private RepoCategoria repoCategoria;
	
	@Autowired
	private RepoUsuario repoUsuario;
	
	@GetMapping("/registroSubcategoria/{idVendedor}")
	public String registroSubcategoria(Model model, @PathVariable int idVendedor){
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("usuario", repoUsuario.findById(idVendedor));
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		return "registroSubcategoria";
	}
	
	@PostMapping("/guardarSubcategoria")
	public String guardarSubcategoria(Model model, Subcategoria s) {
		if(s.getIdCategoria() > 0) {
			Categoria c = repoCategoria.findById(s.getIdCategoria());
			s.setCategoria(c);
			repoSubcategoria.save(s);
			model.addAttribute("subcategoria", new Subcategoria());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaCategorias", repoCategoria.findAll());
			return "redirect:/registroSubcategoria";
		}else {
			model.addAttribute("subcategoria", new Subcategoria());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaCategorias", repoCategoria.findAll());
			return "redirect:/registroSubcategoria";
		}
		
	}
	
	@GetMapping("/editarSubcategoria/{id}")
	public String editarSubcategoria(Model model, @PathVariable int id) {
		Subcategoria s = repoSubcategoria.findById(id);
		model.addAttribute("subcategoria", s);
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		return "editarSubcategoria";
	}
	
	@PostMapping("/modificarSubcategoria/{id}")
	public String modificarSubcategoria(Model model, Subcategoria s, @PathVariable int id) {
		Categoria c = repoCategoria.findById(s.getIdCategoria());
		s.setCategoria(c);
		s.setId(id);
		repoSubcategoria.save(s);
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		return "redirect:/registroSubcategoria";
	}
	
	@GetMapping("/eliminarSubcategoria/{id}")
	public String eliminarSubcategoria(Model model, @PathVariable int id) {
		repoSubcategoria.deleteById(id);
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		return "redirect:/registroSubcategoria";
	}
}

 	





