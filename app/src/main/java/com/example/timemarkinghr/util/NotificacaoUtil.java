package com.example.timemarkinghr.util;

import android.content.Context;
import android.widget.Toast;

public class NotificacaoUtil {
    public static void enviarNotificacao(Context context, String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }
}
