package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FooterViewHolder;

/**
 * Created by sunyu on 2016/10/17.
 */

public class CollectsAdapter extends Adapter {
    Context mContext;
    ArrayList<CollectBean> mList;
    String footerText;

    public int getSoryBy() {
        return soryBy;
    }

    public void setSoryBy(int soryBy) {
        this.soryBy = soryBy;
        notifyDataSetChanged();
    }

    int soryBy = I.SORT_BY_ADDTIME_DESC;

    /**
     * 添加新的一页数据
     *
     * @param list
     */
    public void addData(ArrayList<CollectBean> list) {

        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setFooter(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    boolean isMore;

    public CollectsAdapter(Context mContext, ArrayList mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new CollectsViewHolder(View.inflate(mContext, R.layout.item_collect, null));
        }

        return holder;
    }

    public void initData(ArrayList<CollectBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFooterString());
        } else {
            CollectsViewHolder gvh = (CollectsViewHolder) holder;
            CollectBean goods = mList.get(position);
            ImageLoader.downloadImg(mContext, gvh.ivCollectThumb, goods.getGoodsThumb(), true);
            gvh.ivCollectName.setText(goods.getGoodsName());
            gvh.layoutCollect.setTag(goods.getGoodsId());
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    class CollectsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivCollectThumb)
        ImageView ivCollectThumb;
        @BindView(R.id.ivCollectName)
        TextView ivCollectName;
        @BindView(R.id.imageButton)
        ImageButton imageButton;
        @BindView(R.id.layout_collect)
        RelativeLayout layoutCollect;

        CollectsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.layout_collect)
        public void onCollectClock() {
            int goodsId = (int) layoutCollect.getTag();
            MFGT.gotoGoodsDetailsActivity(mContext, goodsId);
        }
    }

    private int getFooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }


}
