package com.example.proyectoacolitame.controladoresRest;

import com.example.proyectoacolitame.modelo.Empresa;
import com.example.proyectoacolitame.repositorio.EmpresaRepositorio;
import com.example.proyectoacolitame.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Bienvenida {
    @Autowired
    EmpresaRepositorio empresaRepositorio;
    @RequestMapping(method = RequestMethod.GET, value = "/verificacion/",params = {"num"})
    public String index(@RequestParam("num")int idempresa) {
        //ControladorEmpresa controladorEmpresa=new ControladorEmpresa();
        //controladorEmpresa.verficar(idempresa);
        System.out.println("idempresa: "+idempresa);
        System.out.println("hola");
        return "Bienvenida.html";

    }

}
