package cn.ucai.fulicenter.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.DisplayUtils;

public class PersonalInformationActivity extends BaseActivity {

    @BindView(R.id.user_head_avatar)
    ImageView userHeadAvatar;
    @BindView(R.id.user_account)
    TextView userAccount;
    @BindView(R.id.user_nickname)
    TextView userNickname;
    PersonalInformationActivity mContext;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    User user = null;
    OnSetAvatarListener mOnSetAvatarListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initView() {

        DisplayUtils.initBackWithTitle(this, "设置");

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        } else {
            showInfo();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInfo();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            return;
        }
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_NICK) {

        }
        mOnSetAvatarListener.setAvatar(requestCode,data,userHeadAvatar);
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateUserAvatar();
        }
    }

    private void updateUserAvatar() {
        File file= OnSetAvatarListener.getAvatarFile(mContext,user.getMuserName());
        L.e("file"+file.exists());
        L.e("file"+file.getAbsolutePath());
        NetDao.updateUserAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s,User.class);
                L.e("result="+result);
            }

            @Override
            public void onError(String error) {
                L.e("error="+error);
            }
        });
    }

    @OnClick({R.id.user_head_avatar, R.id.user_account, R.id.user_nickname,R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_head_avatar:
                mOnSetAvatarListener = new OnSetAvatarListener(mContext,R.id.layout_avatar,user.getMuserName(), I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.user_account:
                CommonUtils.showShortToast("账户名不能修改");
                break;
            case R.id.user_nickname:
                MFGT.gotoReviseNickActivity(mContext);
                break;
            case R.id.btn_login:
                logout();
                break;
        }
    }


    private void logout() {
        if (user != null) {
            SharePrefrenceUtils.getInstence(mContext).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLoginActivity(mContext);
        }
    }
    private void showInfo() {
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, userHeadAvatar);
            userAccount.setText(user.getMuserName());
            userNickname.setText(user.getMuserNick());
        }
    }
}
