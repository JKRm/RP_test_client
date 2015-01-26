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
import android.os.Message;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpStatus;

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
import cn.ac.iie.model.BindRequest;
import cn.ac.iie.network.UserNetwork;
import cn.ac.iie.rpclient.R;

/**
 * Created by wangyang on 14/12/25.
 */
public class BindActivity extends BaseActivity {

    EditText usernameEv;
    EditText passwordEv;

    private IUAFClient mInterface;
    private String Key_Hash = "";

    private Discovery discovery;
    private UAFMessage message;

    private final static String TAG = "BindActivity";

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
        setContentView(R.layout.activity_bind);
        usernameEv = (EditText) findViewById(R.id.user_login_tv);
        passwordEv = (EditText) findViewById(R.id.user_pass_tv);

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

        Intent intent =  new Intent("android.intent.action.AIDLService");
        intent.setPackage("cn.ac.iie.fidoclient");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    public void bind(View view){
        Log.d("MyActivity", "bind button clicked");
        String username = usernameEv.getText().toString().replace(" ", "");
        String password = passwordEv.getText().toString();

        if (username == null  || username.length() == 0){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
        }
        if (password == null  || password.length() == 0){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
        }
        do_bind(username, password);
    }


    private void do_bind(String username, String password){
//        showWaitDialog("正在绑定...", true);
        UserNetwork un = UserNetwork.getInstance();
        String response = null;
        try {
            response = un.bind(username, password);
            Toast.makeText(AppApplication.getContext(), "bind successful", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(AppApplication.getContext(), "bind error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        try{
            discovery = mInterface.getDiscovery();
            Toast.makeText(BindActivity.this, discovery.clientVendor, Toast.LENGTH_SHORT).show();

            message = new UAFMessage();
            message.uafProtocolMessage = response;
            message.additionalData = "bind";
            mInterface.processUAFMessage(message, Key_Hash, null, true, responseCb, errorCb);
            //TODO: 完成notifyUAFResult
//            mInterface.notifyUAFResult(0, "hello");
        }catch (RemoteException e){
            Log.e(TAG, "remoteException");
            Toast.makeText(BindActivity.this, "error", Toast.LENGTH_LONG).show();
        }
    }

    private final IUAFErrorCallback.Stub errorCb = new IUAFErrorCallback.Stub() {
        @Override
        public void response(long code) throws RemoteException {
            Toast.makeText(BindActivity.this, "error callback id invoked", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "error callback is invoked");
        }
    };

    private final IUAFResponseCallback.Stub responseCb = new IUAFResponseCallback.Stub() {
        @Override
        public void response(UAFMessage uafResponse) throws RemoteException {
            Toast.makeText(BindActivity.this, uafResponse.uafProtocolMessage, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "response callback is invoked");

            UserNetwork un = UserNetwork.getInstance();
            String response = null;
            try {
                response = un.uploadBindResponse(uafResponse.uafProtocolMessage);
                Toast.makeText(AppApplication.getContext(), "bind successful", Toast.LENGTH_LONG).show();
                if(response != null && response.length() != 0){
                    mInterface.notifyUAFResult(0, response);
                }
            } catch (IOException e) {
                Toast.makeText(AppApplication.getContext(), "bind error", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return;
            }

        }
    };
}