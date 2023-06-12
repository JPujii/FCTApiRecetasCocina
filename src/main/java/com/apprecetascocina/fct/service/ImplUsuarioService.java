package com.apprecetascocina.fct.service;

import com.apprecetascocina.fct.dto.UsuariosDTO;
import com.apprecetascocina.fct.firebase.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class ImplUsuarioService implements IUsuarioService{
    @Autowired
    private FirebaseInitializer firebase;

    private static final int SALT_LENGTH = 16;

    @Override
    public List<UsuariosDTO> list() {
        List<UsuariosDTO> response = new ArrayList<>();
        UsuariosDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(UsuariosDTO.class);
                if(post != null) {
                    post.setId(doc.getId());
                    response.add(post);
                }
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UsuariosDTO listById(String id) {
        DocumentReference docRef = getCollection().document(String.valueOf(id));

        try {
            DocumentSnapshot document = docRef.get().get();
            if (document.exists()) {
                UsuariosDTO usuario = document.toObject(UsuariosDTO.class);
                if (usuario != null) {
                    usuario.setId(document.getId());
                    return usuario;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean add(UsuariosDTO post) {
        String id = post.getEmail();

        DocumentReference docRef = getCollection().document(id);

        // Encriptar la contraseña
        String salt = generateSalt();
        post.setSalt(salt);
        String encryptedPassword = null;
        try {
            encryptedPassword = hashPassword(post.getPassword(), salt);
            post.setPassword(encryptedPassword);

            Map<String, Object> docData = getDocData(post);
            ApiFuture<WriteResult> writeResultApiFuture = docRef.set(docData); // Utilizar set() en lugar de create()
            try {
                if(null != writeResultApiFuture.get()){
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            } catch (Exception e) {
                return Boolean.FALSE;
            }

        } catch (NoSuchAlgorithmException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean edit(String id, UsuariosDTO post) {
        String salt = generateSalt();
        post.setSalt(salt);
        String encryptedPassword;
        try {
            encryptedPassword = hashPassword(post.getPassword(), salt);
            post.setPassword(encryptedPassword);
        } catch (NoSuchAlgorithmException e) {
            return Boolean.FALSE;
        }

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

    @Override
    public boolean login(UsuariosDTO credenciales) {
        UsuariosDTO usuario = listById(credenciales.getEmail());
        if(usuario != null) {

            String encryptedPassword;
            try {
                encryptedPassword = hashPassword(credenciales.getPassword(), usuario.getSalt());
                credenciales.setPassword(encryptedPassword);
            } catch (NoSuchAlgorithmException e) {
                return Boolean.FALSE;
            }

            if((credenciales.getEmail().equals("josepujante@gmail.com") && credenciales.getPassword().equals(usuario.getPassword())) || ((credenciales.getEmail().equals("raumuro@gmail.com") && credenciales.getPassword().equals(usuario.getPassword()))) || ((credenciales.getEmail().equals("jmabarca@gmail.com") && credenciales.getPassword().equals(usuario.getPassword())))) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            // El usuario no existe
            return Boolean.FALSE;
        }

    }

    // Obtener la coleccion de firebase
    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("Usuarios");
    }

    // Crea y devolve un mapa que contiene datos extraídos de un objeto UsuariosDTO
    private Map<String, Object> getDocData(UsuariosDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", post.getEmail());
        docData.put("nombre", post.getNombre());
        docData.put("apellidos", post.getApellidos());
        docData.put("email", post.getEmail());
        docData.put("password", post.getPassword());
        docData.put("fecha_nacimiento", post.getFecha_nacimiento());
        docData.put("salt", post.getSalt());
        return docData;
    }

    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes());
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
