package cn.ac.iie.useragent;

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
import android.widget.Toast;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import cn.ac.iie.application.AppApplication;
import cn.ac.iie.fidoclient.Discovery;
import cn.ac.iie.fidoclient.IUAFClient;
import cn.ac.iie.fidoclient.IUAFErrorCallback;
import cn.ac.iie.fidoclient.IUAFResponseCallback;
import cn.ac.iie.fidoclient.UAFMessage;
import cn.ac.iie.interfaces.INetworkHandler;
import cn.ac.iie.network.UserNetwork;
import cn.ac.iie.rpclient.R;

/**
 * Created by wangyang on 14/12/25.
 */
public class LoginActivity extends BaseActivity {
    private IUAFClient mInterface;
    private String Key_Hash = "";

    private Discovery discovery;
    private UAFMessage message;

    private final static String TAG = "LoginActivity";

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Key_Hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", Key_Hash);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "key hash computation error");
        }catch ( NoSuchAlgorithmException e){
            Log.e(TAG, "key hash computation error");
        }

        Intent intent =  new Intent("android.intent.action.AIDLService");
        intent.setPackage("cn.ac.iie.fidoclient");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void authenticate(View v){
        Log.d("MyActivity", "authenticate button clicked");

        do_authenticate();
    }

    private void do_authenticate(){
        UserNetwork un = UserNetwork.getInstance();
        String response;
        try {
            response = un.authenticate();
            Toast.makeText(AppApplication.getContext(), "authenticate successful", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(AppApplication.getContext(), "authenticate error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        try{
            discovery = mInterface.getDiscovery();
            Toast.makeText(AppApplication.getContext(), discovery.clientVendor, Toast.LENGTH_SHORT).show();

            message = new UAFMessage();
            message.uafProtocolMessage = response;
            message.additionalData = "authenticate";
            mInterface.processUAFMessage(message, Key_Hash, null, true, responseCb, errorCb);
            mInterface.notifyUAFResult(1, "hello");
        }catch (RemoteException e){
            Log.e(TAG, "remoteException");
            Toast.makeText(AppApplication.getContext(), "error", Toast.LENGTH_LONG).show();
        }
    }

    private final IUAFErrorCallback.Stub errorCb = new IUAFErrorCallback.Stub() {
        @Override
        public void response(long code) throws RemoteException {
            Toast.makeText(AppApplication.getContext(), "error callback id invoked", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "error callback is invoked");
        }
    };

    private final IUAFResponseCallback.Stub responseCb = new IUAFResponseCallback.Stub() {
        @Override
        public void response(UAFMessage uafResponse) throws RemoteException {
            Toast.makeText(AppApplication.getContext(), "response callback is invoked", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "response callback is invoked");
        }
    };

}