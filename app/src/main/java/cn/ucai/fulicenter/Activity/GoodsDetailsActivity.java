package cn.ucai.fulicenter.Activity;

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
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.PropertiesBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
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
    boolean isChecked;
    boolean isCarted = false;
    @BindView(R.id.iv_good_cart)
    ImageView ivGoodCart;
    int count = 1;
    CartBean[] mResult;

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
        isCart();
    }

    private void isCart() {
        L.e(TAG, "------------" + goodsId);
        if (user != null) {
            NetDao.findCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    if (result != null) {
                        mResult = result;
                        for (int i = 0; i < result.length; i++) {
                            if (mResult[i].getGoodsId() == goodsId) {

                                isCarted = true;
                            }
                        }
                    } else {
                        isCarted = false;
                    }
                    updateGoodsCartStatus();
                }

                @Override
                public void onError(String error) {
                    isCarted = false;
                }
            });
        }
        updateGoodsCartStatus();
    }

    @Override
    protected void initView() {
        mResult = new CartBean[100];
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

    private void updateGoodsCartStatus() {
        if (!isCarted) {
            ivGoodCart.setImageResource(R.mipmap.bg_cart_selected);
        } else {
            ivGoodCart.setImageResource(R.mipmap.menu_item_cart_selected);
        }
    }

    @OnClick(R.id.iv_good_cart)
    public void onCart() {
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            if (!isCarted) {
                NetDao.addCart(mContext, goodsId, user.getMuserName(), count, isChecked, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        isCart();

                    }

                    @Override
                    public void onError(String error) {
                        isCart();
                    }
                });
            } else {
                for (int i = 0; i < mResult.length; i++) {
                    if (mResult[i].getGoodsId() == goodsId) {

                    NetDao.deleteCart(mContext, mResult[i].getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            isCart();
                        }

                        @Override
                        public void onError(String error) {
                            isCart();
                        }
                    });
                    }
                }
            }
        }

    }

    @OnClick(R.id.iv_good_collect)
    public void onCollect() {
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
            return;
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

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    @OnClick(R.id.iv_good_share)
    public void onShareClick() {
        showShare();
    }


}

