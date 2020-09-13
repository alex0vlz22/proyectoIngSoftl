package co.com.example.main.controller;

import javax.management.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoProducto;
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

	@GetMapping("")
	public String index(Model model) {
		return "index";
	}

	@GetMapping("/ingreso")
	public String ingresoUsuario(Model model, Usuario usuario) {
		if (usuario.getRol().equals("Cliente")) {
			try {
				Usuario u = repoUsuario.findByDNI(usuario.getDNI());
				if (u.getRol().equals("Cliente")) {
					model.addAttribute("usuario", u);
					model.addAttribute("listaSubcategorias", this.repoSubcategoria.findAll());
					model.addAttribute("listaProductos", this.repoProducto.findAll());
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
					model.addAttribute("listaSubcategorias", this.repoSubcategoria.findAll());
					model.addAttribute("listaProductos", this.repoProducto.findAll());
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
			return "redirect:/";
		} else {
			if (usuario.getCodigoEmpresa().equalsIgnoreCase("A7B8C9")) {
				usuario.setRol("Vendedor");
				repoUsuario.save(usuario);
				return "redirect:/";
			} else {
				usuario.setRol("Vendedor");
				model.addAttribute("usuario", usuario);
				model.addAttribute("errorCodigo", true);
				return "registroUsuario";
			}
		}
	}

}
