package br.com.mayron.matheus.armrobotcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.mayron.matheus.armrobotcontrol.Bluetooth.Conectar;
import br.com.mayron.matheus.armrobotcontrol.DAL.PersistenceHelper;
import br.com.mayron.matheus.armrobotcontrol.save.ListMovimentWithSave;

import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_NEXT_MOVEMENT;
import static br.com.mayron.matheus.armrobotcontrol.Constants.KEY_NEXT_POSITION;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);


        PersistenceHelper helper = PersistenceHelper.getInstance(getApplicationContext());
        helper.getReadableDatabase();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(!sharedPreferences.contains(KEY_NEXT_POSITION)){
            sharedPreferences.edit().putInt(KEY_NEXT_POSITION, 0).apply();
        }

        if(!sharedPreferences.contains(KEY_NEXT_MOVEMENT)){
            sharedPreferences.edit().putInt(KEY_NEXT_MOVEMENT, 0).apply();
        }

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(),Conectar.class);
                //Intent intent = new Intent(getApplicationContext(),ArmControlActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
