package work.pengzhe.com.mylistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;


/**
 * Created on 2017/6/14 15:34
 *
 * @author PengZee
 */

public class CustomViewGroup extends ViewGroup {

    int widthTop = 0;
    int widthBottom = 0;
    int heightLeft = 0;
    int heightRight = 0;

    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1.获得上一级ViewGroup推荐的宽高及测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //2.计算 wrapContent时的宽高 并确定子View测量模式和推荐宽高（本质上测量就是采用递归调用的方式）
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            //计算左边两个View的宽度
            if (i == 0 || i == 1) {
                widthTop += getChildAt(i).getWidth();
            }
            //计算右边两个View的宽度
            if (i == 2 || i == 3) {
                widthBottom += getChildAt(i).getWidth();
            }
            //计算左边两个View的高度
            if (i == 0 || i == 2) {
                heightLeft += getChildAt(i).getHeight();
            }
            //计算右边两个View的高度
            if (i == 1 || i == 3) {
                heightRight += getChildAt(i).getHeight();
            }
            width = Math.max(widthTop, widthBottom);
            height = Math.max(heightLeft, heightRight);

        }

        //3.设置自己的宽高 wrapContent时为自己测量的结果 matchParent时为获得上一级ViewGroup推荐的宽高
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }
}
