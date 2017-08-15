package me.myweather.app.View;

import android.content.Context;
import android.util.AttributeSet;

import com.baoyz.swipemenulistview.SwipeMenuListView;

/**
 * Created by guyu on 2017/8/15.
 */

public class ScrollListView extends SwipeMenuListView {
    public ScrollListView(Context context) {
        super(context);
    }

    public ScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //此处是代码的关键
        //MeasureSpec.AT_MOST的意思就是wrap_content
        //Integer.MAX_VALUE >> 2 是使用最大值的意思,也就表示的无边界模式
        //Integer.MAX_VALUE >> 2 此处表示是福布局能够给他提供的大小
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
