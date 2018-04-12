package com.example.admin.hn.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.ShipInfo;
import com.example.admin.hn.ui.adapter.AllTabAdapter;
import com.example.admin.hn.ui.fragment.seaShart.ShipSelectFragment;
import com.example.admin.hn.ui.fragment.shop.HnShopFragment;
import com.example.admin.hn.ui.fragment.shop.ShopFragment;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 选择船舶
 * @author Administrator
 */
public class ShipSelectActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView right;
    @Bind(R.id.text_tile_del)
    TextView text_tile_del;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    private boolean isCancel=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tablaout);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    @Override
    public void initTitleBar() {
        textTitle.setText("选择船舶");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        right.setText("提交");
        text_tile_del.setVisibility(View.VISIBLE);
        text_tile_del.setText("全选");
    }
    /**
     *
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShipSelectActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.text_title_back,R.id.text_tile_right, R.id.text_tile_del})
    public void onClick(View v) {
        Intent intent = new Intent("ShipSelectFragment");
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.text_tile_right://提交
                intent.putExtra("status", 1);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                break;
            case R.id.text_tile_del:
                if (isCancel) {
                    isCancel=false;
                    text_tile_del.setText("取消");
                    intent.putExtra("status", 2);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }else {
                    isCancel=true;
                    text_tile_del.setText("全选");
                    intent.putExtra("status", 3);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
                break;
        }
    }

    @Override
    public void initData() {
        AllTabAdapter adapter = new AllTabAdapter(this, viewPager);
        adapter.addTab("未选择","0", ShipSelectFragment.class);
        adapter.addTab("已选择","1", ShipSelectFragment.class);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateUI(position);
    }

    private void updateUI(int position) {
        switch (position) {
            case 0://未选择
                text_tile_del.setVisibility(View.VISIBLE);
                if (isCancel) {
                    text_tile_del.setText("全选");
                }else {
                    text_tile_del.setText("取消");
                }
                right.setText("提交");
                break;
            case 1://已选择
                text_tile_del.setVisibility(View.GONE);
                right.setText("");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
