package com.example.timemarkinghr.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timemarkinghr.R;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.timemarkinghr.data.model.RegistroPonto;
import com.example.timemarkinghr.ui.activity.RegistroPontoActivity;
import com.example.timemarkinghr.utils.DateUtils;

import java.util.List;

public class HistoricoPontoAdapter extends RecyclerView.Adapter<HistoricoPontoAdapter.PontoViewHolder> {
    private List<RegistroPonto> listaPontos;

    public HistoricoPontoAdapter(List<RegistroPonto> listaPontos) {
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

        return (listaPontos != null) ? listaPontos.size() : 0;
    }

    class PontoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTipoPonto, tvDataHora, tvLocalizacao;

        public PontoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoPonto = itemView.findViewById(R.id.tvTipoPonto);
            tvDataHora = itemView.findViewById(R.id.tvDataHora);
            tvLocalizacao = itemView.findViewById(R.id.tvLocalizacao);
        }

        public void bind(RegistroPonto ponto) {
            if (ponto == null) return;

            tvTipoPonto.setText("Registro de Ponto");
            tvLocalizacao.setText("Lat: " + ponto.getLatitude() + " | Lng: " + ponto.getLongitude());

//            if (ponto.getDataHoraAsLocalDateTime() != null) {
//                tvDataHora.setText(DateUtils.formatarParaExibicao(ponto.getDataHoraAsLocalDateTime()));
//            } else {
//                tvDataHora.setText("Data não disponível");
//            }
        }
    }
}