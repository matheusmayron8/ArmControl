package br.com.mayron.matheus.armrobotcontrol.DAL;

/**
 * @author  Matheus Mayron
 * @since  29/09/2017.
 */

public class Movimento {
    private int id;
    private String descricao;
    private int idPosicao;

    public Movimento(int id, String descricao, int idPosicao) {
        this.id = id;
        this.descricao = descricao;
        this.idPosicao = idPosicao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getIdPosicao() {
        return idPosicao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setIdPosicao(int idPosicao) {
        this.idPosicao = idPosicao;
    }
}
