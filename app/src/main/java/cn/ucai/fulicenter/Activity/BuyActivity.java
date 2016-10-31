package cn.ucai.fulicenter.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.views.DisplayUtils;

public class BuyActivity extends BaseActivity {
    BuyActivity mContext;
    double mPrice;
    @BindView(R.id.tv_price_cart)
    TextView mTvPriceCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this, "添加收货地址");
        double price = getIntent().getDoubleExtra(I.SUM_CART,0.0);
        mTvPriceCart.setText(price+"");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

}
