package co.com.example.main.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import co.com.example.main.domain.Bodega;
import co.com.example.main.domain.Usuario;

public interface RepoBodega extends CrudRepository<Bodega, Integer>{

	Bodega findById(int id);
	
	List<Bodega> findByUsuario(Usuario user);
	
	Page<Bodega> findByUsuario(Pageable page, Usuario user);
}
