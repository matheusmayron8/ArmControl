package br.com.mayron.matheus.armrobotcontrol.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Raphael on 25/09/2017.
 */

public class PersistenceHelper extends SQLiteOpenHelper {

    public static final String NOME_BANCO =  "ExemploVeiculo";
    public static final int VERSAO =  2;

    private static PersistenceHelper instance;

    private PersistenceHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    public static PersistenceHelper getInstance(Context context) {
        if(instance == null)
            instance = new PersistenceHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PosicaoDAO.SCRIPT_CRIACAO_TABELA_POSICOES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PosicaoDAO.SCRIPT_DELECAO_TABELA);
        onCreate(db);
    }

}
