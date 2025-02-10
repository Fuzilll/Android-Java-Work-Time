package com.example.timemarkinghr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.timemarkinghr.R;
import com.example.timemarkinghr.model.RegistroPonto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PontoAdapter extends RecyclerView.Adapter<PontoAdapter.ViewHolder> {
    private final List<RegistroPonto> listaPontos;

    public PontoAdapter(List<RegistroPonto> listaPontos) {
        this.listaPontos = listaPontos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ponto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegistroPonto ponto = listaPontos.get(position);
        holder.tvTipoPonto.setText(ponto.getTipoPonto());
        holder.tvLocalizacao.setText(String.format(Locale.getDefault(), "Lat: %.5f, Long: %.5f", ponto.getLatitude(), ponto.getLongitude()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvDataHora.setText(sdf.format(new Date(ponto.getTimestamp())));

        Glide.with(holder.itemView.getContext()).load(ponto.getImageUrl()).into(holder.ivFotoPonto);
    }

    @Override
    public int getItemCount() {
        return listaPontos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipoPonto, tvLocalizacao, tvDataHora;
        ImageView ivFotoPonto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoPonto = itemView.findViewById(R.id.tvTipoPonto);
            tvLocalizacao = itemView.findViewById(R.id.tvLocalizacao);
            tvDataHora = itemView.findViewById(R.id.tvDataHora);
            ivFotoPonto = itemView.findViewById(R.id.ivFotoPonto);
        }
    }
}