package co.com.example.main.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Pedido {

	@Id
	@GeneratedValue
	private int id;
	
	private int cantidadArticulos;
	
	@ManyToOne
	private Usuario usuario;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.REMOVE)
	private List<Facturaa> factura;
	
	private double valorTotal;
	
}
