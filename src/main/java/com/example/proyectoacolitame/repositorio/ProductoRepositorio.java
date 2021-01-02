package com.example.proyectoacolitame.repositorio;


import com.example.proyectoacolitame.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepositorio extends JpaRepository<Producto,Integer> {
    @Query(value="select * from producto  where producto.id_producto=?1 and producto.id_empresa=?2",nativeQuery=true)
    Producto findByIdProductoAndEmpresa(Integer idProducto,Integer idEmpresa);
    @Query(value="select * from producto  where producto.id_producto=?1",nativeQuery=true)
    Producto findByIdProducto(Integer idProducto);
    @Query(value="select * from producto  where producto.id_empresa=?1",nativeQuery=true)
    List<Producto> findByEmpresa(Integer idEmpresa);
    @Query(value="select producto.id_producto,producto.nombre as nombre1,producto.bytes_foto,producto.precio,producto.descripcion,empresa.id_empresa,empresa.nombre as nombre2 from categoria,empresa,producto  where empresa.id_categoria=categoria.id_categoria and categoria.id_categoria=?1 and empresa.id_empresa=producto.id_empresa",nativeQuery=true)//numero3
    List<Object[]> findByCategoria(Integer idCategoria);
    @Query(value="select producto.id_producto,producto.nombre,producto.bytes_foto,producto.precio,producto.descripcion,empresa.id_empresa,empresa.nombre as nombre2,empresa.bytes_foto as foto2 from producto,empresa  where lower(producto.nombre) like CONCAT('%',lower(?1),'%')",nativeQuery=true)//numero5
    List<Object[]> findByNombre(String Nombre);

}
