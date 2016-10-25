package cn.ucai.fulicenter.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.DisplayUtils;

public class ReviseNickActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.btn_revise)
    Button btnRevise;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.et_revise_nick)
    EditText etReviseNick;
    User user = null;
    ReviseNickActivity mContext;
    String nickname = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_nick);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        DisplayUtils.initBackWithTitle(this, "修改昵称");
        user = FuLiCenterApplication.getUser();
        etReviseNick.setText(user.getMuserNick());
    }


    @OnClick({R.id.btn_revise, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_revise:
                nickname = etReviseNick.getText().toString().trim();
                if (TextUtils.isEmpty(nickname)) {
                    CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
                    if (nickname.equals(user.getMuserNick())) {
                        etReviseNick.requestFocus();
                    }
                    return;
                }
                updateNick();
                finish();
                break;
            case R.id.btn_back:
                finish();
                break;
        }

    }

    private void updateNick() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("更新中");
        pd.show();
        NetDao.updateNick(mContext, user.getMuserName(), nickname, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s,User.class);
                L.e(TAG,"result="+result);
                if(result==null){
                    CommonUtils.showLongToast(R.string.user_database_error);
                }else{
                    if(result.isRetMsg()){
                        User u = (User) result.getRetData();
                        L.e(TAG,"user="+u);
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.saveUser(u);
                        if(isSuccess){
                            FuLiCenterApplication.setUser(u);
                            MFGT.finish(mContext);
                        }else{
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }
                    }else{
                        if(result.getRetCode()== I.MSG_LOGIN_UNKNOW_USER){
                            CommonUtils.showLongToast(R.string.login_fail_unknow_user);
                        }else if(result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                            CommonUtils.showLongToast(R.string.login_fail_error_password);
                        }else{
                            CommonUtils.showLongToast(R.string.login_fail);
                        }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(error);
                L.e(TAG,"error="+error);
            }
        });
    }

}
