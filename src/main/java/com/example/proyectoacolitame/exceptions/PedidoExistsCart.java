package com.example.proyectoacolitame.exceptions;

public class PedidoExistsCart extends RuntimeException{
    public PedidoExistsCart(){
        super("Pedido ya existe en el carrito");
    }
}
