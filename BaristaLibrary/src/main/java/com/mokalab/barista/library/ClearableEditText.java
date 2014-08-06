package com.mokalab.barista.library;

/*******************************************************************************
 * Copyright 2014 MokaLab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

/**
 * This EditText will allow the user to clear it's text. It uses the compound drawables to accomplish this.
 *
 * <br><br>
 * Use R.styleable.ClearableEditTextAttrs to define some initial settings.
 *
 * <br><br>
 * Author: Pirdad S.
 */
public class ClearableEditText extends EditText implements OnTouchListener, OnFocusChangeListener {

    /**
     * Use this interface to get a callback when the {@link com.mokalab.barista.library.ClearableEditText} clears the edit text
     * field when the clear button is pressed.
     */
    public interface OnClearTextListener {

        /**
         * Gets invoked when the Edit Text field gets cleared.
         */
        public void onTextCleared();
    }

    private static final int COMPOUND_DRAWABLE_LEFT = 0;
    private static final int COMPOUND_DRAWABLE_TOP = 1;
    private static final int COMPOUND_DRAWABLE_RIGHT = 2;
    private static final int COMPOUND_DRAWABLE_BOTTOM = 3;

    private Drawable mDrawable;
    private boolean mClearButtonVisibleAlways;
    private int mClearButtonDrawable;

    /**
     * Listeners.
     */
    private OnClearTextListener mClearTextListener;
    private OnTouchListener mOnTouchListener;
    private OnFocusChangeListener mFocusChangeListener;

    /**
     * EditText that has an option to clear it's text.
     */
    public ClearableEditText(Context context) {

        super(context);
        init(context, null, 0);
    }

    /**
     * EditText that has an option to clear it's text.
     */
    public ClearableEditText(Context context, AttributeSet attrs) {

        super(context, attrs, 0);
        init(context, attrs, 0);
    }

    /**
     * EditText that has an option to clear it's text.
     */
    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ClearableEditTextAttrs, 0, defStyle);

        if (attributes != null) {
            mClearButtonVisibleAlways = attributes.getBoolean(R.styleable.ClearableEditTextAttrs_clearButtonAlwaysVisible, false);
            mClearButtonDrawable = attributes.getInt(R.styleable.ClearableEditTextAttrs_clearButtonDrawable, 0);
            attributes.recycle();
        }

        if (mClearButtonDrawable == 0) {
            mClearButtonDrawable = getDefaultClearButtonDrawable();
        }

        mDrawable = getCompoundDrawables()[COMPOUND_DRAWABLE_RIGHT];
        if (mDrawable == null) {
            mDrawable = getResources().getDrawable(mClearButtonDrawable);
        }
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());

        setClearIcon(shouldClearButtonBeVisible(false));
        setOnTouchListener(this);
        setOnFocusChangeListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (getCompoundDrawables()[COMPOUND_DRAWABLE_RIGHT] != null) {

            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - mDrawable.getIntrinsicWidth());

            if (tappedX) {

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    setText("");
                    if (mClearTextListener != null) {
                        mClearTextListener.onTextCleared();
                    }
                }
                return true;
            }
        }

        if (mOnTouchListener != null) {
            return mOnTouchListener.onTouch(v, event);
        }

        return false;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (mClearButtonVisibleAlways) {

            setClearIcon(true);

        } else {

            if (hasFocus) {
                setClearIcon(getText().length() != 0);
            } else {
                setClearIcon(false);
            }
        }

        if (mFocusChangeListener != null) {
            mFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (isFocused()) {

            if (!mClearButtonVisibleAlways) {
                setClearIcon(text.length() != 0);
            }
        }
    }

    /**
     * It sets Clear Button but also takes the visibility into account.
     * @param visible true to visible otherwise false.
     */
    protected void setClearIcon(boolean visible) {

        Drawable x = visible ? mDrawable : null;
        if (x == null) return;

        setCompoundDrawables(
                getCompoundDrawables()[COMPOUND_DRAWABLE_LEFT],
                getCompoundDrawables()[COMPOUND_DRAWABLE_TOP],
                x,
                getCompoundDrawables()[COMPOUND_DRAWABLE_BOTTOM]);
    }

    @Override
    public void setOnTouchListener(OnTouchListener listener) {
        mOnTouchListener = listener;
    }

    /**
     * Set the Listener for when the text gets cleared.
     */
    public void setClearTextListener(OnClearTextListener clearTextListener) {
        mClearTextListener = clearTextListener;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        mFocusChangeListener = listener;
    }

    private boolean shouldClearButtonBeVisible(boolean initialValue) {

        if (mClearButtonVisibleAlways) {
            return true;
        } else {
            return initialValue;
        }
    }

    private int getDefaultClearButtonDrawable() {

        return R.drawable.selector_edit_text_clear;
    }
}