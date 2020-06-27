package com.pedropablo.trabalhofinal;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PontuacaoAdapter extends BaseAdapter {

    private ArrayList<Pontuacao> pontuacoes;
    private Context context;

    public PontuacaoAdapter(ArrayList<Pontuacao> pontuacoes, Context context) {
        this.pontuacoes = pontuacoes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pontuacoes.size();
    }

    @Override
    public Object getItem(int position) {
        return pontuacoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(this.context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        Pontuacao pontuacao = pontuacoes.get(position);
        textView.setText(String.format(context.getResources().getString(R.string.pontuacao_item_formato),
                position + 1, pontuacao.getUsuarioNome(),
                NumberFormat.getNumberInstance(Locale.getDefault()).format(pontuacao.getPontos())));
        return textView;
    }
}
