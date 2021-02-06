package com.example.proyectoacolitame.controladoresRest;

import com.auth0.jwt.interfaces.Claim;
import com.example.proyectoacolitame.modelo.Calificacion;
import com.example.proyectoacolitame.repositorio.CalificacionRepositorio;
import com.example.proyectoacolitame.repositorio.EmpresaRepositorio;
import com.example.proyectoacolitame.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calificacion")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT, RequestMethod.POST})
public class ControladorCalificacion {
    @Autowired
    CalificacionRepositorio calificacionRepositorio;
    @Autowired
    EmpresaRepositorio empresaRepositorio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @PostMapping("/calificar")
    //optimizar
    public int calificar(@RequestBody Map<String,Object> mapJson, Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        int idEmpresa=(Integer)mapJson.get("idEmpresa");
        String idusuario=user.get("sub").asString();
        if(calificacionRepositorio.findByEmpresaAndUsuarioRegistrado(idEmpresa,idusuario)!=null){
            Calificacion calificacion=calificacionRepositorio.findByEmpresaAndUsuarioRegistrado(idEmpresa,idusuario);
            calificacion.setValor((Integer)mapJson.get("valor"));
            return calificacionpromedio(idEmpresa);
        }else{
            Calificacion calificacion= new Calificacion();
            calificacion.setEmpresa(empresaRepositorio.findById(idEmpresa));
            calificacion.setUsuarioRegistrado(usuarioRepositorio.findById(idusuario).get());
            calificacion.setValor((Integer)mapJson.get("valor"));
            return  calificacionpromedio(idEmpresa);
        }


    }

    private int calificacionpromedio(int idEmpresa){
        List<Object> calificacions= calificacionRepositorio.findByEmpresa(idEmpresa);
        int suma=0;
        for(int i=0;i<calificacions.size();i++){
            suma=suma+(Integer)calificacions.get(i);
        }
        if(calificacions.size()>0){
            return suma/calificacions.size();
        }else{return 0;}
    }
    @GetMapping("/getCalificacion/idEmpresa/{id_empresa}")
    public Integer obtenerCalificacionPromedio(@PathVariable(value = "id_empresa")Integer idEmpresa){
        return calificacionpromedio(idEmpresa);

    }
}
