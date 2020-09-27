package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Proveedor;
import co.com.example.main.repository.RepoProveedor;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlProveedor {

	@Autowired
	private RepoProveedor repoProveedor;

	@Autowired
	private RepoUsuario repoUsuario;

	@GetMapping("/registroProveedor/{idVendedor}")
	public String registroProveedor(Model model, @PathVariable int idVendedor,
			@RequestParam(defaultValue = "0") int page) {
		model.addAttribute("proveedor", new Proveedor());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("usuario", repoUsuario.findById(idVendedor));
		model.addAttribute("listaProveedores", repoProveedor.findAll(PageRequest.of(page, 2)));
		model.addAttribute("producto", new Producto());
		return "registroProveedor";
	}

	@GetMapping("/registroProveedor/{idVendedor}/pag/{page}")
	public String pagRegistroProveedor(Model model, @PathVariable int idVendedor, @PathVariable("page") int page) {
		model.addAttribute("proveedor", new Proveedor());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("usuario", repoUsuario.findById(idVendedor));
		model.addAttribute("listaProveedores", repoProveedor.findAll(PageRequest.of(page, 2)));
		model.addAttribute("producto", new Producto());
		return "registroProveedor";
	}

	@PostMapping("/guardarProveedor/{idVendedor}")
	public String guardarProveedor(Model model, Proveedor proveedor, @PathVariable int idVendedor) {
		proveedor.setUsuario(repoUsuario.findById(idVendedor));
		repoProveedor.save(proveedor);
		model.addAttribute("proveedor", new Proveedor());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("producto", new Producto());
		return "redirect:/registroProveedor/" + idVendedor;
	}

	@GetMapping("/editarProveedor/{id}")
	public String editarProveedor(@PathVariable int id, Model model) {
		Proveedor p = repoProveedor.findById(id);
		model.addAttribute("proveedor", p);
		model.addAttribute("usuario", p.getUsuario());
		model.addAttribute("producto", new Producto());
		return "editarProveedor";
	}

	@PostMapping("/modificarProveedor/{id}")
	public String modificarProveedor(Model model, Proveedor proveedor, @PathVariable int id) {
		int idVendedor = repoProveedor.findById(id).getUsuario().getId();
		proveedor.setId(id);
		proveedor.setUsuario(repoUsuario.findById(idVendedor));
		repoProveedor.save(proveedor);
		model.addAttribute("proveedor", new Proveedor());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("producto", new Producto());
		return "redirect:/registroProveedor/" + idVendedor;
	}

	@GetMapping("/eliminarProveedor/{id}")
	public String eliminarProveedor(Model model, @PathVariable int id) {
		int idVendedor = repoProveedor.findById(id).getUsuario().getId();
		repoProveedor.deleteById(id);
		model.addAttribute("proveedor", new Proveedor());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("producto", new Producto());
		return "redirect:/registroProveedor/" + idVendedor;
	}

}
