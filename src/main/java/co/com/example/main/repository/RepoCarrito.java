package co.com.example.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.example.main.domain.Carrito;

public interface RepoCarrito extends JpaRepository<Carrito, Integer>{

}
