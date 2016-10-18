package cn.ucai.fulicenter.fragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
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
    Context mContext;
    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    int mPageId=1;
    GridLayoutManager glm ;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_newgoods, container, false);
        ButterKnife.bind(this, layout);
        mContext = getContext();
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext,mList);
        glm= new GridLayoutManager(mContext, I.COLUM_NUM);
        initView();
        initData();
        return layout;
    }

    private void initData() {
        NetDao.downloadNewGoods(mContext, mPageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                L.e("result="+result);
                if(result!=null && result.length>0){
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    mAdapter.initData(list);
                }
            }

            @Override
            public void onError(String error) {
                L.e("error:"+error);
            }
        });
    }

    private void initView() {
        srl.setColorSchemeColors(getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow));
        mRv.setLayoutManager(glm);
        mRv.setHasFixedSize(true);
        mRv.setAdapter(mAdapter);
    }

}
