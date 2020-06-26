package com.pedropablo.trabalhofinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pedropablo.trabalhofinal.Jogo;
import com.pedropablo.trabalhofinal.R;
import com.pedropablo.trabalhofinal.Usuario;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class JogoActivity extends AppCompatActivity {

    private Usuario usuario;
    private Jogo jogo;

    private Handler handlerAtualizar;
    private Timer timerCompra;
    private Timer timerFuncionario;

    // Componentes da tela
    private TextView textViewCaixa;
    private TextView textViewCustoAtual;
    private TextView textViewEstoque;
    private TextView textViewPrecoAtual;
    private TextView textViewFuncionarios;
    private TextView textViewLucroPrejuizo;
    private TextView textviewProduzidos;
    private TextView textViewReceita;
    private Button btnDefinirPreco;
    private Button btnFuncionario;
    private Button btnProduzir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        textViewCaixa = findViewById(R.id.textViewCaixa);
        textViewCustoAtual = findViewById(R.id.textViewCustoAtual);
        textViewEstoque = findViewById(R.id.textViewEstoque);
        textViewPrecoAtual = findViewById(R.id.textViewPrecoAtual);
        textViewFuncionarios = findViewById(R.id.textViewFuncionarios);
        textViewLucroPrejuizo = findViewById(R.id.textViewLucroPrejuizo);
        textviewProduzidos = findViewById(R.id.textViewProduzidos);
        textViewReceita = findViewById(R.id.textViewReceita);

        btnDefinirPreco = findViewById(R.id.btnPreco);
        btnDefinirPreco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirDialogoPreco();
            }
        });

        btnFuncionario = findViewById(R.id.btnFuncionario);
        btnFuncionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogo.contratarFuncionario(getApplicationContext());
            }
        });

        btnProduzir = findViewById(R.id.btnProduzir);
        btnProduzir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jogo.produzir(1);
                atualizarTela();
            }
        });

        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        usuario = Usuario.buscarUsuario(usuarioId, getApplicationContext());
        jogo = Jogo.obterJogoAtivo(usuario.getId(), getApplicationContext());
        if (jogo == null) {
            jogo = Jogo.iniciarNovoJogo(usuario.getId(), getApplicationContext());
        }

        //TODO: atualizar custo, produção de funcionários e compras

        handlerAtualizar = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                atualizarTela();
            }
        };

        iniciarTimers();
        atualizarTela();
    }

    private void iniciarTimers() {
        timerCompra = new Timer(true);
        timerCompra.schedule(new TimerTask() {
            @Override
            public void run() {
                jogo.comprarProdutos();
                handlerAtualizar.sendEmptyMessage(0);
            }
        }, 1000, 1000);

        timerFuncionario = new Timer(true);
        timerFuncionario.schedule(new TimerTask() {
            @Override
            public void run() {
                jogo.produzir(jogo.getFuncionarios());
                handlerAtualizar.sendEmptyMessage(0);
            }
        }, jogo.getFuncionarioDelay(), jogo.getFuncionarioDelay());
    }

    private void cancelarTimers() {
        timerCompra.cancel();
        timerFuncionario.cancel();
    }

    @Override
    protected void onPause() {
        cancelarTimers();
        jogo.salvar(getApplicationContext());
        super.onPause();
    }

    public void atualizarTela() {
        textViewCaixa.setText(formatarDinheiro(jogo.getCaixa()));
        textViewCustoAtual.setText(formatarDinheiro(jogo.getCustoAtual()));
        textViewEstoque.setText(String.format("%d/%d", jogo.getProdutosEstoque(), jogo.getCapacidadeEstoque()));
        textViewPrecoAtual.setText(formatarDinheiro(jogo.getPrecoAtual()));
        textViewFuncionarios.setText(Integer.toString(jogo.getFuncionarios()));
        textviewProduzidos.setText(Integer.toString(jogo.getProdutosFabricados()));
        textViewLucroPrejuizo.setText(formatarDinheiro(jogo.getLucroProduto()));
        textViewReceita.setText(formatarDinheiro(jogo.getReceita()));
        btnFuncionario.setText(String.format("%s\n%s", getResources().getString(R.string.botao_contratar), formatarDinheiro(jogo.getPrecoFuncionario())));
        if (jogo.getLucroProduto() > 0) {
            textViewLucroPrejuizo.setTextColor(ContextCompat.getColor(this, R.color.colorTextoPositivo));
        } else {
            textViewLucroPrejuizo.setTextColor(ContextCompat.getColor(this, R.color.colorTextoNegativo));
        }
    }

    private String formatarDinheiro(float n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }

    private void exibirDialogoPreco() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText dialogEditText = new EditText(this);
        dialogEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialogLayout.addView(dialogEditText);
        dialogBuilder.setView(dialogLayout);
        dialogBuilder.setTitle(R.string.preco_dialog_titulo);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dialogText = dialogEditText.getText().toString();
                if (dialogText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.preco_vazio, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    jogo.setPrecoAtual(Float.parseFloat(dialogText));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.preco_invalido, Toast.LENGTH_SHORT).show();
                }
                atualizarTela();
            }
        });
        dialogBuilder.setMessage(R.string.preco_dialog);
        dialogBuilder.show();
    }

}
