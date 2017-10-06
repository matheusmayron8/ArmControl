package br.com.mayron.matheus.armrobotcontrol.DAL;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by Raphael on 25/09/2017.
 */

public class Posicao implements Parcelable {

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

    public Posicao(){
        id = 0;
        velocidade = 2;
        cintura = 10;
        giroPulso = 150;
        alturaPulso = 130;
        cotovelo = 110;
        ombro = 100;
        garra = 100;
    }

    @Override
    public String toString() {
        return String.format(new Locale("pt-br"),"{%d;%d;%d;%d;%d;%d;%d}", velocidade, cintura,  giroPulso,  alturaPulso,  cotovelo,  ombro,  garra);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(cintura);
        parcel.writeInt(giroPulso);
        parcel.writeInt(alturaPulso);
        parcel.writeInt(cotovelo);
        parcel.writeInt(ombro);
        parcel.writeInt(garra);
        parcel.writeInt(velocidade);
    }

    protected Posicao(Parcel in) {
        id = in.readInt();
        cintura = in.readInt();
        giroPulso = in.readInt();
        alturaPulso = in.readInt();
        cotovelo = in.readInt();
        ombro = in.readInt();
        garra = in.readInt();
        velocidade = in.readInt();
    }

    public static final Creator<Posicao> CREATOR = new Creator<Posicao>() {
        @Override
        public Posicao createFromParcel(Parcel in) {
            return new Posicao(in);
        }

        @Override
        public Posicao[] newArray(int size) {
            return new Posicao[size];
        }
    };
}
