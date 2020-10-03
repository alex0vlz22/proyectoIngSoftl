package co.com.example.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import co.com.example.main.domain.Categoria;
import co.com.example.main.domain.Usuario;

public interface RepoCategoria extends JpaRepository<Categoria, Integer> {

	Categoria findById(int id);
	
	Categoria findByNombre(String nombre);
	
	Page<Categoria> findByUsuario(Pageable page, Usuario user);
	
}
