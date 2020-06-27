package com.pedropablo.trabalhofinal.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pedropablo.trabalhofinal.Jogo;
import com.pedropablo.trabalhofinal.Pontuacao;
import com.pedropablo.trabalhofinal.R;
import com.pedropablo.trabalhofinal.Upgrade;
import com.pedropablo.trabalhofinal.Usuario;

import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

public class JogoActivity extends AppCompatActivity {

    private Usuario usuario;
    private Jogo jogo;

    private Handler handlerAtualizar;
    private Timer timerCompra;
    private Timer timerFuncionario;
    private Timer timerCusto;
    private MediaPlayer mediaPlayer;

    // Componentes da tela
    // Textviews
    private TextView textViewCaixa;
    private TextView textViewCustoAtual;
    private TextView textViewEstoque;
    private TextView textViewPrecoAtual;
    private TextView textViewFuncionarios;
    private TextView textViewLucroPrejuizo;
    private TextView textviewProduzidos;

    // Buttons
    private Button btnDefinirPreco;
    private Button btnFuncionario;
    private Button btnProduzir;
    private ImageButton btnInfo;
    private Button btnEncerrar;

    // Switches
    private Switch switchUpgrade0;
    private Switch switchUpgrade1;
    private Switch switchUpgrade2;
    private Switch switchUpgrade3;
    private Switch switchUpgrade4;

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
                reproduzirSom(R.raw.contratar);
                jogo.contratarFuncionario(getApplicationContext());
            }
        });

        btnProduzir = findViewById(R.id.btnProduzir);
        btnProduzir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jogo.getCaixa() < 0) {
                    return;
                }
                jogo.produzir(1);
                atualizarTela();
            }
        });

        btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirDialogoInfo();
            }
        });

        btnEncerrar = findViewById(R.id.btnEncerrar);
        btnEncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirDialogoEncerrar();
            }
        });

        switchUpgrade0 = findViewById(R.id.switchUpgrade0);
        switchUpgrade0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprarUpgrade(Upgrade.UPGRADE0);
            }
        });

        switchUpgrade1 = findViewById(R.id.switchUpgrade1);
        switchUpgrade1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprarUpgrade(Upgrade.UPGRADE1);
            }
        });

        switchUpgrade2 = findViewById(R.id.switchUpgrade2);
        switchUpgrade2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprarUpgrade(Upgrade.UPGRADE2);
            }
        });

        switchUpgrade3 = findViewById(R.id.switchUpgrade3);
        switchUpgrade3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprarUpgrade(Upgrade.UPGRADE3);
            }
        });

        switchUpgrade4 = findViewById(R.id.switchUpgrade4);
        switchUpgrade4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprarUpgrade(Upgrade.UPGRADE4);
            }
        });

        int usuarioId = getIntent().getIntExtra("usuarioId", -1);
        usuario = Usuario.buscarUsuario(usuarioId, getApplicationContext());
        jogo = Jogo.obterJogoAtivo(usuario.getId(), getApplicationContext());
        if (jogo == null) {
            jogo = Jogo.iniciarNovoJogo(usuario.getId(), getApplicationContext());
        }

        handlerAtualizar = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                atualizarTela();
            }
        };
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
        }, Jogo.COMPRA_DELAY_PADRAO, Jogo.COMPRA_DELAY_PADRAO);

        timerCusto = new Timer(true);
        timerCusto.schedule(new TimerTask() {
            @Override
            public void run() {
                jogo.aumentarCustos();
            }
        }, Jogo.CUSTO_DELAY_PADRAO, Jogo.CUSTO_DELAY_PADRAO);

        timerFuncionario = new Timer(true);
        timerFuncionario.schedule(new TimerTask() {
            @Override
            public void run() {
                if (jogo.getFuncionarios() > 0) {
                    if (jogo.estoqueTemEspaco()) {
                        reproduzirSom(R.raw.produto);
                        jogo.produzir(jogo.getFuncionarios());
                        handlerAtualizar.sendEmptyMessage(0);
                    } else {
                        reproduzirSom(R.raw.limite_estoque);
                    }
                }
            }
        }, jogo.getFuncionarioDelay(), jogo.getFuncionarioDelay());
    }

    private void cancelarTimers() {
        timerCompra.cancel();
        timerCusto.cancel();
        timerFuncionario.cancel();
    }

    @Override
    protected void onPause() {
        cancelarTimers();
        jogo.salvar(getApplicationContext());
        super.onPause();
    }

    @Override
    protected void onResume() {
        iniciarTimers();
        super.onResume();
    }

    public void atualizarTela() {
        textViewCaixa.setText(formatarDinheiro(jogo.getCaixa()));
        textViewCustoAtual.setText(formatarDinheiro(jogo.getCustoAtual()));
        textViewEstoque.setText(String.format("%d/%d", jogo.getProdutosEstoque(), jogo.getCapacidadeEstoque()));
        textViewPrecoAtual.setText(formatarDinheiro(jogo.getPrecoAtual()));
        textViewFuncionarios.setText(Integer.toString(jogo.getFuncionarios()));
        textviewProduzidos.setText(Integer.toString(jogo.getProdutosFabricados()));
        textViewLucroPrejuizo.setText(formatarDinheiro(jogo.getLucroProduto()));
        btnFuncionario.setText(String.format("%s\n%s", getResources().getString(R.string.botao_contratar), formatarDinheiro(jogo.getPrecoFuncionario())));

        btnFuncionario.setEnabled(jogo.getCaixa() >= jogo.getPrecoFuncionario());
        btnProduzir.setEnabled(jogo.getCaixa() >= jogo.getCustoAtual());

        int corNegativo = ContextCompat.getColor(this, R.color.colorTextoNegativo);
        int corPositivo = ContextCompat.getColor(this, R.color.colorTextoPositivo);

        if (jogo.estoqueTemEspaco()) {
            textViewEstoque.setTextColor(corPositivo);
        } else {
            textViewEstoque.setTextColor(corNegativo);
        }

        if (jogo.getLucroProduto() > 0) {
            textViewLucroPrejuizo.setTextColor(corPositivo);
        } else {
            textViewLucroPrejuizo.setTextColor(corNegativo);
        }

        if (jogo.getCaixa() > 0) {
            textViewCaixa.setTextColor(corPositivo);
        } else {
            textViewCaixa.setTextColor(corNegativo);
        }

        switchUpgrade0.setText(String.format("%s\n%s", getResources().getString(R.string.upgrade0), formatarDinheiro(Jogo.UPGRADE0_PRECO)));
        switchUpgrade0.setChecked(jogo.getUpgrade0());
        switchUpgrade0.setEnabled(!jogo.getUpgrade0() && jogo.getCaixa() >= Jogo.UPGRADE0_PRECO);

        switchUpgrade1.setText(String.format("%s\n%s", getResources().getString(R.string.upgrade1), formatarDinheiro(Jogo.UPGRADE1_PRECO)));
        switchUpgrade1.setChecked(jogo.getUpgrade1());
        switchUpgrade1.setEnabled(!jogo.getUpgrade1() && jogo.getCaixa() >= Jogo.UPGRADE1_PRECO);

        switchUpgrade2.setText(String.format("%s\n%s", getResources().getString(R.string.upgrade2), formatarDinheiro(Jogo.UPGRADE2_PRECO)));
        switchUpgrade2.setChecked(jogo.getUpgrade2());
        switchUpgrade2.setEnabled(!jogo.getUpgrade2() && jogo.getCaixa() >= Jogo.UPGRADE2_PRECO);

        switchUpgrade3.setText(String.format("%s\n%s", getResources().getString(R.string.upgrade3), formatarDinheiro(Jogo.UPGRADE3_PRECO)));
        switchUpgrade3.setChecked(jogo.getUpgrade3());
        switchUpgrade3.setEnabled(!jogo.getUpgrade3() && jogo.getCaixa() >= Jogo.UPGRADE3_PRECO);

        switchUpgrade4.setText(String.format("%s\n%s", getResources().getString(R.string.upgrade4), formatarDinheiro(Jogo.UPGRADE4_PRECO)));
        switchUpgrade4.setChecked(jogo.getUpgrade4());
        switchUpgrade4.setEnabled(!jogo.getUpgrade4() && jogo.getCaixa() >= Jogo.UPGRADE4_PRECO);
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
        dialogBuilder.setPositiveButton(R.string.preco_dialog_botao, new DialogInterface.OnClickListener() {
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

    private void exibirDialogoInfo() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.info_dialog_titulo);
        dialogBuilder.setMessage(
                String.format(getResources().getString(R.string.info_dialog_mensagem),
                        formatarDinheiro(jogo.getReceita()), formatarDinheiro(jogo.getCustoTotal()),
                        formatarDinheiro(jogo.getLucro()), jogo.getMargemLucro(), jogo.getProdutosVendidos()));
        dialogBuilder.show();
    }

    private void exibirDialogoEncerrar() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.encerrar_dialog_titulo);
        dialogBuilder.setMessage(R.string.encerrar_dialog_mensagem);
        dialogBuilder.setPositiveButton(R.string.encerrar_dialog_botao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jogo.encerrar(getApplicationContext());
                Pontuacao pontuacao = Pontuacao.novaPontuacao(usuario.getId(), jogo);
                pontuacao.inserir(getApplicationContext());
                Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.jogo_encerrado), pontuacao.getPontos()), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dialogBuilder.show();

    }

    private void reproduzirSom(int resId) {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), resId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }

    private void comprarUpgrade(Upgrade upgrade) {
        reproduzirSom(R.raw.upgrade);
        jogo.comprarUpgrade(getApplicationContext(), upgrade);
        if (upgrade == Upgrade.UPGRADE1 || upgrade == Upgrade.UPGRADE3){
            cancelarTimers();
            iniciarTimers();
        }
        atualizarTela();
    }

}
