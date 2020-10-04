package co.com.example.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.example.main.domain.Pedido;

public interface RepoPedido extends JpaRepository<Pedido, Integer>{

}
