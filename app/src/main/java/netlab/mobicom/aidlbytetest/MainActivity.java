package netlab.mobicom.aidlbytetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import netlab.mobicom.aidlserver.IByte;

public class MainActivity extends AppCompatActivity {

    IByte iByte;
    TextView textView;
    ImageView imageView;
    String urlString;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            long rangeS, rangeE;
            ByteArrayOutputStream EResponse = new ByteArrayOutputStream();
            final long LENGTH_PER_REQUEST = 1000*1024;
            rangeS = 0;
            rangeE = LENGTH_PER_REQUEST;
            iByte = IByte.Stub.asInterface(service);
            do {
                try {
                    String sProperty = "bytes=" + rangeS + "-" + rangeE;
                    byte[] byteFromRemote = iByte.getByteFromAIDL(urlString, sProperty);
                    if(byteFromRemote == null){
                        break;
                    }
                    Log.d("receiveByte","receive byte:" + byteFromRemote.length);
                    EResponse.write(byteFromRemote);
                    rangeS += LENGTH_PER_REQUEST;
                    rangeE += LENGTH_PER_REQUEST;
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while (true);
//            textView.setText(new String(EResponse.array()));
            try {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(EResponse.toByteArray(), 0, EResponse.toByteArray().length));
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iByte = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //允许主线程进行网络操作
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        textView = (TextView) findViewById(R.id.textview);
        imageView = (ImageView) findViewById(R.id.imageview);
//        urlString = "http://52.88.216.252/boat.jpg";
        urlString = "http://121.42.158.232/Book.jpg";

        //绑定服务
        Intent intent = new Intent("netlab.mobicom.aidlserver.ByteService").setPackage("netlab.mobicom.aidlserver");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }
}
