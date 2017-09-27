package br.com.mayron.matheus.armrobotcontrol.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raphael on 25/09/2017.
 */

public class PosicaoDAO {

    public static final String NOME_TABELA = "posicao";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_CINTURA = "cintura";
    public static final String COLUNA_GIRO_PULSO = "giroPulso";
    public static final String COLUNA_ALTURA_PULSO = "alturaPulso";
    public static final String COLUNA_COTOVELO = "cotovelo";
    public static final String COLUNA_OMBRO = "ombro";
    public static final String COLUNA_GARRA = "garra";
    public static final String COLUNA_VELOCIDADE = "velocidade";

    //region script de criação da tabela
    public static final String SCRIPT_CRIACAO_TABELA_POSICOES = "CREATE TABLE " + NOME_TABELA +
            "(" +
                COLUNA_ID + " INTEGER PRIMARY KEY," +
                COLUNA_CINTURA + " INTEGER," +
                COLUNA_GIRO_PULSO + " INTEGER," +
                COLUNA_ALTURA_PULSO + " INTEGER," +
                COLUNA_COTOVELO + " INTEGER," +
                COLUNA_OMBRO + " INTEGER," +
                COLUNA_GARRA + " INTEGER," +
                COLUNA_VELOCIDADE + " INTEGER" +
            ")";
    //endregion

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + NOME_TABELA;

    private SQLiteDatabase dataBase = null;

    private static PosicaoDAO instance;

    public static PosicaoDAO getInstance(Context context) {
        if(instance == null)
            instance = new PosicaoDAO(context);
        return instance;
    }

    private PosicaoDAO(Context context) {
        PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
        dataBase = persistenceHelper.getWritableDatabase();
    }

    public void salvar(Posicao posicao) {
        ContentValues values = gerarContentValuesPosicao(posicao);
        long retorno  = dataBase.insert(NOME_TABELA, null, values);
    }

    public List<Posicao> recuperarTodos() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Posicao> posicoes = construirPosicoesPorCursor(cursor);
        return posicoes;
    }

    public void deletar(Posicao posicao) {

        String[] valoresParaSubstituir = {
                String.valueOf(posicao.getId())
        };

        dataBase.delete(NOME_TABELA, COLUNA_ID + " =  ?", valoresParaSubstituir);
    }

    public void editar(Posicao posicao) {
        ContentValues valores = gerarContentValuesPosicao(posicao);

        String[] valoresParaSubstituir = {
                String.valueOf(posicao.getId())
        };

        dataBase.update(NOME_TABELA, valores, COLUNA_ID + " = ?", valoresParaSubstituir);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen())
            dataBase.close();
    }

    private List<Posicao> construirPosicoesPorCursor(Cursor cursor) {
        List<Posicao> posicoes = new ArrayList<Posicao>();
        if(cursor == null)
            return posicoes;

        try {

            if (cursor.moveToFirst()) {
                do {

                    int indexID = cursor.getColumnIndex(COLUNA_ID);
                    int indexCintura = cursor.getColumnIndex(COLUNA_CINTURA);
                    int indexGiroPulso = cursor.getColumnIndex(COLUNA_GIRO_PULSO);
                    int indexAlturaPulso = cursor.getColumnIndex(COLUNA_ALTURA_PULSO);
                    int indexCotovelo = cursor.getColumnIndex(COLUNA_COTOVELO);
                    int indexOmbro = cursor.getColumnIndex(COLUNA_OMBRO);
                    int indexGarra = cursor.getColumnIndex(COLUNA_GARRA);
                    int indexVelocidade = cursor.getColumnIndex(COLUNA_VELOCIDADE);

                    int id = cursor.getInt(indexID);
                    int cintura = cursor.getInt(indexCintura);
                    int giroPulso = cursor.getInt(indexGiroPulso);
                    int alturaPulso = cursor.getInt(indexAlturaPulso);
                    int cotovelo = cursor.getInt(indexCotovelo);
                    int ombro = cursor.getInt(indexOmbro);
                    int garra = cursor.getInt(indexGarra);
                    int velocidade = cursor.getInt(indexVelocidade);

                    Posicao posicao = new Posicao(id, cintura, giroPulso, alturaPulso, cotovelo, ombro, garra, velocidade);

                    posicoes.add(posicao);

                } while (cursor.moveToNext());
            }

        } finally {
            cursor.close();
        }
        return posicoes;
    }

    private ContentValues gerarContentValuesPosicao(Posicao posicao) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_CINTURA, posicao.getCintura());
        values.put(COLUNA_GIRO_PULSO, posicao.getGiroPulso());
        values.put(COLUNA_ALTURA_PULSO, posicao.getAlturaPulso());
        values.put(COLUNA_COTOVELO, posicao.getCotovelo());
        values.put(COLUNA_OMBRO, posicao.getOmbro());
        values.put(COLUNA_GARRA, posicao.getGarra());
        values.put(COLUNA_VELOCIDADE, posicao.getVelocidade());
        return values;
    }
}
