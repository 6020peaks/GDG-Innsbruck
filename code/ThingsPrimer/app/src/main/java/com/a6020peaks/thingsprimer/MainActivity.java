package com.a6020peaks.thingsprimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button helloBtn = findViewById(R.id.helloBtn);
        helloBtn.setOnClickListener(this);
        Button cameraBtn = findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.helloBtn:
                startActivity(new Intent(this, HelloActivity.class));
                break;
            case R.id.cameraBtn:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            default:
                throw new RuntimeException("Unknown id");
        }
    }
}
