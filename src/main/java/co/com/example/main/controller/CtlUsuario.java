package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoProducto;
import co.com.example.main.repository.RepoProveedor;
import co.com.example.main.repository.RepoSubcategoria;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlUsuario {

	@Autowired
	private RepoUsuario repoUsuario;

	@Autowired
	private RepoSubcategoria repoSubcategoria;

	@Autowired
	private RepoProducto repoProducto;

	@Autowired
	private RepoProveedor repoProveedor;

	@GetMapping("")
	public String index(Model model) {
		model.addAttribute("usuarioRegistrado", true);
		return "index";
	}

	@GetMapping("/inicio/{idUsuario}")
	public String inicio(Model model, @PathVariable int idUsuario) {
		Usuario u = repoUsuario.findById(idUsuario);
		model.addAttribute("usuario", u);
		model.addAttribute("listaSubcategorias", this.repoSubcategoria.findAll());
		model.addAttribute("listaProveedores", this.repoProveedor.findAll());
		model.addAttribute("listaProductos", this.repoProducto.findAll());
		model.addAttribute("producto", new Producto());
		return "ingresoUsuario";
	}

	@GetMapping("/ingreso")
	public String ingresoUsuario(Model model, Usuario usuario) {
		if (usuario.getRol().equals("Cliente")) {
			try {
				Usuario u = repoUsuario.findByDNI(usuario.getDNI());
				if (u.getRol().equals("Cliente")) {
					model.addAttribute("usuario", u);
					model.addAttribute("listaSubcategorias", this.repoSubcategoria.findAll());
					model.addAttribute("listaProveedores", this.repoProveedor.findAll());
					model.addAttribute("listaProductos", this.repoProducto.findAll());
					model.addAttribute("producto", new Producto());
					return "ingresoUsuario";
				} else {
					// no se encontro un cliente por este dni
					model.addAttribute("noEncontrado", true);
					return "login";
				}
			} catch (NullPointerException e) {
				model.addAttribute("noEncontrado", true);
				return "login";
			}
		} else {
			try {
				Usuario u = repoUsuario.findByDNI(usuario.getDNI());
				if (u.getRol().equals("Vendedor")) {
					model.addAttribute("usuario", u);
					model.addAttribute("listaProveedores", this.repoProveedor.findAll());
					model.addAttribute("listaSubcategorias", this.repoSubcategoria.findAll());
					model.addAttribute("listaProductos", this.repoProducto.findAll());
					model.addAttribute("producto", new Producto());
					return "ingresoUsuario";
				} else {
					// no se encontro un vendedor por este dni
					model.addAttribute("noEncontrado", true);
					return "login";
				}
			} catch (NullPointerException e) {
				model.addAttribute("noEncontrado", true);
				return "login";
			}
		}
	}

	@GetMapping("/registroClienteVendedor")
	public String registroClienteVendedor(Model model) {
		return "registroClienteVendedor";
	}

	@GetMapping("/soporte")
	public String soporte(Model model) {
		return "soporte";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("noEncontrado", false);
		model.addAttribute("usuario", new Usuario());
		return "login";
	}

	@GetMapping("/QuedateEnCasa")
	public String video(Model model) {
		return "video";
	}

	@GetMapping("/registroCliente")
	public String registroCliente(Model model) {
		Usuario usuario = new Usuario();
		usuario.setRol("Cliente");
		model.addAttribute("usuario", usuario);
		return "registroUsuario";
	}

	@GetMapping("/registroVendedor")
	public String registroVendedor(Model model) {
		Usuario usuario = new Usuario();
		usuario.setRol("Vendedor");
		model.addAttribute("usuario", usuario);
		return "registroUsuario";
	}

	@PostMapping("/guardarUsuario")
	public String guardarUsaurio(Model model, Usuario usuario) {
		if (usuario.getCodigoEmpresa() == null) {
			usuario.setRol("Cliente");
			repoUsuario.save(usuario);
			model.addAttribute("usuarioRegistrado", true);
			return "redirect:/";
		} else {
			if (usuario.getCodigoEmpresa().equalsIgnoreCase("A7B8C9")) {
				usuario.setRol("Vendedor");
				repoUsuario.save(usuario);
				model.addAttribute("usuarioRegistrado", true);
				return "redirect:/";
			} else {
				usuario.setRol("Vendedor");
				model.addAttribute("usuario", usuario);
				model.addAttribute("errorCodigo", true);
				return "registroUsuario";
			}
		}
	}

	@GetMapping("/editarPerfil/{idUsuario}")
	public String editarUsuario(Model model, @PathVariable int idUsuario) {
		model.addAttribute("usuario", this.repoUsuario.findById(idUsuario));
		model.addAttribute("producto", new Producto());
		return "editarUsuario";
	}

	@PostMapping("/modificarUsuario/{idUsuario}")
	public String modificarUsuario(Model model, Usuario usuario, @PathVariable("idUsuario") int idUsuario) {
		usuario.setId(idUsuario);
		this.repoUsuario.save(usuario);
		model.addAttribute("usuario", usuario);
		model.addAttribute("listaProveedores", this.repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", this.repoSubcategoria.findAll());
		model.addAttribute("listaProductos", this.repoProducto.findAll());
		model.addAttribute("producto", new Producto());
		return "redirect:/inicio/" + idUsuario;
	}

	@GetMapping("/eliminarCuenta/{idUsuario}")
	public String eliminarCuenta(Model model, @PathVariable("idUsuario") int id) {
		this.repoUsuario.delete(this.repoUsuario.findById(id));
		return "redirect:/";
	}

}
