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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Entity
@Data
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	private String DNI;

	@Size(min = 3, max = 40, message = "Ingrese entre 3 y 40 letras")
	private String nombre;
	@Size(min = 3, max = 40, message = "Ingrese entre 3 y 40 letras")
	private String apellido;

	@Size(min = 7, max = 12, message = "Ingrese entre 7 y 12 letras")
	private String telefono;

	@Size(min = 10, max = 40, message = "Ingrese entre 10 y 40 letras")
	private String direccion;

	@Email(message = "Ingrese un correo Válido")
	private String correo;

	private String rol;

	@NotBlank(message = "Debe Elegir una foto")
	private String urlFoto;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	private List<Pedido> pedido;

	@OneToMany(mappedBy = "comprador", cascade = CascadeType.REMOVE)
	private List<Facturaa> facturaComprador;

	@OneToMany(mappedBy = "vendedor", cascade = CascadeType.REMOVE)
	private List<DetalleFactura> detalleFacturaVendedor;

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

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	private List<Carrito> carrito;

}
