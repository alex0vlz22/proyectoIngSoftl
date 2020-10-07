package co.com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import co.com.example.main.domain.Facturaa;
import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoDetalleFactura;
import co.com.example.main.repository.RepoFacturaa;
import co.com.example.main.repository.RepoUsuario;

@Controller
public class CtlFacturaa {

	@Autowired
	private RepoFacturaa repoFactura;

	@Autowired
	private RepoUsuario repoUsuario;

	@Autowired
	private RepoDetalleFactura repoDetalleFactura;

	@GetMapping("/visualizarFacturas/{idUsuario}")
	public String visualizarFacturas(Model model, @PathVariable("idUsuario") int idUsuario) {
		Usuario user = this.repoUsuario.findById(idUsuario);
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaFacturas", this.repoFactura.findByComprador(PageRequest.of(0, 5), user));
		model.addAttribute("usuario", user);
		return "visualizarFacturas";
	}

	@GetMapping("/visualizarDetalle/{idFactura}/{idUsuario}")
	public String visualizarDetalleFactura(Model model, @PathVariable("idUsuario") int idUsuario,
			@PathVariable("idFactura") int idFactura) {
		Facturaa f = this.repoFactura.findById(idFactura);
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaDetalles", this.repoDetalleFactura.findByFactura(PageRequest.of(0, 6), f));
		model.addAttribute("usuario", this.repoUsuario.findById(idUsuario));
		model.addAttribute("factura", f);
		return "detalleFactura";
	}

}
