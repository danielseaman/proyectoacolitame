package com.example.proyectoacolitame.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "registro_loginusuario")
public class RegistroLogInUsuario {
    @Id
    @Column(name = "id_registro_login_usuario")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idRegistroLogInUsuario;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @JsonBackReference
    private UsuarioRegistrado usuarioRegistrado;

    @Column (name = "date")
    private String date;

    public int getIdRegistroLogInUsuario() {
        return idRegistroLogInUsuario;
    }

    public void setIdRegistroLogInUsuario(int idRegistroLogInUsuario) {
        this.idRegistroLogInUsuario = idRegistroLogInUsuario;
    }

    public UsuarioRegistrado getUsuarioRegistrado() {
        return usuarioRegistrado;
    }

    public void setUsuarioRegistrado(UsuarioRegistrado usuarioRegistrado) {
        this.usuarioRegistrado = usuarioRegistrado;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
