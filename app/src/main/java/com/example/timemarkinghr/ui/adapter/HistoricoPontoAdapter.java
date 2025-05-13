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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

            // Definir tipo de ponto (Entrada/Saída)
            String tipo = ponto.getTipo();
            tvTipoPonto.setText(tipo != null ? tipo.equals("Entrada") ? "Entrada" : "Saída" : "Registro");

            // Formatar data e hora
            if (ponto.getData_hora() != null) {
                tvDataHora.setText(formatarData(ponto.getData_hora()));
            } else {
                tvDataHora.setText("Data não disponível");
            }

            // Mostrar localização se disponível
            if (ponto.getLatitude() != 0 && ponto.getLongitude() != 0) {
                tvLocalizacao.setText(String.format("Lat: %.4f, Lng: %.4f",
                        ponto.getLatitude(), ponto.getLongitude()));
            } else {
                tvLocalizacao.setText("Localização não registrada");
            }
        }

        private String formatarData(String dataOriginal) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date date = inputFormat.parse(dataOriginal);
                return outputFormat.format(date);
            } catch (ParseException e) {
                return dataOriginal; // retorna original se não conseguir formatar
            }
        }
    }
}