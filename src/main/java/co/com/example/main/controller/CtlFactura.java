package co.com.example.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import co.com.example.main.domain.Factura;
import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoFactura;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlFactura {

	@Autowired
	private RepoUsuario repoUsuario;
	
	@Autowired
	private RepoFactura repoFactura;
	@GetMapping("/misFacturas/{idUsuario}")
	public String listarFacturas(Model model, @PathVariable("idUsuario") int idUsuario) {
		Usuario usuario = repoUsuario.findById(idUsuario);
		List<Factura> listaFacturas= repoFactura.findByCliente(usuario);
		model.addAttribute("listaFacturas", listaFacturas);
		return "misFacturas";
	}
}
