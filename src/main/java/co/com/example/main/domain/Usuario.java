package co.com.example.main.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(unique=true)
	private String DNI;
	
	private String nombre;
	
	private String apellido;
	
	private String telefono;
	
	private String direccion;
	
	private String correo;
	
	private String rol;
	
	private String urlFoto;
	
	// codigoEmpresa es un campo disponible en el registro usuario
	// solo si quien se quiere registrar es un vendedor, el codigoEmpresa
	// sería un código cualquiera inventado por nosotros, a la hora de crear
	// el usuario, validaríamos que el código ingresado sea correcto.
	@Transient
	private String codigoEmpresa;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	private List<Bodega> bodega;
	
	@OneToMany(mappedBy = "vendedor", cascade = CascadeType.REMOVE)
	private List<Producto> producto;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	private List<Categoria> categoria;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	private List<Proveedor> proveedor;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	private List<Subcategoria> subcategoria;
	
}
