package com.mokalab.barista.library.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Base Class for Fragment State Pager Adapter.
 * Created by pirdad on 1/26/2014.
 */
public abstract class BaseFragmentPagerAdapter<T> extends FragmentStatePagerAdapter {

    protected ArrayList<T> mData;
    private SparseArray<WeakReference<Fragment>> registeredFragments;
    private IPagerInstantiatedListener mPagerInstantiatedListenerListener;

    /**
     * Construct new Fragment Pager Adapter of Type T.
     * @param fm required FragmentManager
     * @param data optional data list
     */
    public BaseFragmentPagerAdapter(FragmentManager fm, ArrayList<T> data) {

        super(fm);

        mData = data;
        registeredFragments = new SparseArray<WeakReference<Fragment>>();
    }

    public BaseFragmentPagerAdapter(FragmentManager fm) {

        super(fm);
        registeredFragments = new SparseArray<WeakReference<Fragment>>();
    }

    @Override
    public int getCount() {

        if (mData == null) return 0;
        return mData.size();
    }

    /**
     * Set Associated DataList.
     * Call notifyDataSetChanged() if the views need to be updated.
     * @param data
     */
    public void setData(ArrayList<T> data) {

        mData = data;
    }

    /**
     * Get Associated Data List.
     * @return
     */
    public ArrayList<T> getData() {

        return mData;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, new WeakReference<Fragment>(fragment));
        if (mPagerInstantiatedListenerListener != null) mPagerInstantiatedListenerListener.onPagerInstantiated(fragment, position);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {

        WeakReference<Fragment> fr = registeredFragments.get(position);
        return (fr != null) ? fr.get() : null;
    }

    public void setPagerInstantiatedListenerListener(IPagerInstantiatedListener pagerInstantiatedListenerListener) {

        mPagerInstantiatedListenerListener = pagerInstantiatedListenerListener;
    }
}