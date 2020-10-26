package co.com.example.main.domain;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.Data;

@Entity
@Data
public class Bodega {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	@Size(min = 3, max = 30, message = "Ingrese entre 3 y 30 caracteres. ")
	private String nombre;

	@Size(min = 3, max = 35, message = "Ingrese entre 3 y 35 caracteres. ")
	private String direccion;
	@Min(1)
	private int capacidad;

	private int espacioDisponible;

	@OneToMany(mappedBy = "bodega", cascade = CascadeType.REMOVE)
	private List<Producto> producto;

	@ManyToOne
	private Usuario usuario;

}
