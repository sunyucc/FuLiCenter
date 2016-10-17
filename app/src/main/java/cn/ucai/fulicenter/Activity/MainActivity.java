package cn.ucai.fulicenter.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rbGoodNews)
    RadioButton rbGoodNews;
    @BindView(R.id.rbBoutique)
    RadioButton rbBoutique;
    @BindView(R.id.rbCategory)
    RadioButton rbCategory;
    @BindView(R.id.rbCart)
    RadioButton rbCart;
    @BindView(R.id.tvCartHint)
    TextView tvCartHint;
    @BindView(R.id.rbContact)
    RadioButton rbContact;

    int index;
    RadioButton[] mRb ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity.onCreate");
        initView();
    }

    private void initView() {
        mRb = new RadioButton[]{rbGoodNews,rbBoutique,rbCategory,rbCart,rbContact};
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rbGoodNews:
                index =0 ;
                break;
            case R.id.rbBoutique:
                index =1 ;
                break;
            case R.id.rbCategory:
                index =2 ;
                break;
            case R.id.rbCart:
                index =3 ;
                break;
            case R.id.rbContact:
                index =4 ;
                break;
        }
            setRadioButtonStatus();
    }

    private void setRadioButtonStatus() {
        for(int i= 0;i<mRb.length;i++) {
            if (i == index) {
                mRb[i].setChecked(true);
            } else {
                mRb[i].setChecked(false);
                }
        }
    }
}
