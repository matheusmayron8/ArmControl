package br.com.mayron.matheus.armrobotcontrol.history;

/**
 * @author Matheus Mayron
 * @since 05/10/2017
 * */

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.mayron.matheus.armrobotcontrol.Bluetooth.GerenciadorBluetooth;
import br.com.mayron.matheus.armrobotcontrol.DAL.Movimento;
import br.com.mayron.matheus.armrobotcontrol.DAL.MovimentoDAO;
import br.com.mayron.matheus.armrobotcontrol.DAL.Posicao;
import br.com.mayron.matheus.armrobotcontrol.DAL.PosicaoDAO;
import br.com.mayron.matheus.armrobotcontrol.R;
import br.com.mayron.matheus.armrobotcontrol.save.adapter.AdapterMovimentWithSave;
import br.com.mayron.matheus.armrobotcontrol.utils.RecyclerItemClickListener;

public class HistoricoMovimentos extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Movimento> movimentoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_movimentos);

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
                //..
                mostrarOpcoesMovimento(movimentoList.get(position), position);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
                mostrarOpcoesMovimento(movimentoList.get(position), position);
            }
        }));
    }

    public void mostrarOpcoesMovimento(final Movimento movimento, final int position){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("MOVIMENTO: " + movimento.getDescricao());
        alertDialog.setMessage("Digite a descrição do novo movimento");
        alertDialog.setIcon(R.drawable.arm_robot);

        alertDialog.setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //metodo enviar movimento
                enviarMovimento(movimento);
            }
        });

        alertDialog.setNeutralButton("DELETAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //metodo deletar movimento
                deletarMovimento(movimento, position);
            }
        });

        alertDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss
            }
        });

        alertDialog.show();
    }

    private void enviarMovimento(Movimento movimento){
        if(movimento == null){
            return;
        }

        //Recupera todas as posições relativas ao movimento
        List<Movimento> list = MovimentoDAO.getInstance(this).recuperarPorId(movimento.getId());

        if(list.isEmpty()){
            return;
        }

        List<Posicao> posicaoList = new ArrayList<>();
        for (Movimento mov : list){
            Posicao posicao = PosicaoDAO.getInstance(this).recuperarPosicaoPorId(mov.getIdPosicao());
            if(posicao != null){
                posicaoList.add(posicao);
            }
        }

        if(posicaoList.isEmpty()){
            return;
        }

        boolean result = ((GerenciadorBluetooth) getApplication()).enviarComando(posicaoList);

        if(result){
            Toast.makeText(this, "Enviado com sucesso!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Falha ao enviar!", Toast.LENGTH_SHORT).show();
        }

    }

    private void deletarMovimento(Movimento movimento, final int position){
        if(movimento == null){
            return;
        }

        //Recupera todas as posições relativas ao movimento
        List<Movimento> list = MovimentoDAO.getInstance(this).recuperarPorId(movimento.getId());

        if(list.isEmpty()){
            return;
        }

        for(Movimento mov : list){
            Posicao posicao = PosicaoDAO.getInstance(this).recuperarPosicaoPorId(mov.getIdPosicao());
            if(posicao != null){
                //Deleta as posiçoes referentes ao movimento
                PosicaoDAO.getInstance(this).deletar(posicao);
            }
        }

        //Deleta movimento pelo id
        MovimentoDAO.getInstance(this).deletar(movimento);
        movimentoList.remove(position);
        mAdapter.notifyDataSetChanged();
    }
}
