package co.com.example.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import co.com.example.main.domain.Carrito;
import co.com.example.main.domain.DetalleFactura;
import co.com.example.main.domain.Facturaa;
import co.com.example.main.domain.Pedido;
import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Proveedor;
import co.com.example.main.domain.Subcategoria;
import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoBodega;
import co.com.example.main.repository.RepoCarrito;
import co.com.example.main.repository.RepoDetalleFactura;
import co.com.example.main.repository.RepoFacturaa;
import co.com.example.main.repository.RepoPedido;
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

	@Autowired
	private RepoCarrito repoCarrito;

	@Autowired
	private RepoPedido repoPedido;

	@Autowired
	private RepoFacturaa repoFactura;

	@Autowired
	private RepoDetalleFactura repoDetalleFactura;

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

	@GetMapping("/registroProducto/{idVendedor}")
	public String registroProducto(Model model, @PathVariable int idVendedor,
			@RequestParam(defaultValue = "0") int page) {
		Usuario user = this.repoUsuario.findById(idVendedor);
		model.addAttribute("bodegaSinEspacio", false);
		model.addAttribute("producto", new Producto());
		model.addAttribute("productoForm", new Producto());
		model.addAttribute("usuario", user);
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos",
				repoProducto.findByVendedor(PageRequest.of(page, 4), repoUsuario.findById(idVendedor)));
		model.addAttribute("listaBodegas", this.repoBodega.findByUsuario(user));
		model.addAttribute("idVendedor", idVendedor);
		return "registroProducto";
	}

	@GetMapping("/registroProducto/{idVendedor}/pag/{page}")
	public String pagRegistroProducto(Model model, @PathVariable int idVendedor, @PathVariable("page") int page) {
		Usuario user = this.repoUsuario.findById(idVendedor);
		model.addAttribute("bodegaSinEspacio", false);
		model.addAttribute("producto", new Producto());
		model.addAttribute("productoForm", new Producto());
		model.addAttribute("usuario", user);
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos",
				repoProducto.findByVendedor(PageRequest.of(page, 4), repoUsuario.findById(idVendedor)));
		model.addAttribute("listaBodegas", this.repoBodega.findByUsuario(user));
		model.addAttribute("idVendedor", idVendedor);
		return "registroProducto";
	}

	@PostMapping("/guardarProducto/{idVendedor}")
	public String guardarProducto(Model model, Producto producto, @RequestParam("file") MultipartFile file,
			@PathVariable int idVendedor) {
		Proveedor p = repoProveedor.findById(producto.getIdProveedor());
		Subcategoria c = repoSubcategoria.findById(producto.getIdSubcategoria());
		Bodega b = repoBodega.findById(producto.getIdBodega());
		Usuario u = repoUsuario.findById(idVendedor);

		if (producto.getCantidad() <= b.getEspacioDisponible() && producto.getCantidad() <= b.getCapacidad()) {
			try {
				Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
				System.out.println(uploadResult.get("url").toString());
				producto.setUrlFoto(uploadResult.get("url").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			b.setEspacioDisponible(b.getEspacioDisponible() - producto.getCantidad());
			this.repoBodega.save(b);
			producto.setProveedor(p);
			producto.setSubcategoria(c);
			producto.setBodega(b);
			producto.setVendedor(u);
			repoProducto.save(producto);
			model.addAttribute("bodegaSinEspacio", false);
			model.addAttribute("productoForm", new Producto());
			model.addAttribute("listaProveedores", repoProveedor.findAll());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaProductos",
					repoProducto.findByVendedor(PageRequest.of(0, 4), repoUsuario.findById(idVendedor)));
			model.addAttribute("listaBodegas", this.repoBodega.findByUsuario(this.repoUsuario.findById(idVendedor)));
			model.addAttribute("producto", new Producto());

			model.addAttribute("usuario", this.repoUsuario.findById(idVendedor));
			return "registroProducto";
		} else {
			model.addAttribute("bodegaSinEspacio", true);
			model.addAttribute("productoForm", producto);
			model.addAttribute("listaProveedores", repoProveedor.findAll());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaProductos",
					repoProducto.findByVendedor(PageRequest.of(0, 4), repoUsuario.findById(idVendedor)));
			model.addAttribute("listaBodegas", this.repoBodega.findByUsuario(this.repoUsuario.findById(idVendedor)));
			model.addAttribute("producto", new Producto());

			model.addAttribute("usuario", this.repoUsuario.findById(idVendedor));

			return "registroProducto";
		}
	}

	@GetMapping("/editarProducto/{id}")
	public String editarProducto(Model model, @PathVariable int id) {
		int idVendedor = repoProducto.findById(id).getVendedor().getId();
		try {
			Producto p = repoProducto.findById(id);
			model.addAttribute("bodegaSinEspacio", false);
			model.addAttribute("productoParaEditar", p);
			model.addAttribute("listaProveedores", repoProveedor.findAll());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaBodegas", this.repoBodega.findByUsuario(this.repoUsuario.findById(idVendedor)));
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

		Producto productoViejo = this.repoProducto.findById(id);
		b.setEspacioDisponible(b.getEspacioDisponible() + productoViejo.getCantidad());

		if (p.getCantidad() <= b.getEspacioDisponible() && p.getCantidad() <= b.getCapacidad()) {

			if (cambioUrl) {
				try {
					Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
					System.out.println(uploadResult.get("url").toString());
					p.setUrlFoto(uploadResult.get("url").toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			b.setEspacioDisponible(b.getEspacioDisponible() - p.getCantidad());
			this.repoBodega.save(b);

			p.setId(id);
			p.setProveedor(pro);
			p.setSubcategoria(c);
			p.setBodega(b);
			p.setVendedor(repoUsuario.findById(idVendedor));
			repoProducto.save(p);
			model.addAttribute("bodegaSinEspacio", false);
			model.addAttribute("productoForm", new Producto());
			model.addAttribute("listaProveedores", repoProveedor.findAll());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaProductos",
					repoProducto.findByVendedor(PageRequest.of(0, 4), repoUsuario.findById(idVendedor)));
			model.addAttribute("listaBodegas", repoBodega.findAll());
			model.addAttribute("producto", new Producto());
			model.addAttribute("usuario", this.repoUsuario.findById(idVendedor));
			return "redirect:/registroProducto/" + idVendedor;
		} else {
			p.setId(id);
			p.setProveedor(pro);
			p.setSubcategoria(c);
			p.setBodega(b);
			p.setVendedor(repoUsuario.findById(idVendedor));
			model.addAttribute("bodegaSinEspacio", true);
			model.addAttribute("productoParaEditar", p);
			model.addAttribute("listaProveedores", repoProveedor.findAll());
			model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
			model.addAttribute("listaProductos", repoProducto.findAll());
			model.addAttribute("listaBodegas", this.repoBodega.findByUsuario(this.repoUsuario.findById(idVendedor)));
			model.addAttribute("producto", new Producto());
			model.addAttribute("usuario", this.repoUsuario.findById(idVendedor));
			return "editarProducto";
		}
	}

	@GetMapping("/eliminarProducto/{id}")
	public String eliminarProducto(Model model, @PathVariable int id) {
		int idVendedor = repoProducto.findById(id).getVendedor().getId();
		repoProducto.deleteById(id);
		model.addAttribute("bodegaSinEspacio", false);
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaProveedores", repoProveedor.findAll());
		model.addAttribute("listaSubcategorias", repoSubcategoria.findAll());
		model.addAttribute("listaProductos",
				repoProducto.findByVendedor(PageRequest.of(0, 4), repoUsuario.findById(idVendedor)));
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

	@GetMapping("/detalleProducto/{idUsuario}/{idProducto}")
	public String detalleProducto(Model model, @PathVariable("idUsuario") int idUsuario,
			@PathVariable("idProducto") int idProducto) {
		Usuario usuario = repoUsuario.findById(idUsuario);
		Producto producto = repoProducto.findById(idProducto);
		model.addAttribute("agregado", false);
		model.addAttribute("agotado", false);
		model.addAttribute("usuario", usuario);
		model.addAttribute("productoVisualizado", producto);
		model.addAttribute("proveedor", producto.getProveedor());
		model.addAttribute("subcategoria", producto.getSubcategoria());
		model.addAttribute("producto", new Producto());
		return "detalleProducto";
	}

	@GetMapping("/producto/{idProducto}/carrito/{idUsuario}")
	public String agregarCarrito(Model model, @PathVariable("idProducto") int idProducto,
			@PathVariable("idUsuario") int idUsuario) {
		Usuario usuario = repoUsuario.findById(idUsuario);
		Producto producto = repoProducto.findById(idProducto);
		if (producto.getCantidad() == 0) {
			model.addAttribute("agregado", false);
			model.addAttribute("agotado", true);
			model.addAttribute("usuario", usuario);
			model.addAttribute("productoVisualizado", producto);
			model.addAttribute("proveedor", producto.getProveedor());
			model.addAttribute("subcategoria", producto.getSubcategoria());
			model.addAttribute("producto", new Producto());
			return "detalleProducto";
		} else {
			Carrito productoCarrito = new Carrito();
			productoCarrito.setProducto(producto);
			productoCarrito.setUsuario(usuario);
			this.repoCarrito.save(productoCarrito);
			model.addAttribute("agregado", true);
			model.addAttribute("agotado", false);
			model.addAttribute("usuario", usuario);
			model.addAttribute("productoVisualizado", producto);
			model.addAttribute("proveedor", producto.getProveedor());
			model.addAttribute("subcategoria", producto.getSubcategoria());
			model.addAttribute("producto", new Producto());
			return "detalleProducto";
		}
	}

	@GetMapping("/carrito/{idUsuario}")
	public String miCarrito(Model model, @PathVariable("idUsuario") int idUsuario) {
		Usuario user = this.repoUsuario.findById(idUsuario);
		double valorTotal = 0;
		valorTotal = calcularPrecio(idUsuario, valorTotal, user);
		// validaciones
		model.addAttribute("productoNoDisponible", false);
		model.addAttribute("productoAgotado", "");
		model.addAttribute("noHayProductos", false);
		model.addAttribute("compraRealizada", false);
		//
		model.addAttribute("valorTotal", valorTotal);
		model.addAttribute("listaProductos", this.repoCarrito.findByUsuario(PageRequest.of(0, 12), user));
		model.addAttribute("listaProductosNoPage", this.repoCarrito.findByUsuario(user));
		model.addAttribute("usuario", user);
		model.addAttribute("producto", new Producto());
		return "carrito";
	}

	private double calcularPrecio(int idUsuario, double valorTotal, Usuario user) {
		List<Carrito> lista = this.repoCarrito.findByUsuario(user);
		if (lista.isEmpty()) {
			return 0;
		} else {
			for (Carrito carrito : lista) {
				valorTotal += carrito.getProducto().getPrecio() * carrito.getCantidad();
			}
		}
		return valorTotal;
	}

	@GetMapping("/eliminar/prod/{idCarrito}/carrito/{idUsuario}")
	public String eliminarDelCarrito(Model model, @PathVariable("idCarrito") int idCarrito,
			@PathVariable("idUsuario") int idUsuario) {
		this.repoCarrito.deleteById(idCarrito);
		return "redirect:/carrito/" + idUsuario;
	}

	@GetMapping("/realizarCompra/{idUsuario}/productos")
	public String comprar(Model model, @PathVariable("idUsuario") int idComprador) {

		Usuario user = this.repoUsuario.findById(idComprador);
		List<Carrito> listaProductos = this.repoCarrito.findByUsuario(user);

		if (listaProductos.isEmpty()) {
			// validaciones
			model.addAttribute("productoNoDisponible", false);
			model.addAttribute("productoAgotado", "");
			model.addAttribute("noHayProductos", true);
			model.addAttribute("compraRealizada", false);
			//
			double valorTotal = 0;
			valorTotal = calcularPrecio(idComprador, valorTotal, user);
			model.addAttribute("valorTotal", valorTotal);
			model.addAttribute("listaProductos", this.repoCarrito.findByUsuario(PageRequest.of(0, 12), user));
			model.addAttribute("listaProductosNoPage", this.repoCarrito.findByUsuario(user));
			model.addAttribute("usuario", user);
			model.addAttribute("producto", new Producto());
		} else {
			String productoValidado = validarAlgunProductoAgotado(listaProductos);
			if (!productoValidado.equals("")) {
				// validaciones
				model.addAttribute("productoNoDisponible", true);
				model.addAttribute("productoAgotado", productoValidado);
				model.addAttribute("noHayProductos", false);
				model.addAttribute("compraRealizada", false);
				//
				double valorTotal = 0;
				valorTotal = calcularPrecio(idComprador, valorTotal, user);
				model.addAttribute("valorTotal", valorTotal);
				model.addAttribute("listaProductos", this.repoCarrito.findByUsuario(PageRequest.of(0, 12), user));
				model.addAttribute("listaProductosNoPage", this.repoCarrito.findByUsuario(user));
				model.addAttribute("usuario", user);
				model.addAttribute("producto", new Producto());
			} else {
				Pedido pedido = new Pedido();
				pedido = construirPedido(listaProductos, idComprador, pedido);
				Facturaa factura = new Facturaa();
				factura = construirFactura(listaProductos, idComprador, pedido, factura);
				construirDetalle(listaProductos, idComprador, pedido, factura);
				// validaciones
				model.addAttribute("productoNoDisponible", false);
				model.addAttribute("productoAgotado", "");
				model.addAttribute("noHayProductos", false);
				model.addAttribute("compraRealizada", true);
				//
				double valorTotal = 0;
				valorTotal = calcularPrecio(idComprador, valorTotal, user);
				model.addAttribute("valorTotal", valorTotal);
				model.addAttribute("listaProductos", this.repoCarrito.findByUsuario(PageRequest.of(0, 12), user));
				model.addAttribute("listaProductosNoPage", this.repoCarrito.findByUsuario(user));
				model.addAttribute("usuario", user);
				model.addAttribute("producto", new Producto());
			}
		}
		return "carrito";
	}

	private String validarAlgunProductoAgotado(List<Carrito> listaProductos) {
		for (Carrito c : listaProductos) {
			if (c.getProducto().getCantidad() == 0) {
				return c.getProducto().getNombre();
			}
		}
		return "";
	}

	private Pedido construirPedido(List<Carrito> listaProductos, int idComprador, Pedido pedido) {
		int cantidadArticulos = 0;
		double valorTotal = 0;
		for (Carrito c : listaProductos) {
			cantidadArticulos += c.getCantidad();
			valorTotal += c.getProducto().getPrecio() * c.getCantidad();

			// modifico la cantidad del producto comprado en la base de datos, es decir, le
			// resto
			// la cantidad que se haya comprado
			Producto p = this.repoProducto.findById(c.getProducto().getId());
			p.setCantidad(p.getCantidad() - c.getCantidad());
			this.repoProducto.save(p);

			// modifico la cantidad de la bodega, es decir, había un espacio ocupado por el
			// producto
			// en la bodega, pero ahora, ese espacio queda disponible.
			Bodega b = this.repoBodega.findById(c.getProducto().getBodega().getId());
			b.setEspacioDisponible(b.getEspacioDisponible() + c.getCantidad());
			this.repoBodega.save(b);
		}
		pedido.setCantidadArticulos(cantidadArticulos);
		pedido.setUsuario(this.repoUsuario.findById(idComprador));
		pedido.setValorTotal(valorTotal);
		this.repoPedido.save(pedido);
		return pedido;
	}

	private Facturaa construirFactura(List<Carrito> listaProductos, int idComprador, Pedido pedido, Facturaa factura) {
		double valorTotal = 0;
		for (Carrito c : listaProductos) {
			valorTotal += c.getProducto().getPrecio() * c.getCantidad();
		}
		Date fecha = new Date(System.currentTimeMillis());
		factura.setPedido(pedido);
		factura.setComprador(this.repoUsuario.findById(idComprador));
		factura.setFecha(fecha);
		factura.setValorTotal(valorTotal);
		this.repoFactura.save(factura);
		return factura;
	}

	private void construirDetalle(List<Carrito> listaProductos, int idComprador, Pedido pedido, Facturaa factura) {
		DetalleFactura detalleFactura;
		for (Carrito c : listaProductos) {
			detalleFactura = new DetalleFactura();
			detalleFactura.setProducto(c.getProducto());
			detalleFactura.setFactura(factura);
			detalleFactura.setVendedor(c.getProducto().getVendedor());
			detalleFactura.setCantidad(c.getCantidad());
			Date fecha = new Date(System.currentTimeMillis());
			detalleFactura.setFecha(fecha);
			detalleFactura.setValor(c.getProducto().getPrecio() * c.getCantidad());
			this.repoDetalleFactura.save(detalleFactura);
			this.repoCarrito.delete(c);
		}
	}

}
