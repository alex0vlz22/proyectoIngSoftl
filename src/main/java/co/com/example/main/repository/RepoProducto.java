package co.com.example.main.repository;

import org.springframework.data.repository.CrudRepository;

import co.com.example.main.domain.Producto;

public interface RepoProducto extends CrudRepository<Producto, Integer>{

	Producto findById(int id);
	
}
