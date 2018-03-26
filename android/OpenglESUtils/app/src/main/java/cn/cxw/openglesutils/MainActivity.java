package cn.cxw.openglesutils;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.cxw.openglesutils.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        binding
    }
    public void onClick(View v)
    {
        if (v.getId() == R.id.tv_capturetest)
        {
            Intent intent = new Intent(this, CaptureTestActivity.class);
            startActivity(intent);
        }
    }
}
