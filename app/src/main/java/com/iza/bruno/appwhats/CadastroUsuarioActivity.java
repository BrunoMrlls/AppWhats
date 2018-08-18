package com.iza.bruno.appwhats;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.iza.bruno.cfg.ConfiguracaoFirebase;
import com.iza.bruno.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnCadastrar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtNome  = (EditText) findViewById(R.id.edtnome);
        edtEmail = (EditText) findViewById(R.id.edtemail);
        edtSenha = (EditText) findViewById(R.id.edtsenha);
        btnCadastrar = (Button) findViewById(R.id.btncadastrar);

        btnCadastrar.setOnClickListener( cadastrarUsuario() );

    }

    private View.OnClickListener cadastrarUsuario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Usuario usuario = new Usuario();
                usuario.setNome( edtNome.getText().toString() );
                usuario.setEmail( edtEmail.getText().toString() );
                usuario.setSenha( edtSenha.getText().toString() );

                firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

                firebaseAuth.createUserWithEmailAndPassword(
                        usuario.getEmail().toString(),
                        usuario.getSenha().toString()).addOnCompleteListener(
                        CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //ao concluir verificar se realmente foi feito cad. usu
                                if ( task.isSuccessful()  ) {
                                    Toast.makeText(CadastroUsuarioActivity.this,
                                         "Cadastro realizado com sucesso.",
                                          Toast.LENGTH_SHORT).show();
                                    usuario.setId( task.getResult().getUser().getUid().toString() );
                                    usuario.salvar();
                                    firebaseAuth.signOut();
                                    finish();
                                } else {
                                    String xErro = "";
                                    try {  //docs erros https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth#createUserWithEmailAndPassword(java.lang.String,%20java.lang.String)
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        xErro = "Senha muito fraca, senha precisa de no minimo de 6 digitos.";
                                        e.printStackTrace();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        xErro = "Email mal formado.";
                                        e.printStackTrace();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        xErro = "Usuario ja existente.";
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        xErro = "Erro inesperado, tente novamente.";
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(CadastroUsuarioActivity.this, xErro,
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }

                );

            }
        };
    }


}
