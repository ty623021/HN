package com.example.admin.hn.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.admin.hn.http.Constant;
import com.example.admin.hn.model.GoodsInfo;
import com.example.admin.hn.ui.fragment.shop.bean.ShopCartInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by WIN10 on 2018/5/10.
 * 购物车增删改查
 */

public class ToolShopCartUtil {

    /**
     * 添加到购物车
     */
    public static void addShopCartInfo(Context context,GoodsInfo goodsInfo,int number) {
        List<ShopCartInfo> all = DataSupport.findAll(ShopCartInfo.class);
        if (ToolString.isEmptyList(all)) {
            for (int i = 0; i < all.size(); i++) {
                ShopCartInfo shop = all.get(i);
                List<String> specItemsIds = shop.getCurrGoodsSpecItemsIds();
                List<String> itemsIds = goodsInfo.currGoodsSpecItemsIds;
                boolean is = true;
                for (int j = 0; j < specItemsIds.size(); j++) {
                    //检测添加的规格参数是否相同
                    String s1 = specItemsIds.get(j);
                    String s2 = itemsIds.get(j);
                    if (!s1.equals(s2)) {
                        //找到一个规格数据不匹配的时候就跳出循环
                        is = false;
                        break;
                    }
                }
                if (shop.getGoodsId().equals(goodsInfo.goods.id) && is) {
                    //如果找到同一个商品同一种规格就修改购买的数量
                    shop.setBuyNumber(shop.getBuyNumber() + 1);
                    shop.update(shop.getId());
                    break;
                }
                if (i == all.size() - 1) {
                    save(context, goodsInfo, number);
                }
            }
        }else {
            save(context, goodsInfo, number);
        }
    }

    private static void save(Context context, GoodsInfo goodsInfo, int number) {
        //如果到最后都没找到相同的商品就添加新的列
        ShopCartInfo shopCartInfo = new ShopCartInfo();
        shopCartInfo.setBuyNumber(number);
        shopCartInfo.setSelect(false);
        shopCartInfo.setEditSelect(false);
        shopCartInfo.setGoodsId(goodsInfo.goods.id);
        shopCartInfo.setGoodsName(goodsInfo.spu.goodsName);
        shopCartInfo.setGoodsPrice(goodsInfo.goods.goodsPrice);
        shopCartInfo.setQty(goodsInfo.goods.qty);
        shopCartInfo.setImageUrl(goodsInfo.goods.imageUrl);
        shopCartInfo.setSpuId(goodsInfo.spu.id);
        shopCartInfo.setUsp(goodsInfo.spu.usp);
        shopCartInfo.setCurrGoodsSpecItemsIds(goodsInfo.currGoodsSpecItemsIds);
        boolean save = shopCartInfo.save();
        if (save) {
            int count = DataSupport.count(ShopCartInfo.class);
            Intent intent = new Intent(Constant.ACTION_GOODS_DETAIL_ACTIVITY);
            intent.putExtra("status", 2);
            intent.putExtra("count", count);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } else {
            ToolAlert.showToast(context, "添加失败");
        }
    }

    public static void deleteShopCartInfo(Context context, List<ShopCartInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            ShopCartInfo cartInfo = list.get(i);
            cartInfo.delete();
        }
        int count = DataSupport.count(ShopCartInfo.class);
        Intent intent = new Intent(Constant.ACTION_GOODS_DETAIL_ACTIVITY);
        intent.putExtra("status", 2);
        intent.putExtra("count", count);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
