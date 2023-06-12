package com.apprecetascocina.fct.service;

import com.apprecetascocina.fct.dto.UsuariosDTO;

import java.util.List;

public interface IUsuarioService {
    List<UsuariosDTO> list();
    UsuariosDTO listById(String id);
    boolean add(UsuariosDTO post);
    boolean edit(String id, UsuariosDTO post);
    boolean delete(String id);
    boolean login(UsuariosDTO credenciales);
}
