package com.example.proyectoacolitame.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "administrador_empresa")
public class AdministradorEmpresa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_administrador")
    private String idAdministrador;
    @Column(name = "correo")
    private String correo;
    @Column(name ="clave")
    private String clave;
    @Column(name = "verificado")
    private boolean verificado;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    @JsonBackReference
    Empresa empresa;

    @JsonManagedReference
    @OneToMany(mappedBy = "administradorEmpresa", cascade = CascadeType.ALL, orphanRemoval = true)
    List<RegistroLogInAdministrador> registroLogInAdministradorList;

    public String getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(String idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<RegistroLogInAdministrador> getRegistroLogInAdministradorList() {
        return registroLogInAdministradorList;
    }

    public void setRegistroLogInAdministradorList(List<RegistroLogInAdministrador> registroLogInAdministradorList) {
        this.registroLogInAdministradorList = registroLogInAdministradorList;
    }
}
