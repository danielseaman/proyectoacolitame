package com.example.proyectoacolitame.controladoresRest;

import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.modelo.Producto;
import com.example.proyectoacolitame.repositorio.ComentarioRepositorio;
import com.example.proyectoacolitame.repositorio.EmpresaRepositorio;
import com.example.proyectoacolitame.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/producto")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT, RequestMethod.POST})
public class ControladorProducto {
    @Autowired
    ProductoRepositorio productoRepositorio;
    @Autowired
    EmpresaRepositorio empresaRepositorio;
    @Autowired
    ComentarioRepositorio comentarioRepositorio;
    ByteOperation byteOperation = new ByteOperation();
    @PostMapping("/insertar/{id_empresa}")
    public Producto guardar(@RequestBody Map<String, Object> mapJson, @PathVariable(value = "id_empresa")Integer idEmpresa){
        Producto producto = new Producto();
        producto.setNombre(mapJson.get("nombre").toString());
        producto.setDescripcion(mapJson.get("descripcion").toString());
        producto.setEmpresa(empresaRepositorio.findById(idEmpresa).get());
        double precio = (double)mapJson.get("precio");
        producto.setPrecio(precio);
        return productoRepositorio.save(producto);
    }
    @PutMapping("/actualizar/idProducto/{id_producto}")
    public Producto actualizar(@RequestBody Map<String, Object> mapJson,@PathVariable(value = "id_producto")Integer idproducto){
        Producto producto = productoRepositorio.findById(idproducto).get();
        producto.setNombre(mapJson.get("nombre").toString());
        producto.setDescripcion(mapJson.get("descripcion").toString());
        double precio = (double)mapJson.get("precio");
        producto.setPrecio(precio);

        return productoRepositorio.save(producto);
    }
    @DeleteMapping("/borrar/idProducto/{id_producto}")
    public String borrar(@PathVariable(value = "id_producto")Integer id){
        productoRepositorio.deleteById(id);
        return "done";
    }
    @GetMapping("/id/{id}")
    public Producto getById(@PathVariable(value = "id")Integer id){

        if(productoRepositorio.findById(id).isPresent()){
            Producto p = productoRepositorio.findById(id).get();
            p.setFoto(byteOperation.decompressBytes(p.getFoto()));
            return p;
        }else{
            throw new DataNotFoundException();
        }

        //excepcion
    }
    @PutMapping(path = "/image/{id}")
    public Producto guardarFoto(@RequestParam(value = "fileImage") MultipartFile file, @PathVariable(value = "id") Integer id) throws IOException {
        Producto producto = productoRepositorio.findById(id).get();
        producto.setFoto(byteOperation.compressBytes(file.getBytes()));
        return productoRepositorio.save(producto);
    }
    @GetMapping("/nombre/{nombre}")
    public List<HashMap<String,Object>> getByNombre(@PathVariable(value = "nombre")String nombre){
        List<Object[]> productos=productoRepositorio.findByNombre(nombre);
        List<HashMap<String,Object>> respuesta=new ArrayList<>();
        if(productos!=null){
            for(int i=0;i<productos.size();i++){
                Object[] objects=productos.get(i);
                HashMap<String,Object> mapa=new HashMap<>();
                mapa.put("id_producto",objects[0]);
                mapa.put("nombre",objects[1]);
                mapa.put("foto",byteOperation.decompressBytes((byte[])objects[2]));
                mapa.put("precio", objects[3]);
                mapa.put("descripcion", objects[4]);
                mapa.put("id_empresa", objects[5]);
                mapa.put("nombreEmpresa", objects[6]);
                mapa.put("fotoEmpresa",byteOperation.decompressBytes((byte[])objects[7]));
                respuesta.add(mapa);
            }
            return respuesta;
        }else{
            throw new DataNotFoundException();
        }
    }
    @GetMapping("/categoria/{categoria}")
    public List<HashMap<String,Object>> getByCategoria(@PathVariable(value = "categoria")int categoria){
        List<Object[]> productos=productoRepositorio.findByCategoria(categoria);
        List<HashMap<String,Object>> respuesta=new ArrayList<>();
        for (Object[] producto : productos) {
            HashMap<String, Object> mapa = new HashMap<>();
            Object[] objects = producto;
            mapa.put("id_producto", objects[0]);
            mapa.put("nombre", objects[1]);
            mapa.put("foto",byteOperation.decompressBytes((byte[])objects[2]));
            mapa.put("precio", objects[3]);
            mapa.put("descripcion", objects[4]);
            mapa.put("id_empresa", objects[5]);
            mapa.put("nombreEmpresa", objects[6]);
            respuesta.add(mapa);
        }
        return respuesta;
    }
}
