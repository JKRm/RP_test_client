package cn.ac.iie.rpclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.ac.iie.fidoclient.Discovery;
import cn.ac.iie.fidoclient.IUAFClient;
import cn.ac.iie.fidoclient.IUAFErrorCallback;
import cn.ac.iie.fidoclient.IUAFResponseCallback;
import cn.ac.iie.fidoclient.StatusCode;
import cn.ac.iie.fidoclient.UAFMessage;


public class MainActivity extends Activity implements StatusCode {

    private IUAFClient mInterface;
    private Button bindButton;
    private Button unbindButton;
    private Button helloButton;
    private Discovery discovery;
    private UAFMessage message;
    private final static String TAG = "RP_Client_Main_Activity";
    private String Key_Hash = "";

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mInterface = IUAFClient.Stub.asInterface(service);
            Log.i(TAG, "Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mInterface = null;
            Log.i(TAG, "Service Disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the APK signing certificate and compute with SHA and Base64
        setContentView(R.layout.activity_main);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Key_Hash = Base64.encodeToString(md.digest(), Base64.URL_SAFE | Base64.NO_WRAP);
                Log.e("MY KEY HASH:", Key_Hash);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "key hash computation error");
        }catch ( NoSuchAlgorithmException e){
            Log.e(TAG, "key hash computation error");
        }
        bindButton = (Button)findViewById(R.id.bindButton);
        bindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent("android.intent.action.AIDLService");
                intent.setPackage("cn.ac.iie.fidoclient");
                bindService(intent, conn, Context.BIND_AUTO_CREATE);
                unbindButton.setEnabled(true);
                helloButton.setEnabled(true);
                bindButton.setEnabled(false);
            }
        });
        unbindButton = (Button)findViewById(R.id.unbind_button);
        unbindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(conn);
                unbindButton.setEnabled(false);
                bindButton.setEnabled(true);
                helloButton.setEnabled(false);
            }
        });
        message = new UAFMessage();
        message.uafProtocolMessage = "{\"policy\": {\"accepted\": [], \"disallowed\": [{\"aaid\": \"1234#5678\"}]}, " +
                "\"header\": {\"upv\": {\"mn\": 0, \"mj\": 1}, \"appid\": \"http://192.168.112.29:8000/trustedapps\"," +
                " \"op\": \"Reg\"}, \"username\": \"admin\", \"chanllenge\": " +
                "\"_Rmc5dhvFhCVsP3nzo6jTf3u26AZg3I7wkkCX5bb_aZ-eKdhJyXjdyBDruS84-Eyh3XoWkC10Ebmb-tz2gDG5g==\"}";
        message.uafProtocolMessage = "{\"policy\": {\"accepted\": [], \"disallowed\": [{\"aaid\": \"1234#5678\"}]}, " +
                "\"header\": {\"upv\": {\"mn\": 0, \"mj\": 1}, \"appid\": \"http://192.168.112.29:8000/trustedapps\"," +
                " \"op\": \"Auth\"}, \"challenge\": \"Wdc5YiMoc7mLJAe20ETkAQpESxuTUMjK2lY-CCDXkfkzAAQMJmJKWEFVrsvwkmDgLtNf_x-g5VpTbJXkAYy5Rg==\"," +
                " \"transaction\": {\"content\": \"hello world\", \"contentType\": \"text/plain\"}}";
        helloButton = (Button)findViewById(R.id.hello_button);
        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    discovery = mInterface.getDiscovery();
                    Toast.makeText(MainActivity.this, discovery.clientVendor, Toast.LENGTH_SHORT).show();
                    mInterface.processUAFMessage(message, Key_Hash, null, true, responseCb, errorCb);
                    mInterface.notifyUAFResult(UAF_STATUS_OK, "hello");
                }catch (RemoteException e){
                    Log.e(TAG, "remoteException");
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private final IUAFErrorCallback.Stub errorCb = new IUAFErrorCallback.Stub() {
        @Override
        public void response(long code) throws RemoteException {
            Toast.makeText(MainActivity.this, "error callback is invoked", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "error callback is invoked");
        }
    };

    private final IUAFResponseCallback.Stub responseCb = new IUAFResponseCallback.Stub() {
        @Override
        public void response(UAFMessage uafResponse) throws RemoteException {
            Toast.makeText(MainActivity.this, uafResponse.uafProtocolMessage, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "response callback is invoked");
        }
    };

}
