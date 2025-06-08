package ong.desi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ong.desi.entity.ItemReceta;
@Repository
public interface ItemRecetaRepository extends JpaRepository<ItemReceta, Long> {



}
