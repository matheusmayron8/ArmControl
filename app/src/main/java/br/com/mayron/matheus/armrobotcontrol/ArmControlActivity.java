package br.com.mayron.matheus.armrobotcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.UUID;

import br.com.mayron.matheus.armrobotcontrol.Bluetooth.GerenciadorBluetooth;
import br.com.mayron.matheus.armrobotcontrol.DAL.Posicao;
import br.com.mayron.matheus.armrobotcontrol.history.HistoricoMovimentos;
import br.com.mayron.matheus.armrobotcontrol.save.ListMovimentWithSave;

import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_POSITION_EXTRA;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_SEEK_CLAW;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_SEEK_ELBOW;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_SEEK_PULSE_HEIGHT;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_SEEK_PULSE_ROTATE;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_SEEK_SHOULDER;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_SEEK_WAIST;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_VELOCITY;

public class ArmControlActivity extends AppCompatActivity {
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private SeekBar seekCintura;
    private SeekBar seekPulsoGira;
    private SeekBar seekPulsoSobeDesce;
    private SeekBar seekCotovelo;
    private SeekBar seekOmbro;
    private SeekBar seekGarra;

    private TextView textCintura;
    private TextView textPulsoGira;
    private TextView textPulsoSobeDesce;
    private TextView textCotovelo;
    private TextView textOmbro;
    private TextView textGarra;

    private RadioGroup rgVelocidade;

    private Posicao posicao;

    private GerenciadorBluetooth gerenciadorBluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constraint_arm_control);

        seekCintura = (SeekBar) findViewById(R.id.seek_cintura);
        seekPulsoGira = (SeekBar) findViewById(R.id.seek_pulso_gira);
        seekPulsoSobeDesce = (SeekBar) findViewById(R.id.seek_pulso_sobedesce);
        seekCotovelo = (SeekBar) findViewById(R.id.seek_cotovelo);
        seekOmbro = (SeekBar) findViewById(R.id.seek_ombro);
        seekGarra = (SeekBar) findViewById(R.id.seek_garra);

        textCintura = (TextView) findViewById(R.id.text_cintura);
        textPulsoGira = (TextView) findViewById(R.id.text_pulso_gira);
        textPulsoSobeDesce = (TextView) findViewById(R.id.text_pulso_sobedesce);
        textCotovelo = (TextView) findViewById(R.id.text_cotovelo);
        textOmbro = (TextView) findViewById(R.id.text_ombro);
        textGarra = (TextView) findViewById(R.id.text_garra);

        gerenciadorBluetooth = ((GerenciadorBluetooth) getApplication());
        posicao = new Posicao();

        setupSeekBars();
        setupRGVelocidade();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SEEK_WAIST,posicao.getCintura());
        outState.putInt(KEY_SEEK_PULSE_ROTATE, posicao.getGiroPulso());
        outState.putInt(KEY_SEEK_PULSE_HEIGHT, posicao.getAlturaPulso());
        outState.putInt(KEY_SEEK_ELBOW, posicao.getCotovelo());
        outState.putInt(KEY_SEEK_SHOULDER, posicao.getOmbro());
        outState.putInt(KEY_SEEK_CLAW, posicao.getGarra());
        outState.putInt(KEY_VELOCITY, posicao.getVelocidade());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int waist = savedInstanceState.getInt(KEY_SEEK_WAIST);
        int pulseR = savedInstanceState.getInt(KEY_SEEK_PULSE_ROTATE);
        int pulseH = savedInstanceState.getInt(KEY_SEEK_PULSE_HEIGHT);
        int elbow = savedInstanceState.getInt(KEY_SEEK_ELBOW);
        int shoulder = savedInstanceState.getInt(KEY_SEEK_SHOULDER);
        int claw = savedInstanceState.getInt(KEY_SEEK_CLAW);
        int velocidade = savedInstanceState.getInt(KEY_VELOCITY);

        posicao = new Posicao(0,waist,pulseR,pulseH,elbow,shoulder,claw,velocidade);

        seekCintura.setProgress(waist);
        seekPulsoGira.setProgress(pulseR);
        seekPulsoSobeDesce.setProgress(pulseH);
        seekCotovelo.setProgress(elbow);
        seekOmbro.setProgress(shoulder);
        seekGarra.setProgress(claw);
    }

    @Override
    public void onBackPressed() {
        gerenciadorBluetooth.fecharSocket();
        super.onBackPressed();
    }

    private void setupRGVelocidade(){
        //Seleciona o radio button de acordo com a velocidade inicial setada
        switch (posicao.getVelocidade()){
            case Constants.VELOCIDADE.LENTA:
                ((RadioButton) findViewById(R.id.rb_lenta)).setChecked(true);
                break;
            case Constants.VELOCIDADE.MEDIA:
                ((RadioButton) findViewById(R.id.rb_media)).setChecked(true);
                break;
            case Constants.VELOCIDADE.RAPIDA:
                ((RadioButton) findViewById(R.id.rb_rapida)).setChecked(true);
                break;
            default:
                ((RadioButton) findViewById(R.id.rb_media)).setChecked(true);
        }

        //Cria um listener para alterar a velocidade de acordo com a RadioButton marcado
        rgVelocidade = (RadioGroup) findViewById(R.id.rg_velocidade);
        rgVelocidade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_lenta:
                        posicao.setVelocidade(Constants.VELOCIDADE.LENTA);
                        break;
                    case R.id.rb_media:
                        posicao.setVelocidade(Constants.VELOCIDADE.MEDIA);
                        break;
                    case R.id.rb_rapida:
                        posicao.setVelocidade(Constants.VELOCIDADE.RAPIDA);
                        break;
                    default:
                        posicao.setVelocidade(Constants.VELOCIDADE.MEDIA);
                }
            }
        });
    }

    private void setupSeekBars(){
        //Exibe os valores iniciais nos texviews
        textCintura.setText(String.valueOf(posicao.getCintura()).concat("º"));
        textPulsoGira.setText(String.valueOf(posicao.getGiroPulso()).concat("º"));
        textPulsoSobeDesce.setText(String.valueOf(posicao.getAlturaPulso()).concat("º"));
        textCotovelo.setText(String.valueOf(posicao.getCotovelo()).concat("º"));
        textOmbro.setText(String.valueOf(posicao.getOmbro()).concat("º"));
        textGarra.setText(String.valueOf(posicao.getGarra()).concat("º"));

        // Calcula os valores iniciais para setar nas seekbars
        int cintura = map(posicao.getCintura(),10,100,0,100);
        int pulsoGira = map(posicao.getGiroPulso(),10,150,0,100);
        int pulsoSD = map(posicao.getAlturaPulso(),10,150,0,100);
        int cotovelo = map(posicao.getCotovelo(),50,170,0,100);
        int ombro = map(posicao.getOmbro(),50,140,0,100);
        int garra = map(posicao.getGarra(),90,130,0,100);
        //Seta os valores iniciais na seekbars
        seekCintura.setProgress(cintura);
        seekPulsoGira.setProgress(pulsoGira);
        seekPulsoSobeDesce.setProgress(pulsoSD);
        seekCotovelo.setProgress(cotovelo);
        seekOmbro.setProgress(ombro);
        seekGarra.setProgress(garra);

        //Cria listeners para as seekbars
        seekCintura.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = map(i,0,100,10,100);
                textCintura.setText(String.valueOf(i).concat("º"));
                posicao.setCintura(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //gerenciadorBluetooth.enviarComando(posicao);
            }
        });
        seekPulsoGira.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = map(i,0,100,10,150);
                textPulsoGira.setText(String.valueOf(i).concat("º"));
                posicao.setGiroPulso(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //gerenciadorBluetooth.enviarComando(posicao);
            }
        });
        seekPulsoSobeDesce.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = map(i,0,100,10,150);
                textPulsoSobeDesce.setText(String.valueOf(i).concat("º"));
                posicao.setAlturaPulso(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //gerenciadorBluetooth.enviarComando(posicao);
            }
        });
        seekCotovelo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = map(i,0,100,50,170);
                textCotovelo.setText(String.valueOf(i).concat("º"));
                posicao.setCotovelo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //gerenciadorBluetooth.enviarComando(posicao);
            }
        });
        seekOmbro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = map(i,0,100,50,140);
                textOmbro.setText(String.valueOf(i).concat("º"));
                posicao.setOmbro(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //gerenciadorBluetooth.enviarComando(posicao);
            }
        });
        seekGarra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = map(i,0,100,90,130);
                textGarra.setText(String.valueOf(i).concat("º"));
                posicao.setGarra(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //gerenciadorBluetooth.enviarComando(posicao);
            }
        });
    }

    public void salvarPosicao(View view) {
        Intent intent = new Intent(getApplicationContext(), ListMovimentWithSave.class);
        intent.putExtra(KEY_POSITION_EXTRA, posicao);
        startActivity(intent);
    }

    public void movimentosSalvos(View view) {
        Intent intent = new Intent(this, HistoricoMovimentos.class);
        startActivity(intent);
    }

    public void enviar(View view) {
        gerenciadorBluetooth.enviarComando(posicao);
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
