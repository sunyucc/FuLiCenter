package cn.ucai.fulicenter.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.i("MainActivity.onCreate");
    }

    public void onCheckedChange(View view) {

    }
}
