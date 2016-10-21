package cn.ucai.fulicenter.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.dao.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.DisplayUtils;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    @BindView(R.id.btn_Register)
    Button btnRegister;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etNick)
    EditText etNick;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    String username;
    String nickname;
    String password;
    RegisterActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        mContext = this;
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this, "用户注册");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_Register)
    public void onClick() {
        String username = etUserName.getText().toString().trim();
        String nickname = etNick.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            CommonUtils.showLongToast(R.string.user_name_connot_be_empty);
            etUserName.requestFocus();
            return;
        } else if (!username.matches("[a-zA-Z]\\w{5,15}")) {
            CommonUtils.showLongToast(R.string.illegal_user_name);
            etUserName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(nickname)) {
            CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
            etNick.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            CommonUtils.showLongToast(R.string.password_connot_be_empty);
            etPassword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirm)) {
            CommonUtils.showLongToast(R.string.confirm_password_connot_be_empty);
            etConfirmPassword.requestFocus();
            return;
        } else if (!password.equals(confirm)) {
            CommonUtils.showLongToast(R.string.two_input_password);
            etConfirmPassword.requestFocus();
            return;
        }
        register();
    }

    private void register() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
        NetDao.register(mContext, username, nickname, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                if (result == null) {
                    CommonUtils.showLongToast(R.string.register_fail);
                } else {
                    if (result.isRetMsg()) {
                        MFGT.finish(mContext);

                    } else {
                        etUserName.requestFocus();
                    }
                }
                MFGT.gotoMainActivity(mContext);
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(error);
                L.e(TAG, "" + error);
            }
        });
    }
}
