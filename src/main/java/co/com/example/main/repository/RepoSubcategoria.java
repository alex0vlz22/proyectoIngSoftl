package co.com.example.main.repository;

import org.springframework.data.repository.CrudRepository;

import co.com.example.main.domain.Subcategoria;

public interface RepoSubcategoria extends CrudRepository<Subcategoria, Integer>{

	Subcategoria findById(int id);
	
}
