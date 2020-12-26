package com.example.proyectoacolitame.controladoresRest;

import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.modelo.Categoria;
import com.example.proyectoacolitame.repositorio.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categoria")
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
        categoria.setNombre(mapJson.get("nombre").toString());
        return categoriaRepositorio.save(categoria);
    }
    @DeleteMapping("/borrar/{id_categoria}")
    public void borrar(@PathVariable(value = "id_categoria")Integer id){ categoriaRepositorio.deleteById(id);
    }
    @GetMapping("/todos")
    public List<Categoria> getTodos(){
        return categoriaRepositorio.findAllBy();
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
