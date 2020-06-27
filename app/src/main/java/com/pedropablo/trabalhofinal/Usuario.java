package com.pedropablo.trabalhofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Usuario {

    private int id;
    private String nome;
    private byte[] senha;

    public Usuario(String nome, byte[] senha) {
        this.nome = nome;
        this.senha = senha;
    }

    private Usuario(int id, String nome, byte[] senha) {
        this(nome, senha);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public byte[] getSenha() {
        return senha;
    }

    public Usuario cadastrar(Context context) {
        SQLiteDatabase db = JogoOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("senha", senha);
        db.insertOrThrow("usuario", null, values);
        return buscarUsuario(nome, context);
    }

    public static Usuario buscarUsuario(int id, Context context) {
        SQLiteDatabase db = JogoOpenHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, nome, senha FROM usuario WHERE id = ?",
                new String[] {Integer.toString(id)});
        return preencherUsuario(cursor);
    }

    public static Usuario buscarUsuario(String nome, Context context) {
        SQLiteDatabase db = JogoOpenHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, nome, senha FROM usuario WHERE nome = ?",
                new String[] {nome});
        return preencherUsuario(cursor);
    }

    private static Usuario preencherUsuario(Cursor cursor) {
        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = new Usuario(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getBlob(cursor.getColumnIndex("senha"))
            );
        }
        return usuario;
    }

}
