package com.example.proyectoacolitame.repositorio;

import com.example.proyectoacolitame.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidosRepositorio extends JpaRepository<Pedido,Integer> {
    @Query(value="select * from pedido  where pedido.revisado=false and pedido.id_empresa=?1",nativeQuery=true)
    List<Pedido> findByRevisadoAndEmpresa(Integer idEmpresa);

    @Query(value="select * from pedido  where pedido.id_usuario=?1",nativeQuery=true)
    List<Pedido> findByUsuarioRegistrado(Integer idUsuario);
    @Query(value="select * from pedido  where pedido.id_usuario=?1 and pedido.id_producto=?2",nativeQuery=true)
    List<Pedido> findByUsuarioRegistradoAnAndProducto(String idUsuario,Integer idProducto);
}
