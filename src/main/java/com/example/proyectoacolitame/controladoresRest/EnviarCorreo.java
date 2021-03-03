package com.example.proyectoacolitame.controladoresRest;

import com.example.proyectoacolitame.mail.Mail;

public class EnviarCorreo extends Thread{
    private String correo1;

    private String mensaje;

    private String asunto;
    public EnviarCorreo() {
    }
    public void crearCorreo(String correo1,String mensaje1,String asunto){
        this.correo1=correo1;

        this.mensaje=mensaje1;

        this.asunto=asunto;
    }

    @Override
    public void run() {
        Mail mail=new Mail();
        mail.enviarMail(correo1,asunto,mensaje);

    }
}
