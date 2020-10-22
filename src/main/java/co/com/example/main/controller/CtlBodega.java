package co.com.example.main.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	public String registroBodegaPag(Model model, @PathVariable("idVendedor") int idVendedor,
			@PathVariable("page") int page) {
		Usuario user = repoUsuario.findById(idVendedor);
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", this.repoBodega.findAll(PageRequest.of(page, 3)));
		model.addAttribute("usuario", user);
		model.addAttribute("producto", new Producto());
		return "registroBodega";
	}

	@GetMapping("/registroBodega/{idVendedor}")
	public String registroBodega(Model model, @PathVariable int idVendedor) {
		Usuario user = this.repoUsuario.findById(idVendedor);
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", this.repoBodega.findAll(PageRequest.of(0, 3)));
		model.addAttribute("usuario", user);
		model.addAttribute("producto", new Producto());
		return "registroBodega";
	}

	@PostMapping("/guardarBodega/{idVendedor}")
	public String guardarBodega(@Valid Bodega b, BindingResult result, Model model, @PathVariable int idVendedor) {
		b.setUsuario(repoUsuario.findById(idVendedor));
		b.setEspacioDisponible(b.getCapacidad());
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("idVendedor", idVendedor);
		model.addAttribute("listaBodegas", this.repoBodega.findAll(PageRequest.of(0, 3)));
		model.addAttribute("producto", new Producto());

		if (result.hasErrors()) {
			model.addAttribute("usuario", repoUsuario.findById(idVendedor));
			
			model.addAttribute("error", "El valor debe ser mayor que 1");
			return "registroBodega";
		} else {
			if (buscarBodegaPorNombre(b.getNombre()) != null) {
				model.addAttribute("error", "Ya existe una bodega con el mismo nombre");
				model.addAttribute("usuario", repoUsuario.findById(idVendedor));
				return "registroBodega";
			} else {
				repoBodega.save(b);
				return "redirect:/registroBodega/" + idVendedor;
			}
		}
	}

	private Bodega buscarBodegaPorNombre(String nombre) {
		List<Bodega> listaBodegas = (List<Bodega>) repoBodega.findAll();
		for (int i = 0; i < listaBodegas.size(); i++) {
			if (listaBodegas.get(i).getNombre().equals(nombre)) {
				return listaBodegas.get(i);
			}
		}
		return null;
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
		return "redirect:/registroBodega/" + idVendedor;
	}

	@GetMapping("/eliminarBodega/{id}")
	public String eliminarBodega(Model model, @PathVariable int id) {
		int idVendedor = repoBodega.findById(id).getUsuario().getId();
		model.addAttribute("idVendedor", idVendedor);
		repoBodega.deleteById(id);
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("listaBodegas", repoBodega.findAll());
		model.addAttribute("producto", new Producto());
		return "redirect:/registroBodega/" + idVendedor;
	}
}
