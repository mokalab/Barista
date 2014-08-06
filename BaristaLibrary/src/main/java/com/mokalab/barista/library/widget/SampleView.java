package com.mokalab.barista.library.widget;

import com.antoniotari.android.util.Log;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

public class SampleView extends View
{
	private Drawable mDrawable;
	private Drawable[] mDrawables;
	private Paint mPaint;
	private Paint mPaint2;
	private float mPaintTextOffset;
	private int[] mColors;
	private PorterDuff.Mode[] mModes;
	private int mModeIndex;
	//private Typeface futura_bold;
	//private AssetManager assets;

	//-----------------------------------------------------------------------
	//--------------------------
	private static void addToTheRight(Drawable curr, Drawable prev) 
	{
		Rect r = prev.getBounds();
		int x = r.right + 12;
		int center = (r.top + r.bottom) >> 1;
		int h = curr.getIntrinsicHeight();
		int y = center - (h >> 1);

		curr.setBounds(x, y, x + curr.getIntrinsicWidth(), y + h);
	}

	private SampleView(Context activity) 
	{
		super(activity);
	}
	
	//-----------------------------------------------------------------------
	//--------------------------
	public SampleView(Context activity,Drawable bgDrawable,Drawable[] drawables) 
	{
		this(activity);

		setFocusable(true);

		/**1. GET DRAWABLE, SET BOUNDS */
		//assets = activity.getAssets();
		mDrawable = bgDrawable;// activity.getResources().getDrawable(R.drawable.roundrect_gray_button_bg_nine);
		mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());

		mDrawable.setDither(true);
		
		mDrawables=drawables;
		Drawable prev = mDrawable;
		for (int i = 0; i < mDrawables.length; i++)
		{
			mDrawables[i].setDither(true);
			addToTheRight(mDrawables[i], prev);
			prev = mDrawables[i];
		}
		
		/**2. SET Paint for writing text on buttons */
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(16);
		mPaint.setTextAlign(Paint.Align.CENTER);

		mPaint2 = new Paint(mPaint);
		/** Calculating size based on font */
		//futura_bold = Typeface.createFromAsset(assets,"fonts/futurastd-bold.otf");
		//Determine size and offset to write text in label based on font size.
		//mPaint.setTypeface(futura_bold);
		Paint.FontMetrics fm = mPaint.getFontMetrics();
		mPaintTextOffset = (fm.descent + fm.ascent) * 0.5f;

		mColors = new int[] {
				0,
				0xFFA60017,//WE USE THESE
				0xFFC6D405,
				0xFF4B5B98,
				0xFF656565,
				0xFF8888FF,
				0xFF4444FF,
		};

		mModes = new PorterDuff.Mode[] {
				PorterDuff.Mode.DARKEN,
				PorterDuff.Mode.DST,
				PorterDuff.Mode.DST_ATOP,
				PorterDuff.Mode.DST_IN,
				PorterDuff.Mode.DST_OUT,
				PorterDuff.Mode.DST_OVER,
				PorterDuff.Mode.LIGHTEN,
				PorterDuff.Mode.MULTIPLY,
				PorterDuff.Mode.SCREEN,
				PorterDuff.Mode.SRC,
				PorterDuff.Mode.SRC_ATOP,
				PorterDuff.Mode.SRC_IN,
				PorterDuff.Mode.SRC_OUT,
				PorterDuff.Mode.SRC_OVER,
				PorterDuff.Mode.XOR
		};
		mModeIndex = 0;

		updateTitle(activity);
	}

	private static Drawable[] createDrawablesFromId(Activity activity,int[] resIDs)
	{
		Drawable[] drawables = new Drawable[resIDs.length];
		for (int i = 0; i < resIDs.length; i++)
		{
			drawables[i] = activity.getResources().getDrawable(resIDs[i]);
		}
		
		return drawables;
	}
		
	//-----------------------------------------------------------------------
	//--------------------------
	public SampleView(Activity activity,Drawable bgDrawable,int[] resIDs) 
	{
		this(activity,bgDrawable,createDrawablesFromId(activity,resIDs));
		//int[] resIDs = new int[] {
		//		R.drawable.roundrect_gray_button_bg,
		//		R.drawable.order_button_white,
		//		R.drawable.yellowbar
		//};
	}

	//-----------------------------------------------------------------------
	//--------------------------
	private void swapPaintColors() 
	{
		if (mPaint.getColor() == 0xFF000000) 
		{
			mPaint.setColor(0xFFFFFFFF);
			mPaint2.setColor(0xFF000000);
		}
		else
		{
			mPaint.setColor(0xFF000000);
			mPaint2.setColor(0xFFFFFFFF);
		}
		mPaint2.setAlpha(0);
	}

	//-----------------------------------------------------------------------
	//--------------------------
	private void updateTitle(Context activity)
	{
		if(activity instanceof Activity )
			((Activity)activity).setTitle(mModes[mModeIndex].toString());
	}

	//-----------------------------------------------------------------------
	//--------------------------
	private void drawSample(Canvas canvas, ColorFilter filter)
	{
		/** Create a rect around bounds, ensure size offset */
		Rect r = mDrawable.getBounds();
		float x = (r.left + r.right) * 0.5f;
		float y = (r.top + r.bottom) * 0.5f - mPaintTextOffset;

		/**Set color filter to selected color 
		 * create canvas (filled with this color)
		 * Write text using paint (new color)
		 */
		mDrawable.setColorFilter(filter);
		mDrawable.draw(canvas);
		/** If the text doesn't fit in the button, make the text size smaller until it does*/
		final float size = mPaint.measureText("Label");
		if((int) size > (r.right-r.left)) {
			float ts = mPaint.getTextSize();
			Log.w("DEBUG","Text size was"+ts);
			mPaint.setTextSize(ts-2);
		}
		canvas.drawText("Sausage Burrito", x, y, mPaint);
		/** Write the text and draw it onto the drawable*/

		for (Drawable dr : mDrawables) {
			dr.setColorFilter(filter);
			dr.draw(canvas);
		}
	}

	PorterDuffColorFilter porterDuffColorFilter=null;
	//-----------------------------------------------------------------------
	//--------------------------
	@Override protected void onDraw(Canvas canvas) 
	{
		canvas.drawColor(0xFFCCCCCC);            
		canvas.translate(8, 12);
		for (int color : mColors)
		{
			ColorFilter filter;
			if (color == 0) 
			{
				filter = null;
			} 
			else 
			{
				//if(porterDuffColorFilter==null)
				porterDuffColorFilter=new PorterDuffColorFilter(color,mModes[mModeIndex]);
				//else{}
				filter = porterDuffColorFilter;
			}
			drawSample(canvas, filter);
			canvas.translate(0, 55);
		}
	}

	//-----------------------------------------------------------------------
	//--------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) 
		{
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			// update mode every other time we change paint colors
			if (mPaint.getColor() == 0xFFFFFFFF) 
			{
				mModeIndex = (mModeIndex + 1) % mModes.length;
				updateTitle(getContext());
			}
			swapPaintColors();
			invalidate();
			break;
		}
		return true;
	}
}