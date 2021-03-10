package com.example.proyectoacolitame.repositorio;

import com.example.proyectoacolitame.modelo.AdministradorEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface AdministradorRepositorio extends JpaRepository<AdministradorEmpresa ,String> {
    @Query(value="select administrador_empresa.id_administrador,administrador_empresa.correo from administrador_empresa join empresa using (id_administrador) where id_empresa=?1",nativeQuery=true)
    AdministradorEmpresa findByEmpresa(Integer idEmpresa);
    @Query(value="select * from administrador_empresa where administrador_empresa.correo=?1",nativeQuery=true)
    AdministradorEmpresa findByCorreo(String nombre);
    @Query(value="select id_administrador,correo from administrador_empresa where id_empresa=?1",nativeQuery = true)
    List<Map<String,Object>> findAllByIdAdministrador(Integer idEmpres);
}
