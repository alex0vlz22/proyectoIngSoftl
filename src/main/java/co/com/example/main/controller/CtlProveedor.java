package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Proveedor;
import co.com.example.main.repository.RepoProveedor;

@Controller
public class CtlProveedor {

	@Autowired
	private RepoProveedor repoProveedor;
	
	@GetMapping("/registroProveedor")
	public String registroProveedor(Model model) {
		model.addAttribute("proveedor", new Proveedor());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		return "registroProveedor";
	}
	
	@PostMapping("/guardarProveedor")
	public String guardarProveedor(Model model, Proveedor proveedor) {
		repoProveedor.save(proveedor);
		model.addAttribute("proveedor", new Proveedor());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		return "redirect:/registroProveedor";
	}
	
	@GetMapping("/editarProveedor/{id}")
	public String editarProveedor(@PathVariable int id, Model model) {
		Proveedor p = repoProveedor.findById(id);
		model.addAttribute("proveedor", p);
		return "editarProveedor";
	}
	
	@PostMapping("/modificarProveedor/{id}")
	public String modificarProveedor(Model model, Proveedor proveedor, @PathVariable int id) {
		proveedor.setId(id);
		repoProveedor.save(proveedor);
		model.addAttribute("proveedor", new Proveedor());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		return "redirect:/registroProveedor";
	}
	
	@GetMapping("/eliminarProveedor/{id}")
	public String eliminarProveedor(Model model, @PathVariable int id) {
		repoProveedor.deleteById(id);
		model.addAttribute("proveedor", new Proveedor());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		return "redirect:/registroProveedor";
	}
	
}




