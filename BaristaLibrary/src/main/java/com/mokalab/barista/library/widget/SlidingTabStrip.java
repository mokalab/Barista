package com.mokalab.barista.library.widget;

import com.antoniotari.android.util.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


/**
 * Created by Pirdad Sakhizada on 12/11/13.
 */
public class SlidingTabStrip extends HorizontalScrollView {

    private boolean mWorksWithViewPager = false;

    private boolean mShowDefaultIndicator = true;
    private boolean shouldExpand = false;
    private boolean selectOnClick = false;
    private boolean mInitialSelectionMade = false;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private int tabCount;
    private int scrollOffset = 52;

    private int lastScrollX = 0;

    // custom layout attributes
    private int mTabBackgroundResId = R.drawable.bg_tab;
    private int mIndicatorColor = 0xFF666666;
    private int mUnderlineColor = 0x1A000000;
    private int mDividerColor = 0x1A000000;
    private int mMinHeight = 0;
    private int mMinChildWidth = 0;
    private int mUnderlineHeight = 2;
    private int mIndicatorHeight = 8;
    private int mDividerWidth = 2;
    private int mDividerPadding = 12;

    private Paint rectPaint;

    private Paint dividerPaint;
    private LinearLayout mTabsContainer;
    private ViewPager mViewPager;
    private BaseAdapter mAdapter;
    private TabClickListener mTabClickListener;
    private PageListener mPageListener;
    private ViewPager.OnPageChangeListener mPagerChangeListener;
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private View mInitialTabView;
    private DataSetObserver mDataSetObserver = new DataSetObserver() {

        @Override
        public void onChanged() {

            super.onChanged();
            notifyDataSetChanged(false);
        }
    };

    private LinearLayout.LayoutParams expandedTabLayoutParams;

    public SlidingTabStrip(Context context) {

        super(context);
        init(context);
    }

    public SlidingTabStrip(Context context, AttributeSet attrs) {

        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabStrip, 0, 0);
        try {
            mTabBackgroundResId = a.getInteger(R.styleable.SlidingTabStrip_tabBackgroundResId, mTabBackgroundResId);
            mIndicatorColor = a.getInteger(R.styleable.SlidingTabStrip_indicatorColor, mIndicatorColor);
            mUnderlineColor = a.getInteger(R.styleable.SlidingTabStrip_underlineColor, mUnderlineColor);
            mDividerColor = a.getInteger(R.styleable.SlidingTabStrip_dividerColor, mDividerColor);
            mMinHeight = a.getInteger(R.styleable.SlidingTabStrip_tabMinHeight, mMinHeight);
            mMinChildWidth = a.getInteger(R.styleable.SlidingTabStrip_minChildWidth, mMinChildWidth);
            mUnderlineHeight = a.getInteger(R.styleable.SlidingTabStrip_underlineHeight, mUnderlineHeight);
            mIndicatorHeight = a.getInteger(R.styleable.SlidingTabStrip_indicatorHeight, mIndicatorHeight);
            mDividerWidth = a.getInteger(R.styleable.SlidingTabStrip_tabDividerWidth, mDividerWidth);
            mDividerPadding = a.getInteger(R.styleable.SlidingTabStrip_customDividerPadding, mDividerPadding);

        } finally {
            a.recycle();
        }
        init(context);
    }

    public SlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context) {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        mMinHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, dm);
        mMinChildWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, dm);
        mDividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerWidth, dm);
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorHeight, dm);
        mUnderlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mUnderlineHeight, dm);
        mDividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerPadding, dm);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        setFillViewport(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);

        mPageListener = new PageListener();
        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabsContainer.setMinimumHeight(mMinHeight);

        addView(mTabsContainer);
    }

    public void setViewPager(ViewPager viewPager, boolean makeInitialSelection) {

        mWorksWithViewPager = true;
        this.mViewPager = viewPager;

        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(mPageListener);
        }

        notifyDataSetChanged(makeInitialSelection);
    }

    /**
     * @param adapter
     * @param tabClickListener
     * @param makeInitialSelection
     */
    public void setAdapter(BaseAdapter adapter, TabClickListener tabClickListener, boolean makeInitialSelection) {

        mWorksWithViewPager = false;
        this.mAdapter = adapter;
        this.mTabClickListener = tabClickListener;
        notifyDataSetChanged(makeInitialSelection);
    }

    public void notifyDataSetChanged(boolean makeInitialSelection) {

        this.mInitialSelectionMade = !makeInitialSelection;
        mTabsContainer.removeAllViews();
        if (mWorksWithViewPager) {
            tabCount = mViewPager.getAdapter().getCount();
        } else {
            tabCount = mAdapter.getCount();
        }

        if (isViewPager() && mViewPager.getAdapter() != null) {
            unregisterDataSetObserver();
            mViewPager.getAdapter().registerDataSetObserver(mDataSetObserver);
            addTabsByViewPager();
        } else if (!mWorksWithViewPager && mAdapter != null) {
            unregisterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
            addTabsByAdapter();
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

//              TODO:  
           	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                   getViewTreeObserver().removeGlobalOnLayoutListener(this);
                   } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            	
            	//getViewTreeObserver().removeGlobalOnLayoutListener(this);

                if (isViewPager()) {
                    currentPosition = mViewPager.getCurrentItem();
                }
                scrollToChild(currentPosition, 0);
                if (!isViewPager() && !mInitialSelectionMade) {
                    changeSelectionState(mInitialTabView);
                    if (mTabClickListener != null) {
                        mTabClickListener.onTabSelected(mInitialTabView, currentPosition);
                    }
                    mInitialSelectionMade = true;
                    mInitialTabView = null;
                }
            }
        });
    }

    private void unregisterDataSetObserver() {

        try {
            if (mAdapter != null) {
                mAdapter.unregisterDataSetObserver(mDataSetObserver);
            } else if (mViewPager != null && mViewPager.getAdapter() != null) {
                mViewPager.getAdapter().unregisterDataSetObserver(mDataSetObserver);
            }
        } catch (IllegalStateException e) {
            // WHEN DATA SET OBSERVER IS NOT REGISTERED...
            e.printStackTrace();
        }
    }

    private void addTabsByAdapter() {

        for (int i = 0; i < tabCount; i++) {
            addCustomTab(i, mAdapter.getView(i, null, null)); // NOT GONNA IMPLEMENT RECYCLING HERE...
        }
    }

    private void addTabsByViewPager() {

        if (!(mViewPager.getAdapter() instanceof PagerAdapterCustomViewProvider)) {
            throw new IllegalStateException("The ViewPager Adapter should implement PagerAdapterCustomViewProvider");
        }

        for (int i = 0; i < tabCount; i++)
        {
        	View customV=((PagerAdapterCustomViewProvider) mViewPager.getAdapter()).getCustomView(i);
        	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 0, LinearLayout.LayoutParams.MATCH_PARENT);
        	params.weight = 1.0f;
        	customV.setLayoutParams(params);
            addCustomTab(i,customV );
        }
    }

    public void addCustomTab(final int position, View customView) {

        if (currentPosition == position) {
            mInitialTabView = customView;
        }

        customView.setBackgroundResource(mTabBackgroundResId);
        customView.setMinimumWidth(mMinChildWidth);

        customView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isViewPager()) {
                    mViewPager.setCurrentItem(position);
                } else {
                    scrollToChild(position, 0);
                    currentPosition = position;
                    changeSelectionState(v);
                    invalidate();
                    mTabClickListener.onTabSelected(v, position);
                }
            }
        });

        mTabsContainer.addView(customView, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    private void changeSelectionState(View viewToSelect) {

        if (selectOnClick) {
            for (int i = 0; i < mTabsContainer.getChildCount(); i++) {
                View childView = mTabsContainer.getChildAt(i);
                childView.setSelected(false);
            }
            viewToSelect.setSelected(true);
        }
    }

    private boolean isViewPager() {

        if (mWorksWithViewPager && mViewPager != null) {
            return true;
        }

        return false;
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        if (mShowDefaultIndicator) {
            drawIndicator(canvas);
        }

        // draw underline
        int containerHeight = mTabsContainer.getHeight();
        rectPaint.setColor(mUnderlineColor);
        canvas.drawRect(0, containerHeight - mUnderlineHeight, mTabsContainer.getWidth(), containerHeight, rectPaint);

        // draw divider
        dividerPaint.setStrokeWidth(mDividerWidth);
        dividerPaint.setColor(mDividerColor);
        for (int i = 0; i < tabCount - 1; i++) {

            View tab = mTabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), mDividerPadding, tab.getRight(), containerHeight - mDividerPadding, dividerPaint);
        }
    }

    private void drawIndicator(Canvas canvas) {

        rectPaint.setColor(mIndicatorColor);

        // default: line below current tab
        View currentTab = mTabsContainer.getChildAt(currentPosition);
        float lineLeft = (currentTab != null) ? currentTab.getLeft() : 0;
        float lineRight = (currentTab != null) ? currentTab.getRight() : 0;

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = mTabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = (nextTab != null) ? nextTab.getLeft() : 0;
            final float nextTabRight = (nextTab != null) ? nextTab.getRight() : 0;

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        int height = getHeight();
        canvas.drawRect(lineLeft, height - mIndicatorHeight, lineRight, height, rectPaint);
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            if (tabCount > 0) scrollToChild(position, (int) (positionOffset * mTabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (mPagerChangeListener != null) {
                mPagerChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mViewPager.getCurrentItem(), 0);
            }

            if (mPagerChangeListener != null) {
                mPagerChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {

            if (mPagerChangeListener != null) {
                mPagerChangeListener.onPageSelected(position);
            }
        }

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {

        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {

        int currentPosition;

        public SavedState(Parcelable superState) {

            super(superState);
        }

        private SavedState(Parcel in) {

            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {

                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {

                return new SavedState[size];
            }
        };
    }

    public void setShouldExpand(boolean shouldExpand) {

        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public void setShowDefaultIndicator(boolean showDefaultIndicator) {

        this.mShowDefaultIndicator = showDefaultIndicator;
    }

    public void setSelectedStateOnClick(boolean selectedState) {

        this.selectOnClick = selectedState;
    }

    public void setPagerChangeListener(ViewPager.OnPageChangeListener viewPagerChangeListener) {

        this.mPagerChangeListener = viewPagerChangeListener;
    }

    public void setMinChildWidth(int minChildWidth) {

        this.mMinChildWidth = minChildWidth;
        requestLayout();
    }

    public void setTabBackgroundResId(int backgroundResId) {

        this.mTabBackgroundResId = backgroundResId;
        requestLayout();
    }

    public void makeInitialSelectionCallback(boolean makeInitialSelectionCall) {

        this.mInitialSelectionMade = (!makeInitialSelectionCall);
    }

    public void setCurrentPosition(int currentPosition) {

        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {

        return this.currentPosition;
    }

    public void moveToFirstPosition() {

        moveToPosition(0);
    }

    public void moveToLastPosition() {
        if (mAdapter == null) return;
         int count = mAdapter.getCount();
        if(count > 0){
            moveToPosition(count - 1);
        }
    }

    public boolean moveToPosition(int position) {

        if (mAdapter == null) return false;
        if (0 <= position && position < mAdapter.getCount()) {
            setCurrentPosition(position);
            if (isViewPager()) {
                mViewPager.setCurrentItem(position);
            }
            notifyDataSetChanged(true);

            return true;
        }
        return false;
    }

    public boolean moveToNextPosition() {

        int newPosition = this.currentPosition + 1;
        if (mAdapter == null) return false;
        if (newPosition < mAdapter.getCount()) {
            moveToPosition(newPosition);
            return true;
        } else {
            return false;
        }
    }

    public boolean moveToPreviousPosition() {

        int newPosition = this.currentPosition + -1;
        if (mAdapter == null) return false;
        if (newPosition >= 0) {
            moveToPosition(newPosition);
            return true;
        } else {
            return false;
        }
    }


    public void setDividerColor(int color) {

        this.mDividerColor = color;
    }

    public void setDividerWidth(int dividerWidth) {

        this.mDividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, getResources().getDisplayMetrics());
    }

    public void setDividerPadding(int dividerPadding) {

        this.mDividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, getResources().getDisplayMetrics());
    }

    public void setIndicatorColor(int indicatorColor) {

        this.mIndicatorColor = indicatorColor;
    }
}
