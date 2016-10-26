package cn.ucai.fulicenter.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.views.DisplayUtils;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    @BindView(R.id.btn_Register)
    Button btnRegister;
    @BindView(R.id.et_UserName)
    EditText etUserName;
    @BindView(R.id.et_Nick)
    EditText etNick;
    @BindView(R.id.et_Password)
    EditText etPassword;
    @BindView(R.id.et_ConfirmPassword)
    EditText etConfirmPassword;
    String username;
    String nickname;
    String password;
    RegisterActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        mContext = this;
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
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
        username = etUserName.getText().toString().trim();
        nickname = etNick.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
            etUserName.requestFocus();
            return;
        }else if(!username.matches("[a-zA-Z]\\w{5,15}")){
            CommonUtils.showShortToast(R.string.illegal_user_name);
            etUserName.requestFocus();
            return;
        }else if(TextUtils.isEmpty(nickname)){
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            etNick.requestFocus();
            return;
        }else if(TextUtils.isEmpty(password)){
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            etPassword.requestFocus();
            return;
        }else if(TextUtils.isEmpty(confirm)){
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
            etConfirmPassword.requestFocus();
            return;
        }else if(!password.equals(confirm)){
            CommonUtils.showShortToast(R.string.two_input_password);
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
                if(result==null){
                    CommonUtils.showShortToast(R.string.register_fail);
                }else{
                    if(result.isRetMsg()){
                        CommonUtils.showLongToast(R.string.register_success);
                        setResult(RESULT_OK,new Intent().putExtra(I.User.NICK,nickname));
                        MFGT.finish(mContext);
                    }else{
                        CommonUtils.showLongToast(R.string.register_fail_exists);
                        etUserName.requestFocus();
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showShortToast(error);
                L.e(TAG,"register error="+error);
            }
        });
    }
}
