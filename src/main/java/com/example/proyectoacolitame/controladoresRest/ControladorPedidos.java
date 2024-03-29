package com.example.proyectoacolitame.controladoresRest;

import com.auth0.jwt.interfaces.Claim;
import com.example.proyectoacolitame.exceptions.DataNotFoundException;
import com.example.proyectoacolitame.exceptions.PedidoExistsCart;
import com.example.proyectoacolitame.mail.Mail;
import com.example.proyectoacolitame.modelo.Pedido;
import com.example.proyectoacolitame.modelo.Producto;
import com.example.proyectoacolitame.repositorio.EmpresaRepositorio;
import com.example.proyectoacolitame.repositorio.PedidosRepositorio;
import com.example.proyectoacolitame.repositorio.ProductoRepositorio;
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
@RequestMapping("/api/pedido")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT, RequestMethod.POST})
public class ControladorPedidos {
    @Autowired
    PedidosRepositorio pedidosRepositorio;
    @Autowired
    EmpresaRepositorio empresaRepositorio;
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    ProductoRepositorio productoRepositorio;

    ByteOperation byteOperation = new ByteOperation();
    @PostMapping("/insertarCarrito")
    public HashMap<String, Object> guardarCarritoCompras(@RequestBody Map<String, Object> mapJson, Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        String idusuario=user.get("sub").asString();

        Pedido pedido= new Pedido();
        /*DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        pedido.setFecha(dft.format(now));*/
        List<Pedido> pedidos=pedidosRepositorio.findByUsuarioRegistradoAnAndProducto(idusuario,(Integer)mapJson.get("idProducto"));
        if(pedidos==null){
            pedido.setUsuarioRegistrado(usuarioRepositorio.findById(idusuario).get());
            pedido.setEmpresa(empresaRepositorio.findById((Integer)mapJson.get("idEmpresa")).get());
            pedido.setProducto(productoRepositorio.findById((Integer)mapJson.get("idProducto")).get());
        }else{
            boolean existe=false;
            for(int i=0;i<pedidos.size();i++){
                if(pedidos.get(i).getFecha()==null){
                    existe=true;
                    break;
                }
            }
            if(!existe){
                pedido.setUsuarioRegistrado(usuarioRepositorio.findById(idusuario).get());
                pedido.setEmpresa(empresaRepositorio.findById((Integer)mapJson.get("idEmpresa")).get());
                pedido.setProducto(productoRepositorio.findById((Integer)mapJson.get("idProducto")).get());
            }else {
                throw new PedidoExistsCart();
            }
        }

        pedido=pedidosRepositorio.save(pedido);
        HashMap<String, Object> mapa = new HashMap<>();

        mapa.put("id_pedido", pedido.getIdPedido());
        mapa.put("mensaje", pedido.getMensaje());

        mapa.put("fecha",pedido.getFecha());
        mapa.put("id_empresa", pedido.getEmpresa().getIdEmpresa());
        mapa.put("nombre_empresa",pedido.getEmpresa().getNombre());
        mapa.put("id_producto",pedido.getProducto().getIdProducto());
        mapa.put("nombre_producto",pedido.getProducto().getNombre());
        mapa.put("precio",pedido.getProducto().getPrecio());
        mapa.put("foto","http://localhost/api/producto/image/"+pedido.getProducto().getIdProducto());

        return mapa;

    }
    @PostMapping("/borrarCarro/{id_pedido}")
    public void eliminarPedido(@PathVariable(value = "id_pedido")Integer idPedido,Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        Pedido pedido=pedidosRepositorio.findById(idPedido).get();
        if (pedido != null) {
            if(pedido.getFecha()==null){
                pedidosRepositorio.deleteById(idPedido);
            }
        }else{
            throw new DataNotFoundException();
        }

    }
    @PostMapping("/realizarPedido")
    public List<Pedido> realizarPedido(@RequestBody Map<String, Object> mapJson,Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        //int idusuario=user.get("sub").asInt();
        String nombre=user.get("name").asString();
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        ArrayList<Integer> arreglo=(ArrayList<Integer>) mapJson.get("idpedidos");
        String mensaje=mapJson.get("mensaje").toString();
        List<Pedido> pedidos = new ArrayList<>();


        //enviar correo a las empresas
        for(int i=0;i<arreglo.size();i++){
            EnviarCorreo enviarCorreo= new EnviarCorreo();

            if(pedidosRepositorio.findById(arreglo.get(i)).isPresent()){
                Pedido pedido= pedidosRepositorio.findById(arreglo.get(i)).get();
                pedido.setFecha(dft.format(now));
                pedido.setRevisado(false);
                pedido.setMensaje(mensaje);
                pedidos.add(pedido);
                pedidosRepositorio.save(pedido);
                String correo=pedido.getEmpresa().getCorreo();
                String body="El usuario: "+pedido.getUsuarioRegistrado().getNombre()+" ha pedido el producto: "+pedido.getProducto().getNombre()+"\n" +
                        "Mensaje del cliente: "+pedido.getMensaje()+"\n" +
                        "Contacto con el cliente: "+pedido.getUsuarioRegistrado().getCorreo();

                enviarCorreo.crearCorreo(correo,body,"Pedido");
                enviarCorreo.start();
                String telefono=pedido.getEmpresa().getTelefono();
                String producto=pedido.getProducto().getNombre().replaceAll(" ","%20");
                System.out.println(producto);
                String mensaje2 ="Su pedido se ha realizado correctamente \n" +
                        "Chat de whatsapp disponible:  https://wa.me/"+telefono+"?text=Hola%20estoy%20interesado%20en%20el%20producto:%20"+producto +
                        " \nCorreo de contacto con la empresa: "+pedido.getEmpresa().getCorreo();//no es necesario el + en el codigo de telefono
                EnviarCorreo enviarCorreo1 = new EnviarCorreo();
                enviarCorreo1.crearCorreo(pedido.getUsuarioRegistrado().getCorreo(),mensaje2,"Pedido");
                enviarCorreo1.start();
            }else{
                throw new DataNotFoundException();
            }

        }
        return pedidos;
    }


    @GetMapping("/getCarrito")
    public List<Map<String,Object>>getPedidos(Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        String idUsuario=user.get("sub").asString();
        List<Pedido> pedidos = pedidosRepositorio.findByUsuarioRegistrado(idUsuario);
        List<Map<String,Object>> respuesta=new ArrayList<>();
        for (int i=0;i<pedidos.size();i++){
            Pedido pedido=pedidos.get(i);
            Producto producto=pedido.getProducto();
            Map<String,Object> pedidocompleto=new HashMap<>();
            pedidocompleto.put("idPedido",pedido.getIdPedido()+"");
            //pedidocompleto.put("fecha",pedido.getFecha());
            //pedidocompleto.put("revisado",pedido.getRevisado().toString());
            //pedidocompleto.put("mensaje",pedido.getMensaje());
            System.out.println(pedido.getFecha());
            if(pedido.getFecha()==null){
                pedidocompleto.put("idProducto", producto.getIdProducto());
                pedidocompleto.put("nombre", producto.getNombre());
                pedidocompleto.put("precio", producto.getPrecio());
                pedidocompleto.put("descripcion", producto.getDescripcion());
                pedidocompleto.put("foto", "http://localhost/api/producto/image/"+producto.getIdProducto());
                respuesta.add(pedidocompleto);
            }

        }
        return respuesta;
    }
    @GetMapping("/chat/{id_pedido}")
    public String getChat(@PathVariable(value = "id_pedido")int idpedido){
        Pedido pedido=pedidosRepositorio.findById(idpedido).get();
        String telefono=pedido.getEmpresa().getTelefono();
        String producto=pedido.getProducto().getNombre().replaceAll(" ","%20");
        System.out.println(producto);
        return "https://wa.me/"+telefono+"?text=Hola%20estoy%20interesado%20en%20el%20producto:%20"+producto;//no es necesario el +
    }

}
