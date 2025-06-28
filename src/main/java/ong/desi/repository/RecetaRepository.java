package ong.desi.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ong.desi.entity.Receta;
@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    Optional<Receta> findByNombre(String nombre);
    List<Receta> findByActivaTrue();
    Optional<Receta> findByIdAndActivaTrue(Long id);

    
    @Query("SELECT r FROM Receta r JOIN r.items i " +
    	       "WHERE r.activa = true " +
    	       "GROUP BY r.id " +
    	       "HAVING SUM(i.calorias) BETWEEN :minCalorias AND :maxCalorias")
    	List<Receta> buscarPorCalorias(@Param("minCalorias") int minCalorias,
    	                                @Param("maxCalorias") int maxCalorias);

}
