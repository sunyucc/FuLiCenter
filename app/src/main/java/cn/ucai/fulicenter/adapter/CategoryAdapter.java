package cn.ucai.fulicenter.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;


/**
 * Created by sunyu on 2016/10/20.
 */

public class    CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        this.mContext = mContext;
        mGroupList = new ArrayList<>();
        mGroupList.addAll(groupList);
        mChildList = new ArrayList<>();
        mChildList.addAll(childList);
    }

    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null ? mChildList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList != null ? mGroupList.get(groupPosition) : null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList != null && mChildList.get(groupPosition)!= null?mChildList.get(groupPosition).get(childPosition):null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            convertView = convertView.inflate(mContext, R.layout.item_category_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        CategoryGroupBean group =  getGroup(groupPosition);
        if (group != null) {
            ImageLoader.downloadImg(mContext, holder.mIvGroupThumb, group.getImageUrl(), true);
            holder.mTvGroupName.setText(group.getName());
            holder.mIvIndicator.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_child, null);
            holder = new ChildViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ChildViewHolder) view.getTag();
        }
        final CategoryChildBean child = getChild(groupPosition, childPosition);
        if(child!=null){
            ImageLoader.downloadImg(mContext,holder.mIvChildThumb,child.getImageUrl());
            holder.mTvChildName.setText(child.getName());
            holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<CategoryChildBean> list = mChildList.get(groupPosition);
                    String groupName = mGroupList.get(groupPosition).getName();
                    MFGT.gotoCategoryChildActivity(mContext,child.getId(),groupName,list);
                }
            });
        }
        return view;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        if (mGroupList != null) {
            mGroupList.clear();
        }
        mGroupList.addAll(groupList);
        if (mChildList != null) {
            mChildList.clear();
        }
        mChildList.addAll(childList);
        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        @BindView(R.id.iv_group_thumb)
        ImageView mIvGroupThumb;
        @BindView(R.id.tv_group_name)
        TextView mTvGroupName;
        @BindView(R.id.iv_indicator)
        ImageView mIvIndicator;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_child_thumb)
        ImageView mIvChildThumb;
        @BindView(R.id.tv_child_name)
        TextView mTvChildName;
        @BindView(R.id.relativeLayout)
        RelativeLayout mRelativeLayout;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
