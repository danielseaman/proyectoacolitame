package com.example.proyectoacolitame.repositorio;

import com.example.proyectoacolitame.modelo.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComentarioRepositorio extends JpaRepository<Comentario,Integer> {
    @Query(value="select * from comentario  where comentario.id_empresa=?1",nativeQuery=true)
    List<Comentario> findByEmpresa(Integer idEmpresa);

}
