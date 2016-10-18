package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ucai.fulicenter.Activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.Activity.MainActivity;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;

public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
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
        ((MainActivity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
