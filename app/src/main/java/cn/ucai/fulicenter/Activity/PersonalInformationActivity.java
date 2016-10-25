package cn.ucai.fulicenter.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.views.DisplayUtils;

public class PersonalInformationActivity extends BaseActivity {

    @BindView(R.id.user_head_avatar)
    ImageView userHeadAvatar;
    @BindView(R.id.user_account)
    TextView userAccount;
    @BindView(R.id.user_nickname)
    TextView userNickname;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        mContext =this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        DisplayUtils.initBackWithTitle(this, "设置");
        User user = FuLiCenterApplication.getUser();
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, userHeadAvatar);
        userAccount.setText(user.getMuserName());
        userNickname.setText(user.getMuserNick());
    }

    @Override
    protected void setListener() {

    }

}
