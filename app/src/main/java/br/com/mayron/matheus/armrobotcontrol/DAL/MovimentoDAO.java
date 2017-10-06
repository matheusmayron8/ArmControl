package br.com.mayron.matheus.armrobotcontrol.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matheus
 * @since 29/09/2017.
 */

public class MovimentoDAO {

    public static final String NOME_TABELA = "movimento";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_DESCRICAO = "descricao";
    public static final String COLUNA_ID_POSICAO= "id_posicao";

    //region script de criação da tabela
    public static final String SCRIPT_CRIACAO_TABELA_MOVIMENTOS = "CREATE TABLE " + NOME_TABELA +
            "(" +
            COLUNA_ID + " INTEGER," +
            COLUNA_DESCRICAO + " TEXT," +
            COLUNA_ID_POSICAO + " INTEGER" +
            ")";
    //endregion

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + NOME_TABELA;

    private SQLiteDatabase dataBase = null;
    private static MovimentoDAO mInstance;

    public static MovimentoDAO getInstance(Context context) {
        if(mInstance == null)
            mInstance = new MovimentoDAO(context);
        return mInstance;
    }

    private MovimentoDAO(Context context){
        PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
        dataBase = persistenceHelper.getWritableDatabase();
    }

    public void salvar(Movimento movimento) {
        ContentValues values = gerarContentValuesMovimento(movimento);
        long retorno  = dataBase.insert(NOME_TABELA, null, values);
    }

    public List<Movimento> recuperarTodos() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        return construirMovimentosPorCursor(cursor);
    }

    public List<Movimento> recuperarTodosDistintos() {
        String queryDistinct = "SELECT DISTINCT "+ COLUNA_ID + " FROM " + NOME_TABELA;
        Cursor cursor = dataBase.rawQuery(queryDistinct, null);
        List<Integer> ids = construirListaIdPorCursor(cursor);
        List<Movimento> list = new ArrayList<>();
        for(Integer id : ids) {
            String query = "SELECT * FROM " + NOME_TABELA + " WHERE " + COLUNA_ID + " = " + id;
            Cursor cursor1 = dataBase.rawQuery(query, null);
            list.add(construirMovimentosPorCursor(cursor1).get(0));
        }

        return list;
    }

    public List<Movimento> recuperarPorId(int id) {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA + " WHERE " + COLUNA_ID + " = " + id + " ORDER BY " + COLUNA_ID_POSICAO;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        return construirMovimentosPorCursor(cursor);
    }

    public void deletar(Movimento movimento) {
        String[] valoresParaSubstituir = {
                String.valueOf(movimento.getId())
        };
        dataBase.delete(NOME_TABELA, COLUNA_ID + " =  ?", valoresParaSubstituir);
    }

    public void editar(Movimento movimento) {
        ContentValues valores = gerarContentValuesMovimento(movimento);

        String[] valoresParaSubstituir = {
                String.valueOf(movimento.getId())
        };

        dataBase.update(NOME_TABELA, valores, COLUNA_ID + " = ?", valoresParaSubstituir);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen())
            dataBase.close();
    }


    private List<Movimento> construirMovimentosPorCursor(Cursor cursor) {
        List<Movimento> movimentos = new ArrayList<>();
        if(cursor == null)
            return movimentos;

        try {

            if (cursor.moveToFirst()) {
                do {

                    int indexID = cursor.getColumnIndex(COLUNA_ID);
                    int indexDescricao = cursor.getColumnIndex(COLUNA_DESCRICAO);
                    int indexIdPosicao = cursor.getColumnIndex(COLUNA_ID_POSICAO);

                    int id = cursor.getInt(indexID);
                    String descricao = cursor.getString(indexDescricao);
                    int idPosicao = cursor.getInt(indexIdPosicao);

                    Movimento movimento = new Movimento(id,descricao,idPosicao);

                    movimentos.add(movimento);

                } while (cursor.moveToNext());
            }

        } finally {
            cursor.close();
        }
        return movimentos;
    }


    private ContentValues gerarContentValuesMovimento(Movimento movimento) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_ID, movimento.getId());
        values.put(COLUNA_DESCRICAO, movimento.getDescricao());
        values.put(COLUNA_ID_POSICAO, movimento.getIdPosicao());
        return values;
    }

    private List<Integer> construirListaIdPorCursor(Cursor cursor) {
        List<Integer> ids = new ArrayList<>();
        if(cursor == null)
            return ids;

        try {

            if (cursor.moveToFirst()) {
                do {

                    int indexID = cursor.getColumnIndex(COLUNA_ID);

                    int id = cursor.getInt(indexID);

                    ids.add(id);

                } while (cursor.moveToNext());
            }

        } finally {
            cursor.close();
        }
        return ids;
    }
}
