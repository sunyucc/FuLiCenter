package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FooterViewHolder;

/**
 * Created by sunyu on 2016/10/19.
 */

public class BoutiqueAdapter extends Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mList;
    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public BoutiqueAdapter(Context mContext,ArrayList<BoutiqueBean> list) {
        this.mContext = mContext;
        this.mList=list;
    }

    public void addList(ArrayList<BoutiqueBean> list) {

        this.mList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_footer, parent, false));
        } else {
            holder = new BoutiqueHolder(View.inflate(mContext, R.layout.item_boutique, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewholder = (FooterViewHolder) holder;
            footerViewholder.tvFooter.setText(getFooterString());

        } else {
            BoutiqueBean boutiquebean = mList.get(position);
            BoutiqueHolder boutiqueHolder = (BoutiqueHolder) holder;
            ImageLoader.downloadImg(mContext,
                    ((BoutiqueHolder) holder).ivBoutiqueImg
                    , boutiquebean.getImageurl(),true);
            boutiqueHolder.tvBoutiqueDescription.setText(boutiquebean.getDescription());
            boutiqueHolder.tvBoutiqueName.setText(boutiquebean.getName());
            boutiqueHolder.tvBoutiqueTitle.setText(boutiquebean.getTitle());
            boutiqueHolder.layoutBoutiqueItem.setTag(boutiquebean);

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
        } else {
            return I.TYPE_ITEM;
        }
    }

     class BoutiqueHolder extends ViewHolder{
        @BindView(R.id.ivBoutiqueImg)
        ImageView ivBoutiqueImg;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueDescription)
        TextView tvBoutiqueDescription;
        @BindView(R.id.layout_boutique_item)
        RelativeLayout layoutBoutiqueItem;

        BoutiqueHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.layout_boutique_item)
        public void onBoutiqueClick(){
            BoutiqueBean  bean = (BoutiqueBean) layoutBoutiqueItem.getTag();
            MFGT.gotoBoutiqueChildActivity(mContext,bean);
        }
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if (mList != null) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }
    private int getFooterString(){
        return isMore ? R.string.load_more : R.string.no_more;
    }
}
