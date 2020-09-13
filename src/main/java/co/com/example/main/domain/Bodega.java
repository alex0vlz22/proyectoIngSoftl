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

import lombok.Data;

@Entity
@Data
public class Bodega {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(unique = true)
	private String nombre;
	
	private String direccion;
	
	private int capacidad;
	
	@OneToMany(mappedBy = "bodega", cascade = CascadeType.REMOVE)
	private List<Producto> producto;
	
	@ManyToOne
	private Usuario usuario;
	
}
