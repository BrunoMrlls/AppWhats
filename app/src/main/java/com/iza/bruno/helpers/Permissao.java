package com.iza.bruno.helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 11/12/17.
 */

public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity , String[] permissoes ){

        if (Build.VERSION.SDK_INT >= 23) { //se o android for 23 >

            List<String> listaPermissoes = new ArrayList<String>();
            //percorre as permissoes, verificando  uma a uma se tem permissao liberada
            for(String permissao : permissoes){
                Boolean validaPermissao = ContextCompat.checkSelfPermission( activity , permissao ) == PackageManager.PERMISSION_GRANTED;
                if ( !validaPermissao ) listaPermissoes.add(permissao);
            }
            //se a lista de permissoes esta vazia nao e necessario solicitar permissao para a activity
            if ( listaPermissoes.isEmpty() ) return true;
            //solicita permissao

            String[] novasPermissoes = new String[ listaPermissoes.size() ];
            listaPermissoes.toArray( novasPermissoes );

            ActivityCompat.requestPermissions( activity , novasPermissoes , requestCode );

        }  //nao esquecer de inserir evento  alertaValidacaoPermissao na activity que chamou utilizando onRequestPermissionsResult

        return true;
    }


}
