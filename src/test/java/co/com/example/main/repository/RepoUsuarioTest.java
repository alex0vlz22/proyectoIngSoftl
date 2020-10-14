package co.com.example.main.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoUsuario;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RepoUsuarioTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	RepoUsuario repoUsuario;
	
	private Usuario getUsuario(String dni) {
		Usuario user = new Usuario();
		user.setDNI(dni);
		user.setNombre("j"+dni);
		user.setApellido("j"+dni);
		user.setTelefono("5754567");
		user.setCorreo("j"+dni+"@gmail.com");
		user.setRol("Cliente");
		user.setUrlFoto("www");
		user.setDireccion("dffd");
		return user;
	}
	
	
	
	@Test
	public void testListarVacio() {
		Iterable<Usuario> lista = repoUsuario.findAll();
		assertThat(lista).isEmpty();
	}
	
	@Test
	public void testGuardar() {
		Usuario usuario = entityManager.persist(getUsuario("1"));
		assertNotNull(usuario);
	}
	
	@Test
	public void testBuscarId() {
		Usuario usuario = entityManager.persist(getUsuario("1"));
		Usuario b = repoUsuario.findById(usuario.getId());
		assertThat(usuario).isEqualTo(b);
	}
	
	@Test
	public void TestModificar() {
		Usuario usuario = entityManager.persist(getUsuario("1"));
		usuario.setNombre("t1");
		usuario.setApellido("t1");
		Usuario update = repoUsuario.save(usuario);
		assertThat(update).isEqualTo(usuario);
	}
	
	@Test
	public void testEliminar() {
		Usuario usuario = entityManager.persist(getUsuario("1"));
		repoUsuario.delete(usuario);
		Usuario b = repoUsuario.findById(usuario.getId());
		assertNull(b);
	}
	
	@Test
	public void testListarTodos() {
		entityManager.persist(getUsuario("1"));
		entityManager.persist(getUsuario("2"));
		entityManager.persist(getUsuario("3"));
		
		Iterable<Usuario> lista = repoUsuario.findAll();
		assertThat(lista).isNotEmpty();
	}
	
	@Test
	public void testBuscarDni() {
		Usuario usuario = entityManager.persist(getUsuario("1"));
		Usuario b = repoUsuario.findByDNI(usuario.getDNI());
		assertThat(usuario).isEqualTo(b);
	}
}
