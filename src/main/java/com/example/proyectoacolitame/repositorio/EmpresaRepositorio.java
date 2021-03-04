package com.example.proyectoacolitame.repositorio;

import com.example.proyectoacolitame.modelo.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpresaRepositorio extends JpaRepository<Empresa,Integer> {
    @Query(value="select id_empresa,nombre,bytes_foto,latitud,longitud from empresa  where lower(empresa.nombre) like CONCAT('%',lower(?1),'%')",nativeQuery=true)//Probar si vale con'' numero4
    List<Object[]> findByNombre(String nombre);
    @Query(value="select * from empresa  where empresa.correo=?1",nativeQuery=true)
    Empresa findByCorreo(String correo);
    @Query(value="select * from empresa  where empresa.id_empresa=?1",nativeQuery=true)
    Empresa findById(int id_empresa);
    @Query(value="select id_empresa,nombre,correo,bytes_foto from empresa  where empresa.id_categoria=?1",nativeQuery=true)//numero 2
    List<Object[]> findByCategoria(int categoria);
    @Query(value="select id_empresa,empresa.nombre,facebook,twitter,instagram from empresa,administrador_empresa where administrador_empresa.id_empresa=empresa.id_empresa and id_administrador=?1",nativeQuery = true)
    List<Object[]> findbyAdmin(String idAdministrador);
}
