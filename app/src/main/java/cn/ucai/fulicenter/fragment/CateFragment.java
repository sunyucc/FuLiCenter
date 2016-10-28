package cn.ucai.fulicenter.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.views.SpaceItemDecoration;


public class CateFragment extends BaseFragment {
    Context mContext;
    CartAdapter mAdapter;
    ArrayList<CartBean> mList;
    int mPageId = 1;
    LinearLayoutManager llm;
    @BindView(R.id.tv_refresh)
    TextView mTv;
    @BindView(R.id.recyclerView)
    RecyclerView mRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tvSumPrice)
    TextView tvSumPrice;
    @BindView(R.id.tvSavePrice)
    TextView tvSavePrice;
    CartBean bean;
    updateCartReceiver mReceiver;
    @BindView(R.id.tv_nothing)
    TextView mTvNothing;
    @BindView(R.id.layout_rel_cart)
    RelativeLayout mLayoutCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, layout);
        mContext = getContext();
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(mContext, mList);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void setListener() {
        setPullUpListener();
        setPullDownListener();
        IntentFilter filter = new IntentFilter(I.BOEADCAST_UPDATA_CART);
        mReceiver = new updateCartReceiver();
        mContext.registerReceiver(mReceiver, filter);
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                mRv.setVisibility(View.VISIBLE);
                mPageId = 1;
                downloadNewGoods(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void downloadNewGoods(final int action) {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.findCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    srl.setRefreshing(false);
                    mTv.setVisibility(View.GONE);
                    mAdapter.setMore(true);
                    L.e("result=" + result);
                    if (result != null && result.length > 0) {
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        if (action != I.ACTION_PULL_UP) {
                            mList = list;
                            mAdapter.initData(list);

                            sumPrice();
                        } else {
                            mAdapter.addData(list);
                        }

                        if (list.size() < I.PAGE_SIZE_DEFAULT) {
                            mAdapter.setMore(false);
                        }
                    } else {
                        mAdapter.setMore(false);
                    }
                }

                @Override
                public void onError(String error) {
                    srl.setRefreshing(false);
                    mTv.setVisibility(View.GONE);
                    mAdapter.setMore(false);
                    CommonUtils.showShortToast(error);
                    L.e("error:" + error);
                }
            });
        }
    }

    private void setPullUpListener() {
        mRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = llm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastPosition == mAdapter.getItemCount() - 1 &&
                        mAdapter.isMore()) {
                    mPageId++;
                    L.e(String.valueOf(mAdapter.isMore()));
                    downloadNewGoods(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = llm.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition == 0);
            }
        });
    }

    @Override
    protected void initData() {
        downloadNewGoods(I.ACTION_DOWNLOAD);

    }

    @Override
    protected void initView() {
        srl.setColorSchemeColors(getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow));
        llm = new LinearLayoutManager(mContext);
        mRv.setLayoutManager(llm);
        mRv.setHasFixedSize(true);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(new SpaceItemDecoration(12));
        bean = new CartBean();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        mRv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    class updateCartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            sumPrice();
            setCartLayout(mList != null && mList.size() > 0);
        }
    }

    private void setCartLayout(boolean hasCart) {
        mLayoutCart.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        mTvNothing.setVisibility(hasCart ? View.GONE : View.VISIBLE);
        mRv.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        sumPrice();
    }

    private void sumPrice() {
        int sumPrice = 0;
        int rankPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                if (c.isChecked()) {
                    sumPrice += getPrice(c.getGoods().getCurrencyPrice()) * c.getCount();
                    rankPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                }
            }
            tvSumPrice.setText("合计:￥" + Double.valueOf(rankPrice));
            tvSavePrice.setText("节省:￥" + Double.valueOf(sumPrice - rankPrice));

        } else {
            tvSumPrice.setText("合计:￥0");
            tvSavePrice.setText("节省:￥0");
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
    }
}
