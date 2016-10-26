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
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.net.OkHttpUtils;
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
        if(user!=null){
            etReviseNick.setText(user.getMuserNick());
            etReviseNick.setSelectAllOnFocus(true);
        }else{
            finish();
        }
    }


    @OnClick({R.id.btn_revise, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_revise:
                if(user!=null){
                    String nick =etReviseNick.getText().toString().trim();
                    if(nick.equals(user.getMuserNick())){
                        CommonUtils.showLongToast("昵称未作更改"  );
                    }else if(TextUtils.isEmpty(nick)){
                        CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
                    }else{
                        updateNick(nick);
                    }
                }
                break;
            case R.id.btn_back:
                finish();
                break;
        }

    }

    private void updateNick(String nickname) {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("更新中");
        pd.show();
        NetDao.updateNick(mContext, user.getMuserName(), nickname, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                L.e(TAG, "result=" + result);
                if (result == null) {
                } else {
                    if (result.isRetMsg()) {
                        User u = (User) result.getRetData();
                        L.e(TAG, "user=" + u);
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.updateUser(u);
                        if (isSuccess) {
                            FuLiCenterApplication.setUser(u);
                            setResult(RESULT_OK);
                            MFGT.finish(mContext);
                        } else {
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(error);
                L.e(TAG, "error=" + error);
            }
        });
    }

}
