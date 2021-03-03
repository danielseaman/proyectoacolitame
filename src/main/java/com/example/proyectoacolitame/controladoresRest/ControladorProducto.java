package com.example.proyectoacolitame.controladoresRest;

import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.modelo.Producto;
import com.example.proyectoacolitame.repositorio.ComentarioRepositorio;
import com.example.proyectoacolitame.repositorio.EmpresaRepositorio;
import com.example.proyectoacolitame.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public HashMap<String,Object> actualizar(@RequestBody Map<String, Object> mapJson,@PathVariable(value = "id_producto")Integer idproducto){
        Producto producto = productoRepositorio.findById(idproducto).get();
        producto.setNombre(mapJson.get("nombre").toString());
        producto.setDescripcion(mapJson.get("descripcion").toString());
        double precio = (double)mapJson.get("precio");
        producto.setPrecio(precio);
        HashMap<String,Object> mapa=new HashMap<>();
        mapa.put("id_producto",producto.getIdProducto());
        mapa.put("nombre",producto.getNombre());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] image = byteOperation.decompressBytes(producto.getFoto());
        headers.setContentLength(image.length);
        HttpEntity<byte[]> imagen =new HttpEntity<>(image,headers);
        mapa.put("foto",imagen);
        mapa.put("precio", producto.getPrecio());
        mapa.put("descripcion", producto.getDescripcion());
        mapa.put("id_empresa", producto.getEmpresa().getIdEmpresa());
        mapa.put("nombreEmpresa", producto.getEmpresa().getNombre());
        productoRepositorio.save(producto);
        return mapa;
    }
    @DeleteMapping("/borrar/idProducto/{id_producto}")
    public String borrar(@PathVariable(value = "id_producto")Integer id){
        productoRepositorio.deleteById(id);
        return "done";
    }
    @GetMapping("/id/{id}")//pasar a object
    public Producto getById(@PathVariable(value = "id")Integer id){

        if(productoRepositorio.findById(id).isPresent()){
            Producto p = productoRepositorio.findById(id).get();
            p.setFoto(byteOperation.decompressBytes(p.getFoto()));
            return p;
        }else{
            throw new DataNotFoundException();
        }


    }
    @PutMapping(path = "/image/{id}")
    public  HttpEntity<byte[]> guardarFoto(@RequestParam(value = "fileImage") MultipartFile file, @PathVariable(value = "id") Integer id) throws IOException {
        Producto producto = productoRepositorio.findById(id).get();
        producto.setFoto(byteOperation.compressBytes(file.getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] image = byteOperation.decompressBytes(producto.getFoto());
        headers.setContentLength(image.length);

        return new HttpEntity<>(image,headers);
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
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                byte[] image = byteOperation.decompressBytes((byte[])objects[2]);
                headers.setContentLength(image.length);
                HttpEntity<byte[]> imagen =new HttpEntity<>(image,headers);
                mapa.put("foto",imagen);
                mapa.put("precio", objects[3]);
                mapa.put("descripcion", objects[4]);
                mapa.put("id_empresa", objects[5]);
                mapa.put("nombreEmpresa", objects[6]);
                HttpHeaders headers2 = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                byte[] image2 = byteOperation.decompressBytes((byte[])objects[7]);
                headers.setContentLength(image.length);
                HttpEntity<byte[]> imagen2 =new HttpEntity<>(image2,headers2);
                mapa.put("fotoEmpresa",imagen2);
                respuesta.add(mapa);
            }
            return respuesta;
        }else{
            throw new DataNotFoundException();
        }
    }
    @GetMapping("/categoria/{categoria}/{actual}/{cantidadmaxima}")
    public List<HashMap<String,Object>> getByCategoria(@PathVariable(value = "categoria")int categoria,@PathVariable(value = "actual")int actual,@PathVariable(value = "cantidadmaxima")int cantidadmaxima){
        List<Object[]> productos=productoRepositorio.findByCategoria(categoria);
        List<HashMap<String,Object>> respuesta=new ArrayList<>();
        if(cantidadmaxima>productos.size()){
            cantidadmaxima=productos.size();
        }
        for (int i=actual;i<cantidadmaxima;i++) {
            HashMap<String, Object> mapa = new HashMap<>();
            Object[] objects = productos.get(i);
            mapa.put("id_producto", objects[0]);
            mapa.put("nombre", objects[1]);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            byte[] image = byteOperation.decompressBytes((byte[])objects[2]);
            headers.setContentLength(image.length);
            HttpEntity<byte[]> imagen =new HttpEntity<>(image,headers);
            mapa.put("foto",imagen);
            mapa.put("precio", objects[3]);
            mapa.put("descripcion", objects[4]);
            mapa.put("id_empresa", objects[5]);
            mapa.put("nombreEmpresa", objects[6]);
            respuesta.add(mapa);
        }
        return respuesta;
    }
    @GetMapping("/empresa/{id_empresa}/{actual}/{cantidadmaxima}")
    public List<HashMap<String,Object>> getByEmpresa(@PathVariable(value="id_empresa")int empresa,@PathVariable(value="actual")int actual,@PathVariable(value="cantidadmaxima")int cantidadmaxima){
        List<Object[]> productos=productoRepositorio.findByEmpresa(empresa);
        List<HashMap<String,Object>> respuesta=new ArrayList<>();
        if(cantidadmaxima>productos.size()){
            cantidadmaxima=productos.size();
        }
        for (int i=actual;i<cantidadmaxima;i++) {
            HashMap<String, Object> mapa = new HashMap<>();
            Object[] objects = productos.get(i);
            mapa.put("id_producto", objects[0]);
            mapa.put("nombre", objects[1]);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            byte[] image = byteOperation.decompressBytes((byte[])objects[4]);
            headers.setContentLength(image.length);
            HttpEntity<byte[]> imagen =new HttpEntity<>(image,headers);
            mapa.put("foto",imagen);
            mapa.put("precio", objects[2]);
            mapa.put("descripcion", objects[3]);

            respuesta.add(mapa);
        }
        return respuesta;
    }
}
