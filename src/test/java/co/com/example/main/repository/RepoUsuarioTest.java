package co.com.example.main.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
	@Test
	public void testListarVacio() {
		Iterable<Usuario> lista = repoUsuario.findAll();
		assertThat(lista).isEmpty();
	}
	
	@Test
	public void testGuardar() {
		Usuario user = new Usuario();
		user.setDNI("1");
		user.setNombre("j");
		user.setApellido("j");
		user.setTelefono("5754567");
		user.setCorreo("j@gmail.com");
		user.setRol("Cliente");
		user.setUrlFoto("www");
		user.setDireccion("dffd");
		Usuario usuario = entityManager.persist(user);
		assertNotNull(usuario);
	}
	
	
}
