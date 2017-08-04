package com.cn.uca.view;

import com.cn.uca.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * 自定义首页的搜索框
 * 
 * @author Administrator
 *
 */
public class MyEditText extends EditText {

	public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyEditText(Context context) {
		super(context);

	}

	// 在Textview中集成了接口 TextWatcher,并且也重写了位置变化的事件响应方法，所以
	// 子类中EditTextWithClearButton
	// 直接来重写父类中的 文字变化的事件响应方法 onTextChanged
	@SuppressWarnings("deprecation")
	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);

		// 这个方法返回的是一个Drawable[]数组，这个数组有4个元素，分别为TextView的
		// 左上右下 4个方向的drawable对象，通过这些Drawable对象 可以分别为一个TextView的
		// 左上右下 这几个方向添加图片

		Drawable[] drawable = getCompoundDrawables();

		// 如果输入框里面的文本字符长度不为0，且有焦点，则给这个输入框的右边 动态的添加一个 删除的图标
		if (!getText().toString().equals("") && hasFocus()) {
			setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1],
					getContext().getResources().getDrawable(R.mipmap.ic_clear), drawable[3]);

		} else {
			setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1], null, drawable[3]);

		}

		// 最后 调用方法invalidate() 来刷新
		invalidate();

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);

		Drawable[] drawable = getCompoundDrawables();

		// 如果输入框里面的文本字符长度不为0，且有焦点，则给这个输入框的右边 动态的添加一个 删除的图标
		if (!getText().toString().equals("") && hasFocus()) {
			setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1],
					getContext().getResources().getDrawable(R.mipmap.ic_clear), drawable[3]);

		} else {
			setCompoundDrawablesWithIntrinsicBounds(drawable[0], drawable[1], null, drawable[3]);

		}

		// 最后 调用方法invalidate() 来刷新
		invalidate();
	}

	// 重写（覆盖）父类中的方法 onTouchEvent(MotionEvent event) ,当用户手指触摸此组件 会触发这个方法
	// ，并且 在哪在这里 定义根据业务需求实现的逻辑代码

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean re = super.onTouchEvent(event);
		// 当你在屏幕上 操作了，类MotionEvent能够知道你的 具体动作
		switch (event.getAction()) {// event.getAction()得到你的具体动作状态
		case MotionEvent.ACTION_DOWN:// 当触摸动作按下的时候，啥都不做

			break;

		case MotionEvent.ACTION_UP:// 当触摸动作松开的时候.....
			Drawable[] drawable = getCompoundDrawables();

			// 如果右边的drawable不为null，说明这个删除图标 是存在的，正在显示在组件上，。
			// 点击这个 删除图标 应该是 输入框中的内容清空，且 删除图片 消失

			if (drawable[2] != null) {

				// 思路：
				// 第一步：确定这个删除图标的 位置区域范围（这个图标 是有面积，上下左右 四个边界值 不同）
				// 第二步：根据MotionEvent的方法 getX 和 getY 来确定 在屏幕上 你的触摸点的（x ,y）
				// 第三步：若在屏幕上，你的触摸点（x，y）在 删除图片的区域范围内，则 清空文本字符串 和
				// 让删除图标 消失。
				// 定义是 删除图标 的左边距
				int left = getWidth() - getPaddingRight() - drawable[2].getIntrinsicWidth();
				// 定义是 删除图标 的右边距
				int right = getWidth() - getPaddingRight();
				// 定义是 删除图标 的上边距
				int top = getPaddingTop();
				// 定义是 删除图标 的下边距
				int bottom = getHeight() - getPaddingBottom();

				// (event.getX(),event.getY()) 若在 删除图片的范围内，则说明触摸了 这个删除图标
				if (event.getX() < right && event.getX() > left && event.getY() > top && event.getY() < bottom) {
					setText("");
					// invalidate(); --- UI线程中
					// postInvalidate(); ---- 非UI线程中

				}
			}

			break;

		default:
			break;
		}

		return re;
	}

}
