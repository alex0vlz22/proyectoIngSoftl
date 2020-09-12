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

	private int cantidad;
	
	private double precio;
	@Column(length = 255)
	private String urlFoto;

	@ManyToOne
	private Proveedor proveedor;
	
	@ManyToOne
	private Subcategoria subcategoria;
	
	@Transient
	private int idSubcategoria;
	
	@Transient
	private String nombreProveedor;

}
