package com.iza.bruno.appwhats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.iza.bruno.cfg.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {
    private Button btnsair;
    private Toolbar toolbar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsair = (Button) findViewById(R.id.btnsair);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("AppWhats");
        setSupportActionBar(toolbar);
    }

    public void deslogarUsuario(){
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        auth.signOut();

        Intent intent = new Intent( MainActivity.this, LoginActivity2.class);
        startActivity( intent );

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu );

        return true; //sobre escreve metodo com o meu proprio menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_menu_main_sair:
                deslogarUsuario();
                return true;
            case R.id.item_menu_main_config:
                Toast.makeText(this, "Configuraçoes", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
