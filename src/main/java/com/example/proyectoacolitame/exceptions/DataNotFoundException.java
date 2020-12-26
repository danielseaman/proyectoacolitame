package com.example.proyectoacolitame.exceptions;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {
        super("No data found");
    }
}
