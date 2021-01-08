package com.example.proyectoacolitame.controladoresRest;

import com.auth0.jwt.interfaces.Claim;
import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.modelo.AdministradorEmpresa;
import com.example.proyectoacolitame.modelo.Empresa;
import com.example.proyectoacolitame.modelo.Pedido;
import com.example.proyectoacolitame.repositorio.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/empresa")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT, RequestMethod.POST})
public class ControladorEmpresa {
    @Autowired
    EmpresaRepositorio empresaRepositorio;
    @Autowired
    CategoriaRepositorio categoriaRepositorio;
    @Autowired
    AdministradorRepositorio administradorRepositorio;
    @Autowired
    PedidosRepositorio pedidosRepositorio;
    @Autowired
    ProductoRepositorio productoRepositorio;


    ByteOperation byteOperation;
    private Empresa setDatos(@RequestBody Map<String, Object> mapJson, Empresa empresa) {
        empresa.setNombre(mapJson.get("nombre").toString());
        empresa.setDireccion(mapJson.get("direccion").toString());
        empresa.setCorreo(mapJson.get("correo").toString());
        empresa.setTelefono(mapJson.get("telefono").toString());
        double latitud=(double)mapJson.get("latitud");
        double longitud=(double)mapJson.get("longitud");
        empresa.setLatitud(latitud);
        empresa.setLongitud(longitud);
        AdministradorEmpresa administradorEmpresa= new AdministradorEmpresa();
        administradorEmpresa.setClave(mapJson.get("claveAdmin").toString());//hay que hashear
        administradorEmpresa.setCorreo(mapJson.get("correoAdmin").toString());
        administradorRepositorio.save(administradorEmpresa);
        empresa.setCategoria(categoriaRepositorio.findByNombre(mapJson.get("categoria").toString()));
        return empresaRepositorio.save(empresa);
    }
    @PostMapping("/insertar")
    public Empresa guardar(@RequestBody Map<String,Object> mapJson){
        Empresa empresa = new Empresa();
        return setDatos(mapJson, empresa);
    }
    @GetMapping("/getCercanas/{latitud}/{longitud}/{categoria}")
    public List<Map<String,Object>> getCercanasCategoria(@PathVariable(value = "latitud")String latitud,@PathVariable(value = "longitud")String longitud,@PathVariable(value = "categoria")Integer categoria){
        List<Empresa> empresas=empresaRepositorio.findAll();
        //Empresa origen =empresaRepositorio.findById(idEmpresa).get();
        List<Map<String,Object>> listaUbicaciones=new ArrayList<>();
        for(int i=0;i<empresas.size();i++){
            Map<String,Object> mapa= new HashMap<>();
            Unirest.setTimeouts(0, 0);
            try {

                double distancia=distancia(latitud, longitud, empresas, i);
                if(distancia<5000 && empresas.get(i).getCategoria().getIdCategoria()==categoria){
                    mapa.put("nombreEmpresa",empresas.get(i).getNombre());
                    mapa.put("correo", empresas.get(i).getCorreo());
                    mapa.put("ubicacion", new double[]{empresas.get(i).getLatitud(), empresas.get(i).getLongitud()});
                    listaUbicaciones.add(mapa);
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        return listaUbicaciones;
    }

    @GetMapping("/getCercanas/test")
    public String testCercanas(){
        return "You are allow";
    }

    @GetMapping("/getCercanas/usuario/{latitud}/{longitud}")
    public List<Map<String,Object>> getCercanas(@PathVariable(value = "latitud")String latitud,@PathVariable(value = "longitud")String longitud){
        List<Empresa> empresas=empresaRepositorio.findAll();
        //Empresa origen =empresaRepositorio.findById(idEmpresa).get();
        List<Map<String,Object>> listaUbicaciones=new ArrayList<>();
        for(int i=0;i<empresas.size();i++){
            Map<String,Object> mapa= new HashMap<>();
            Unirest.setTimeouts(0, 0);
            try {
                double distancia=distancia(latitud, longitud, empresas, i);
                if(distancia<5000){
                    mapa.put("nombreEmpresa",empresas.get(i).getNombre());
                    mapa.put("correo", empresas.get(i).getCorreo());
                    mapa.put("ubicacion", new double[]{empresas.get(i).getLatitud(), empresas.get(i).getLongitud()});
                    listaUbicaciones.add(mapa);
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        return listaUbicaciones;
    }
    private double distancia(@PathVariable("latitud") String latitud, @PathVariable("longitud") String longitud, List<Empresa> empresas, int i) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+latitud+","+longitud+"&destinations="+empresas.get(i).getLatitud()+","+empresas.get(i).getLongitud()+"&key=AIzaSyCzmhpNES0ubHOre7YUhX_VLLBSz-a0TLg").asJson();
        JSONObject jsonObject=response.getBody().getObject();
        JSONArray jsonObjectInterno=(JSONArray) jsonObject.get("rows");
        JSONObject loquesirve=jsonObjectInterno.getJSONObject(0);
        JSONArray jsonArray=(JSONArray)loquesirve.get("elements");
        JSONObject loquesirve2=jsonArray.getJSONObject(0);

        JSONObject ahorasi=(JSONObject) loquesirve2.get("distance");
        double distancia=ahorasi.getDouble("value");
        System.out.println("cantidad emrpesas"+empresas.size()+"distancia"+distancia);
        return distancia;
    }

    @GetMapping("/test")
    public String testSecurity(){
        return "Hello";
    }

    @GetMapping("/correo/{correo}")
    public Empresa getEmpresaByCorreo(@PathVariable(value = "correo")String correo){
        Empresa em = empresaRepositorio.findByCorreo(correo);
        if(em!=null){
            em.setFoto(byteOperation.decompressBytes(em.getFoto()));
            return em;
        }else{
            throw new DataNotFoundException();
        }
        //manejar excepcion
    }

//    @GetMapping("/correo/{correo}")
//    public Empresa getEmpresaByCorreo(@PathVariable(value = "correo")String correo, Authentication authentication ){  //Principal object is injected
//        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
//        System.out.println(user.toString());
//        System.out.println(user.get("admin").asBoolean());
//        Empresa em = empresaRepositorio.findByCorreo(correo);
//        if(em!=null){
//            em.setFoto(byteOperation.decompressBytes(em.getFoto()));
//            return em;
//        }else{
//            throw new DataNotFoundException();
//        }
//        //manejar excepcion
//    }
    @GetMapping("/id/{correo}")
    public HashMap<String,Object> getEmpresaById(@PathVariable(value = "correo")int correo){
        Empresa em = empresaRepositorio.findById(correo);
        if(em!=null){
            em.setFoto(byteOperation.decompressBytes(em.getFoto()));
            HashMap<String,Object> mapa=new HashMap<>();
            mapa.put("id_empresa",em.getIdEmpresa());
            mapa.put("nombre",em.getNombre());
            mapa.put("direccion",em.getDireccion());
            mapa.put("telefono",em.getTelefono());
            mapa.put("correo",em.getCorreo());
            mapa.put("latitud",em.getLatitud());
            mapa.put("longitud",em.getLongitud());
            mapa.put("foto",em.getFoto());
            mapa.put("facebook",em.getFacebook());
            mapa.put("twitter",em.getTwitter());
            mapa.put("instagram",em.getInstagram());
            mapa.put("categoria",em.getCategoria().getNombre());
            List<Object[]> productos=productoRepositorio.findByEmpresa(em.getIdEmpresa());
            List<HashMap<String,Object>> listaproductos=new ArrayList<>();
            for(int i=0;i<productos.size();i++){
                Object[] objects1=productos.get(i);
                HashMap<String,Object> mapita=new HashMap<>();
                mapita.put("id_producto",objects1[0]);
                mapita.put("nombre",objects1[1]);
                mapita.put("precio",objects1[2]);
                listaproductos.add(mapita);
            }
            //mapa.put("productos",listaproductos);
            //mapa.put("comentarios",em.getComentarios());
            return mapa;
        }else{
            throw new DataNotFoundException();
        }
        //manejar excepcion
    }
    @GetMapping("/nombre/{nombre}")
    public List<HashMap<String,Object>> getEmpresaByNombre(@PathVariable(value = "nombre")String nombre){
        List<Object[]> em = empresaRepositorio.findByNombre(nombre);
        List<HashMap<String,Object>> respuesta=new ArrayList<>();
        if(em!=null){
            for (int i=0;i<em.size();i++) {
                HashMap<String,Object> mapa=new HashMap<>();
                Object[] objects=em.get(i);
                mapa.put("id_empresa",objects[0]);
                mapa.put("nombre",objects[1]);
                //mapa.put("foto",byteOperation.decompressBytes((byte[])objects[2]));
                mapa.put("latitud",(double)objects[3]);
                mapa.put("longitud",(double)objects[4]);
                respuesta.add(mapa);

            }
            return respuesta;
        }else{
            throw new DataNotFoundException();
        }
    }
    @GetMapping("/getPedidos/idEmpresa/{id_empresa}")
    public List<Pedido> getPedidos(@PathVariable(value = "id_empresa")Integer idEmpresa){
        return pedidosRepositorio.findByRevisadoAndEmpresa(idEmpresa);
    }
    @GetMapping("/getCategoria/{id_empresa}")
    public String getCategoria(@PathVariable(value = "id_empresa")Integer id){
        if(empresaRepositorio.findById(id).isPresent()){
            return empresaRepositorio.findById(id).get().getCategoria().getNombre();
        }else{
            throw new DataNotFoundException();
        }
    }

    @GetMapping("/getAllE")
    public List<Empresa> getEmpresaTodas(){
        List<Empresa> empresas = empresaRepositorio.findAll();
        if(empresas!=null){
            for (Empresa empresa : empresas) {
                empresa.setFoto(byteOperation.decompressBytes(empresa.getFoto()));
            }
            return empresas;
        }else{
            throw new DataNotFoundException();
        }
    }

    @GetMapping("/categoria/{categoria}/{actual}/{cantidadmaxima}")
    public List<HashMap<String,Object>> getEmpresaCategoria(@PathVariable(value = "categoria")int categoria,@PathVariable(value = "actual")int actual,@PathVariable(value = "cantidadmaxima")int cantidadmaxima) throws DataNotFoundException{
        List<Object[]> empresas = empresaRepositorio.findByCategoria(categoria);
        List<HashMap<String,Object>> respuestas=new ArrayList<>();
        if(cantidadmaxima>empresas.size()){
            cantidadmaxima=empresas.size();
        }
        if(empresas!=null){
            for (int i=actual;i<cantidadmaxima;i++) {
                HashMap<String,Object> mapa= new HashMap<>();
                Object[] objects=empresas.get(i);
                mapa.put("id_empresa",objects[0]);
                mapa.put("nombre",objects[1]);
                mapa.put("correo",objects[2]);
                mapa.put("foto",byteOperation.decompressBytes((byte[])objects[3]));
                respuestas.add(mapa);

            }
            return respuestas;
        }else{
            throw new DataNotFoundException();
        }
    }
    @PutMapping("/insertarRedes")
    public Empresa putredes(@RequestBody Map<String,Object> mapJson){
        Empresa empresa = empresaRepositorio.findById((Integer)mapJson.get("idEmpresa")).get();
        if(!mapJson.get("facebook").toString().equals("")){
            empresa.setFacebook(mapJson.get("facebook").toString());
        }else{
            empresa.setFacebook("");
        }
        if(!mapJson.get("twitter").toString().equals("")){
            empresa.setTwitter(mapJson.get("twitter").toString());
        }else{
            empresa.setTwitter("");
        }
        if(!mapJson.get("instagram").toString().equals("")){
            empresa.setInstagram(mapJson.get("instagram").toString());
        }else{
            empresa.setInstagram("");
        }
        empresaRepositorio.save(empresa);
        return empresa;
    }
    @PutMapping("/eliminarFacebook/idEmpresa/{id_empresa}")
    public Empresa eliminaFacebook(@PathVariable(value = "id_empresa")Integer idEmpresa){
        Empresa empresa = empresaRepositorio.findById(idEmpresa).get();
        System.out.println("hola");
        empresa.setFacebook("");
        System.out.println("adios");
        empresaRepositorio.save(empresa);
        return empresa;
    }
    @PutMapping("/eliminarTwitter/idEmpresa/{id_empresa}")
    public Empresa eliminaTwitter(@PathVariable(value = "id_empresa")Integer idEmpresa){
        Empresa empresa = empresaRepositorio.findById(idEmpresa).get();

        empresa.setTwitter("");
        empresaRepositorio.save(empresa);
        return empresa;
    }
    @PutMapping("/eliminarInstagram/idEmpresa/{id_empresa}")
    public Empresa eliminaInstagram(@PathVariable(value = "id_empresa")Integer idEmpresa){
        Empresa empresa = empresaRepositorio.findById(idEmpresa).get();

        empresa.setInstagram("");
        empresaRepositorio.save(empresa);
        return empresa;
    }

    @PutMapping("/actualizar/idEmpresa/{id_empresa}")
    public Empresa actualizar(@RequestBody Map<String,Object> mapJson, @PathVariable(value = "id_empresa")Integer idEmpresa){
        Empresa empresa = empresaRepositorio.findById(idEmpresa).get();
        return setDatos(mapJson, empresa);

    }
    @DeleteMapping("/borrar/idEmpresa/{id_empresa")
    public void borrarEmpresa(@PathVariable(value = "id_empresa")Integer idEmpresa){
        empresaRepositorio.deleteById(idEmpresa);
    }
    @PutMapping(path = "/image/{id}")
    public Empresa guardarFoto(@RequestParam(value = "fileImage") MultipartFile file, @PathVariable(value = "id") Integer id) throws IOException {
        Empresa empresa = empresaRepositorio.findById(id).get();
        empresa.setFoto(byteOperation.compressBytes(file.getBytes()));
        return empresaRepositorio.save(empresa);
    }
}
