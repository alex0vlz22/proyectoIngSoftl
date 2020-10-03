package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Categoria;
import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Subcategoria;
import co.com.example.main.domain.Usuario;
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

	@GetMapping("/registroSubcategoria/{idVendedor}/pag/{page}")
	public String registroSubcategoria(Model model, @PathVariable("idVendedor") int idVendedor,
			@PathVariable("page") int page) {
		Usuario user = this.repoUsuario.findById(idVendedor);
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("listaSubcategorias", this.repoSubcategoria.findByUsuario(PageRequest.of(page, 2), user));
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		model.addAttribute("usuario", user);
		model.addAttribute("producto", new Producto());
		return "registroSubcategoria";
	}

	@GetMapping("/registroSubcategoria/{idVendedor}")
	public String registroSubcategoria(Model model, @PathVariable int idVendedor) {
		Usuario user = this.repoUsuario.findById(idVendedor);
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("listaSubcategorias", this.repoSubcategoria.findByUsuario(PageRequest.of(0, 2), user));
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		model.addAttribute("usuario", user);
		model.addAttribute("producto", new Producto());
		return "registroSubcategoria";
	}

	@PostMapping("/guardarSubcategoria/{idVendedor}")
	public String guardarSubcategoria(Model model, Subcategoria s, @PathVariable int idVendedor) {
		if (s.getIdCategoria() > 0) {
			Categoria c = repoCategoria.findById(s.getIdCategoria());
			s.setCategoria(c);
			s.setUsuario(repoUsuario.findById(idVendedor));
			repoSubcategoria.save(s);
			model.addAttribute("subcategoria", new Subcategoria());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaCategorias", repoCategoria.findAll());
			model.addAttribute("idVendedor", idVendedor);
			model.addAttribute("producto", new Producto());
			return "redirect:/registroSubcategoria/" + idVendedor;
		} else {
			model.addAttribute("subcategoria", new Subcategoria());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaCategorias", repoCategoria.findAll());
			model.addAttribute("idVendedor", idVendedor);
			model.addAttribute("producto", new Producto());
			return "redirect:/registroSubcategoria/" + idVendedor;
		}
	}

	@GetMapping("/editarSubcategoria/{id}")
	public String editarSubcategoria(Model model, @PathVariable int id) {
		Subcategoria s = repoSubcategoria.findById(id);
		model.addAttribute("subcategoria", s);
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		model.addAttribute("usuario", s.getUsuario());
		model.addAttribute("idSubcategoria", s.getId());
		model.addAttribute("producto", new Producto());
		return "editarSubcategoria";
	}

	@PostMapping("/modificarSubcategoria/{id}")
	public String modificarSubcategoria(Model model, Subcategoria s, @PathVariable int id) {
		int idVendedor = repoSubcategoria.findById(id).getUsuario().getId();
		Categoria c = repoCategoria.findById(s.getIdCategoria());
		s.setCategoria(c);
		s.setId(id);
		s.setUsuario(repoUsuario.findById(idVendedor));
		repoSubcategoria.save(s);
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("producto", new Producto());
		return "redirect:/registroSubcategoria/" + idVendedor;
	}

	@GetMapping("/eliminarSubcategoria/{id}")
	public String eliminarSubcategoria(Model model, @PathVariable int id) {
		int idVendedor = repoSubcategoria.findById(id).getUsuario().getId();
		repoSubcategoria.deleteById(id);
		model.addAttribute("subcategoria", new Subcategoria());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaCategorias", repoCategoria.findAll());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("producto", new Producto());
		return "redirect:/registroSubcategoria/" + idVendedor;
	}
}
