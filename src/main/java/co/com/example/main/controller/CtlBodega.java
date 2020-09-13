package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Bodega;
import co.com.example.main.repository.RepoBodega;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlBodega {

	@Autowired
	private RepoBodega repoBodega;
	
	@Autowired
	private RepoUsuario repoUsuario;
	
	@GetMapping("/registroBodega/{idVendedor}")
	public String registroBodega(Model model, @PathVariable int idVendedor) {
		model.addAttribute("usuario", repoUsuario.findById(idVendedor));
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", repoBodega.findAll());
		return "registroBodega";
	}
	
	@PostMapping("/guardarBodega")
	public String guardarBodega(Model model, Bodega b) {
		repoBodega.save(b);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", repoBodega.findAll());
		return "redirect:/registroBodega";
	}
	
	@GetMapping("/editarBodega/{id}")
	public String editarBodega(Model model, @PathVariable int id) {
		Bodega b = repoBodega.findById(id);
		model.addAttribute("bodega", b);
		return "editarBodega";
	}
	
	@PostMapping("/modificarBodega/{id}")
	public String modificarBodega(Model model, @PathVariable int id, Bodega b) {
		b.setId(id);
		repoBodega.save(b);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", repoBodega.findAll());
		return "redirect:/registroBodega";
	}
	
	@GetMapping("/eliminarBodega/{id}")
	public String eliminarBodega(Model model, @PathVariable int id) {
		repoBodega.deleteById(id);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", repoBodega.findAll());
		return "redirect:/registroBodega";
	}
	
	
	
	
}
