package co.com.example.main.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Bodega;
import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoBodega;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlBodega {

	@Autowired
	private RepoBodega repoBodega;
	
	@Autowired
	private RepoUsuario repoUsuario;
	
	@GetMapping("registroBodega/{idVendedor}/pag/{page}")
	public String registroBodegaPag(Model model, @PathVariable("idVendedor") int idVendedor, @PathVariable("page") int page) {
		Usuario user = repoUsuario.findById(idVendedor);
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", repoBodega.findByUsuario(PageRequest.of(page, 3), user));
		model.addAttribute("usuario", user);
		model.addAttribute("producto", new Producto());
		return "registroBodega";
	}
	
	@GetMapping("/registroBodega/{idVendedor}")
	public String registroBodega(Model model, @PathVariable int idVendedor) {
		Usuario user = this.repoUsuario.findById(idVendedor);
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", this.repoBodega.findByUsuario(PageRequest.of(0, 3), user));
		model.addAttribute("usuario", user);
		model.addAttribute("producto", new Producto());
		return "registroBodega";
	}
	
	@PostMapping("/guardarBodega/{idVendedor}")
	public String guardarBodega(Model model, Bodega b, @PathVariable int idVendedor) {
		b.setUsuario(repoUsuario.findById(idVendedor));
		b.setEspacioDisponible(b.getCapacidad());
		repoBodega.save(b);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("listaBodegas", repoBodega.findAll());
		model.addAttribute("producto", new Producto());
		return "redirect:/registroBodega/"+idVendedor;
	}
	
	@GetMapping("/editarBodega/{id}")
	public String editarBodega(Model model, @PathVariable int id) {
		Bodega b = repoBodega.findById(id);
		model.addAttribute("bodega", b);
		model.addAttribute("usuario", b.getUsuario());
		model.addAttribute("producto", new Producto());
		return "editarBodega";
	}
	
	@PostMapping("/modificarBodega/{id}")
	public String modificarBodega(Model model, @PathVariable int id, Bodega b) {
		Bodega bodAux = repoBodega.findById(id);
		int idVendedor = bodAux.getUsuario().getId();
		b.setId(id);
		b.setUsuario(repoUsuario.findById(idVendedor));
		repoBodega.save(b);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", repoBodega.findAll());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("producto", new Producto());
		return "redirect:/registroBodega/"+idVendedor;
	}
	
	@GetMapping("/eliminarBodega/{id}")
	public String eliminarBodega(Model model, @PathVariable int id) {
		int idVendedor = repoBodega.findById(id).getUsuario().getId();
		model.addAttribute("idVendedor", idVendedor);
		repoBodega.deleteById(id);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", repoBodega.findAll());
		model.addAttribute("producto", new Producto());
		return "redirect:/registroBodega/"+ idVendedor;
	}
}
