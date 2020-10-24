package co.com.example.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import co.com.example.main.domain.Proveedor;
import co.com.example.main.domain.Usuario;

public interface RepoProveedor extends JpaRepository<Proveedor, Integer> {

	Proveedor findById(int id);

	Proveedor findByNombre(String nombre);

	Page<Proveedor> findAll(Pageable page);

}
