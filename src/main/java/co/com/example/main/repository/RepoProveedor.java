package co.com.example.main.repository;

import org.springframework.data.repository.CrudRepository;

import co.com.example.main.domain.Proveedor;

public interface RepoProveedor extends CrudRepository<Proveedor, Integer>{

	Proveedor findById(int id);
	
	Proveedor findByNombre(String nombre);
	
}
