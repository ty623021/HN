package com.example.admin.hn.ui.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.hn.R;
import com.example.admin.hn.api.Api;
import com.example.admin.hn.base.BaseActivity;
import com.example.admin.hn.model.AddressInfo;
import com.example.admin.hn.ui.adapter.SelectAddressAdapter;
import com.example.admin.hn.utils.GsonUtils;
import com.example.admin.hn.utils.ToolAlert;
import com.example.admin.hn.utils.ToolRefreshView;
import com.example.admin.hn.utils.ToolString;
import com.example.admin.hn.volley.RequestListener;
import com.example.admin.hn.recycleView.DeleteRecyclerView;
import com.example.admin.hn.recycleView.OnItemClickListener;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hjy on 2016/11/5.
 * 选择地址
 */
public class SelectAddressActivity extends BaseActivity {

    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_tile_right)
    TextView text_tile_right;
    @Bind(R.id.recycleView)
    DeleteRecyclerView recycleView;
    @Bind(R.id.network_disabled)
    RelativeLayout network;
    @Bind(R.id.network_img)
    ImageView network_img;
    @Bind(R.id.noData_img)
    ImageView noData_img;
    private List<AddressInfo> list=new ArrayList<>();
    private SelectAddressAdapter adapter;
    private String url = Api.SHOP_BASE_URL + Api.GET_ADDRESS_LIST;
    private AddressInfo addressInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ButterKnife.bind(this);
        initTitleBar();
        initView();
        initData();
    }

    /**
     * @param context
     */
    public static void startActivity(Activity context, AddressInfo info) {
        Intent intent = new Intent(context, SelectAddressActivity.class);
        intent.putExtra("info", info);
        context.startActivityForResult(intent, 100);
    }


    @Override
    public void initTitleBar() {
        textTitle.setText("选择收货地址");
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
        text_tile_right.setText("管理");
    }

    @Override
    public void initView() {

        adapter = new SelectAddressAdapter(this, R.layout.item_select_address, list);
        recycleView.setLayoutManager(new LinearLayoutManager(context));
        recycleView.setAdapter(adapter);

        addressInfo = (AddressInfo) getIntent().getSerializableExtra("info");

        recycleView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AddressInfo info = list.get(position);
                Intent intent = new Intent();
                intent.putExtra("info", info);
                setResult(100,intent);
                finish();
            }

            @Override
            public void onDeleteClick(int position) {
                adapter.delAddress(position);
            }
        });
    }


    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendHttp();
    }

    @OnClick({R.id.text_title_back,R.id.text_tile_right,R.id.add_address})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_back:
                finish();
                break;
            case R.id.text_tile_right:
                ManagerAddressActivity.startActivity(context);
                break;
            case R.id.add_address:
                CreateAddressActivity.startActivity(context);
                break;
        }
    }

    private void sendHttp() {
        http.get(url, params, progressTitle, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                Logger.e("收货地址列表", json);
                if (GsonUtils.isShopSuccess(json)) {
                    TypeToken typeToken = new TypeToken<List<AddressInfo>>() {};
                    List<AddressInfo> data = (List<AddressInfo>) GsonUtils.jsonToList2(json, typeToken, "content");
                    if (list.size() > 0) {
                        list.clear();
                    }
                    if (ToolString.isEmptyList(data)) {
                        list.addAll(data);
                    }
                    setSelectAddress();
                }else {
                    ToolAlert.showToast(context, GsonUtils.getError(json));
                }
                ToolRefreshView.hintView(adapter, false, network, noData_img, network_img);
            }

            @Override
            public void requestError(String message) {
                ToolAlert.showToast(context, message);
                ToolRefreshView.hintView(adapter, true, network, noData_img, network_img);
            }
        });
    }

    /**
     * 设置当前选中的地址
     */
    private void setSelectAddress() {
        if (addressInfo != null) {
            for (int i = 0; i < list.size(); i++) {
                AddressInfo info = list.get(i);
                if (info.id.equals(addressInfo.id)) {
                    info.isSelect = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 && data != null) {
            //如果接收到 创建地址返回的数据就关闭当前 并把数据返回给初始页面
            setResult(resultCode,data);
            finish();
        }
    }
}
