package com.pedropablo.trabalhofinal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pedropablo.trabalhofinal.Hashing;
import com.pedropablo.trabalhofinal.JogoOpenHelper;
import com.pedropablo.trabalhofinal.R;
import com.pedropablo.trabalhofinal.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsuario;
    private EditText editTextSenha;
    private Button btnCadastrar;
    private Button btnEntrar;
    private Button btnPontuacao;
    private JogoOpenHelper jogoOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextSenha = findViewById(R.id.editTextSenha);

        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsuario = obterNomeUsuario();
                String senha = obterSenhaUsuario();
                if (nomeUsuario == null || senha == null) {
                    return;
                }

                Context context = getApplicationContext();
                Usuario usuario = Usuario.buscarUsuario(nomeUsuario, context);
                if (usuario != null) {
                    Toast.makeText(v.getContext(), context.getString(R.string.usuario_cadastro_existe), Toast.LENGTH_SHORT).show();
                } else {
                    usuario = new Usuario(nomeUsuario, Hashing.gerarHash(senha));
                    usuario = usuario.cadastrar(context);
                    if (usuario != null) {
                        Toast.makeText(v.getContext(), context.getString(R.string.usuario_cadastro_sucesso), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), context.getString(R.string.usuario_cadastro_falha), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsuario = obterNomeUsuario();
                String senha = obterSenhaUsuario();
                if (nomeUsuario == null || senha == null) {
                    return;
                }

                Context context = getApplicationContext();
                Usuario usuario = Usuario.buscarUsuario(nomeUsuario, context);
                boolean valido = usuario != null && Hashing.compararHash(senha, usuario.getSenha());
                if (valido) {
                    Intent intent = new Intent(MainActivity.this, JogoActivity.class);
                    intent.putExtra("usuarioId", usuario.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(v.getContext(), context.getString(R.string.erro_login_invalido), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPontuacao = findViewById(R.id.btnPontuacao);
        btnPontuacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PontuacaoActivity.class);
                startActivity(intent);
            }
        });

    }

    private String obterNomeUsuario() {
        String nome = editTextUsuario.getText().toString();
        if (nome.isEmpty()) {
            Toast.makeText(this, this.getString(R.string.erro_usuario_vazio), Toast.LENGTH_SHORT).show();
            return null;
        }
        return nome;
    }

    private String obterSenhaUsuario() {
        String senha = editTextSenha.getText().toString();
        if (senha.isEmpty()) {
            Toast.makeText(this, this.getString(R.string.erro_senha_vazio), Toast.LENGTH_SHORT).show();
            return null;
        }
        return senha;
    }

}
