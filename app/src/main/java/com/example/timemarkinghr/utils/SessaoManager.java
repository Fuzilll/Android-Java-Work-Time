package com.example.timemarkinghr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.timemarkinghr.data.model.Usuario;
import com.google.gson.Gson;

public class SessaoManager {
    private static final String PREF_NAME = "SessaoUsuario";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_ID = "idUsuario";
    private static final String KEY_NIVEL = "nivelUsuario";

    public static void salvarSessao(Context context, String token, Usuario usuario) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USUARIO, new Gson().toJson(usuario));
        editor.putInt(KEY_ID, usuario.getId());
        editor.putString(KEY_NIVEL, usuario.getNivel());

        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    public static Usuario getUsuario(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String usuarioJson = prefs.getString(KEY_USUARIO, null);
        return new Gson().fromJson(usuarioJson, Usuario.class);
    }

    public static boolean estaLogado(Context context) {
        return getToken(context) != null;
    }

    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}