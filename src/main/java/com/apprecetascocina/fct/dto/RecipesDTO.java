package com.apprecetascocina.fct.dto;

import lombok.Data;

@Data
public class RecipesDTO {

    private int id;
    private String nombre;
    private String ingredientes;
    private String pasos;
    private int tiempo;
    private String userID;

}
