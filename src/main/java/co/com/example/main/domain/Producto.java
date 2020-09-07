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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	private String nombre;

	private int cantidad;

	@ManyToOne
	private Proveedor proveedor;
	
	@ManyToOne
	private Subcategoria subcategoria;
	
	@Transient
	private int idSubcategoria;
	
	@Transient
	private String nombreProveedor;

}
