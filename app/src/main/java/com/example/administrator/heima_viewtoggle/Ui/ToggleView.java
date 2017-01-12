package com.example.administrator.heima_viewtoggle.Ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * android控件绘制过程
 * 测量 ---  布局  --- 绘制
 * measure--layout----draw
 * 分别重下下列三个方法
 * onMeasure - onLayout - onDraw
 * view
 * onMeasure（再此方法内指定自己的宽高） onDraw（再此方法内绘制自己的内容）
 * viewgroup
 * onMeasure（再此方法内指定自己的宽高，所有子view的宽高）onLayout（摆放所有的子view） onDraw（再此方法内绘制自己的内容）
 */


public class ToggleView extends View {

    private Bitmap switchBackgroundBitmap;
    private Bitmap slideButtonBitmap;
    private Paint paint;
    private boolean isSwitch = true;
    private boolean istouch = false;
    private float startX;
    private int maxLeft;
    private float lastLeft;
    private OnSwitchStateChangeListener onSwitchStateChangeListener;


    /**
     * 用于代码创建控件
     *
     * @param context
     */
    public ToggleView(Context context) {
        super(context);
        initUi();
    }


    /**
     * 用于Xml内的属性定义
     *
     * @param context
     * @param attrs
     */
    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUi();
        String spacename = "http://schemas.android.com/apk/res/com.example.administrator.heima_viewtoggle";
        int switchBackgroundResource = attrs.getAttributeResourceValue(spacename, "switch_background", 0);
        int slideButtonResource = attrs.getAttributeResourceValue(spacename, "slide_button", 0);
        boolean switchState = attrs.getAttributeBooleanValue(spacename, "switch_state", false);
        setSlideButtonResource(slideButtonResource);
        setSwitchBackgroundResource(switchBackgroundResource);
        setSwitchState(switchState);
    }

    /**
     * 用于Xml内的属性定义，指定默认的style样式
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUi();
    }

    /**
     * 初始化画壁
     */
    private void initUi() {
        paint = new Paint();
    }

    /**
     * 再此方法内指定自己的宽高，（通过测量最大图片大小）
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switchBackgroundBitmap.getWidth(), switchBackgroundBitmap.getHeight());
    }

    /**
     * 在此方法内绘制图像，因为开关有两个状态，需要根据判断值确定顶层图片覆盖位置
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(switchBackgroundBitmap, 0, 0, paint);
        //用户在触摸屏幕时执行动画效果
        if (istouch) {
            //设置生产背景的在手指按下位置的中间，需要减去控件宽度的一半
            lastLeft = startX - slideButtonBitmap.getWidth() / 2.0f;
            //设置控件滑动距离
            if (lastLeft < 0) {
                lastLeft = 0;
            } else if (lastLeft > maxLeft) {
                lastLeft = maxLeft;
            }
            canvas.drawBitmap(slideButtonBitmap, lastLeft, 0, paint);

        } else {
            maxLeft = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();
            if (isSwitch) {
                //开关打开了
                canvas.drawBitmap(slideButtonBitmap, maxLeft, 0, paint);
            } else {
                //开关关闭了
                canvas.drawBitmap(slideButtonBitmap, 0, 0, paint);
            }
        }
    }

    /**
     * 设置Toggle背景图片
     *
     * @param switch_background
     */
    public void setSwitchBackgroundResource(int switch_background) {
        switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(), switch_background);
    }

    /**
     * 设置开关按钮样式
     *
     * @param slide_button
     */
    public void setSlideButtonResource(int slide_button) {
        slideButtonBitmap = BitmapFactory.decodeResource(getResources(), slide_button);
    }

    /**
     * 设置Toggle开关两种状态
     *
     * @param isSwitch
     */
    public void setSwitchState(boolean isSwitch) {
        this.isSwitch = isSwitch;
    }


    /**
     * 在背景图片范围内按下才会触发触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                System.out.println("ACTION_DOWN" + startX);

                istouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                startX = event.getX();
                System.out.println("ACTION_MOVE" + startX);

                break;
            case MotionEvent.ACTION_UP:
                startX = event.getX();
                System.out.println("ACTION_UP" + startX);
                istouch = false;

                //抬起事件执行时，当前抬起所在x值与中心位置坐对比，少于中心位置为关闭开工，大于中心位置为打开开关
                float center = switchBackgroundBitmap.getWidth() / 2.0f;
                boolean state = startX > center;

                if (state == !isSwitch && onSwitchStateChangeListener != null) {
                    onSwitchStateChangeListener.onStateChange(state);
                }
                isSwitch = state;
                break;
        }
        //重新调用onDrow的方法，重新绘制背景
        invalidate();
        return true;
    }

    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener onSwitchStateChangeListener) {
        this.onSwitchStateChangeListener = onSwitchStateChangeListener;
    }

    public interface OnSwitchStateChangeListener {
        void onStateChange(boolean isSwitch);
    }
}
