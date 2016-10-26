package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FooterViewHolder;

/**
 * Created by sunyu on 2016/10/17.
 */

public class GoodsAdapter extends Adapter {
    Context mContext;
    ArrayList<NewGoodsBean> mList;
    String footerText;

    public int getSoryBy() {
        return soryBy;
    }

    public void setSoryBy(int soryBy) {
        this.soryBy = soryBy;
        sortBy();
        notifyDataSetChanged();
    }

    int soryBy = I.SORT_BY_ADDTIME_DESC;

    /**
     * 添加新的一页数据
     * @param list
     */
    public void addData(ArrayList<NewGoodsBean> list) {

        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setFooter(String footerText) {
        this.footerText=footerText;
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
    public GoodsAdapter(Context mContext, ArrayList mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new GoodsViewHolder(View.inflate(mContext, R.layout.item_goods, null));
        }

        return holder;
    }

    public void initData(ArrayList<NewGoodsBean> list) {
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
            GoodsViewHolder gvh = (GoodsViewHolder) holder;
            NewGoodsBean goods = mList.get(position);
            ImageLoader.downloadImg(mContext,gvh.ivGoodsThumb,goods.getGoodsThumb(),true);
            gvh.ivGoodsName.setText(goods.getGoodsName());
            gvh.tvGoodsPrice.setText(goods.getCurrencyPrice());
            gvh.mLayout_goods.setTag(goods.getGoodsId());
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

     class GoodsViewHolder extends ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.ivGoodsName)
        TextView ivGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.layout_goods)
        LinearLayout mLayout_goods;
        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.layout_goods)
        public void onGoodsItemClick(){
            int goodsId = (int) mLayout_goods.getTag();
            MFGT.gotoGoodsDetailsActivity(mContext,goodsId);
        }
    }
    private int getFooterString(){
        return isMore ? R.string.load_more : R.string.no_more;
    }

    private void sortBy(){
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                int result=0;
                switch (soryBy){
                    case I.SORT_BY_ADDTIME_ASC:
                        result= (int) (Long.valueOf(left.getAddTime())-Long.valueOf(right.getAddTime()));
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result= (int) (Long.valueOf(right.getAddTime())-Long.valueOf(left.getAddTime()));
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(left.getCurrencyPrice())-getPrice(right.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(right.getCurrencyPrice())-getPrice(left.getCurrencyPrice());
                        break;
                }
                return result;
            }
            private int getPrice(String price){
                price = price.substring(price.indexOf("￥")+1);
                return Integer.valueOf(price);
            }
        });
    }

}
