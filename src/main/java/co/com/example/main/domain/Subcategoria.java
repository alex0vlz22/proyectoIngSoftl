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

import lombok.Data;

@Entity
@Data
public class Subcategoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(unique=true)
	private String nombre;
	
	@ManyToOne
	private Categoria categoria;
	
	@OneToMany(mappedBy = "subcategoria", cascade = CascadeType.REMOVE)
	private List<Producto> producto;
	
	@Transient
	private int idCategoria;
	
}
