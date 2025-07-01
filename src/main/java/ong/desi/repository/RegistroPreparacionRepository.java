package ong.desi.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ong.desi.entity.Receta;
import ong.desi.entity.RegistroPreparacion;
@Repository
public interface RegistroPreparacionRepository extends JpaRepository<RegistroPreparacion, Long> {
	

	 List<RegistroPreparacion> findByReceta(Receta receta);
	 
	
}

