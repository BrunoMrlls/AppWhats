package com.iza.bruno.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.iza.bruno.cfg.ConfiguracaoFirebase;

/**
 * Created by bruno on 10/03/18.
 */

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;

    public Usuario(){

    }

    //temporariamente aqui, depois criar um "Repository"
    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("usuario").child( getId() ).setValue( this ); //this passa o objto usuario completo pro firebase
    }

    @Exclude //p/ quando o firebase salvar o objt, nao salvar o Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
