package co.com.example.main.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Facturaa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private Usuario comprador;
	
	@ManyToOne
	private Pedido pedido;
	
	@OneToMany(mappedBy = "factura", cascade = CascadeType.REMOVE)
	private List<DetalleFactura> detalleFactura;
	
	private Date fecha;
	
	private double valorTotal;

}
