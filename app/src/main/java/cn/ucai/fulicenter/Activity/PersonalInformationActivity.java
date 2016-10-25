package cn.ucai.fulicenter.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
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
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, userHeadAvatar);
            userAccount.setText(user.getMuserName());
            userNickname.setText(user.getMuserNick());
        } else {
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, userHeadAvatar);
            userAccount.setText(user.getMuserName());
            userNickname.setText(user.getMuserNick());
        } else {
            finish();
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.user_head_avatar, R.id.user_account, R.id.user_nickname,R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_head_avatar:
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
}
