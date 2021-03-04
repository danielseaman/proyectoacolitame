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

import static java.lang.Integer.parseInt;

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
    public HashMap<String,Object> guardar(@RequestBody Map<String,Object> mapJson, Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        System.out.println(user);
        String idusuario=user.get("sub").asString();
        try {
            Comentario comentario = new Comentario();
            DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            comentario.setContenido(mapJson.get("contenido").toString());
            comentario.setFecha(dft.format(now));
            /*si necesitotodo por estos sets*/
            comentario.setUsuarioRegistrado(usuarioRepositorio.findById(idusuario).get());
            comentario.setEmpresa(empresaRepositorio.findById((Integer)mapJson.get("idEmpresa")).get());
            String correo1 =empresaRepositorio.findById((Integer)mapJson.get("idEmpresa")).get().getCorreo();
            String correo2=usuarioRepositorio.findById(idusuario).get().getCorreo();
            EnviarCorreo enviarCorreo= new EnviarCorreo();
            String mensaje1="El usuario "+correo2+" ha escrito un comentario en tu empresa";
            String mensaje2="Has escrito un comentario";
            enviarCorreo.crearCorreo(correo1,mensaje1,"Comentario");
            EnviarCorreo enviarCorreo2= new EnviarCorreo();
            enviarCorreo.crearCorreo(correo2,mensaje2,"Comentario");
            enviarCorreo.start();
            enviarCorreo2.start();

            comentarioRepositorio.save(comentario);
            HashMap<String,Object> respuesta=new HashMap<>();
            respuesta.put("contenido",comentario.getContenido());
            respuesta.put("fecha",comentario.getFecha());
            respuesta.put("idComentario",comentario.getIdComentario());
            respuesta.put("usuario",comentarioRepositorio.findById(comentario.getIdComentario()).get().getUsuarioRegistrado().getNombre());
            return respuesta;
        }catch (Exception e){
            System.out.println(e.getMessage());
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
        for(int i=actual;i<actual+cantidadmaxima;i++){
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
