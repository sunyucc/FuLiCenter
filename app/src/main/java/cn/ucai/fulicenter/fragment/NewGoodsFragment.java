package cn.ucai.fulicenter.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by sunyu on 2016/10/17.
 */

public class NewGoodsFragment extends Fragment {
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.textView)
    TextView mTv;
    @BindView(R.id.recyclerView)
    RecyclerView mRv;
    MainActivity mContext;
    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_newgoods, container, false);
        ButterKnife.bind(this, layout);
        mContext = new MainActivity();
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext,mList);
        initView();
        initData();
        return layout;
    }

    private void initData() {
        NetDao.downloadNewGoods(mContext,1, new OkHttpUtils.OnCompleteListener<NewGoodsBean>() {
            @Override
            public void onSuccess(NewGoodsBean result) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initView() {
        srl.setColorSchemeColors(getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow));
        GridLayoutManager glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        mRv.setLayoutManager(glm);
        mRv.setHasFixedSize(true);
        mRv.setAdapter(mAdapter);
    }

}
