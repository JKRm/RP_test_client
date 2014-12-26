package cn.ac.iie.useragent;

import android.os.Bundle;
import android.util.Log;

import java.util.Random;

import cn.ac.iie.interfaces.INetworkHandler;
import cn.ac.iie.network.UserNetwork;
import cn.ac.iie.rpclient.R;

/**
 * Created by wangyang on 14/12/25.
 */
public class LoginActivity extends BaseActivity implements INetworkHandler {

    int requestTag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authenticate();
    }

    public void authenticate(){
        Log.d("MyActivity", "authenticate button clicked");

        do_authenticate();
    }

    @Override
    public void networkCallback(String result, int httpCode, int requestTag) {
        dismissWaitDialog();
    }

    private void do_authenticate(){
        showWaitDialog("正在认证...", true);
        Random rand = new Random();
        requestTag = rand.nextInt();
        UserNetwork un = UserNetwork.getInstance(this, requestTag);
        un.authenticate();
    }
}