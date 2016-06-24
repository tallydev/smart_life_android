package com.tallty.smart_life_android.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeAdapter;
import com.tallty.smart_life_android.base.BaseFragment;
import com.tallty.smart_life_android.custom.RecyclerVIewItemTouchListener;
import com.tallty.smart_life_android.holder.HomeBannerHolderView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kang on 16/6/20.
 * 首页
 */
public class HomeFragment extends BaseFragment implements OnItemClickListener{
    private ConvenientBanner banner;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    // 列表数据
    private String[] itemTitles = {
            "智慧健康", "健身达人", "市政大厅", "社区活动", "智慧家居", "社区IT", "新品上市", "限量发售"
    };
    private Integer[] itemImages = {
        R.drawable.smart_healthy, R.drawable.fitness_people, R.drawable.community_activity,
        R.drawable.government, R.drawable.smart_home, R.drawable.community_it,
        R.drawable.new_product, R.drawable.on_sail
    };
    private String[][] itemButtons = {
        {"预约体检", "健康报告", "预约专家", "• • •"},
        {"更多数据"},
        {"天气查询", "违章查询", "公积金", "医保卡", "预约办证", "更多查询"},
        {"活动详情"},
        {"远程控制", "电子猫眼"},
        {"IT学堂", "在线冲印", "IT服务", "• • •"},
        {"我要预约"},
        {"更多臻品"}
    };
    private Integer[][] itemIcons = {
        {R.mipmap.smart_healthy_one, R.mipmap.smart_healthy_two, R.mipmap.smart_healthy_three},
        {R.mipmap.fitness_people_one},
        {R.mipmap.goverment_one, R.mipmap.goverment_two, R.mipmap.goverment_three, R.mipmap.goverment_four, R.mipmap.goverment_five, R.mipmap.goverment_six},
        {R.mipmap.community_activity_one},
        {R.mipmap.smart_home_one, R.mipmap.smart_home_two},
        {R.mipmap.community_it_one, R.mipmap.community_it_two, R.mipmap.community_it_three},
        {R.mipmap.new_product_one},
        {R.mipmap.on_sail_one}

    };
    // banner图数据
    private String[] imagesUrl = {
        "http://d.3987.com/sqmy_131219/001.jpg",
        "http://img2.3lian.com/2014/f2/37/d/39.jpg",
        "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
        "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg"
    };

    public HomeFragment() {

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        banner = getViewById(R.id.homeBanner);
        recyclerView = getViewById(R.id.home_list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic() {
        setBannerImages();
        setList();
    }

    @Override
    public void onClick(View v) {

    }


    // ========================逻辑方法=========================
    private void setList() {
        recyclerView.setLayoutManager(layoutManager);
        // 初始化adapter
        HomeAdapter homeAdapter = new HomeAdapter(context, itemTitles, itemImages, itemButtons, itemIcons);
        recyclerView.setAdapter(homeAdapter);
        // 交互事件
        setRecycleViewListener();
    }

    private void setBannerImages() {
        List<String> networkImages = Arrays.asList(imagesUrl);
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

    /**
     * banner点击事件
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        showToast("点击了第"+position+"个");
    }

    /**
     * RecycleView点击事件
     */
    private void setRecycleViewListener() {
        recyclerView.addOnItemTouchListener(new RecyclerVIewItemTouchListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                showToast("您点击了第"+vh.getAdapterPosition()+"个");
            }
        });
    }
}
