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
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data

public class Proveedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique = true)
	@Size(min = 3, max = 30, message = "Ingrese entre 3 y 30 caracteres")
	private String nombre;

	@Size(min = 3, max = 40, message = "Ingrese entre 3 y 40 caracteres")
	private String descripcion;

	@OneToMany(mappedBy = "proveedor", cascade = CascadeType.REMOVE)
	private List<Producto> producto;

	@ManyToOne
	private Usuario usuario;

}
