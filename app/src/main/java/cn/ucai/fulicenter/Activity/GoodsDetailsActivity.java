package cn.ucai.fulicenter.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

public class GoodsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
       int goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details","goodsId"+goodsId);
    }
}
