package cn.ac.iie.rpclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.ac.iie.fidoclient.Discovery;
import cn.ac.iie.fidoclient.IUAFClient;
import cn.ac.iie.fidoclient.IUAFErrorCallback;
import cn.ac.iie.fidoclient.IUAFResponseCallback;
import cn.ac.iie.fidoclient.UAFMessage;


public class MainActivity extends Activity {

    private IUAFClient mInterface;
    private Button bindButton;
    private Button unbindButton;
    private Button helloButton;
    private Discovery discovery;
    private UAFMessage message;
    private final static String TAG = "RP_Client_Main_Activity";

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
        setContentView(R.layout.activity_main);
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
                helloButton.setEnabled(true);
            }
        });
        message = new UAFMessage();
        helloButton = (Button)findViewById(R.id.hello_button);
        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    discovery = mInterface.getDiscovery();
                    Toast.makeText(MainActivity.this, discovery.clientVendor, Toast.LENGTH_SHORT).show();
                    mInterface.processUAFMessage(message, null, null, true, responseCb, errorCb);
                    mInterface.notifyUAFResult(1, "hello");
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
            Toast.makeText(MainActivity.this, "error callback id invoked", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "error callback is invoked");
        }
    };

    private final IUAFResponseCallback.Stub responseCb = new IUAFResponseCallback.Stub() {
        @Override
        public void response(UAFMessage uafResponse) throws RemoteException {
            Toast.makeText(MainActivity.this, "response callback is invoked", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "response callback is invoked");
        }
    };

}
