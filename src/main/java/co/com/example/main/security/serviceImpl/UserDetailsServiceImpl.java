package co.com.example.main.security.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.com.example.main.domain.Usuario;
import co.com.example.main.repository.RepoUsuario;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    RepoUsuario usuarioRepo;
	
    @Override
     public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
    	UserDetails user=null;
    	System.out.println("inicio logiar");
    	List grantList = new ArrayList();
    	Usuario usuario = usuarioRepo.findByCorreo(correo);
    	if(usuario != null) {
		        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(usuario.getRol());
		        grantList.add(grantedAuthority);
			System.out.println("user");
			user = (UserDetails) new User(usuario.getCorreo(), usuario.getContrasena(), grantList);
		}
    	if (user != null) {
			return user;
		}else {
			System.out.println("error al logiar");
			throw new UsernameNotFoundException("No existe usuario");
		}
    }
}