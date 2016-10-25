package cn.ucai.fulicenter.Activity;

import android.app.Activity;
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
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.utils.CommonUtils;

public class ReviseNickActivity extends Activity {
    @BindView(R.id.btn_revise)
    Button btnRevise;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.et_revise_nick)
    EditText etReviseNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_nick);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        User user = FuLiCenterApplication.getUser();
        etReviseNick.setText(user.getMuserNick());
    }

    @OnClick({R.id.btn_revise, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_revise:
                String nickname = etReviseNick.getText().toString().trim();
                if (TextUtils.isEmpty(nickname)) {
                    CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
                    etReviseNick.requestFocus();
                    return;
                } else {

                }
                    break;
                    case R.id.btn_back:
                        finish();
                        break;
                }

        }
    }
