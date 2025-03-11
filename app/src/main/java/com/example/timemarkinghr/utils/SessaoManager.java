package com.example.timemarkinghr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.timemarkinghr.data.model.Usuario;

public class SessaoManager {
    private static final String PREF_NAME = "Sessao";
    private static final String TOKEN_KEY = "token";
    private static final String KEY_ID = "idUsuario";

    // Salva o token e ID do usuário no SharedPreferences
    public static void salvarSessao(Context context, String token, Usuario usuario) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.putInt(KEY_ID, usuario.getId());
        editor.apply();
    }

    // Obtém o token armazenado
    public static String obterToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, null);
    }

    // Obtém o ID do usuário armazenado
    public static int obterIdUsuario(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_ID, -1); // Retorna -1 se não houver ID salvo
    }

    // Verifica se o usuário está logado
    public static boolean estaLogado(Context context) {
        return obterToken(context) != null;
    }

    // Remove o token e dados do usuário, fazendo logout
    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Limpa tudo da sessão
        editor.apply();
    }
}
