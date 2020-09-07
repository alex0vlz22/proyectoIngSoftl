package co.com.example.main.repository;

import org.springframework.data.repository.CrudRepository;

import co.com.example.main.domain.Bodega;

public interface RepoBodega extends CrudRepository<Bodega, Integer>{

	Bodega findById(int id);
	
}
