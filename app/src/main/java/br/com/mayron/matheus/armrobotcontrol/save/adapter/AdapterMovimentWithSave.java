package br.com.mayron.matheus.armrobotcontrol.save.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.mayron.matheus.armrobotcontrol.DAL.Movimento;
import br.com.mayron.matheus.armrobotcontrol.R;

/**
 * @author Matheus Mayron
 * @since 30/09/2017.
 */

public class AdapterMovimentWithSave extends RecyclerView.Adapter<AdapterMovimentWithSave.MyViewHolder>{

    private List<Movimento> movimentos;

    public AdapterMovimentWithSave(List<Movimento> movimentos){
        this.movimentos = movimentos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimento, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movimento movimento = movimentos.get(position);

        holder.descricaoTxt.setText(movimento.getDescricao());
    }

    @Override
    public int getItemCount() {
        return movimentos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricaoTxt;

        MyViewHolder(View itemView) {
            super(itemView);

            descricaoTxt = (TextView) itemView.findViewById(R.id.mov_descricao);
        }
    }
}
