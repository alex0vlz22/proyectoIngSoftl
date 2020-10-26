package co.com.example.main.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data

public class Producto {

	private static final long serialVersionUID = 2679541203984681110L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	
	@Size(min = 3, max = 50, message = "Ingrese entre 3 y 50 caracteres")
	private String nombre;
	
	@Size(min = 5, max = 50, message = "Ingrese entre 5 y 50 caracteres")
	private String descripcion;

	@Min(value = 1, message = "La cantidad debe ser de al menos 1")
	private int cantidad;

	@Min(value = 1, message = "El precio debe ser de al menos 1")
	private double precio;

	@Column(length = 255)
	private String urlFoto;

	@OneToMany(mappedBy = "producto", cascade = CascadeType.REMOVE)
	private List<Carrito> carrito;

	@OneToMany(mappedBy = "producto", cascade = CascadeType.REMOVE)
	private List<DetalleFactura> detalleFactura;

	@ManyToOne
	private Proveedor proveedor;

	@ManyToOne
	private Subcategoria subcategoria;

	@ManyToOne
	private Bodega bodega;

	@ManyToOne
	private Usuario vendedor;

	@Transient
	private int idSubcategoria;

	@Transient
	private int idProveedor;

	@Transient
	private int idBodega;

	@Transient
	private double precioMinimo;

	@Transient
	private double precioMaximo;

}
