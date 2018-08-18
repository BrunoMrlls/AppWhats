package com.iza.bruno.appwhats;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.iza.bruno.helpers.Permissao;
import com.iza.bruno.helpers.Preferencias;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtCodArea;
    private EditText edtCodPais;
    private Button btnCad;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1, this, permissoesNecessarias ); //verifica sem ha permissoes a serem pedida

        edtNome     = (EditText) findViewById(R.id.idEdtNome);
        edtTelefone = (EditText) findViewById(R.id.idEdtNumTel);
        edtCodArea  = (EditText) findViewById(R.id.idEdtCodArea);
        edtCodPais  = (EditText) findViewById(R.id.idEdtCodPais);
        btnCad      = (Button) findViewById(R.id.idBtnEntrar);

        SimpleMaskFormatter maskFormatterTel = new SimpleMaskFormatter( "N NNNN-NNNN" );
        MaskTextWatcher textWatcherTel = new MaskTextWatcher( edtTelefone , maskFormatterTel );
        edtTelefone.addTextChangedListener( textWatcherTel );

        SimpleMaskFormatter maskFormatterCodArea = new SimpleMaskFormatter( "NN" );
        MaskTextWatcher textWatcherCodArea = new MaskTextWatcher( edtCodArea , maskFormatterCodArea );
        edtCodArea.addTextChangedListener( textWatcherCodArea );

        SimpleMaskFormatter maskFormatterCodPais = new SimpleMaskFormatter( "+NN" );
        MaskTextWatcher textWatcherCodPais = new MaskTextWatcher( edtCodPais , maskFormatterCodPais );
        edtCodPais.addTextChangedListener( textWatcherCodPais );

        btnCad.setOnClickListener( onClickBtnCad() );

    }

    private View.OnClickListener onClickBtnCad() {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome     = edtNome.getText().toString();
                String codPais  = edtCodPais.getText().toString().replace("+","");
                String codArea  = edtCodArea.getText().toString();
                String telefone = edtTelefone.getText().toString().
                        replaceAll(" ", "").replace("-","");

                if ( nome.isEmpty() ){
                    Toast.makeText(LoginActivity.this,
                            "Ops, Qual e o seu nome ?", Toast.LENGTH_SHORT).show();
                    edtNome.requestFocus();
                    return;

                } else if (codPais.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Pareçe que voce ensqueçeu de preencher o codigo do pais ...",
                            Toast.LENGTH_SHORT).show();
                    edtCodPais.requestFocus();
                    return;
                } else if ( codArea.isEmpty() ){
                    Toast.makeText(LoginActivity.this,
                            "Pareçe que voce esqueceu de preencher o codigo de area ...",
                            Toast.LENGTH_SHORT).show();
                    edtCodArea.requestFocus();
                    return;
                } else if ( telefone.isEmpty() ) {
                    Toast.makeText(LoginActivity.this,
                            "Parece que voce esqueceo o numero do telefone",
                            Toast.LENGTH_SHORT).show();
                    edtTelefone.requestFocus();
                    return;
                }

                String telefoneCompleto = codPais + codArea + telefone;

                Random random = new Random();
                String token  = String.valueOf( random.nextInt( 9999 - 1000 ) + 1000 );

//                Log.i("TELEFONE", telefoneCompleto);
//                Log.i("TOKEN", token );

                //salva dados de cadastro
                Preferencias preferencias = new Preferencias( getApplicationContext() );
                preferencias.writeUsuarioPreferences( nome , telefone , token ); //salva no shared preferences

//                HashMap< String , String> dadosUsu = preferencias.readUsuarioPreferencias(); //var do tipo Hashmap pra rec. return
//                Log.i("token" , "t:"+dadosUsu.get( "token" ) );
                enviaSMS( "+"+telefoneCompleto , "Seja Bem vindo, seu token e: "+token );

            }
        };
    }

    private boolean enviaSMS( String telefone , String text ){

        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, text, null, null);
            return true;

        } catch ( Exception e ) {

            e.printStackTrace();
            return false;

        }

    };

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult( requestCode , permissions , grantResults);

        for ( int resultado : grantResults ){
            if ( resultado == PackageManager.PERMISSION_DENIED)
                    alertaValidacaoPermissao();
        }

    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( this );
        alertDialog.setTitle( " Permissao negada! " );
        alertDialog.setMessage("para utilizar esse app, e necessario aceitar as permissoes!" );
        alertDialog.setPositiveButton("Ok, na proxima vez aceitarei.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.setCancelable(true);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

}
