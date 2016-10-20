package cn.ucai.fulicenter.Activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.CatChildFilterButton;
import cn.ucai.fulicenter.views.SpaceItemDecoration;


public class CategoryChildActivity extends BaseActivity {

    @BindView(R.id.ivReturn)
    ImageView ivReturn;
    @BindView(R.id.layout_title)
    RelativeLayout layoutTitle;
    @BindView(R.id.layout_sort)
    LinearLayout layoutSort;
    @BindView(R.id.tv_refresh_hint)
    TextView tvRefreshHint;
    @BindView(R.id.rv_category_child)
    RecyclerView rvCategoryChild;
    @BindView(R.id.srl_category_child)
    SwipeRefreshLayout srlCategoryChild;

    CategoryChildActivity mContext;
    GoodsAdapter mAdapter;
    ArrayList<CategoryGroupBean> mList;
    int pageId = 1;
    GridLayoutManager glm;
    int catId;
    @BindView(R.id.btnPriceSort)
    Button btnPriceSort;
    @BindView(R.id.btnAddTimeSort)
    Button btnAddTimeSort;
    boolean addTimeAsc = false;
    boolean priceAsc = false;
    int sortBy = I.SORT_BY_ADDTIME_DESC;
    @BindView(R.id.btnCatChildFilter)
    CatChildFilterButton btnCatChildFilter;
    String groupName;
    ArrayList<CategoryChildBean> mChildList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext, mList);
        catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        if (catId == 0) {
            finish();
        }
        groupName = getIntent().getStringExtra(I.CategoryGroup.NAME);
        mChildList = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        srlCategoryChild.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        rvCategoryChild.setLayoutManager(glm);
        rvCategoryChild.setHasFixedSize(true);
        rvCategoryChild.setAdapter(mAdapter);
        rvCategoryChild.addItemDecoration(new SpaceItemDecoration(12));
        btnCatChildFilter.setText(groupName);
    }

    @Override
    protected void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() {
        srlCategoryChild.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlCategoryChild.setRefreshing(true);
                tvRefreshHint.setVisibility(View.VISIBLE);
                pageId = 1;
                downloadCategoryGoods(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void downloadCategoryGoods(final int action) {
        NetDao.downloadCategoryGoods(mContext, catId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srlCategoryChild.setRefreshing(false);
                tvRefreshHint.setVisibility(View.GONE);
                mAdapter.setMore(true);
                L.e("result=" + result);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if (action != I.ACTION_PULL_UP) {
                        mAdapter.initData(list);
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
                srlCategoryChild.setRefreshing(false);
                tvRefreshHint.setVisibility(View.GONE);
                CommonUtils.showLongToast(error);
                L.e("error:" + error);
            }
        });
    }

    private void setPullUpListener() {
        rvCategoryChild.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = glm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == mAdapter.getItemCount() - 1
                        && mAdapter.isMore()) {
                    pageId++;
                    downloadCategoryGoods(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = glm.findFirstVisibleItemPosition();
                srlCategoryChild.setEnabled(firstPosition == 0);
            }
        });
    }

    @Override
    protected void initData() {
        downloadCategoryGoods(I.ACTION_DOWNLOAD);
        btnCatChildFilter.setOnCatFilterClickListener(groupName,mChildList);
    }

    @OnClick(R.id.ivReturn)
    public void onClick() {
        MFGT.finish(this);

    }

    @OnClick({R.id.btnPriceSort, R.id.btnAddTimeSort})
    public void onClick(View view) {
        Drawable right;
        switch (view.getId()) {
            case R.id.btnPriceSort:
                if (priceAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    btnPriceSort.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                }
                priceAsc = !priceAsc
                ;
                break;
            case R.id.btnAddTimeSort:
                if (addTimeAsc) {
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    btnAddTimeSort.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                }
                addTimeAsc = !addTimeAsc;
                break;
        }
        mAdapter.setSoryBy(sortBy);
    }
}
