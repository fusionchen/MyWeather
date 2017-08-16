package me.myweather.app.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import me.myweather.app.activity.ManageCityActivity;

/**
 * Created by admin on 2017/8/16.
 */

public class PicTool {
    private static Context context;
    public static void init(Context context) {
        PicTool.context = context;
    }
    public static Drawable cutImage(int id, int x, int y, int width, int height) {
        Bitmap picSrc = BitmapFactory.decodeResource(context.getResources(), id);
        int picWidth = picSrc.getWidth();
        int picHeight = picSrc.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float)getScreenWidth()) / picWidth;
        float scaleHeight = ((float)getScreenHeight()) / picHeight;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap picNewRes = Bitmap.createBitmap(picSrc, 0, 0,picWidth, picHeight, matrix, true);
        picNewRes = Bitmap.createBitmap(picNewRes, x, y, width, height);
        Drawable drawable = new BitmapDrawable(picNewRes);
        return drawable;
    }
    public static int getScreenWidth() {
        return ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
    }
    public static int getScreenHeight() {
        return ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
    }
    public static int dp2px(ManageCityActivity manageCityActivity, int dp) {
        float scale = manageCityActivity.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
