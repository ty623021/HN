package com.example.admin.hn.ui.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.ApplyingDetailInfo;
import com.example.admin.hn.ui.adapter.AuditingApplyingAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.SpaceItemDecoration;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 审核申请中详情列表
 *
 * @author Administrator
 */
public class AuditingApplyingActivity extends BaseActivity implements OnRefreshListener,OnLoadmoreListener {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView text_tile_right;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    @Bind(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private AuditingApplyingAdapter adapter;
    private ArrayList<ApplyingDetailInfo> list = new ArrayList<>();
    private String url = Api.BASE_URL + Api.GET_APPLY_ORDER_DETAIL;
    private String url_pass=Api.BASE_URL + Api.PASS_APPLY;
    private int page = 0;
    private String applyno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_layout);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    @Override
    public void initTitleBar() {
        Intent intent = getIntent();
        applyno = intent.getStringExtra("applyno");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        textTitle.setText("申请详情");
        text_tile_right.setText("通过");
    }

    @Override
    public void initView() {
        //下拉刷新
        ToolRefreshView.setRefreshLayout(this, refreshLayout, this, this);
        adapter = new AuditingApplyingAdapter(this,applyno, R.layout.item_auditing_applying_adapter, list);
        recycleView.addItemDecoration(new SpaceItemDecoration(10, 20, 0, 0));
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        recycleView.setAdapter(adapter);
    }

    /**
     *
     */
    public static void startActivity(Context context,String applyno) {
        Intent intent = new Intent(context, AuditingApplyingActivity.class);
        intent.putExtra("applyno", applyno);
        ((Activity) context).startActivityForResult(intent, 1000);
    }

    @OnClick({R.id.text_title_back,R.id.text_tile_right,R.id.network_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                close();
            break;
            case R.id.text_tile_right:
                submit();
                break;
            case R.id.network_img:
                network_img.setVisibility(View.GONE);
                sendHttp();
                break;

        }
    }

    private void close() {
        setResult(1000);
        finish();
    }

    private void submit() {
        Map map = new HashMap();
        map.put("applyNo", applyno + "");
        http.postJson(url_pass, map, "正在审核...", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("审核", json);
                if (GsonUtils.isSuccess(json)) {
                    ToolAlert.showToast(context, "审核通过");
                    close();
                }else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        sendHttp();
    }


    public void sendHttp() {
        params.put("page", page + "");
        params.put("applyNo", applyno);
        http.postJson(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("申请单", json);
                progressTitle = null;
                if (GsonUtils.isSuccess(json)) {
                    TypeToken typeToken=new TypeToken<List<ApplyingDetailInfo>>(){};
                    List<ApplyingDetailInfo> applys = (List<ApplyingDetailInfo>) GsonUtils.jsonToList(json, typeToken, "applysDetail");
                    if (ToolString.isEmptyList(applys)) {
                        if (isRefresh) {
                            list.clear();
                        }
                        list.addAll(applys);
                    }
                }else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
                ToolRefreshView.hintView(adapter,refreshLayout,false,network,noData_img,network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
                ToolRefreshView.hintView(adapter,refreshLayout,true,network,noData_img,network_img);
            }
        });
    }
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        isRefresh = false;
        sendHttp();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        isRefresh = true;
        sendHttp();
    }
}
