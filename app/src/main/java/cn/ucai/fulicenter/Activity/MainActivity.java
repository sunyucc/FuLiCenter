package cn.ucai.fulicenter.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CategoryFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends BaseActivity {
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
    Fragment[] mFragments;
    int index;
    int currentIndex;
    RadioButton[] mRb;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("MainActivity.onCreate");
        super.onCreate(savedInstanceState);

    }


    private void initFragment() {
        mFragments = new Fragment[5];
        mBoutiqueFragment = new BoutiqueFragment();
        mNewGoodsFragment = new NewGoodsFragment();
        mCategoryFragment = new CategoryFragment();
        mFragments[0] = mNewGoodsFragment;
        mFragments[1] = mBoutiqueFragment;
        mFragments[2] = mCategoryFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_con, mNewGoodsFragment)
                .add(R.id.fragment_con, mBoutiqueFragment)
                .add(R.id.fragment_con, mCategoryFragment)
                .hide(mBoutiqueFragment)
                .hide(mCategoryFragment)
                .show(mNewGoodsFragment)
                .commit();
    }

    @Override
    protected void initView() {
        mRb = new RadioButton[]{rbGoodNews, rbBoutique, rbCategory, rbCart, rbContact};
    }

    @Override
    protected void initData() {
        initFragment();
    }

    @Override
    protected void setListener() {

    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rbGoodNews:
                index = 0;
                break;
            case R.id.rbBoutique:
                index = 1;
                break;
            case R.id.rbCategory:
                index = 2;
                break;
            case R.id.rbCart:
                index = 3;
                break;
            case R.id.rbContact:
                index = 4;
                break;
        }
        setFragment();

    }

    private void setFragment() {
        if (index != currentIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragments[currentIndex]);
            if (!mFragments[index].isAdded()) {
                ft.add(R.id.fragment_con, mFragments[index]);

            }
            ft.show(mFragments[index]).commit();
        }
        setRadioButtonStatus();
        currentIndex = index;
    }

    private void setRadioButtonStatus() {
        for (int i = 0; i < mRb.length; i++) {
            if (i == index) {
                mRb[i].setChecked(true);
            } else {
                mRb[i].setChecked(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
