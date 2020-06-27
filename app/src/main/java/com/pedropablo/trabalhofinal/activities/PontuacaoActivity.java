package com.pedropablo.trabalhofinal.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.pedropablo.trabalhofinal.Pontuacao;
import com.pedropablo.trabalhofinal.PontuacaoAdapter;
import com.pedropablo.trabalhofinal.R;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PontuacaoActivity extends AppCompatActivity {

    private ListView listViewPontuacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pontuacao);

        listViewPontuacao = findViewById(R.id.listViewPontuacao);
        ArrayList<Pontuacao> pontuacoes = Pontuacao.obterPontuacoes(getApplicationContext());
        if (!pontuacoes.isEmpty()) {
            listViewPontuacao.setAdapter(new PontuacaoAdapter(pontuacoes, this));
        }
    }
}
