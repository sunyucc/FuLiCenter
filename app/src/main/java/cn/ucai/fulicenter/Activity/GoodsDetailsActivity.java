package cn.ucai.fulicenter.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.FlowIndicator;
import cn.ucai.fulicenter.views.SlideAutoLoopView;

public class GoodsDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvgoodenglish)
    TextView tvgoodenglish;
    @BindView(R.id.tvGoodName)
    TextView tvGoodName;
    @BindView(R.id.tvgoodPriceShop)
    TextView tvgoodPriceShop;
    @BindView(R.id.tvGoodPriceCurrent)
    TextView tvGoodPriceCurrent;
    @BindView(R.id.goodbref)
    WebView goodbref;
    int goodsId;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.ivgoodshare)
    ImageView ivgoodshare;
    @BindView(R.id.ivcollect)
    ImageView ivcollect;
    @BindView(R.id.ivgoodcart)
    ImageView ivgoodcart;
    @BindView(R.id.tvcartcount)
    TextView tvcartcount;
    @BindView(R.id.layouttitle)
    RelativeLayout layouttitle;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details", "goodsId" + goodsId);
        if (goodsId == 0) {
            finish();
        }
        mContext=this;
        initVeiw();
        initData();
        setListener();

    }

    private void setListener() {
    }

    private void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details="+result);
                if (result != null) {
                    showGoogDetails(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    private void showGoogDetails(GoodsDetailsBean details) {
        tvgoodenglish.setText(details.getGoodsEnglishName());
        tvGoodName.setText(details.getGoodsName());
        tvgoodPriceShop.setText(details.getShopPrice());
        tvGoodPriceCurrent.setText(details.getCurrencyPrice());
//        salv.startPlayLoop(indicator,);
    }

    private void initVeiw() {
    }
}
