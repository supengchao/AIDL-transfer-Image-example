package netlab.mobicom.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jym on 2016/2/26.
 */
public class ByteService extends Service{

    final static int BUFFER_SIZE =4096;

    IByte.Stub binder = new IByte.Stub(){

        @Override
        public byte[] getByteFromAIDL(String urlString, String sProperty) throws RemoteException {
            byte[] data = new byte[BUFFER_SIZE];
            try {
                //这里是aidl接口的真正实现部分，一旦远端进程调用了getByteFromAIDL都会执行这里的操作。
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("RANGE", sProperty);
                InputStream is = httpURLConnection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int count = -1;
                while((count = is.read(data, 0, BUFFER_SIZE)) != -1)
                    outputStream.write(data, 0 , count);
                data = null;
                return outputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
