package co.com.example.main.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import co.com.example.main.domain.Producto;
import co.com.example.main.domain.Usuario;

public interface RepoProducto extends CrudRepository<Producto, Integer>{

	Producto findById(int id);
	
	@Query("Select p from Producto p where p.nombre like '% ?1 %'")
	public Iterable<Producto> buscarProductoConPalabra(String titulo);
	
	public Iterable<Producto> findAllByNombreContainingIgnoreCase(String nombre);
	
	public Iterable<Producto> findAllByDescripcionContainingIgnoreCase(String descripcion);
	
	@Query("Select p from Producto p order by p.precio ASC")
	public Iterable<Producto> ordenarPorPrecioAsc();
	
	@Query("Select p from Producto p order by p.precio DESC")
	public Iterable<Producto> ordenarPorPrecioDesc();
	
	@Query("Select p from Producto p join Subcategoria s on s.id=p.subcategoria where s.nombre=' ?1 ' ")
	public Iterable<Producto> ordenarPorSubcategoria(String nombreCategoria);
	
	@Query("Select p from Producto p join Proveedor pr on pr.id=p.proveedor where pr.nombre=' ?1 ' ")
	public Iterable<Producto> ordenarPorProveedor(String nombreProveedor);
	
	List<Producto> findByVendedor(Usuario usuario);
	
}
