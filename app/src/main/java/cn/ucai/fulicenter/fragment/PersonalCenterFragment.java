package cn.ucai.fulicenter.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends BaseFragment {
    private static final String TAG = PersonalCenterFragment.class.getSimpleName();
    @BindView(R.id.iv_user_avatar)
    ImageView mIvUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;

    MainActivity mContext;
    @BindView(R.id.tv_center_settings)
    TextView mTvCenterSettings;
    User user = null;
    @BindView(R.id.tv_collect_count)
    TextView tvCollectCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getActivity();
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        L.e(TAG, "user=" + user);
        if (user == null) {
            MFGT.gotoLogin(mContext);
        } else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mIvUserAvatar);
            mTvUserName.setText(user.getMuserNick());
        }
        downCollectCount();
    }

    private void downCollectCount() {
        NetDao.collect(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result.isSuccess()) {
                    tvCollectCount.setText(result.getMsg());
                } else {
                    tvCollectCount.setText(String.valueOf(0));
                }
            }

            @Override
            public void onError(String error) {
                tvCollectCount.setText(String.valueOf(0));
                L.e(TAG, "error" + error);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        L.e(TAG, "user=" + user);
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mIvUserAvatar);
            mTvUserName.setText(user.getMuserNick());
            syncUserInfo();
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.tv_center_settings)
    public void onClick() {
        MFGT.gotoPersonalInformationActivity(mContext);
    }

    private void syncUserInfo() {
        NetDao.syncUser(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result r = ResultUtils.getListResultFromJson(s, User.class);
                if (r != null) {
                    User u = (User) r.getRetData();
                    if (user.equals(u)) {
                        UserDao dao = new UserDao(mContext);
                        boolean b = dao.saveUser(u);
                        user = u;
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mIvUserAvatar);
                        mTvUserName.setText(user.getMuserName());
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "error" + error);
            }
        });
    }

    @OnClick(R.id.layout_center_collect)
    public void onLayoutCollect() {
        MFGT.gotoCollectActivity(mContext);
    }
}