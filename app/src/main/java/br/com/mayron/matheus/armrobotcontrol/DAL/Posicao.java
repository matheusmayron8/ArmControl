package br.com.mayron.matheus.armrobotcontrol.DAL;

/**
 * Created by Raphael on 25/09/2017.
 */

public class Posicao {

    int id;
    int cintura;
    int giroPulso;
    int alturaPulso;
    int cotovelo;
    int ombro;
    int garra;
    int velocidade;

    //region getters and setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getCintura() {
        return cintura;
    }

    public void setCintura(int cintura) {
        this.cintura = cintura;
    }

    public int getGiroPulso() {
        return giroPulso;
    }

    public void setGiroPulso(int giroPulso) {
        this.giroPulso = giroPulso;
    }

    public int getAlturaPulso() {
        return alturaPulso;
    }

    public void setAlturaPulso(int alturaPulso) {
        this.alturaPulso = alturaPulso;
    }

    public int getCotovelo() {
        return cotovelo;
    }

    public void setCotovelo(int cotovelo) {
        this.cotovelo = cotovelo;
    }

    public int getOmbro() {
        return ombro;
    }

    public void setOmbro(int ombro) {
        this.ombro = ombro;
    }

    public int getGarra() {
        return garra;
    }

    public void setGarra(int garra) {
        this.garra = garra;
    }

    public int getVelocidade() { return velocidade; }

    public void setVelocidade(int velocidade) { this.velocidade = velocidade; }

    //endregion

    public Posicao(int id, int cintura, int giroPulso, int alturaPulso, int cotovelo, int ombro, int garra, int velocidade) {
        this.id = id;
        this.cintura = cintura;
        this.giroPulso = giroPulso;
        this.alturaPulso = alturaPulso;
        this.cotovelo = cotovelo;
        this.ombro = ombro;
        this.garra = garra;
        this.velocidade = velocidade;
    }

    @Override
    public String toString() {
        return String.format("{%d;%d;%d;%d;%d;%d;%d}", cintura,  giroPulso,  alturaPulso,  cotovelo,  ombro,  garra,  velocidade);
    }
}
