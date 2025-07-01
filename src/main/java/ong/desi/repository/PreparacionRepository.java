package ong.desi.repository;

import java.time.LocalDate; 
import ong.desi.entity.Preparacion;
import ong.desi.entity.Receta; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreparacionRepository extends JpaRepository<Preparacion, Long> {

  
    boolean existsByRecetaAndFechaPreparacion(Receta receta, LocalDate fecha);

}