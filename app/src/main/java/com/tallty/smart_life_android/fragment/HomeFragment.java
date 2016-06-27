package com.tallty.smart_life_android.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.HomeAdapter;
import com.tallty.smart_life_android.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/6/20.
 * 首页
 */
public class HomeFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    // 列表数据
    private List<String> titles = new ArrayList<String>() {
        {
            add("智慧健康");
            add("健身达人");
            add("市政大厅");
            add("社区活动");
            add("智慧家居");
            add("社区IT");
            add("新品上市");
            add("限量发售");
        }
    };
    private List<Integer> images = new ArrayList<Integer>() {
        {
            add(R.drawable.smart_healthy);
            add(R.drawable.fitness_people);
            add(R.drawable.government);
            add(R.drawable.community_activity);
            add(R.drawable.smart_home);
            add(R.drawable.community_it);
            add(R.drawable.new_product);
            add(R.drawable.on_sail);
        }
    };
    private String[][] itemButtons = {
        {"预约体检", "健康报告", "预约专家", "更多"},
        {"更多数据"},
        {"天气查询", "违章查询", "公积金", "医保卡", "预约办证", "更多查询"},
        {"活动详情"},
        {"远程控制", "电子猫眼"},
        {"IT学堂", "在线冲印", "IT服务", "更多"},
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

    // 构造函数
    public HomeFragment() {

    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.home_list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic() {
        setList();
    }

    @Override
    public void onClick(View v) {

    }


    // ========================业务逻辑=========================
    private void setList() {
        recyclerView.setLayoutManager(layoutManager);
        HomeAdapter homeAdapter = new HomeAdapter(context, titles, images, itemButtons, itemIcons);
        recyclerView.setAdapter(homeAdapter);
    }
}
