package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.style.UpdateAppearance;

import java.util.ArrayList;

import cn.ucai.fulicenter.Activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.Activity.CategoryChildActivity;
import cn.ucai.fulicenter.Activity.CollectActivity;
import cn.ucai.fulicenter.Activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.Activity.LoginActivity;
import cn.ucai.fulicenter.Activity.PersonalInformationActivity;
import cn.ucai.fulicenter.Activity.RegisterActivity;
import cn.ucai.fulicenter.Activity.ReviseNickActivity;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;

public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    public static void  gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoGoodsDetailsActivity(Context context, int goodsId ){
        Intent intent = new Intent();
        intent.setClass(context, GoodsDetailsActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID, goodsId);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Intent intent) {
            context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startRegisterActivity(Context context, Intent intent) {
            context.startActivity(intent);
        ((LoginActivity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoBoutiqueChildActivity(Context context, BoutiqueBean bean){
        Intent intent = new Intent();
        intent.setClass(context, BoutiqueChildActivity.class);
        intent.putExtra(I.Boutique.CAT_ID,bean);
        startActivity(context,intent);
    }
    public static void gotoCategoryChildActivity(Context context, int catId, String groupName, ArrayList<CategoryChildBean> list){
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,catId);
        intent.putExtra(I.CategoryGroup.NAME,groupName);
        intent.putExtra(I.CategoryChild.ID,list);
        startActivity(context,intent);
    }
    public static void gotoLoginActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context,LoginActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_CART);
    }
    public static void gotoRegisterActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context,RegisterActivity.class);
        startActivityForResult(context, intent, I.REQUEST_CODE_REGISTER);

    }
    private static void startActivityForResult(Activity context, Intent intent, int requestCode) {
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }


    public static void gotoLogin(Activity context){
        Intent intent = new Intent();
        intent.setClass(context,LoginActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_LOGIN);
    }

    public static void gotoPersonalInformationActivity(Activity context) {
        startActivity(context, PersonalInformationActivity.class);
    }

    public static void gotoReviseNickActivity(Activity context) {
        startActivityForResult(context,new Intent(context, ReviseNickActivity.class),I.REQUEST_CODE_NICK);
    }

    public static void gotoCollectActivity(MainActivity mContext) {
        startActivity(mContext, CollectActivity.class);
    }
}
