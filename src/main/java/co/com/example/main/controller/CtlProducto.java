package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Proveedor;
import co.com.example.main.domain.Subcategoria;
import co.com.example.main.repository.RepoProducto;
import co.com.example.main.repository.RepoProveedor;
import co.com.example.main.repository.RepoSubcategoria;

@Controller
public class CtlProducto {

	@Autowired
	private RepoProducto repoProducto;
	
	@Autowired
	private RepoProveedor repoProveedor;
	
	@Autowired
	private RepoSubcategoria repoSubcategoria;
	
	@GetMapping("/registroProducto")
	public String registroProducto(Model model){
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos", repoProducto.findAll());
		return "registroProducto";
	}
	
	@PostMapping("/guardarProducto")
	public String guardarProducto(Model model, Producto producto) {
		Proveedor p = repoProveedor.findByNombre(producto.getNombreProveedor().toString());
		Subcategoria c = repoSubcategoria.findById(producto.getIdSubcategoria());
		producto.setProveedor(p);
		producto.setSubcategoria(c);
		repoProducto.save(producto);
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos", repoProducto.findAll());
		return "redirect:/registroProducto";
	}
	
	@GetMapping("/editarProducto/{id}")
	public String editarProducto(Model model, @PathVariable int id) {
		Producto p = repoProducto.findById(id);
		model.addAttribute("producto", p);
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		return "editarProducto";
	}
	
	@PostMapping("/modificarProducto/{id}")
	public String modificarProducto(Model model, Producto p, @PathVariable int id) {
		p.setId(id);
		Proveedor producto = repoProveedor.findByNombre(p.getNombreProveedor().toString());
		Subcategoria c = repoSubcategoria.findById(p.getIdSubcategoria());
		p.setProveedor(producto);
		p.setSubcategoria(c);
		repoProducto.save(p);
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos", repoProducto.findAll());
		return "redirect:/registroProducto";
	}
	
	@GetMapping("/eliminarProducto/{id}")
	public String eliminarProducto(Model model, @PathVariable int id) {
		repoProducto.deleteById(id);
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos", repoProducto.findAll());
		return "redirect:/registroProducto";
	}
	
	
}
