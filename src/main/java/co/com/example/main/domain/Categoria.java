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

import org.hibernate.annotations.Check;

import lombok.Data;

@Entity
@Data

public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	@Size(min = 3, max = 30, message = "Ingrese entre 3 y 30 letras")
	private String nombre;

	@OneToMany(mappedBy = "categoria", cascade = CascadeType.REMOVE)
	
	private List<Subcategoria> subcategoria;

	@ManyToOne
	private Usuario usuario;

}
