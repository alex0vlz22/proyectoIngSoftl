package co.com.example.main.repository;

import java.util.List;
import co.com.example.main.domain.Usuario;

import org.springframework.data.repository.CrudRepository;

import co.com.example.main.domain.Factura;

public interface RepoFactura extends CrudRepository<Factura, Integer> {

//	Factura findbyId(int id);
	
	List<Factura> findByCliente(Usuario usuario);
	
	
}
