package com.apprecetascocina.fct.controller;

import com.apprecetascocina.fct.dto.RecipesDTO;
import com.apprecetascocina.fct.dto.UsuariosDTO;
import com.apprecetascocina.fct.service.ImplRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
@CrossOrigin
public class RecipesController {
    @Autowired
    private ImplRecipeService service;

    @GetMapping(value = "/creditos")
    public String greet(){
        return  "Proyecto final de grado superior realizado por Jose Manuel Guardiola Abarca, Raul Muro Morcillo y Jose Pujante Ruiz";
    }

    @GetMapping(value = "/list")
    public ResponseEntity<java.util.List<RecipesDTO>> list(){
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }

    @GetMapping(value ="/list/{id}")
    public ResponseEntity<RecipesDTO> listById(@PathVariable(value = "id") String id) { return new ResponseEntity<>(service.listById(id), HttpStatus.OK);}

    @PostMapping(value = "/add")
    public ResponseEntity<Boolean> add(@RequestBody RecipesDTO post){
        return new ResponseEntity<>(service.add(post), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Boolean> edit(@PathVariable(value = "id") String id, @RequestBody RecipesDTO post){
        return new ResponseEntity<>(service.edit(id,post), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "id") String id){
        return  new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }
}
