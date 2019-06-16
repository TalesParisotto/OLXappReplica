package com.example.olxapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.olxapp.R;
import com.example.olxapp.helper.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail,campoSenha;
    private Switch tipoAcesso;

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(!email.isEmpty()){
                    if(!senha.isEmpty()){

                        if (tipoAcesso.isChecked()){

                            autenticacao.createUserWithEmailAndPassword(
                                    email,senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CadastroActivity.this,
                                                "Cadastro com sucesso", Toast.LENGTH_SHORT).show();
                                    }else {

                                        String erroExecao = "";
                                        try{
                                            throw task.getException();
                                        }catch (Exception e){
                                            erroExecao = "erro mas lembra de colocar as outras exeçoes do android tb";
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(CadastroActivity.this, erroExecao, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else {
                            autenticacao.signInWithEmailAndPassword(
                                    email,senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        Toast.makeText(CadastroActivity.this,
                                                "Logado com sucesso", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),AnunciosActivity.class));

                                    }else {
                                        Toast.makeText(CadastroActivity.this, "Erro ao logar", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }

                    }else {
                        Toast.makeText(CadastroActivity.this,
                                "Preencha a senha", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha o email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void inicializarComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
    }
}
