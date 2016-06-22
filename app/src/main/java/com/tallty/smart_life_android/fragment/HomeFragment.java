package com.tallty.smart_life_android.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.base.BaseFragment;
import com.tallty.smart_life_android.holder.HomeBannerHolderView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kang on 16/6/20.
 * 首页
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener, OnItemClickListener{
    private ConvenientBanner banner;
    private List<String> networkImages;
    private String[] images = {
            "http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

    public HomeFragment() {

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView(View view) {
        banner = getViewById(R.id.homeBanner);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic() {
        loadLocalImages();
    }

    @Override
    public void onClick(View v) {

    }

    private void loadLocalImages() {
        networkImages = Arrays.asList(images);
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new HomeBannerHolderView();
            }
        }, networkImages)
        .setPageIndicator(new int[] {R.drawable.banner_indicator, R.drawable.banner_indicator_focused})
        .setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onPause();
        // 开始自动翻页
        banner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 暂停翻页
        banner.stopTurning();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        showToast("滚动了");
    }

    @Override
    public void onPageSelected(int position) {
        showToast("监听到翻到第"+position+"了");
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        showToast("滚动状态改变了");
    }

    @Override
    public void onItemClick(int position) {
        showToast("点击了第"+position+"个");
    }
}
