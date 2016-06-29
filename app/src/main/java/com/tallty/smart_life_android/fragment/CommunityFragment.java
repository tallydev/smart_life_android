package com.tallty.smart_life_android.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tallty.smart_life_android.R;
import com.tallty.smart_life_android.adapter.CommunityGridViewAdapter;
import com.tallty.smart_life_android.base.BaseFragment;
import com.tallty.smart_life_android.custom.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 16/6/20.
 * 群组
 */
public class CommunityFragment extends BaseFragment{
    private ImageView government;
    private ImageView best_tone;
    private RelativeLayout service;
    private MyGridView gridView;

    private List<Integer> icons = new ArrayList<Integer>() {
        {
            add(R.drawable.community_one);
            add(R.drawable.community_two);
            add(R.drawable.community_three);
            add(R.drawable.community_four);
        }
    };
    private List<String> names = new ArrayList<String>() {
        {
            add("水电费");
            add("交通违章");
            add("天然气");
            add("其他支付");
        }
    };

    public CommunityFragment() {
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initToolBar(Toolbar toolbar, TextView title) {
        title.setText("社区");
    }

    @Override
    protected void initView() {
        government = getViewById(R.id.community_government);
        best_tone = getViewById(R.id.best_tone);
        service = getViewById(R.id.community_service);
        gridView = getViewById(R.id.tab_community_gridView);
    }

    @Override
    protected void setListener() {
        government.setOnClickListener(this);
        best_tone.setOnClickListener(this);
        service.setOnClickListener(this);
    }

    @Override
    protected void processLogic() {
        initGridView();
    }

    private void initGridView() {
        CommunityGridViewAdapter adapter = new CommunityGridViewAdapter(context, icons, names);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        showToast("水电费");
                        break;
                    case 1:
                        showToast("交通违章");
                        break;
                    case 2:
                        showToast("天然气");
                        break;
                    case 3:
                        showToast("其他支付");
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community_service:
                showToast("上门服务");
                break;
            case R.id.community_government:
                showToast("政府信息平台(暂未开放)");
                break;
            case R.id.best_tone:
                showToast("号码百事通(暂未开放)");
                break;
        }
    }
}
