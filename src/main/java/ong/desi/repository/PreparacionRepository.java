package ong.desi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ong.desi.entity.Preparacion;

@Repository
public interface PreparacionRepository extends JpaRepository<Preparacion, Long> {



}