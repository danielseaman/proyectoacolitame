package com.example.proyectoacolitame.controladoresRest;

import com.auth0.jwt.interfaces.Claim;
import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.mail.Mail;
import com.example.proyectoacolitame.modelo.Comentario;
import com.example.proyectoacolitame.modelo.Respuesta;
import com.example.proyectoacolitame.modelo.UsuarioRegistrado;
import com.example.proyectoacolitame.repositorio.AdministradorRepositorio;
import com.example.proyectoacolitame.repositorio.ComentarioRepositorio;
import com.example.proyectoacolitame.repositorio.RespuestaRepositorio;
import com.example.proyectoacolitame.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/respuesta")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT, RequestMethod.POST})

public class ControladorRespuesta {
    @Autowired
    RespuestaRepositorio respuestaRepositorio;

    @Autowired
    ComentarioRepositorio comentarioRepositorio;
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    AdministradorRepositorio administradorRepositorio;
    @PostMapping("/responder")
    public HashMap<String,Object> responder(@RequestBody Map<String,Object> mapJson, Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        String idautor=user.get("sub").asString();
        boolean esEmpresa=user.get("admin").asBoolean();
        Respuesta respuesta= new Respuesta();
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();

        String correoautor="";
        Comentario comentario=comentarioRepositorio.findById((Integer)mapJson.get("idComentario")).get();
        respuesta.setContenido(mapJson.get("contenido").toString());
        respuesta.setFecha(dft.format(now));
        Mail mail=new Mail();
        String correo1=comentarioRepositorio.findById((Integer)mapJson.get("idComentario")).get().getEmpresa().getCorreo();
        //si empresa==null el autor es un usuario sino el autor es empresa entonces usuario==null
        if(esEmpresa){
            //agregaste eso para saber como llega el id de una empresa en una respuesta
            int idautor2=Integer.parseInt(idautor);
            System.out.println("id empresa autora de comentario: "+idautor2);
            correoautor=administradorRepositorio.findByEmpresa(idautor2).getCorreo();//pr
            respuesta.setComentario(comentario);
            respuesta.setEmpresa(comentario.getEmpresa());
        }else{
            UsuarioRegistrado usuarioRegistrado =usuarioRepositorio.findById(idautor).get();
            respuesta.setComentario(comentario);
            respuesta.setUsuarioRegistrado(usuarioRegistrado);
        }
        String body1="El usuario "+correoautor+" ha escrito una respuesta en tu empresa";
        String body2="Has escrito una respuesta";
        EnviarCorreo enviarCorreo = new EnviarCorreo();
        enviarCorreo.crearCorreo(correo1,body1,"Respuesta");
        enviarCorreo.start();
        EnviarCorreo enviarCorreo1=new EnviarCorreo();
        enviarCorreo1.crearCorreo(correoautor,body2,"Respuesta");
        enviarCorreo1.start();
        respuestaRepositorio.save(respuesta);
        HashMap<String,Object> respuesta2=new HashMap<>();
        respuesta2.put("contenido",respuesta.getContenido());
        respuesta2.put("fecha",respuesta.getFecha());
        respuesta2.put("idComentario",respuesta.getIdRespuesta());
        respuesta2.put("usuario", respuestaRepositorio.findById(respuesta.getIdRespuesta()).get().getUsuarioRegistrado().getNombre());
        return respuesta2;
    }
    @GetMapping("/getAutor/{id_respuesta}")
    public Map<String,String> getautor(@PathVariable(value = "id_respuesta")Integer idRespuesta){
        Respuesta respuesta=respuestaRepositorio.findById(idRespuesta).get();
        Map<String,String> mapa=new HashMap<>();
        if(respuesta.getEmpresa()!=null){
            mapa.put("tipo","empresa");
            mapa.put("correo",respuesta.getEmpresa().getCorreo());
            mapa.put("nombre",respuesta.getEmpresa().getNombre());
            mapa.put("id",respuesta.getEmpresa().getIdEmpresa()+"");
        }else{
            if(respuesta.getUsuarioRegistrado()!=null){
                mapa.put("tipo","usuario");
                mapa.put("correo",respuesta.getUsuarioRegistrado().getCorreo());
                mapa.put("nombre",respuesta.getUsuarioRegistrado().getNombre());
                mapa.put("id",respuesta.getUsuarioRegistrado().getIdUsuario()+"");
            }else{
                throw  new DataNotFoundException();
            }
        }
        return mapa;
    }
    //capaz y no se usa este
    @GetMapping("/getRespuestas/{id_comentario}")
    public List<Respuesta> respuestas(@PathVariable(value = "id_comentario")Integer idComentario){
        return respuestaRepositorio.findByComentario(idComentario);
    }
}
