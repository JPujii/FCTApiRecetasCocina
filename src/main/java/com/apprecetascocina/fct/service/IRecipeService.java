package com.apprecetascocina.fct.service;

import com.apprecetascocina.fct.dto.RecipesDTO;


import java.util.List;

public interface IRecipeService {
    List<RecipesDTO> list();
    RecipesDTO listById(String id);
    boolean add(RecipesDTO post);
    boolean edit(String id, RecipesDTO post);
    boolean delete(String id);
}
