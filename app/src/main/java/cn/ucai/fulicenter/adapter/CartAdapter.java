package cn.ucai.fulicenter.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.fragment.CateFragment;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FooterViewHolder;

/**
 * Created by sunyu on 2016/10/27.
 */

public class CartAdapter extends Adapter {
    Context mContext;
    ArrayList<CartBean> mList;
    String footerText;
    int count;
    boolean isChecked = false;

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
    public void addData(ArrayList<CartBean> list) {
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

    public CartAdapter(Context mContext, ArrayList mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new CartViewHolder(View.inflate(mContext, R.layout.item_cart, null));
        }

        return holder;
    }

    public void initData(ArrayList<CartBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFooterString());
        } else {
            CartViewHolder ch = (CartViewHolder) holder;
            final CartBean goods = mList.get(position);
            ImageLoader.downloadImg(mContext, ch.ivGoodsThumb, goods.getGoods().getGoodsThumb(), true);
            ch.tvGoodsName.setText(goods.getGoods().getGoodsName());
            ch.tvCartCount.setText(goods.getCount() + "");
            ch.tvGoodsPrice.setText(goods.getGoods().getCurrencyPrice());
            ch.chkSelect.setChecked(false);
            ch.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    goods.setChecked(isChecked);
                    mContext.sendBroadcast(new Intent(I.BOEADCAST_UPDATA_CART));
                }
            });
            ch.mRelCart.setTag(position);
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

    private int getFooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }


    class CartViewHolder extends ViewHolder {
        @BindView(R.id.chkSelect)
        CheckBox chkSelect;
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.ivAddCart)
        ImageView ivAddCart;
        @BindView(R.id.tvCartCount)
        TextView tvCartCount;
        @BindView(R.id.ivDelCart)
        ImageView ivReduceCart;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.layout_cart)
        RelativeLayout mRelCart;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.layout_cart)
        public void onCart(){
            final int position = (int) mRelCart.getTag();
            CartBean cart = mList.get(position);
            MFGT.gotoGoodsDetailsActivity(mContext,cart.getGoodsId());
        }
        @OnClick(R.id.ivAddCart)
        public void addCart() {
            final int position = (int) mRelCart.getTag();
            CartBean cart = mList.get(position);
            NetDao.updateCartCount(mContext, cart.getId(), cart.getCount() + 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        mList.get(position).setCount(mList.get(position).getCount() + 1);
                        mContext.sendBroadcast(new Intent(I.BOEADCAST_UPDATA_CART));
                        tvCartCount.setText("(" + (mList.get(position).getCount()) + ")");
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }

        @OnClick(R.id.ivDelCart)
        public void delCart() {
            final int position = (int) mRelCart.getTag();
            CartBean cart = mList.get(position);
            if (cart.getCount() > 1) {
                NetDao.updateCartCount(mContext, cart.getId(), cart.getCount() - 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            mList.get(position).setCount(mList.get(position).getCount() - 1);
                            mContext.sendBroadcast(new Intent(I.BOEADCAST_UPDATA_CART));
                            tvCartCount.setText(mList.get(position).getCount());
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                NetDao.deleteCart(mContext, cart.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            mList.remove(position);
                            mContext.sendBroadcast(new Intent(I.BOEADCAST_UPDATA_CART));
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }
}