package com.mokalab.barista.library.widget;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

import com.antoniotari.android.util.R;

/**
 * Created by pirdad on 1/26/2014.
 */
public abstract class BaseTabsFragmentPagerAdapter<T> extends BaseFragmentPagerAdapter<T> implements PagerAdapterCustomViewProvider {

    private LayoutInflater mLayoutInflater;
    private ArrayList<TextView> mTabViews;
    
    private int mTextColor = Color.WHITE;
    private Integer mTextSize = null;
    private Typeface fontType=Typeface.DEFAULT; 
    
    
    private void setTextProprierties(TextView tv)
    {
    	tv.setTextColor(mTextColor);
    	if(mTextSize!=null)
    		tv.setTextSize(mTextSize);
    	try{
    		tv.setTypeface(fontType);
    	}catch(Exception e){
    		tv.setTypeface(fontType,0);
    	}
    }
    
    public void setTextColor(int color)
    {
    	mTextColor=color;
    }
    
    public void setTextTypeface(Typeface type)
    {
    	fontType=type;
    }
    
    public void setFontSize(int fSize)
    {
    	mTextSize= Integer.valueOf(fSize);
    }
    
    public BaseTabsFragmentPagerAdapter(FragmentManager fm, LayoutInflater layoutInflater, ArrayList<T> data) {

        super(fm, data);

        mLayoutInflater = layoutInflater;
        mTabViews = new ArrayList<TextView>((data != null) ? data.size() : 0);
    }
    
    public BaseTabsFragmentPagerAdapter(FragmentManager fm, LayoutInflater layoutInflater, ArrayList<T> data,Activity activity) {

        super(fm, data);

        mLayoutInflater = layoutInflater;
        mTabViews = new ArrayList<TextView>((data != null) ? data.size() : 0);
    }

    /**
     * Create or re-use a TextView that represents a Tab in this adapter based on a tab position.
     * @param position position of the tab
     * @return new View for the tab
     */
    protected View getTabView(int position) {

        TextView txtTab = null;

        if (position < mTabViews.size()) {
            txtTab = mTabViews.get(position);
        }

        if (txtTab == null) {

            txtTab = new TextView(mLayoutInflater.getContext());
            txtTab.setText(getTabTitle(position));
            txtTab.setGravity(Gravity.CENTER);
            txtTab.setTextColor(getTabTextColor(position));
            
            setTextProprierties(txtTab);

            int padding = mLayoutInflater.getContext().getResources().getDimensionPixelOffset(R.dimen.default_item_10_pad);
            txtTab.setPadding(padding, 0, padding, 0);
            mTabViews.add(position, txtTab);
        }

        if (txtTab.getParent() != null) {
            // REMOVE IN-CASE IF ALREADY ATTACHED TO A PREVIOUS PARENT
            ((ViewGroup) txtTab.getParent()).removeView(txtTab);
        }

        LayoutParams params = new LayoutParams( 0, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        txtTab.setLayoutParams(params);
        
        return txtTab;
    }

    /**
     * Called by {@link #getTabView(int)} to return the text for the tab view.
     * @param position position of the tab
     * @return tab's text
     */
    protected abstract String getTabTitle(int position);

    /**
     * Override if needed for customization. It is called by {@link #getTabView(int)} to set the
     * text color of the tab view. Default is: R.color.tabs_text_color
     * @param position
     * @return
     */
    protected int getTabTextColor(int position) {

        return mLayoutInflater.getContext().getResources().getColor(R.color.FloralWhite);
    }

    @Override
    public View getCustomView(int position) {

        return getTabView(position);
    }

    @Override
    public void setData(ArrayList<T> data) {

        super.setData(data);
        mTabViews = new ArrayList<TextView>(data.size());
    }
}
