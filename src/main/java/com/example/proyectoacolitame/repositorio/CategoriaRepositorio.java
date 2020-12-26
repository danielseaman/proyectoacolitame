package com.example.proyectoacolitame.repositorio;

import com.example.proyectoacolitame.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriaRepositorio extends JpaRepository<Categoria,Integer> {

    @Query(value="select * from categoria  where categoria.nombre=?1",nativeQuery=true)
    Categoria findByNombre(String nombre);
    @Query(value="select id_categoria,nombre from categoria",nativeQuery=true)//numero1
    List<Categoria> findAllBy();
}
