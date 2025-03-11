package com.example.timemarkinghr.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NOME = "time";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NOME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Criação das tabelas
        String CREATE_TABLE_USUARIO = "CREATE TABLE usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, email TEXT, senha TEXT, data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(CREATE_TABLE_USUARIO);

        String CREATE_TABLE_REGISTRO_PONTO = "CREATE TABLE registroPonto (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id INTEGER, tipo TEXT, data_hora DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(usuario_id) REFERENCES usuario(id))";
        db.execSQL(CREATE_TABLE_REGISTRO_PONTO);

        String CREATE_TABLE_REGRAS_HORARIO = "CREATE TABLE regrasHorario (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id INTEGER, inicio_hora TEXT, fim_hora TEXT, intervalo TEXT, dia_semana INTEGER, FOREIGN KEY (usuario_id) REFERENCES usuario(id) )";
        db.execSQL(CREATE_TABLE_REGRAS_HORARIO);

        String CREATE_TABLE_LOG_AUDITORIA = "CREATE TABLE logAuditoria (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id INTEGER, acao TEXT, data_hora DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(usuario_id) REFERENCES usuario(id))";
        db.execSQL(CREATE_TABLE_LOG_AUDITORIA);

        String CREATE_TABLE_NOTIFICACAO = "CREATE TABLE notificacao(id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id INTEGER, mensagem TEXT, lida BOOLEAN DEFAULT 0, data_hora DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(usuario_id) REFERENCES usuario(id))";
        db.execSQL(CREATE_TABLE_NOTIFICACAO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Atualização do banco de dados
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS registroPonto");
        db.execSQL("DROP TABLE IF EXISTS regrasHorario");
        db.execSQL("DROP TABLE IF EXISTS logAuditoria");
        db.execSQL("DROP TABLE IF EXISTS notificacao");
        onCreate(db);
    }
}















