package cn.ucai.fulicenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

public class BoutiqueFragment extends Fragment {
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    ArrayList<BoutiqueBean> mList;
    BoutiqueAdapter mAdapter;
    LinearLayoutManager llm;
    int mPageId = 1 ;
    MainActivity mContext  ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_newgoods, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        mList = new ArrayList<>();
        mAdapter = new BoutiqueAdapter(mContext,mList);
        initView();
        initData();
        setListener();
        return layout;
    }
    private void downloadNewGoods(final int action) {
       NetDao.downloadGoodsDetail(mContext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
           @Override
           public void onSuccess(BoutiqueBean[] result) {
               srl.setRefreshing(false);
               textView.setVisibility(View.GONE);
               mAdapter.setMore(true);
               L.e("result="+result);
               if (result != null && result.length > 0) {
                   ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                   if (action != I.ACTION_PULL_UP) {
                       mAdapter.initData(list);
                   } else {
                       mAdapter.addList(list);
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
               textView.setVisibility(View.GONE);
               CommonUtils.showLongToast(error);
               L.e("error:"+error);
           }
       });
    }

    private void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                recyclerView.setVisibility(View.VISIBLE);
                mPageId=1;
                downloadNewGoods(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
            }
        });
    }

    private void initData() {
        downloadNewGoods(I.ACTION_DOWNLOAD);
    }

    private void initView() {
        srl.setColorSchemeColors(getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow));
        llm= new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(12));
    }
}
