package com.example.proyectoacolitame.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pedido")
    private Integer idPedido;
    @Column(name="fecha")
    private String fecha;
    @Column(name="mensaje")
    private String mensaje;
    @Column(name="revisado")
    private Boolean revisado;//true si la empresa ua reviso el pedido, falso aun no*/
    @JsonBackReference
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @JsonBackReference
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private UsuarioRegistrado usuarioRegistrado;

    @JsonBackReference
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public UsuarioRegistrado getUsuarioRegistrado() {
        return usuarioRegistrado;
    }

    public void setUsuarioRegistrado(UsuarioRegistrado usuarioRegistrado) {
        this.usuarioRegistrado = usuarioRegistrado;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
