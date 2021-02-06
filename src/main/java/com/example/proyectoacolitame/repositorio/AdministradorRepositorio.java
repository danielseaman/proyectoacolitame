package com.example.proyectoacolitame.repositorio;

import com.example.proyectoacolitame.modelo.AdministradorEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdministradorRepositorio extends JpaRepository<AdministradorEmpresa ,String> {
    @Query(value="select id_administrador,clave,administrador_empresa.correo from administrador_empresa join empresa using (id_administrador) where id_empresa=?1",nativeQuery=true)
    AdministradorEmpresa findByEmpresa(Integer idEmpresa);
    @Query(value="select * from administrador_empresa where administrador_empresa.correo=?1",nativeQuery=true)
    AdministradorEmpresa findByCorreo(String nombre);
}
