package com.example.proyectoacolitame.repositorio;

import com.example.proyectoacolitame.modelo.UsuarioRegistrado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepositorio extends JpaRepository<UsuarioRegistrado,String> {
    @Query(value="select * from usuario_registrado  where usuario_registrado.correo=?1",nativeQuery=true)
    UsuarioRegistrado findByCorreo(String correo);
}
