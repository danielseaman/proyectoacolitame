package com.example.proyectoacolitame.controladoresRest;

import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.mail.Mail;
import com.example.proyectoacolitame.modelo.Comentario;
import com.example.proyectoacolitame.modelo.Respuesta;
import com.example.proyectoacolitame.repositorio.ComentarioRepositorio;
import com.example.proyectoacolitame.repositorio.RespuestaRepositorio;
import com.example.proyectoacolitame.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/responder")
    public Respuesta responder(@RequestBody Map<String,Object> mapJson){
        Respuesta respuesta= new Respuesta();
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String correoautor=mapJson.get("correoautor").toString();
        //buscar si la la propia empresa respondió
        Comentario comentario=comentarioRepositorio.findById((Integer)mapJson.get("idComentario")).get();
        respuesta.setContenido(mapJson.get("contenido").toString());
        respuesta.setFecha(dft.format(now));
        Mail mail=new Mail();
        String correo1=comentarioRepositorio.findById((Integer)mapJson.get("idComentario")).get().getEmpresa().getCorreo();

        String body1="El usuario "+correoautor+" ha escrito una respuesta en tu empresa";
        String body2="Has escrito una respuesta";
        mail.enviarMail(correo1,"Comentario",body1);
        mail.enviarMail(correoautor,"Comentario",body2);
        if(comentario.getEmpresa().getCorreo().equals(correoautor)){
            respuesta.setComentario(comentario);
            respuesta.setEmpresa(comentario.getEmpresa());//este seria el autor de la respuesta

            //enviar correo a comentario.getEmpresa().getCorreo() y a comentario.getUsuarioRegistrado().getCorreo()

        }else{// si no respodió la empresa respondió cualquier usuarioregistrado
            respuesta.setComentario(comentario);
            respuesta.setUsuarioRegistrado(usuarioRepositorio.findByCorreo(correoautor));
        }
        return respuestaRepositorio.save(respuesta);
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
