package co.com.example.main.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.com.example.main.domain.Pedido;
import co.com.example.main.domain.Usuario;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RepoPedidoTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	RepoPedido repoPedido;
	
	private Pedido getPedido() {
		Pedido pedido = new Pedido();
		pedido.setCantidadArticulos(2);
		pedido.setValorTotal(3445);
		return pedido;
	}
	
	
	
	@Test
	public void testListarVacio() {
		Iterable<Pedido> lista = repoPedido.findAll();
		assertThat(lista).isEmpty();
	}
	
	@Test
	public void testGuardar() {
		Pedido pedido = entityManager.persist(getPedido());
		assertNotNull(pedido);
	}
	
	@Test
	public void testBuscarId() {
		Pedido pedido = entityManager.persist(getPedido());
		Pedido b = repoPedido.findById(pedido.getId()).get();
		assertThat(pedido).isEqualTo(b);
	}
	
	@Test
	public void TestModificar() {
		Pedido pedido = entityManager.persist(getPedido());
		pedido.setCantidadArticulos(45);
		Pedido update = repoPedido.save(pedido);
		assertThat(update).isEqualTo(pedido);
	}
	
	@Test
	public void testEliminar() {
		Pedido pedido = entityManager.persist(getPedido());
		repoPedido.delete(pedido);
		Pedido b = repoPedido.findById(pedido.getId()).get();
		assertNull(b);
	}
	
	@Test
	public void testListarTodos() {
		entityManager.persist(getPedido());
		entityManager.persist(getPedido());
		
		Iterable<Pedido> lista = repoPedido.findAll();
		assertThat(lista).isNotEmpty();
	}

}