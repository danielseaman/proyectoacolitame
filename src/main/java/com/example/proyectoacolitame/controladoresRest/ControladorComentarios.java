package com.example.proyectoacolitame.controladoresRest;

import com.auth0.jwt.interfaces.Claim;
import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.mail.Mail;
import com.example.proyectoacolitame.modelo.Comentario;
import com.example.proyectoacolitame.modelo.Respuesta;
import com.example.proyectoacolitame.repositorio.ComentarioRepositorio;
import com.example.proyectoacolitame.repositorio.EmpresaRepositorio;
import com.example.proyectoacolitame.repositorio.RespuestaRepositorio;
import com.example.proyectoacolitame.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT, RequestMethod.POST})
public class ControladorComentarios {
    @Autowired
    ComentarioRepositorio comentarioRepositorio;
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    EmpresaRepositorio empresaRepositorio;
    @Autowired
    RespuestaRepositorio respuestaRepositorio;

    ByteOperation byteOperation = new ByteOperation();
    @PostMapping("/insertar")
    public Comentario guardar(@RequestBody Map<String,Object> mapJson, Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        int idusuario=user.get("sub").asInt();
        try {
            Comentario comentario = new Comentario();
            DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            comentario.setContenido(mapJson.get("contenido").toString());
            comentario.setFecha(dft.format(now));
            comentario.setUsuarioRegistrado(usuarioRepositorio.findById(idusuario).get());
            comentario.setEmpresa(empresaRepositorio.findById((Integer)mapJson.get("idEmpresa")).get());
            String correo1 =empresaRepositorio.findById((Integer)mapJson.get("idEmpresa")).get().getCorreo();
            String correo2=usuarioRepositorio.findById(idusuario).get().getCorreo();
            Mail mail=new Mail();
            String body1="El usuario "+correo2+" ha escrito un comentario en tu empresa";
            String body2="Has escrito un comentario";
            mail.enviarMail(correo1,"Comentario",body1);
            mail.enviarMail(correo2,"Comentario",body2);
            return comentarioRepositorio.save(comentario);
        }catch (Exception e){
            throw new DataNotFoundException();
        }

    }
    @GetMapping("/getComentarios/{id_empresa}/{actual}/{cantidadmaxima}")
    public List<Map<String,Object>> getCOmentarios(@PathVariable(value = "id_empresa")Integer idEmpresa,@PathVariable(value = "actual")int actual,@PathVariable(value = "cantidadmaxima")int cantidadmaxima){
        //se recibe numero limite de resupuestas, se envia el comentario con respuestas+
        //una variable indicando si existen más respuestas
        //if(cantidadmaxima>empresas.size()){
          //  cantidadmaxima=empresas.size();
        //}
        List<Map<String,Object>> json=new ArrayList<>();
        List<Comentario> comentarios=comentarioRepositorio.findByEmpresa(idEmpresa);
        if(cantidadmaxima>comentarios.size()){
            cantidadmaxima=comentarios.size();
        }
        for(int i=0;i<cantidadmaxima;i++){
            Comentario comentario=comentarios.get(i);
            List<Respuesta> respuestas=comentario.getRespuestas();
            List<Map<String,String>> listarespuestas=new ArrayList<>();
            Map<String,Object> comentarioRespuesta=new HashMap<>();
            for(int j=0;j<respuestas.size();j++){
                Map<String,String> nombreRespuesta=new HashMap<>();
                Respuesta respuesta= respuestas.get(j);
                if(respuesta.getUsuarioRegistrado()!=null){
                    String autorRespuesta=respuesta.getUsuarioRegistrado().getNombre();
                    nombreRespuesta.put("autor",autorRespuesta);
                    nombreRespuesta.put("contenido",respuesta.getContenido());
                    nombreRespuesta.put("fecha",respuesta.getFecha());
                }else{
                    if(respuesta.getEmpresa()!=null){
                        String autorRespuesta=respuesta.getEmpresa().getNombre();
                        nombreRespuesta.put("autor",autorRespuesta);
                        nombreRespuesta.put("contenido",respuesta.getContenido());
                        nombreRespuesta.put("fecha",respuesta.getFecha());
                    }
                }
                listarespuestas.add(nombreRespuesta);
            }
            comentarioRespuesta.put("idComentario",comentario.getIdComentario()+"");
            comentarioRespuesta.put("usuario",comentario.getUsuarioRegistrado().getNombre());
            comentarioRespuesta.put("empresa",comentario.getEmpresa().getNombre());
            comentarioRespuesta.put("contenido",comentario.getContenido());
            comentarioRespuesta.put("fecha",comentario.getFecha());
            comentarioRespuesta.put("respuestas",listarespuestas);
            json.add(comentarioRespuesta);
        }
        return json;

    }

}
