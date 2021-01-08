package com.example.proyectoacolitame.controladoresRest;

import com.example.proyectoacolitame.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Bienvenida {
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String index() {
        return "Bienvenida.html";
    }
}
