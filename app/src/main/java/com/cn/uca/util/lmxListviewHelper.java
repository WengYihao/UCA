package com.cn.uca.util;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by asus on 2017/11/9.
 */

public abstract class lmxListviewHelper {
    ListView lv;
    // position1:被拖动的item的id
    public int position1 = -1;
    // 被拖动的item的view
    // public View view1;
    // public View view2;
    // 被移动的item的原始y坐标
    public float y1 = -1;
    // 抽象方法 交换listview中两个item的位置
    public abstract void changeItemPosition(int p1, int p2);
    // 抽象方法 充值listview
    public abstract void resetListview();
    public lmxListviewHelper(ListView lv) {
        super();
        this.lv = lv;
    }
    /**
     *
     * @Title: enableChangeItems
     * @Description: TODO(是否启用长按交换item位置)
     * @param @param b true-->启用,false-->禁用
     * @return void 返回类型
     * @throws
     */
    public void enableChangeItems(boolean b) {
        if (!b) {
            return;
        }

        // 长按后，确定需要交换的item1的id(position1),确定item1的view
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                position1 = position;
                y1 = lv.getChildAt(position).getY();
                lv.getChildAt(position).setAlpha(0.5f);
                return false;
            }
        });


        // ontouch监听
        /*
        * ACTION_MOVE: 1，item1的视图跟随手指上下移动 2,实时监测当前需要替换的item是哪个,对应的item显示动画效果
        */
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (position1 == -1) {
                            // position1==-1为true表明没有长按某条item
                            break;
                        }
                        // item1的视图跟随手指上下移动
                        lv.getChildAt(position1).setY(event.getY());
                        // 检测当前覆盖的item
                        checkItem2(lv.getChildAt(position1).getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        replaceItems(event.getY());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    protected void checkItem2(float y) {
        for (int i = 0; i < lv.getChildCount(); i++) {
            Log.i("res", "释放替换item" + i);
            if (y > lv.getChildAt(i).getTop()
                    && y < lv.getChildAt(i).getBottom()) {
                // 当前覆盖了item id=i
                startFlick(lv.getChildAt(i));
            } else {
                lv.getChildAt(i).clearAnimation();
            }
        }
    }

    public void replaceItems(float y) {
        if (y1 == -1) {
            // y1未被赋值过 说明onitemlongclick中没有给y1赋值 说明没有item被长按
            Log.i("res", "没有item被长按");
            return;
        }
        for (int i = 0; i < lv.getChildCount(); i++) {
            // 漏了这句找了一天多,记得清除动画!!!
            lv.getChildAt(i).clearAnimation();
            if (y > lv.getChildAt(i).getTop()
                    && y < lv.getChildAt(i).getBottom()) {
                // 抬起手指时所覆盖的item与长按item交换位置
                Log.i("res", "action_up替换下标" + i);
                changeItemPosition(position1, i);
                position1 = -1;
                y1 = -1;
                return;
            }
        }
        Log.i("res", "action_up时无符合item");
        // 若遍历完成未发现所需替代的项 view1移回原位
        clearParams();
    }


    /*
    * 没有交换item,清除各种数据
    */
    public void clearParams() {
        TranslateAnimation translateAnimation = new TranslateAnimation(lv.getChildAt(position1).getX(),
                lv.getChildAt(position1).getX(),
                lv.getChildAt(position1).getY(), y1);
        translateAnimation.setFillAfter(false);
        translateAnimation.setDuration(400);
        lv.getChildAt(position1).startAnimation(translateAnimation);
        resetListview();
       // 若position1是listview的常规元素(非header/footer)
        if (position1 < lv.getAdapter().getCount()
                && lv.getChildAt(position1) != null) {
            lv.getChildAt(position1).clearAnimation();
        }
        y1 = -1;
        position1 = -1;
        resetListview();
    }


    /*
    * 闪烁动画
    */
    private void startFlick(View view) {
        if (null == view) {
            return;
        }
        if (view.getAnimation() == null) {
            Animation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(200);
            alphaAnimation.setInterpolator(new LinearInterpolator());
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            alphaAnimation.setRepeatMode(Animation.REVERSE);
            view.startAnimation(alphaAnimation);
        }
    }
    /**
     * 取消View闪烁效果
     * */
    private void stopFlick(View view) {
        if (null == view) {
            return;
        }
        view.clearAnimation();
    }
}