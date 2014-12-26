package cn.ac.iie.useragent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.ac.iie.rpclient.R;

public class MyActivity extends BaseActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
}
