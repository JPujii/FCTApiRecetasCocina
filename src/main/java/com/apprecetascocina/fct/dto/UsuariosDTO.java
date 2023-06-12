package com.apprecetascocina.fct.dto;

import lombok.Data;

@Data
public class UsuariosDTO {
    private String id;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String fecha_nacimiento;
    private String salt;
}
