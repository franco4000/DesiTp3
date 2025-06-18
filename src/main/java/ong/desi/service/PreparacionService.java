package ong.desi.service;

import java.util.List;
import ong.desi.entity.Preparacion;

public interface PreparacionService {

    Preparacion registrarPreparacion(Preparacion preparacion);

    List<Preparacion> listarPreparaciones();

    Preparacion modificarFechaPreparacion(Long id, Preparacion preparacionActualizada);

    void eliminarPreparacion(Long id);
}