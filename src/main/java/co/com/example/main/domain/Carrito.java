package co.com.example.main.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Carrito {
	
	@Id
	@GeneratedValue
	private int id;
	
	private int cantidad = 1;
	
	@ManyToOne
	private Usuario usuario;
	
	@ManyToOne
	private Producto producto;
	
}
