package br.com.mayron.matheus.armrobotcontrol.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Raphael on 25/09/2017.
 */

public class PersistenceHelper extends SQLiteOpenHelper {

    public static final String NOME_BANCO =  "braco.db";
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
        Log.d("DB","Tabelas criadas");
        db.execSQL(PosicaoDAO.SCRIPT_CRIACAO_TABELA_POSICOES);
        db.execSQL(MovimentoDAO.SCRIPT_CRIACAO_TABELA_MOVIMENTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PosicaoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(MovimentoDAO.SCRIPT_DELECAO_TABELA);
        onCreate(db);
    }

}
