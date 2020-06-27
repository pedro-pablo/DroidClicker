package com.pedropablo.trabalhofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Pontuacao {

    private int id;
    private int usuarioId;
    private String usuarioNome;
    private int jogoId;
    private long pontos;

    private Pontuacao() {}

    private Pontuacao(String usuarioNome, long pontos) {
        this.usuarioNome = usuarioNome;
        this.pontos = pontos;
    }

    public String getUsuarioNome() {
        return this.usuarioNome;
    }

    public long getPontos() {
        return this.pontos;
    }

    public static Pontuacao novaPontuacao(int usuarioId, Jogo jogo) {
        Pontuacao pontuacao = new Pontuacao();
        pontuacao.usuarioId = usuarioId;
        pontuacao.jogoId = jogo.getId();
        try {
            pontuacao.pontos = (long) ((jogo.getCaixa() - 50) + jogo.getProdutosVendidos() + jogo.getProdutosFabricados() +
                    jogo.getProdutosEstoque() + (jogo.getUpgrade0() ? Jogo.UPGRADE0_PRECO : 0) + (jogo.getUpgrade1() ? Jogo.UPGRADE1_PRECO : 0) +
                    (jogo.getUpgrade2() ? Jogo.UPGRADE2_PRECO : 0) + (jogo.getUpgrade3() ? Jogo.UPGRADE3_PRECO : 0) + (jogo.getUpgrade4() ? Jogo.UPGRADE4_PRECO : 0) +
                    (jogo.getFuncionarios() * 1000) + jogo.getReceita() + (jogo.getLucro() * 100) - jogo.getCustoTotal());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return pontuacao;
    }

    public static ArrayList<Pontuacao> obterPontuacoes(Context context) {
        SQLiteDatabase db = JogoOpenHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT usuario.nome AS nome, pontos " +
                "FROM pontuacao " +
                "JOIN usuario " +
                "ON usuario.id = pontuacao.usuario_id " +
                "ORDER BY pontos DESC", null);

        ArrayList<Pontuacao> pontuacoes = new ArrayList<>();

        while (cursor.moveToNext()) {
            Pontuacao pontuacao = new Pontuacao(
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getLong(cursor.getColumnIndex("pontos"))
            );
            pontuacoes.add(pontuacao);
        }
        cursor.close();
        return pontuacoes;
    }

    public void inserir(Context context) {
        SQLiteDatabase db = JogoOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario_id", this.usuarioId);
        values.put("jogo_id", this.jogoId);
        values.put("pontos", this.pontos);
        db.insertOrThrow("pontuacao", null, values);
    }

}
