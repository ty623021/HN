package com.example.admin.hn.ui.fragment.seaShart;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.model.OrderInfo;
import com.example.admin.hn.ui.adapter.MaterialSelectAdapter;
import com.example.admin.hn.utils.SpaceItemDecoration;
import com.example.admin.hn.utils.ToolRefreshView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 已选择
 */
public class MaterialManagerFragment extends BaseFragment implements OnRefreshListener,OnLoadmoreListener{

    private static final String TAG = "已选择";

    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;

    private ArrayList<OrderInfo.Order> list = new ArrayList<>();
    private CommonAdapter adapter;
    private View view;
    //是否审核1已审核2未审核
    private String statu = "1";
    //搜索条件1(查询该用户全部订单) 2(根据船舶名称)3(船舶编号)4(订单号)
    private int status = 1;
    private int page = 1;
    private int screen = 1;
    private String url_order = Api.BASE_URL + Api.ORDER;
    private RefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recycle_layout, container, false);
        ButterKnife.bind(this, view);
        initTitleBar();
        initView();
        initData();
        return view;
    }


    @Override
    public void initView() {
        //下拉刷新
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        ToolRefreshView.setRefreshLayout(activity, refreshLayout, this, this);
        adapter = new MaterialSelectAdapter(activity, R.layout.item_material_layout, list);
        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.addItemDecoration(new SpaceItemDecoration(10,10,0,0));
        recycleView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        data(1, "", 0);
    }


    @Override
    public void initTitleBar() {
        Bundle bundle = getArguments();
        screen = Integer.parseInt(bundle.getString("type"));
    }


    @OnClick({ R.id.network_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_img:
                network_img.setVisibility(View.GONE);
                refreshLayout.finishRefresh(1000);
                break;
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            page = 1;
        }
    }


    public void data(final int status, String down, final int Loadmore) {
        for (int i = 0; i < 10; i++) {
            list.add(new OrderInfo.Order());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        adapter.notifyDataSetChanged();
        refreshlayout.finishLoadmore(1000);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        refreshlayout.finishRefresh(1000);
    }
}
