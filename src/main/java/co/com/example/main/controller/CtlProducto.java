package co.com.example.main.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.utils.ObjectUtils;
import co.com.example.main.CloudinaryConfig;

import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Proveedor;
import co.com.example.main.domain.Subcategoria;
import co.com.example.main.repository.RepoProducto;
import co.com.example.main.repository.RepoProveedor;
import co.com.example.main.repository.RepoSubcategoria;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlProducto {

	@Autowired
	private RepoProducto repoProducto;

	@Autowired
	private RepoProveedor repoProveedor;

	@Autowired
	private RepoSubcategoria repoSubcategoria;

	@Autowired
	private RepoUsuario repoUsuario;
	
	@Autowired
	private CloudinaryConfig cloudc;

	@GetMapping("/registroProducto/{idVendedor}")
	public String registroProducto(Model model, @PathVariable int idVendedor) {
		model.addAttribute("producto", new Producto());
		model.addAttribute("usuario", repoUsuario.findById(idVendedor));
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos", repoProducto.findAll());
		return "registroProducto";
	}

	@PostMapping("/guardarProducto")
	public String guardarProducto(Model model, Producto producto, @RequestParam("file") MultipartFile file) {
		Proveedor p = repoProveedor.findByNombre(producto.getNombreProveedor().toString());
		Subcategoria c = repoSubcategoria.findById(producto.getIdSubcategoria());
		producto.setProveedor(p);
		producto.setSubcategoria(c);
		try {
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
			System.out.println(uploadResult.get("url").toString());
			producto.setUrlFoto(uploadResult.get("url").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public String modificarProducto(Model model, Producto p, @PathVariable int id,
			@RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl) {
		p.setId(id);
		Proveedor producto = repoProveedor.findByNombre(p.getNombreProveedor().toString());
		Subcategoria c = repoSubcategoria.findById(p.getIdSubcategoria());
		p.setProveedor(producto);
		p.setSubcategoria(c);
		if (cambioUrl) {
			try {
				Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
				System.out.println(uploadResult.get("url").toString());
				p.setUrlFoto(uploadResult.get("url").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	@PostMapping("/user/subirImagen")
	public @ResponseBody String subirImagen(@RequestParam("file") MultipartFile file) {
		try {
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
			System.out.println(uploadResult.get("url").toString());
			return uploadResult.get("url").toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return "";
		}
	}

}
