package com.example.proyectoacolitame.controladoresRest;

import com.auth0.jwt.interfaces.Claim;
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
@RequestMapping("/pedido")
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
    public Pedido guardarCarritoCompras(@RequestBody Map<String, Object> mapJson, Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        int idusuario=user.get("sub").asInt();

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

        return pedidosRepositorio.save(pedido);

    }
    @DeleteMapping("/borrarCarro/{id_pedido}")
    public void eliminarPedido(@PathVariable(value = "id_pedido")Integer idPedido){
        Pedido pedido=pedidosRepositorio.findById(idPedido).get();
        if(pedido.getFecha()==null){
            pedidosRepositorio.deleteById(idPedido);
        }
    }
    @PutMapping("/realizarPedido")
    public List<Pedido> realizarPedido(@RequestBody Map<String, Object> mapJson,Authentication authentication){
        Map<String, Claim> user = (Map<String, Claim>) authentication.getPrincipal();
        int idusuario=user.get("sub").asInt();
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        ArrayList<Integer> arreglo=(ArrayList<Integer>) mapJson.get("idpedidos");
        String mensaje=mapJson.get("mensaje").toString();
        List<Pedido> pedidos = new ArrayList<>();
        Mail mail=new Mail();
        //enviar correo a las empresas
        for(int i=0;i<arreglo.size();i++){
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
            mail.enviarMail(correo,"Nuevo Pedido",body);


        }
        return pedidos;
    }


    @GetMapping("/getCarrito/{id_usuario}")
    public List<Map<String,String>>getPedidos(@PathVariable(value = "id_usuario")Integer idUsuario){
        List<Pedido> pedidos = pedidosRepositorio.findByUsuarioRegistrado(idUsuario);
        List<Map<String,String>> respuesta=new ArrayList<>();
        for (int i=0;i<pedidos.size();i++){
            Pedido pedido=pedidos.get(i);
            Producto producto=pedido.getProducto();
            Map<String,String> pedidocompleto=new HashMap<>();
            pedidocompleto.put("idPedido",pedido.getIdPedido()+"");
            //pedidocompleto.put("fecha",pedido.getFecha());
            //pedidocompleto.put("revisado",pedido.getRevisado().toString());
            //pedidocompleto.put("mensaje",pedido.getMensaje());
            System.out.println(pedido.getFecha());
            if(pedido.getFecha()==null){
                pedidocompleto.put("idProducto", producto.getIdProducto()+"");
                pedidocompleto.put("nombre", producto.getNombre());
                pedidocompleto.put("precio", producto.getPrecio()+"");
                pedidocompleto.put("descripcion", producto.getDescripcion());
                respuesta.add(pedidocompleto);
            }

        }
        return respuesta;
    }
    @GetMapping("/chat/{id_pedido}")
    public String getChat(@PathVariable(value = "id_pedido")int idpedido){
        Pedido pedido=pedidosRepositorio.findById(idpedido).get();
        String telefono=pedido.getEmpresa().getTelefono();
        String producto=pedido.getProducto().getNombre();
        return "https://wa.me/"+telefono+"?text=Hola%20estoy%20interesado%20en%20el%20producto:%20"+producto;
    }

}