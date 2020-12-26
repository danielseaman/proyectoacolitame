package com.example.proyectoacolitame.repositorio;


import com.example.proyectoacolitame.modelo.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RespuestaRepositorio extends JpaRepository<Respuesta,Integer> {
    @Query(value="select * from respuesta  where respuesta.id_comentario=?1",nativeQuery=true)
    List<Respuesta> findByComentario(Integer idComentario);
}
