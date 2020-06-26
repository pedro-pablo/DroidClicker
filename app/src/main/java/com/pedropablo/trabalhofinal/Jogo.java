package com.pedropablo.trabalhofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Random;

public class Jogo {

    private int FUNCIONARIO_DELAY_PADRAO = 2500;

    private int id;
    private int usuarioId;
    private int ativo;
    private float caixa;
    private float receita;
    private float custoTotal;
    private float precoAtual;
    private float custoAtual;
    private int capacidadeEstoque;
    private int produtosEstoque;
    private int produtosFabricados;
    private int produtosVendidos;
    private int funcionarios;

    /**
     * Aumenta em 50 a capacidade do estoque.
     */
    private int upgrade0;
    /**
     * Aumenta a produtividade dos funcionários em 25%.
     */
    private int upgrade1;
    /**
     * Aumenta em 200 a capacidade do estoque.
     */
    private int upgrade2;
    /**
     * Aumenta a produtividade dos funcionários em 40%.
     */
    private int upgrade3;
    /**
     * Reduz o preço de contratação de novos funcionários
     * (de 25% sobre o preço dos funcionários atuais para 10%)
     */
    private int upgrade4;


    private int meta0;
    private int meta1;
    private int meta2;
    private int meta3;
    private int meta4;
    private int meta5;
    private int meta6;
    private int meta7;
    private int meta8;
    private int meta9;

    public int getId() {
        return id;
    }

    public float getCaixa() {
        return caixa;
    }

    public float getReceita() {
        return receita;
    }

    public float getCustoTotal() {
        return custoTotal;
    }

    public float getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(float precoAtual) {
        this.precoAtual = precoAtual;
    }

    public float getCustoAtual() {
        return custoAtual;
    }

    public int getCapacidadeEstoque() {
        return capacidadeEstoque;
    }

    public int getProdutosEstoque() {
        return produtosEstoque;
    }

    public int getProdutosVendidos() {
        return produtosVendidos;
    }

    public int getProdutosFabricados() {
        return produtosFabricados;
    }

    public int getFuncionarios() {
        return funcionarios;
    }

    public float getLucroProduto() {
        return this.precoAtual - this.custoAtual;
    }

    public boolean getAtivo() {
        return ativo == 1;
    }

    public boolean getUpgrade0() {
        return upgrade0 == 1;
    }

    public boolean getUpgrade1() {
        return upgrade1 == 1;
    }

    public boolean getUpgrade2() {
        return upgrade2 == 1;
    }

    public boolean getUpgrade3() {
        return upgrade3 == 1;
    }

    public boolean getUpgrade4() {
        return upgrade4 == 1;
    }

    public Jogo(int id, int usuarioId, int ativo, float caixa, float receita, float custoTotal,
                float precoAtual, float custoAtual, int capacidadeEstoque, int produtosEstoque,
                int produtosVendidos, int produtosProduzidos, int funcionarios, int upgrade0, int upgrade1, int upgrade2,
                int upgrade3, int upgrade4, int meta0, int meta1, int meta2, int meta3, int meta4,
                int meta5, int meta6, int meta7, int meta8, int meta9) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.ativo = ativo;
        this.caixa = caixa;
        this.receita = receita;
        this.custoTotal = custoTotal;
        this.precoAtual = precoAtual;
        this.custoAtual = custoAtual;
        this.capacidadeEstoque = capacidadeEstoque;
        this.produtosEstoque = produtosEstoque;
        this.produtosVendidos = produtosVendidos;
        this.produtosFabricados = produtosProduzidos;
        this.funcionarios = funcionarios;
        this.upgrade0 = upgrade0;
        this.upgrade1 = upgrade1;
        this.upgrade2 = upgrade2;
        this.upgrade3 = upgrade3;
        this.upgrade4 = upgrade4;
        this.meta0 = meta0;
        this.meta1 = meta1;
        this.meta2 = meta2;
        this.meta3 = meta3;
        this.meta4 = meta4;
        this.meta5 = meta5;
        this.meta6 = meta6;
        this.meta7 = meta7;
        this.meta8 = meta8;
        this.meta9 = meta9;
    }

    private int getDemanda() {
        Random rng = new Random();
        float valorMercado = custoAtual * (0.2f + rng.nextFloat() * 0.6f);
        //TODO: equilibrar fórmula de demanda
        int prod = Math.round((valorMercado / precoAtual) * capacidadeEstoque * rng.nextFloat() * 0.7f);
        System.out.println(prod);
        return prod;
    }

    public float getPrecoFuncionario() {
        return 1250f + (1250f * ((float)this.funcionarios / 100f));
    }

    public int getFuncionarioDelay() {
        int delay = FUNCIONARIO_DELAY_PADRAO;
        if (getUpgrade1()) {
            delay -= delay * 0.25;
        }
        if (getUpgrade3()) {
            delay -= delay * 0.40;
        }
        return delay;
    }

    public void aumentarCustos() {
        this.custoAtual += this.custoAtual * 0.001f;
    }

    public void salvar(Context context) {
        if (!getAtivo()) {
            return;
        }
        SQLiteDatabase db = JogoOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("caixa", this.caixa);
        values.put("receita", this.receita);
        values.put("custo_total", this.custoTotal);
        values.put("preco_atual", this.precoAtual);
        values.put("custo_atual", this.custoAtual);
        values.put("produtos_fabricados", this.produtosFabricados);
        values.put("capacidade_estoque", this.capacidadeEstoque);
        values.put("produtos_estoque", this.produtosEstoque);
        values.put("produtos_vendidos", this.produtosVendidos);
        values.put("funcionarios", this.funcionarios);
        db.update("Jogo", values, "id = ?", new String[] { Integer.toString(this.id) });
    }

    public void encerrar(Context context) {
        this.ativo = 0;
        SQLiteDatabase db = JogoOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ativo", 0);
        db.update("Jogo", values, "id = ?", new String[] { Integer.toString(this.id) });
    }

    public void produzir(int quantidade) {
        if (capacidadeEstoque - produtosEstoque < quantidade) {
            return;
        }
        this.produtosEstoque += quantidade;
        this.produtosFabricados += quantidade;
        float custoProducao = this.custoAtual * quantidade;
        this.caixa -= custoProducao;
        this.custoTotal += custoProducao;
    }

    public void expandirEstoque(Context context, int capacidade) {
        this.capacidadeEstoque += capacidade;
        this.custoAtual += capacidade * 0.05f;
        this.salvar(context);
    }

    public void contratarFuncionario(Context context) {
        float preco = getPrecoFuncionario();
        if (this.caixa < preco) {
            return;
        }
        this.funcionarios++;
        this.caixa -= preco;
        float multiplicador = getUpgrade4() ? 0.1f : 0.25f;
        this.custoAtual += this.custoAtual * multiplicador;
        this.salvar(context);
    }

    public static Jogo iniciarNovoJogo(int usuarioId, Context context) {
        SQLiteDatabase db = JogoOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario_id", usuarioId);
        values.put("ativo", 1);
        db.insertOrThrow("Jogo", null, values);
        return obterJogoAtivo(usuarioId, context);
    }

    public static Jogo obterJogoAtivo(int usuarioId, Context context) {
        SQLiteDatabase db = JogoOpenHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query("Jogo", new String[] {
                        "id", "caixa", "receita", "custo_total", "preco_atual", "custo_atual",
                        "capacidade_estoque", "produtos_estoque", "produtos_vendidos", "produtos_fabricados",
                        "funcionarios", "upgrade_0", "upgrade_1", "upgrade_2", "upgrade_3", "upgrade_4",
                        "meta_0", "meta_1", "meta_2", "meta_3", "meta_4", "meta_5", "meta_6", "meta_7",
                        "meta_8", "meta_9"
                },
                "usuario_id = ? AND ativo = ?", new String[] {
                        Integer.toString(usuarioId),
                        Integer.toString(1)
                }, null, null, null);

        Jogo jogoAtivo = null;
        if (cursor.moveToFirst()) {
            jogoAtivo = new Jogo(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    usuarioId,
                    1,
                    cursor.getFloat(cursor.getColumnIndex("caixa")),
                    cursor.getFloat(cursor.getColumnIndex("receita")),
                    cursor.getFloat(cursor.getColumnIndex("custo_total")),
                    cursor.getFloat(cursor.getColumnIndex("preco_atual")),
                    cursor.getFloat(cursor.getColumnIndex("custo_atual")),
                    cursor.getInt(cursor.getColumnIndex("capacidade_estoque")),
                    cursor.getInt(cursor.getColumnIndex("produtos_estoque")),
                    cursor.getInt(cursor.getColumnIndex("produtos_vendidos")),
                    cursor.getInt(cursor.getColumnIndex("produtos_fabricados")),
                    cursor.getInt(cursor.getColumnIndex("funcionarios")),
                    cursor.getInt(cursor.getColumnIndex("upgrade_0")),
                    cursor.getInt(cursor.getColumnIndex("upgrade_1")),
                    cursor.getInt(cursor.getColumnIndex("upgrade_2")),
                    cursor.getInt(cursor.getColumnIndex("upgrade_3")),
                    cursor.getInt(cursor.getColumnIndex("upgrade_4")),
                    cursor.getInt(cursor.getColumnIndex("meta_0")),
                    cursor.getInt(cursor.getColumnIndex("meta_1")),
                    cursor.getInt(cursor.getColumnIndex("meta_2")),
                    cursor.getInt(cursor.getColumnIndex("meta_3")),
                    cursor.getInt(cursor.getColumnIndex("meta_4")),
                    cursor.getInt(cursor.getColumnIndex("meta_5")),
                    cursor.getInt(cursor.getColumnIndex("meta_6")),
                    cursor.getInt(cursor.getColumnIndex("meta_7")),
                    cursor.getInt(cursor.getColumnIndex("meta_8")),
                    cursor.getInt(cursor.getColumnIndex("meta_9"))
            );
        }
        cursor.close();
        return jogoAtivo;
    }

    public void comprarProdutos() {
        int produtosComprados = getDemanda();
        produtosComprados = Math.min(produtosComprados, produtosEstoque);
        this.produtosEstoque -= produtosComprados;
        this.produtosVendidos += produtosComprados;
        float receitaGerada = produtosComprados * this.precoAtual;
        this.caixa += receitaGerada;
        this.receita += receitaGerada;
    }
}
