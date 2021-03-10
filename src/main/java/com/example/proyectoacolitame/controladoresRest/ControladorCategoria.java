package com.example.proyectoacolitame.controladoresRest;

import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.modelo.Categoria;
import com.example.proyectoacolitame.repositorio.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categoria")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT, RequestMethod.POST})
public class ControladorCategoria {
    @Autowired
    CategoriaRepositorio categoriaRepositorio;
    @PostMapping("/insertar")
    public Categoria guardar(@RequestBody Map<String, Object> mapJson){
        Categoria categoria = new Categoria();
        categoria.setNombre(mapJson.get("nombre").toString());
        return categoriaRepositorio.save(categoria);
    }
    @PutMapping("/actualizar")
    public Categoria actualizar(@RequestBody Map<String, Object> mapJson){
        Categoria categoria = categoriaRepositorio.findById((Integer)mapJson.get("idCategoria")).get();
        if(categoria!=null){
            categoria.setNombre(mapJson.get("nombre").toString());
            return categoriaRepositorio.save(categoria);
        }else
            throw new DataNotFoundException();

    }
    @DeleteMapping("/borrar/{id_categoria}")
    public void borrar(@PathVariable(value = "id_categoria")Integer id){ categoriaRepositorio.deleteById(id);
    }
    @GetMapping("/todos")
    public List<HashMap<String,Object>> getTodos(){
        List<Object[]> respuestas=categoriaRepositorio.findAllBy();
        List<HashMap<String,Object>> respuesta=new ArrayList<>();
        for (Object[] objects : respuestas) {
            HashMap<String, Object> mapa = new HashMap<>();
            mapa.put("id_categoria", objects[0]);
            mapa.put("nombre", objects[1]);
            respuesta.add(mapa);
        }
       return respuesta;
    }
    @GetMapping("/{nombre}")
    public Categoria getByNombre(@PathVariable(value = "nombre")String nombre){
        if(categoriaRepositorio.findByNombre(nombre)!=null){
            return categoriaRepositorio.findByNombre(nombre);
        }else{
            throw new DataNotFoundException();
        }
    }
}
