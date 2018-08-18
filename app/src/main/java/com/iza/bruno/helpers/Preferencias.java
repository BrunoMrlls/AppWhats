package com.iza.bruno.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by bruno on 10/12/17.
 */

public class Preferencias {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String ARQUIVO_PREFERENCIA = "arquivoPreferencia";
    private static short MODE = 0;
    private static String CHAVE_NOME = "nome";
    private static String CHAVE_TELEFONE = "telefone";
    private static String CHAVE_TOKEN = "token";


    public Preferencias( Context contextoParametro ) {

        context = contextoParametro;
        sharedPreferences = context.getSharedPreferences(ARQUIVO_PREFERENCIA, MODE);
        editor = sharedPreferences.edit();

    }
    public void writeUsuarioPreferences( String nome, String telefone, String token ){

        editor.putString( CHAVE_NOME , nome );
        editor.putString( CHAVE_TELEFONE, telefone );
        editor.putString( CHAVE_TOKEN, token);
        editor.commit();

    }

    public HashMap<String, String> readUsuarioPreferencias(){

            HashMap< String , String > dadosCadUsu = new HashMap<>();
            dadosCadUsu.put( CHAVE_NOME , sharedPreferences.getString( CHAVE_NOME, null ) );
            dadosCadUsu.put( CHAVE_TELEFONE , sharedPreferences.getString( CHAVE_TELEFONE , null ) );
            dadosCadUsu.put( CHAVE_TOKEN , sharedPreferences.getString( CHAVE_TOKEN, null ) );

        return dadosCadUsu;
    };

}
