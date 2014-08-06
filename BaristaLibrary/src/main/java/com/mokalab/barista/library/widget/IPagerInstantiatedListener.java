package com.mokalab.barista.library.widget;

import android.support.v4.app.Fragment;
/**
 * IPagerInstantiatedListener interface
 * Interface used for {@link ca.bellmedia.bnngo.adapters.base.BaseFragmentPagerAdapter#instantiateItem(android.view.ViewGroup, int)}
 * @author Anning Hu
 */
public interface IPagerInstantiatedListener {

    /**
     * Callback for when fragment is instantiated in ViewPager for {@link ca.bellmedia.bnngo.adapters.base.BaseFragmentPagerAdapter}
     *
     * @param fr
     * @param position
     */
    public void onPagerInstantiated(Fragment fr, int position);
}
