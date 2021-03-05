package com.example.proyectoacolitame.controladoresRest;

import com.example.proyectoacolitame.modelo.Empresa;
import com.example.proyectoacolitame.repositorio.EmpresaRepositorio;
import com.example.proyectoacolitame.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*", methods = {RequestMethod.GET})
@RequestMapping("/verificacion")

public class Bienvenida {
    @Autowired
    EmpresaRepositorio empresaRepositorio;
    @GetMapping("/")
    public String index(@RequestParam(name="num")int idempresa) {
        //ControladorEmpresa controladorEmpresa=new ControladorEmpresa();
        //controladorEmpresa.verficar(idempresa);
        //http://localhost:8080/verificacion/?num=3
        System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println("idempresa: "+idempresa);
        System.out.println("hola");
        return "Bienvenida.html";

    }

}
