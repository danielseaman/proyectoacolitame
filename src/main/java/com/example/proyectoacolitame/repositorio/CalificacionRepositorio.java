package com.example.proyectoacolitame.repositorio;

import com.example.proyectoacolitame.modelo.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalificacionRepositorio extends JpaRepository<Calificacion,Integer> {
    @Query(value="select * from calificacion where calificacion.id_empresa=?1 and calificacion.id_usuario=?2",nativeQuery=true)
    Calificacion findByEmpresaAndUsuarioRegistrado(Integer idEmpresa,Integer idUsuario);
    @Query(value="select * from calificacion where calificacion.id_empresa=?1",nativeQuery=true)
    List<Calificacion> findByEmpresa(Integer idEmpresa);
}