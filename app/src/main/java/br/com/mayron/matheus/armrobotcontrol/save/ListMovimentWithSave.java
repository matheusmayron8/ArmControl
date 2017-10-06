package br.com.mayron.matheus.armrobotcontrol.save;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import br.com.mayron.matheus.armrobotcontrol.ArmControlActivity;
import br.com.mayron.matheus.armrobotcontrol.DAL.Movimento;
import br.com.mayron.matheus.armrobotcontrol.DAL.MovimentoDAO;
import br.com.mayron.matheus.armrobotcontrol.DAL.Posicao;
import br.com.mayron.matheus.armrobotcontrol.DAL.PosicaoDAO;
import br.com.mayron.matheus.armrobotcontrol.R;
import br.com.mayron.matheus.armrobotcontrol.save.adapter.AdapterMovimentWithSave;
import br.com.mayron.matheus.armrobotcontrol.utils.RecyclerItemClickListener;

import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_NEXT_MOVEMENT;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_NEXT_POSITION;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_POSITION_EXTRA;

public class ListMovimentWithSave extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Movimento> movimentoList;
    private Posicao mPosicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_moviment_with_save);

        Intent intent = getIntent();

        if(intent == null){
            Toast.makeText(this, "Erro ao recuperar posicao", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            mPosicao = intent.getParcelableExtra(KEY_POSITION_EXTRA);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_lista_movimentos);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        MovimentoDAO movimentoDAO = MovimentoDAO.getInstance(getApplicationContext());
        movimentoList = movimentoDAO.recuperarTodosDistintos();

        mAdapter = new AdapterMovimentWithSave(movimentoList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Movimento movimento = movimentoList.get(position);
                int idPosicao = cadastrarNovaPosicao(mPosicao);
                movimento.setIdPosicao(idPosicao);
                exibirCaixaConfirmacaoCadastro(movimento);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
    }

    public void cancelar(View view) {
        finish();
    }


    public void novoMovimento(View view) {
        exibirCaixaCadastro();
    }

    private void exibirCaixaCadastro(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListMovimentWithSave.this);
        alertDialog.setTitle("NOVO MOVIMENTO");
        alertDialog.setMessage("Digite a descrição do novo movimento");

        final EditText input = new EditText(ListMovimentWithSave.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.arm_robot);

        alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String descricao = input.getText().toString();
                int idPosicao = cadastrarNovaPosicao(mPosicao);
                cadastrarNovoMovimento(descricao,idPosicao);

                Toast.makeText(ListMovimentWithSave.this, "Posicao Cadastrada!", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        alertDialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });

        alertDialog.show();
    }

    private void exibirCaixaConfirmacaoCadastro(final Movimento movimento){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListMovimentWithSave.this);
        alertDialog.setTitle("NOVA POSIÇÃO");
        alertDialog.setMessage("Posição será salva no movimento: ".concat(movimento.getDescricao()));

        alertDialog.setIcon(R.drawable.arm_robot);

        alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MovimentoDAO.getInstance(getApplicationContext()).salvar(movimento);
                Toast.makeText(ListMovimentWithSave.this, "Posicao Cadastrada!", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        alertDialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });

        alertDialog.show();
    }

    private int cadastrarNovaPosicao(Posicao posicao){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int id = sharedPreferences.getInt(KEY_NEXT_POSITION, 100);

        posicao.setId(id);
        PosicaoDAO.getInstance(getApplicationContext()).salvar(posicao);

        int nextId = id + 1;
        sharedPreferences.edit().putInt(KEY_NEXT_POSITION, nextId).apply();

        return id;
    }

    private void cadastrarNovoMovimento(String descricao, int idPosicao){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int id = sharedPreferences.getInt(KEY_NEXT_MOVEMENT, 100);

        Movimento movimento = new Movimento(id,descricao,idPosicao);
        MovimentoDAO.getInstance(getApplicationContext()).salvar(movimento);

        id = id + 1;

        sharedPreferences.edit().putInt(KEY_NEXT_MOVEMENT, id).apply();
    }
}
