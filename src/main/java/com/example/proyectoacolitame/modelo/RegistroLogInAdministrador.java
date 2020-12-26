package com.example.proyectoacolitame.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "registro_loginadministrador")
public class RegistroLogInAdministrador {
    @Id
    @Column(name = "id_registro_login_administrador")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idRegistroLogInAdministrador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    @JsonBackReference
    private AdministradorEmpresa administradorEmpresa;

    @Column (name = "date")
    private String date;

    public int getIdRegistroLogInAdministrador() {
        return idRegistroLogInAdministrador;
    }

    public void setIdRegistroLogInAdministrador(int idRegistroLogInAdministrador) {
        this.idRegistroLogInAdministrador = idRegistroLogInAdministrador;
    }

    public AdministradorEmpresa getAdministradorEmpresa() {
        return administradorEmpresa;
    }

    public void setAdministradorEmpresa(AdministradorEmpresa administradorEmpresa) {
        this.administradorEmpresa = administradorEmpresa;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
