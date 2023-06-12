package com.apprecetascocina.fct.service;

import com.apprecetascocina.fct.dto.RecipesDTO;
import com.apprecetascocina.fct.dto.UsuariosDTO;
import com.apprecetascocina.fct.firebase.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImplRecipeService implements IRecipeService{

    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public List<RecipesDTO> list() {
        List<RecipesDTO> response = new ArrayList<>();
        RecipesDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(RecipesDTO.class);
                if(post != null) {
                    post.setId(Integer.parseInt(doc.getId()));
                    response.add(post);
                }
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public RecipesDTO listById(String id) {
        DocumentReference docRef = getCollection().document(String.valueOf(id));

        try {
            DocumentSnapshot document = docRef.get().get();
            if (document.exists()) {
                RecipesDTO recipe = document.toObject(RecipesDTO.class);
                if (recipe != null) {
                    recipe.setId(Integer.parseInt(document.getId()));
                    return recipe;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean add(RecipesDTO post) {
        String id = post.getId() + "";

        DocumentReference docRef = getCollection().document(id);
        Map<String, Object> docData = getDocData(post);
        ApiFuture<WriteResult> writeResultApiFuture = docRef.create(docData);

        try {
            if(null != writeResultApiFuture.get()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean edit(String id, RecipesDTO post) {
        Map<String, Object> docData = getDocData(post);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(docData);
        try {
            if(null != writeResultApiFuture.get()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean delete(String id) {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();
        try {
            if(null != writeResultApiFuture.get()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    // Obtener la coleccion de firebase
    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("recipes");
    }

    // Crea y devolve un mapa que contiene datos extraídos de un objeto RecipesDTO
    private Map<String, Object> getDocData(RecipesDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", post.getId());
        docData.put("nombre", post.getNombre());
        docData.put("ingredientes", post.getIngredientes());
        docData.put("pasos", post.getPasos());
        docData.put("tiempo", post.getTiempo());
        docData.put("userID", post.getUserID());
        return docData;
    }
}
