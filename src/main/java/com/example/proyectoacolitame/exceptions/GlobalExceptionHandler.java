package com.example.proyectoacolitame.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(com.example.proyectoacolitame.exceptions.DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFound(com.example.proyectoacolitame.exceptions.DataNotFoundException ex, WebRequest request){
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message","No data found");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(com.example.proyectoacolitame.exceptions.PedidoExistsCart.class)
    public ResponseEntity<Object> handlePedido(com.example.proyectoacolitame.exceptions.PedidoExistsCart ex, WebRequest request){
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message","pedido ya existe en el carrito");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(com.example.proyectoacolitame.exceptions.InsertFailed.class)
    public ResponseEntity<Object> handleInsert(com.example.proyectoacolitame.exceptions.InsertFailed ex, WebRequest request){
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message","Error");
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
