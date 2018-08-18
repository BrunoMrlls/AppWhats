package com.iza.bruno.appwhats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.iza.bruno.cfg.ConfiguracaoFirebase;
import com.iza.bruno.model.Usuario;

public class LoginActivity2 extends AppCompatActivity {
    private ProgressDialog mProgress;
    private TextView txtCadConta;
    private Usuario usuario;
    private EditText edtemail, edtpassword;
    private Button btnok;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        verificarUsuarioLogado();

        txtCadConta = (TextView)findViewById( R.id.txtcadastrar_conta );
        edtemail = (EditText) findViewById( R.id.edtemail );
        edtpassword = (EditText) findViewById( R.id.edtsenha );
        btnok = (Button) findViewById( R.id.btnok );

        txtCadConta.setOnClickListener( chamarCadastroUsuario() );
        btnok.setOnClickListener( autenticarUsuario() );
    }

    private void verificarUsuarioLogado() {
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        if (auth.getCurrentUser() != null){
            chamarTelaPrincipal();
        }
    }

    private View.OnClickListener autenticarUsuario() {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( edtemail.getText().toString().isEmpty() || edtpassword.getText().toString().isEmpty() ) {
                    return;
                }
                mostrarTeladeEspera();

                usuario = new Usuario();
                usuario.setEmail( edtemail.getText().toString() );
                usuario.setSenha( edtpassword.getText().toString() );
                auth = ConfiguracaoFirebase.getFirebaseAuth();
                auth.signInWithEmailAndPassword(
                        usuario.getEmail().toString(),
                        usuario.getSenha().toString()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            chamarTelaPrincipal();
                            finish();
                        }else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(LoginActivity2.this, "Usuario invalido.", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(LoginActivity2.this, "Senha invalida", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        fecharTeladeEspera();
                    }
                });

            }
        };
    }

    private void chamarTelaPrincipal() {
        Intent intent = new Intent( getApplicationContext(), MainActivity.class );
        startActivity( intent );
    }

    private void fecharTeladeEspera() {
        mProgress.dismiss();
    }

    private void mostrarTeladeEspera() {
        mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage("Autenticando aguarde...");
        mProgress.show();
    }

    private View.OnClickListener chamarCadastroUsuario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext() , CadastroUsuarioActivity.class );
                startActivity( intent );
            }
        };
    }

}
