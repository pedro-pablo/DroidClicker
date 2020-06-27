package com.pedropablo.trabalhofinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JogoOpenHelper extends SQLiteOpenHelper {

    private static JogoOpenHelper jogoOpenHelper;

    private static final String DATABASE_NAME = "Jogo";

    private static final int DATABASE_VERSION = 1;

    private static final String USUARIO_CREATE_SQL = "CREATE TABLE usuario (" +
            "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT NOT NULL," +
            "senha BLOB NOT NULL);";

    private static final String JOGO_CREATE_SQL = "CREATE TABLE jogo (" +
            "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "usuario_id INTEGER NOT NULL," +
            "ativo INTEGER NOT NULL DEFAULT 1," +
            "caixa REAL NOT NULL DEFAULT 50," +
            "receita REAL NOT NULL DEFAULT 0," +
            "custo_total REAL NOT NULL DEFAULT 0," +
            "custo_atual REAL NOT NULL DEFAULT 2," +
            "preco_atual REAL NOT NULL DEFAULT 3," +
            "capacidade_estoque INTEGER NOT NULL DEFAULT 50," +
            "produtos_estoque INTEGER NOT NULL DEFAULT 0," +
            "produtos_vendidos INTEGER NOT NULL DEFAULT 0," +
            "produtos_fabricados INTEGER NOT NULL DEFAULT 0," +
            "funcionarios INTEGER NOT NULL DEFAULT 0," +
            "upgrade_0 INTEGER NOT NULL DEFAULT 0," +
            "upgrade_1 INTEGER NOT NULL DEFAULT 0," +
            "upgrade_2 INTEGER NOT NULL DEFAULT 0," +
            "upgrade_3 INTEGER NOT NULL DEFAULT 0," +
            "upgrade_4 INTEGER NOT NULL DEFAULT 0," +
            "meta_0 INTEGER NOT NULL DEFAULT 0," +
            "meta_1 INTEGER NOT NULL DEFAULT 0," +
            "meta_8 INTEGER NOT NULL DEFAULT 0," +
            "meta_2 INTEGER NOT NULL DEFAULT 0," +
            "meta_3 INTEGER NOT NULL DEFAULT 0," +
            "meta_4 INTEGER NOT NULL DEFAULT 0," +
            "meta_5 INTEGER NOT NULL DEFAULT 0," +
            "meta_6 INTEGER NOT NULL DEFAULT 0," +
            "meta_7 INTEGER NOT NULL DEFAULT 0," +
            "meta_9 INTEGER NOT NULL DEFAULT 0," +
            "FOREIGN KEY (usuario_id) REFERENCES usuario(id));";

    private static final String PONTUACAO_CREATE_SQL = "CREATE TABLE pontuacao (" +
            "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "usuario_id INTEGER NOT NULL," +
            "jogo_id INTEGER NOT NULL," +
            "pontos INTEGER NOT NULL," +
            "FOREIGN KEY (usuario_id) REFERENCES usuario(id)," +
            "FOREIGN KEY (jogo_id) REFERENCES jogo(id));";


    public static JogoOpenHelper getInstance(Context context) {
        if (jogoOpenHelper == null) {
            jogoOpenHelper = new JogoOpenHelper(context.getApplicationContext());
        }
        return jogoOpenHelper;
    }

    private JogoOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USUARIO_CREATE_SQL);
        db.execSQL(JOGO_CREATE_SQL);
        db.execSQL(PONTUACAO_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS jogo");
        db.execSQL("DROP TABLE IF EXISTS pontuacao");
        onCreate(db);
    }
}
