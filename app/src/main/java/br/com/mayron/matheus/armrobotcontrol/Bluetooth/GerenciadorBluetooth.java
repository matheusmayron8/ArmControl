package br.com.mayron.matheus.armrobotcontrol.Bluetooth;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import br.com.mayron.matheus.armrobotcontrol.DAL.Posicao;

/**
 * @author Matheus Mayron
 * @since 27/09/2017.
 */

public class GerenciadorBluetooth extends Application {
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter btAdapter;
    private BluetoothDevice device;
    private BluetoothSocket btSocket;
    private static final String TAG = "Menu Principal";

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            btSocket.close();
        } catch (IOException e) {
        }
    }

    public boolean abrirSocket(String address) {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
            btSocket.connect();
            return true;
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Falha ao conectar o dispositivo", Toast.LENGTH_LONG).show();
            try{
                btSocket.close();
            } catch (IOException ex) {
                return false;
            }
            return false;
        }
    }

    public void fecharSocket(){
        try {
            btSocket.close();
        } catch (IOException e) {
        }
    }

    public boolean enviarComando(List<Posicao> posicaoList) {
        if(posicaoList == null){
            Log.d(TAG, "lista de Posições é nula");
            return false;
        }

        if(posicaoList.isEmpty()){
            Log.d(TAG, "Lista de Posições esta vazia");
            return false;
        }

        try{
            String comando = String.valueOf(posicaoList.size());
            for(Posicao posicao: posicaoList) {
                comando = comando.concat(posicao.toString());
            }
            //todo: remover linha abaixo
            comando = comando.concat("\n");
            Toast.makeText(this, comando, Toast.LENGTH_LONG).show();

            byte[] msgBuffer = comando.getBytes();           //converts entered String into bytes

            OutputStream mmOutStream = btSocket.getOutputStream();
            mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            return true;

        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean enviarComando(Posicao posicao) {

        if(posicao == null){
            Log.d(TAG, "posição é nula");
            return false;
        }

        try{
            OutputStream mmOutStream = btSocket.getOutputStream();

            String comando = "1";
            comando = comando.concat(posicao.toString());
            //todo: remover linha abaixo
            comando = comando.concat("\n");

            byte[] msgBuffer = comando.getBytes();           //converts entered String into bytes
            mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            return true;

        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean enviarComando(String comando) {

        if(comando == null){
            Log.d(TAG, "comando é nulo");
            return false;
        }

        try{
            //todo: remover linha abaixo
            comando = comando.concat("\n");

            OutputStream mmOutStream = btSocket.getOutputStream();

            byte[] msgBuffer = comando.getBytes();           //converts entered String into bytes
            mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            return true;

        }
        catch (IOException e) {
            return false;
        }
    }



    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }
}