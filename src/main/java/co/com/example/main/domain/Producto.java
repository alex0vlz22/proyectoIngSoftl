package co.com.example.main.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data

public class Producto {

	private static final long serialVersionUID = 2679541203984681110L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 50)
	private String nombre;

	private String descripcion;
	
	private int cantidad;
	
	private double precio;
	@Column(length = 255)
	private String urlFoto;

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
