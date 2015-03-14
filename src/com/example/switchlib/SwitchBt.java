package com.example.switchlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SwitchBt extends RelativeLayout
{

	private TextView mText;

	private FrameLayout mTouchedView; //container of mText and button

	private RelativeLayout mBg; //the whole view

	private Drawable mOffbg;

	private Drawable mOnbg;

	private int mLayoutWidth; //this view's width
	
	private boolean isSwitchOn = false;

	private OnSwitchedListener mListener;

	private static final int ANIMATION_EXECUTE_TIME = 300;

	public SwitchBt(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public SwitchBt(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs)
	{
		mBg = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.switch_layout, null);
		mTouchedView = (FrameLayout) mBg.findViewById(R.id.bt_container);
		mText = (TextView) mBg.findViewById(R.id.bt_text);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.SwitchBt);

		mOffbg = ta.getDrawable(R.styleable.SwitchBt_bg_off);
		mOnbg = ta.getDrawable(R.styleable.SwitchBt_bg_on);

		mText.setTextColor(ta.getColor(R.styleable.SwitchBt_text_color,
				R.color.white));
		mText.setTextSize(ta.getDimension(R.styleable.SwitchBt_text_size, 20));
		mTouchedView.setBackground(ta.getDrawable(R.styleable.SwitchBt_switch_bt));
		
		if(mOffbg != null)
		{
			mBg.setBackground(mOffbg); //default off
		}

		addView(mBg);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mLayoutWidth = MeasureSpec.getSize(widthMeasureSpec);
		int layoutHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		//set backgroud's size
		android.view.ViewGroup.LayoutParams bgParams = mBg.getLayoutParams();
		bgParams.height = layoutHeight;
		bgParams.width = mLayoutWidth;
		mBg.setLayoutParams(bgParams);
		
		//set button's size
		android.view.ViewGroup.LayoutParams btPramas = mTouchedView.getLayoutParams();
		btPramas.height = layoutHeight;
		btPramas.width = layoutHeight;
		mTouchedView.setLayoutParams(btPramas);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:

			break;

		case MotionEvent.ACTION_MOVE:

			int curX = (int) event.getX();
			moveTo(curX);
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:

			touchUp(mTouchedView.getX());
			break;

		default:
			break;
		}

		return true;
	}

	/**
	 * move to the touched x position
	 * @param targetX
	 */
	private void moveTo(float targetX)
	{
		if (targetX < 0 || mTouchedView.getLeft() < 0) //set the left edge
		{
			targetX = 0;
		}

		if (targetX > mLayoutWidth - mTouchedView.getWidth() //set the right edge
				|| mTouchedView.getLeft() > mLayoutWidth - mTouchedView.getWidth())
		{
			targetX = mLayoutWidth - mTouchedView.getWidth();
		}

		mTouchedView.setX(targetX);
		
		if (mTouchedView.getX() <= mLayoutWidth / 2 - mTouchedView.getWidth() / 2)
		{
			mBg.setBackground(mOnbg);
		} else
		{
			mBg.setBackground(mOffbg);
		}

	}

	/**
	 * execute when the touch finished
	 * @param curX touch finished position in this view
	 */
	private void touchUp(float curX)
	{
		if (curX <= mLayoutWidth / 2 - mTouchedView.getWidth() / 2)
		{
			switchOn();
		} else
		{
			switchOff();
		}
	}
	
	/**
	 * turn on switch
	 */
	public void switchOn()
	{
		isSwitchOn = true;
		mTouchedView.setX(0);
		if(mListener != null)
		{
			mListener.onSwitchOn();
		}
	}
	
	/**
	 * turn of switch
	 */
	public void switchOff()
	{
		isSwitchOn = false;
		mTouchedView.setX(mLayoutWidth-mTouchedView.getWidth());
		if(mListener != null)
		{
			mListener.onSwitchOn();
		}
	}
	
	/**
	 * is the switch on
	 * @return
	 */
	public boolean isSwitchOn()
	{
		return isSwitchOn;
	}
	
	public void setText(String dec)
	{
		mText.setText(dec);
	}
	
	public void setOnSwitchedListener(OnSwitchedListener sl)
	{
		mListener = sl;
	}

	@Deprecated
	private void switchOn(float curX)
	{
		if (curX <= 0)
		{
			return;
		}

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
				-mTouchedView.getX() / mLayoutWidth,
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
				0);

		animation.setDuration(ANIMATION_EXECUTE_TIME);
		animation.setFillAfter(true);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		mTouchedView.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				mTouchedView.setX(0);
				if (mListener != null)
				{
					mListener.onSwitchOn();
				}
			}
		});

	}

	@Deprecated
	private void switchOff(float curX)
	{
		if (curX >= mLayoutWidth - mTouchedView.getWidth())
		{
			return;
		}

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
				1 - (mTouchedView.getX() + mTouchedView.getWidth())
						/ mLayoutWidth, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);

		animation.setDuration(ANIMATION_EXECUTE_TIME);
		animation.setFillAfter(true);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		mTouchedView.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				mTouchedView.setX(mLayoutWidth - mTouchedView.getWidth());
				if (mListener != null)
				{
					mListener.onSwitchOff();
				}

			}
		});
	}

	public interface OnSwitchedListener
	{

		void onSwitchOn();

		void onSwitchOff();
	}

}
