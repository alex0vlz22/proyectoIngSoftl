package co.com.example.main.repository;

import org.springframework.data.repository.CrudRepository;

import co.com.example.main.domain.Categoria;

public interface RepoCategoria extends CrudRepository<Categoria, Integer> {

	Categoria findById(int id);
	
	Categoria findByNombre(String nombre);
	
}
