package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlUsuario {

	@Autowired
	private RepoUsuario repoUsuario;

	@GetMapping("")
	public String index(Model model) {
		return "index";
	}
	
	@GetMapping("/ingreso")
	public String ingresoUsuario(Model model) {
		return "ingresoUsuario";
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
