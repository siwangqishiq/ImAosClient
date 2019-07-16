package com.xinlan.imclient.widget;

import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xinlan.imclient.R;

/**
 * 底部indicator
 */
public class BottomIndicator {
    private SparseArray<IndicatorHolder> mIndicators = new SparseArray<IndicatorHolder>();

    private ViewPager mPagerView;

    private int mCurIndex = -1;
    private ViewGroup mRootView;

    public static BottomIndicator create(ViewGroup view) {
        return new BottomIndicator(view);
    }

    public BottomIndicator(ViewGroup root) {
        mRootView = root;
    }

    public void installView(){
        addIndicator(0 , (ImageView) mRootView.findViewById(R.id.recent_icon) , R.drawable.indicator_msg , R.drawable.indicator_msg_checked);
        addIndicator(1 , (ImageView) mRootView.findViewById(R.id.contacts_icon) , R.drawable.indicator_contacts , R.drawable.indicator_contacts_checked);
        addIndicator(2 , (ImageView) mRootView.findViewById(R.id.setting_icon) , R.drawable.indicator_setting , R.drawable.indicator_setting_checked);
    }

    public void addIndicator(final int index, ImageView img, int defaultId, int selectedId) {
        if(img == null)
            return;

        mIndicators.put(index, new IndicatorHolder(img, defaultId, selectedId));

        ((ViewGroup)img.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPagerView != null){
                    mPagerView.setCurrentItem(index);
                }
            }
        });
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mPagerView != null){
//                    mPagerView.setCurrentItem(index);
//                }
//            }
//        });
    }

    public void setPageView(final ViewPager pager){
        if(pager == null || mPagerView == pager)
            return;

        mPagerView = pager;
        mPagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                select(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void select(final int selectIndex){
        if(mCurIndex != selectIndex){
            final IndicatorHolder holder = mIndicators.get(selectIndex);
            if(holder != null){
                if(mIndicators.get(mCurIndex)!= null){
                    mIndicators.get(mCurIndex).unChecked();
                }
                holder.checked();
            }
            mCurIndex = selectIndex;
        }
    }

    public static class IndicatorHolder {
        ImageView img;
        int defaultId;
        int selectedId;

        public IndicatorHolder(ImageView img, int defaultId, int selectedId) {
            this.img = img;
            this.defaultId = defaultId;
            this.selectedId = selectedId;
        }

        public void checked(){
            img.setImageResource(selectedId);
        }

        public void unChecked(){
            img.setImageResource(defaultId);
        }
    }//end inner class

}// end class
