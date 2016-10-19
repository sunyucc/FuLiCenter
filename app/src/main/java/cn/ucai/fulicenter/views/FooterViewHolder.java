package cn.ucai.fulicenter.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;

public  class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        public TextView tvFooter;
    public FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }