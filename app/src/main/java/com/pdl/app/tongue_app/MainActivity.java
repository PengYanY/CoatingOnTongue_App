package com.pdl.app.tongue_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("pdl_main","onCreate");
    }
    //打开相机
    public void openCamera(View view) {
        Intent it=new Intent(MainActivity.this,CameraActivity.class);
        startActivity(it);
    }
    //选取图片
    public void pickImage(View view){
        Intent it=new Intent(MainActivity.this,PickImageActivity.class);
        startActivity(it);
    }
}
