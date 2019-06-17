package com.mycompany.farm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mycompany.farm.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /* main.xml
    *       android:text="登录/注册"
            android:background="@color/CadetBlue"
            android:onClick="login"
    * */
    // 点击以后通过 startActivity跳转到另一个 activity: LoginActivity
    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));

    }
    /*main.xml
    *       android:text="退出"
            android:background="@color/DarkCyan"
            android:onClick="exit"
    * */
    public void exit(View view) {
        finish();
    }

}
