package ong.desi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ong.desi.entity.Ingrediente;
@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    List<Ingrediente> findAll();
    Optional<Ingrediente> findByNombre(String nombre);

	List<Ingrediente> findByActivoTrue();
}