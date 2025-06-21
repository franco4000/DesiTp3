package ong.desi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ong.desi.entity.Familia;  

@Repository
public interface FamiliaRepository extends JpaRepository<Familia, Long> {
	
	List<Familia> findByNombre(String nombre);
	 @Query("SELECT f FROM Familia f WHERE f.activa = true")
	    List<Familia> listarActivas(); 

	// Buscar por nombre 
	 List<Familia> findByNombreIgnoreCase(String nombre);

	 List<Familia> findByActivaTrue();

	 @Query("SELECT f FROM Familia f WHERE LOWER(f.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND f.activa = true")
	 List<Familia> buscarPorNombreParcial(@Param("nombre") String nombre);

} 
