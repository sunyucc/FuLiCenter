package cn.ucai.fulicenter.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.MFGT;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        setListener();
        
    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void setListener();

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }
}
