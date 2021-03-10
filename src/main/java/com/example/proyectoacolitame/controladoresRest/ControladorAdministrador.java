package com.example.proyectoacolitame.controladoresRest;

import com.auth0.jwt.interfaces.Claim;
import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.modelo.AdministradorEmpresa;
import com.example.proyectoacolitame.modelo.UsuarioRegistrado;
import com.example.proyectoacolitame.repositorio.AdministradorRepositorio;
import com.example.proyectoacolitame.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/administrador")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT, RequestMethod.POST})
public class ControladorAdministrador {
    //String link="http://localhost/api/verificacion/?num=";
    @Autowired
    AdministradorRepositorio administradorRepositorio;
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @PostMapping("/insertar")
    public boolean insertar(@RequestBody Map<String, Object> mapJson, Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        String idAdministrador=user.get("sub").asString();
        boolean admin=user.get("admin").asBoolean();
        int idEmpresa=Integer.parseInt((String) mapJson.get("id_empresa"));
        String correoNuevoAdmin=mapJson.get("correo")+"";
        try{
            if(admin){
                AdministradorEmpresa administradorEmpresa=administradorRepositorio.findById(idAdministrador).get();
                if(administradorEmpresa.getPrincipal()){
                    if(administradorEmpresa.getEmpresa().getIdEmpresa()==idEmpresa){
                        AdministradorEmpresa administradorEmpresa1=new AdministradorEmpresa();
                        UsuarioRegistrado usuarioRegistrado= usuarioRepositorio.findByCorreo(correoNuevoAdmin);
                        administradorEmpresa1.setIdAdministrador(usuarioRegistrado.getIdUsuario());
                        administradorEmpresa1.setEmpresa(administradorEmpresa.getEmpresa());
                        administradorEmpresa1.setPrincipal(false);
                        administradorEmpresa1.setCorreo(correoNuevoAdmin);
                        administradorRepositorio.save(administradorEmpresa1);
                        /*String body="Siga el link para verificar su cuenta: "+link+administradorEmpresa.getEmpresa().getIdEmpresa();
                        EnviarCorreo enviarCorreo=new EnviarCorreo();
                        enviarCorreo.crearCorreo(correoNuevoAdmin,body,"Verificación");*/
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch (Exception e){
            throw new DataNotFoundException();
        }
       return true;
    }
    @PostMapping("/borrar")
    public boolean eliminar(@RequestBody Map<String, Object> mapJson, Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        //String idAdministrador=user.get("sub").asString();
        boolean admin=user.get("admin").asBoolean();
        int idEmpresa=Integer.parseInt((String) mapJson.get("id_empresa"));
        String correoEliminar=mapJson.get("correo")+"";
        AdministradorEmpresa administradorEmpresa=administradorRepositorio.findByCorreo(correoEliminar);
        if(admin){
            if(idEmpresa==administradorEmpresa.getEmpresa().getIdEmpresa()){
                administradorRepositorio.delete(administradorEmpresa);
                return true;
            }else{
                throw new DataNotFoundException();
            }
        }else {
            return false;
        }
    }

    @GetMapping("/alla/{id}")
    public List<Map<String, Object>> getAll(@PathVariable(value = "id") Integer id, Authentication authentication){
        Map<String, Claim> user = (Map<String,Claim>) authentication.getPrincipal();
        boolean admin = user.get("admin").asBoolean();
        try{
            if(admin){
                return administradorRepositorio.findAllByIdAdministrador(id);
            }else {
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }


}
