package com.example.timemarkinghr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.model.RegistroPonto;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PontoAdapter extends RecyclerView.Adapter<PontoAdapter.PontoViewHolder> {
    private List<RegistroPonto> listaPontos;

    public PontoAdapter(List<RegistroPonto> listaPontos) {
        this.listaPontos = listaPontos;
    }

    @NonNull
    @Override
    public PontoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ponto, parent, false);
        return new PontoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PontoViewHolder holder, int position) {
        RegistroPonto ponto = listaPontos.get(position);
        holder.bind(ponto);
    }

    @Override
    public int getItemCount() {
        return listaPontos.size();
    }

    static class PontoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTipoPonto, tvDataHora, tvLocalizacao;

        public PontoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoPonto = itemView.findViewById(R.id.tvTipoPonto);
            tvDataHora = itemView.findViewById(R.id.tvDataHora);
            tvLocalizacao = itemView.findViewById(R.id.tvLocalizacao);
        }

        public void bind(RegistroPonto ponto) {
            tvTipoPonto.setText(ponto.getTipoPonto());
            tvLocalizacao.setText(ponto.getLocalizacao());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String dataHora = sdf.format(ponto.getDataHora());
            tvDataHora.setText(dataHora);
        }
    }
}