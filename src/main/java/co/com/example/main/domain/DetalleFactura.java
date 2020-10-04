package co.com.example.main.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class DetalleFactura {

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	private Facturaa factura;
	
	@ManyToOne
	private Producto producto;
	
	@ManyToOne
	private Usuario vendedor;
	
	private int cantidad;
	
	private Date fecha;
	
	private double valor;
	
}
