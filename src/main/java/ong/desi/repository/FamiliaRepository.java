package ong.desi.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ong.desi.entity.Familia;  

public interface FamiliaRepository extends JpaRepository<Familia, Long> {
	
	 @Query("SELECT f FROM Familia f WHERE f.activa = true")
	    List<Familia> findAllActivas();

	// Buscar por nombre (exacto)
	 List<Familia> findByNombreIgnoreCase(String nombre);


	 @Query("SELECT f FROM Familia f WHERE LOWER(f.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND f.activa = true")
	 List<Familia> buscarPorNombreParcial(@Param("nombre") String nombre);

} 
