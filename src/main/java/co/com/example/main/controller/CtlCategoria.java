package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Categoria;
import co.com.example.main.domain.Producto;
import co.com.example.main.repository.RepoCategoria;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlCategoria {

	@Autowired
	private RepoCategoria repoCategoria;
	
	@Autowired
	private RepoUsuario repoUsuario;
	
	@GetMapping("/registroCategoria/{idVendedor}")
	public String registroCategoria(Model model, @PathVariable int idVendedor) {
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		model.addAttribute("usuario", repoUsuario.findById(idVendedor));
		model.addAttribute("producto", new Producto());
		return "registroCategoria";
	}
	
	@PostMapping("/guardarCategoria/{idVendedor}")
	public String guardarCategoria(Model model, Categoria categoria, @PathVariable int idVendedor){
		categoria.setUsuario(repoUsuario.findById(idVendedor));
		repoCategoria.save(categoria);
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("producto", new Producto());
		return "redirect:/registroCategoria/"+idVendedor;
		
	}
	
	@GetMapping("/editarCategoria/{id}")
	public String editarCategoria(@PathVariable int id, Model model){
		Categoria c = repoCategoria.findById(id);
		model.addAttribute("categoria", c);
		model.addAttribute("usuario", c.getUsuario());
		model.addAttribute("producto", new Producto());
		return "editarCategoria";
	}
	
	@PostMapping("/modificarCategoria/{id}")
	public String editarCategoria(@PathVariable int id, Categoria categoria, Model model){
		categoria.setUsuario(repoCategoria.findById(id).getUsuario());
		categoria.setId(id);
		repoCategoria.save(categoria);
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		model.addAttribute("idVendedor", categoria.getUsuario().getId());
		model.addAttribute("producto", new Producto());
		return "redirect:/registroCategoria/"+categoria.getUsuario().getId();
	}
	
	@GetMapping("/eliminarCategoria/{id}")
	public String eliminarCategoria(@PathVariable int id, Model model){
		int idVendedor = repoCategoria.findById(id).getUsuario().getId();
		repoCategoria.deleteById(id);
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("producto", new Producto());
		return "redirect:/registroCategoria/"+idVendedor;
	}
	
}
