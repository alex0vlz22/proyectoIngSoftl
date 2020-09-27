package co.com.example.main.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.utils.ObjectUtils;
import co.com.example.main.CloudinaryConfig;
import co.com.example.main.domain.Bodega;
import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Proveedor;
import co.com.example.main.domain.Subcategoria;
import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoBodega;
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

	@Autowired
	private RepoBodega repoBodega;

	@GetMapping("/pag/{idUsuario}/{page}")
	public String pag(Model model, @PathVariable("idUsuario") int idUsuario, @PathVariable("page") int page) {
		Usuario u = repoUsuario.findById(idUsuario);
		model.addAttribute("usuario", u);
		model.addAttribute("listaSubcategorias", this.repoSubcategoria.findAll());
		model.addAttribute("listaProveedores", this.repoProveedor.findAll());
		model.addAttribute("listaProductos", this.repoProducto.findAll(PageRequest.of(page, 6)));
		model.addAttribute("producto", new Producto());
		return "ingresoUsuario";
	}
	
	@GetMapping("/detalleProducto/{idUsuario}/{idProducto}")
	public String detalleProducto(Model model, @PathVariable("idUsuario") int idUsuario,
			@PathVariable("idProducto") int idProducto) {
		Usuario usuario = repoUsuario.findById(idUsuario);
		Producto producto = repoProducto.findById(idProducto);
		model.addAttribute("usuario", usuario);
		model.addAttribute("productoVisualizado", producto);
		model.addAttribute("proveedor", producto.getProveedor());
		model.addAttribute("subcategoria", producto.getSubcategoria());
		model.addAttribute("producto", new Producto());
		return "detalleProducto";
	}

	@GetMapping("/registroProducto/{idVendedor}")
	public String registroProducto(Model model, @PathVariable int idVendedor, @RequestParam(defaultValue = "0") int page) {
		model.addAttribute("producto", new Producto());
		model.addAttribute("usuario", repoUsuario.findById(idVendedor));
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos", repoProducto.findByVendedor(PageRequest.of(page, 4), repoUsuario.findById(idVendedor)));
		model.addAttribute("listaBodegas", repoBodega.findAll());
		model.addAttribute("idVendedor", idVendedor);
		return "registroProducto";
	}
	
	@GetMapping("/registroProducto/{idVendedor}/pag/{page}")
	public String pagRegistroProducto(Model model, @PathVariable int idVendedor, @PathVariable("page") int page) {
		model.addAttribute("producto", new Producto());
		model.addAttribute("usuario", repoUsuario.findById(idVendedor));
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos", repoProducto.findByVendedor(PageRequest.of(page, 4), repoUsuario.findById(idVendedor)));
		model.addAttribute("listaBodegas", repoBodega.findAll());
		model.addAttribute("idVendedor", idVendedor);
		return "registroProducto";
	}

	@PostMapping("/guardarProducto/{idVendedor}")
	public String guardarProducto(Model model, Producto producto, @RequestParam("file") MultipartFile file,
			@PathVariable int idVendedor){
		Proveedor p = repoProveedor.findById(producto.getIdProveedor());
		Subcategoria c = repoSubcategoria.findById(producto.getIdSubcategoria());
		Bodega b = repoBodega.findById(producto.getIdBodega());
		Usuario u = repoUsuario.findById(idVendedor);
		producto.setProveedor(p);
		producto.setSubcategoria(c);
		producto.setBodega(b);
		producto.setVendedor(u);
		try {
			Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
			System.out.println(uploadResult.get("url").toString());
			producto.setUrlFoto(uploadResult.get("url").toString());
		} catch (Exception e) {
			// LA FOTO EXCEDE EL TAMANO MAXIMO
			e.printStackTrace();
			System.out.println("TAMANOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO MAXIMOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		}
		repoProducto.save(producto);
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos", repoProducto.findAll());
		model.addAttribute("listaBodegas", repoBodega.findAll());
		model.addAttribute("producto", new Producto());
		return "redirect:/registroProducto/" + idVendedor;
	}

	@GetMapping("/editarProducto/{id}")
	public String editarProducto(Model model, @PathVariable int id) {
		int idVendedor = repoProducto.findById(id).getVendedor().getId();
		try {
			Producto p = repoProducto.findById(id);
			model.addAttribute("productoParaEditar", p);
			model.addAttribute("listaProveedores", repoProveedor.findAll());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaBodegas", repoBodega.findAll());
			model.addAttribute("usuario", p.getVendedor());
			model.addAttribute("idVendedor", p.getVendedor().getId());
			model.addAttribute("producto", new Producto());
			return "editarProducto";
		} catch (MaxUploadSizeExceededException e) {
			// el archivo es demasiado grande equisde
		}
		return "redirect:/registroProducto/" + idVendedor;
	}

	@PostMapping("/modificarProducto/{id}")
	public String modificarProducto(Model model, Producto p, @PathVariable int id,
			@RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl) {
		int idVendedor = repoProducto.findById(id).getVendedor().getId();
		Proveedor pro = repoProveedor.findById(p.getIdProveedor());
		Subcategoria c = repoSubcategoria.findById(p.getIdSubcategoria());
		Bodega b = repoBodega.findById(p.getIdBodega());
		p.setId(id);
		p.setProveedor(pro);
		p.setSubcategoria(c);
		p.setBodega(b);
		p.setVendedor(repoUsuario.findById(idVendedor));
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
		model.addAttribute("listaBodegas", repoBodega.findAll());
		model.addAttribute("producto", new Producto());
		return "redirect:/registroProducto/" + idVendedor;
	}

	@GetMapping("/eliminarProducto/{id}")
	public String eliminarProducto(Model model, @PathVariable int id) {
		int idVendedor = repoProducto.findById(id).getVendedor().getId();
		repoProducto.deleteById(id);
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos", repoProducto.findAll());
		model.addAttribute("listaBodegas", repoBodega.findAll());
		model.addAttribute("producto", new Producto());
		return "redirect:/registroProducto/" + idVendedor;
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

	// Consultas

	@GetMapping("/busquedaPorPalabras/{idUsuario}")
	public String buscarPorPalabras(Model model, @PathVariable int idUsuario, Producto producto) {
		String palabras = producto.getDescripcion();
		List<Producto> listaProductos = (List<Producto>) repoProducto.findAllByNombreContainingIgnoreCase(palabras);
		
		if (listaProductos.size() == 0) {
			listaProductos = (List<Producto>) repoProducto.findAllByDescripcionContainingIgnoreCase(palabras);
			
		}
		model.addAttribute("usuario", repoUsuario.findById(idUsuario));
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaSubcategorias", this.repoSubcategoria.findAll());
		model.addAttribute("listaProveedores", this.repoProveedor.findAll());
		model.addAttribute("listaProductos", new PageImpl<>(listaProductos));
		return "ingresoUsuario";
	}

	@GetMapping("/busquedaPorFiltros/{idUsuario}")
	public String buscarPorFiltros(Model model, @PathVariable int idUsuario, Producto producto) {
		// ¡¡¡¡¡¡¡¡ IMPORTANTE !!!!!!!!!
		// Se debe hacer que en los campos de precios por defecto haya un valor, así se
		// borre todo el texto
		// de lo contrario se enviaría un null en un datos double, generaría una
		// excepción importante.
		List<Producto> listaProductos = new ArrayList<Producto>();
		if (producto.getIdProveedor() != 0) {
			if (producto.getIdSubcategoria() != 0) {
				// En caso de que se seleccione una subcategoría y un proveedor.
				Subcategoria s = this.repoSubcategoria.findById(producto.getIdSubcategoria());
				Proveedor p = this.repoProveedor.findById(producto.getIdProveedor());
				listaProductos = (List<Producto>) this.repoProducto.findAllBySubcategoriaOrProveedor(s, p);
				if (verificarSiIngresaronPrecios(producto)) {
					listaProductos = filtrarPorPrecios(producto, listaProductos);
					model = llenarModelFiltros(model, idUsuario, producto, listaProductos);
					return "ingresoUsuario";
				} else {
					// no se ingresaron precios
					model = llenarModelFiltros(model, idUsuario, producto, listaProductos);
					return "ingresoUsuario";
				}
			} else {
				// En caso de que NO se seleccione subcategoría pero SÍ un proveedor.
				Proveedor p = this.repoProveedor.findById(producto.getIdProveedor());
				listaProductos = (List<Producto>) this.repoProducto.findAllByProveedor(p);
				if (verificarSiIngresaronPrecios(producto)) {
					listaProductos = filtrarPorPrecios(producto, listaProductos);
					model = llenarModelFiltros(model, idUsuario, producto, listaProductos);
					return "ingresoUsuario";
				} else {
					// NO se ingresaron precios
					model = llenarModelFiltros(model, idUsuario, producto, listaProductos);
					return "ingresoUsuario";
				}
			}
		} else {
			if (producto.getIdSubcategoria() != 0) {
				// En caso de que NO se seleccione un proveedor pero SÍ una subcategoría.
				Subcategoria s = this.repoSubcategoria.findById(producto.getIdSubcategoria());
				listaProductos = (List<Producto>) this.repoProducto.findAllBySubcategoria(s);
				if (verificarSiIngresaronPrecios(producto)) {
					listaProductos = filtrarPorPrecios(producto, listaProductos);
					model = llenarModelFiltros(model, idUsuario, producto, listaProductos);
					return "ingresoUsuario";
				} else {
					// NO se ingresaron precios
					model = llenarModelFiltros(model, idUsuario, producto, listaProductos);
					return "ingresoUsuario";
				}
			} else {
				// En caso de que NO se seleccione un proveedor ni una subcategoría.
				if (verificarSiIngresaronPrecios(producto)) {
					listaProductos = (List<Producto>) this.repoProducto.findAll();
					listaProductos = filtrarPorPrecios(producto, listaProductos);
					model = llenarModelFiltros(model, idUsuario, producto, listaProductos);
					return "ingresoUsuario";
				} else {
					// NO se ingreso nada
					// aqui se debe notificar que no se ingresó ni un filtro.
					model = llenarModelFiltros(model, idUsuario, producto, listaProductos);
					return "ingresoUsuario";
				}
			}
		}
	}

	private List<Producto> filtrarPorPrecios(Producto producto, List<Producto> listaProductos) {
		if (producto.getPrecioMinimo() != 0 && producto.getPrecioMaximo() != 0) {
			for (int i = 0; i < listaProductos.size(); i++) {
				if (listaProductos.get(i).getPrecio() < producto.getPrecioMinimo()) {
					listaProductos.remove(i);
				} else if (listaProductos.get(i).getPrecio() > producto.getPrecioMaximo()) {
					listaProductos.remove(i);
				}
			}
		} else if (producto.getPrecioMinimo() != 0) {
			for (int i = 0; i < listaProductos.size(); i++) {
				if (listaProductos.get(i).getPrecio() < producto.getPrecioMinimo()) {
					listaProductos.remove(i);
				}
			}
		} else if (producto.getPrecioMaximo() != 0) {
			for (int i = 0; i < listaProductos.size(); i++) {
				if (listaProductos.get(i).getPrecio() > producto.getPrecioMaximo()) {
					listaProductos.remove(i);
				}
			}
		}
		return listaProductos;
	}

	private Model llenarModelFiltros(Model model, int idUsuario, Producto p, List<Producto> listaProductos) {
		model.addAttribute("usuario", repoUsuario.findById(idUsuario));
		model.addAttribute("producto", p);
		model.addAttribute("listaSubcategorias", this.repoSubcategoria.findAll());
		model.addAttribute("listaProveedores", this.repoProveedor.findAll());
		model.addAttribute("listaProductos", listaProductos);
		return model;
	}

	private boolean verificarSiIngresaronPrecios(Producto p) {
		if (p.getPrecioMinimo() != 0 || p.getPrecioMaximo() != 0) {
			return true;
		}
		return false;
	}

}
