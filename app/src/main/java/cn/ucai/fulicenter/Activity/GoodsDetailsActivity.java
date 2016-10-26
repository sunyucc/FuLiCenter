package cn.ucai.fulicenter.Activity;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FlowIndicator;
import cn.ucai.fulicenter.views.SlideAutoLoopView;

public class GoodsDetailsActivity extends BaseActivity {
    private static final String TAG = GoodsDetailsActivity.class.getSimpleName();
    GoodsDetailsActivity mContext;
    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_good_name_english)
    TextView tvGoodNameEnglish;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_good_price_shop)
    TextView tvGoodPriceShop;
    @BindView(R.id.tv_good_price_current)
    TextView tvGoodPriceCurrent;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.layout_image)
    RelativeLayout layoutImage;
    @BindView(R.id.wv_good_brief)
    WebView wvGoodBrief;
    int goodsId;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    boolean isCollected = false;
    @BindView(R.id.iv_good_collect)
    ImageView mIvGoodCollect;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("details", "goodsId" + goodsId);
        if (goodsId == 0) {
            finish();
        }
        mContext = this;
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void initData() {
        NetDao.downloadGoodsDetail(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details=" + result);
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
        tvGoodNameEnglish.setText(details.getGoodsEnglishName());
        tvGoodName.setText(details.getGoodsName());
        tvGoodPriceShop.setText(details.getShopPrice());
        tvGoodPriceCurrent.setText(details.getCurrencyPrice());
        salv.startPlayLoop(indicator, getAlbumImgUrl(details), getAlbumImgCount(details));
        wvGoodBrief.loadDataWithBaseURL(null, details.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlbumImgCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] urls = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            urls = new String[albums.length];
            for (int i = 0; i < albums.length; i++) {
                urls[i] = albums[i].getImgUrl();
            }

        }
        return urls;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollect();
    }

    @Override
    protected void initView() {
    }

    @OnClick(R.id.backClickArea)
    public void onBackClick(View v) {
        MFGT.finish(this);
    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }

    private void isCollect() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.isCollected(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollected = true;

                    } else {
                        isCollected = false;
                    }
                    updateGoodsCollectStatus();
                }

                @Override
                public void onError(String error) {
                    isCollected = false;
                }
            });

        }
        updateGoodsCollectStatus();
    }

    private void updateGoodsCollectStatus() {
        if (isCollected) {
            mIvGoodCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            mIvGoodCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @OnClick(R.id.iv_good_collect)
    public void onClick() {
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {

            if (!isCollected) {

                NetDao.addCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect();
                            CommonUtils.showShortToast(R.string.ADD_COLLECT_SUCCESS);
                        } else {
                            CommonUtils.showShortToast(R.string.ADD_COLLECT_FAIL);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "error" + error);
                        CommonUtils.showShortToast(R.string.ADD_COLLECT_FAIL);
                    }
                });
            } else {
                NetDao.deleteCollects(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect();
                            CommonUtils.showShortToast(R.string.DELETE_COLLECT_SUCCESS);
                        } else {
                            CommonUtils.showShortToast(R.string.DELETE_COLLECT_FAIL);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "error" + error);
                        CommonUtils.showShortToast(R.string.DELETE_COLLECT_FAIL);
                    }
                });
            }

        }
    }
}

