package cn.ac.iie.useragent;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.ac.iie.application.AppApplication;
import cn.ac.iie.rpclient.R;

public class MyActivity extends BaseActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void bind(View view){
        Log.d("MyActivity", "bind button clicked");
        Intent intent = new Intent(this, BindActivity.class);
        startActivity(intent);
    }
    public void login(View view){
        Log.d("MyActivity", "login button clicked");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void local_auth(View view){
        Log.d("MyActivity", "local_auth button clicked");
        Intent mIntent = new Intent();
        mIntent.setAction("cn.ac.iie.action.AUTHENTICATE");
        startActivityForResult(mIntent, 1);
    }

    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     *
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
        Log.i("MyActivity", result);
        if(resultCode == RESULT_OK){
            Toast.makeText(AppApplication.getContext(), "认证成功", Toast.LENGTH_LONG).show();
        }
    }
}
