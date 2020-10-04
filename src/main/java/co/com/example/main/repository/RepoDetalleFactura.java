package co.com.example.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.example.main.domain.DetalleFactura;

public interface RepoDetalleFactura extends JpaRepository<DetalleFactura, Integer>{

}
