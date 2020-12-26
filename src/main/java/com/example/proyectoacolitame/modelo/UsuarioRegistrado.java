package com.example.proyectoacolitame.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "usuario_registrado")
public class UsuarioRegistrado {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario")
    public int idUsuario;
    @Column(name = "clave")
    public String clave;
    @Column(name = "correo")
    public String correo;
    @Column(name = "telefono")
    public String telefono;
    @Column(name = "nombre")
    public String nombre;

    @Column(name = "verificado")
    public boolean verificado;


    @Column(name = "bytes_foto")
    public byte[] foto;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuarioRegistrado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuarioRegistrado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calificacion> calificacions;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuarioRegistrado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos;//no deja arraylist no se porque

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "usuarioRegistrado", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Respuesta> respuestas;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuarioRegistrado", cascade = CascadeType.ALL, orphanRemoval = true)
    List<RegistroLogInUsuario> registroLogInUsuariosList;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Calificacion> getCalificacions() {
        return calificacions;
    }

    public void setCalificacions(List<Calificacion> calificacions) {
        this.calificacions = calificacions;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<RegistroLogInUsuario> getRegistroLogInUsuariosList() {
        return registroLogInUsuariosList;
    }

    public void setRegistroLogInUsuariosList(List<RegistroLogInUsuario> registroLogInUsuariosList) {
        this.registroLogInUsuariosList = registroLogInUsuariosList;
    }
}
