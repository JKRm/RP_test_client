package cn.ac.iie.useragent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import cn.ac.iie.interfaces.INetworkHandler;
import cn.ac.iie.network.UserNetwork;
import cn.ac.iie.rpclient.R;

/**
 * Created by wangyang on 14/12/25.
 */
public class BindActivity extends BaseActivity implements INetworkHandler {

    EditText usernameEv;
    EditText passwordEv;
    int requestTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        usernameEv = (EditText) findViewById(R.id.user_login_tv);
        passwordEv = (EditText) findViewById(R.id.user_pass_tv);


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

    @Override
    public void networkCallback(String result, int httpCode, int requestTag) {
        dismissWaitDialog();
    }

    private void do_bind(String username, String password){
        showWaitDialog("正在绑定...", true);
        Random rand = new Random();
        requestTag = rand.nextInt();
        UserNetwork un = UserNetwork.getInstance(this, requestTag);
        un.bind(username, password);
    }
}