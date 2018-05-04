package com.example.admin.hn.api;

/**
 * Created by duantao
 *
 * @date on 2017/9/11 14:40
 */
public class Api {

//    public final static String BASE_URL = "http://zxs.tunnel.qydev.com/menusystem/";
    public  static  String BASE_URL = "http://222.66.158.231:9000/";//船舶
    public  static  String SHOP_BASE_URL = "http://172.16.0.10:8990/";//商城

//    public  static  String BASE_URL = "http://58.67.133.129:8080/";
    //注册
    public static final String REGISTER = "menusystem/mobile/registered.action";

    //登录
    public static final String LOGIN = "menusystem/mobile/login.action";

    //订单
    public static final String ORDER = "menusystem/orderinterfsce/getorder.action";

    //库存
    public static final String INVENTORY = "menusystem/orderinterfsce/getstock.action";

    //海图
    public static final String CHART = "menusystem/orderinterfsce/getchart.action";

    //修改密码
    public static final String CHANGEPASSWORD = "menusystem/mobile/updatepassword.action";

    //修改邮箱
    public static final String CHANGEPHONE = "menusystem/mobile/updatephonenumber.action";

    //忘记密码
    public static final String FINDPASSWORD = "menusystem/mobile/forgetpassword.action";

    //注册协议
    public static final String AGREEMENT = "menusystem/HTML/agreement.html";

    //消息通知
    public static final String MESSAGE = "menusystem/mobile/notice.action ";

    //邮箱验证
    public static final String EMAIL = "menusystem/mobile/getVerification.action";

    //消息通知消息刪除
    public static final String MESSAGEDEL = "menusystem/mobile/deletepush.action";

    //订单明细
    public static final String ORDERDETAIL = "menusystem/mobilenew/getOrderDetail.action";

    //船舶查询
    public static final String SHIPINQUIRY = "menusystem/mobile/shipinquiry.action";

    //船舶选择
    public static final String SHIPSELECTION = "menusystem/mobile/shipselection.action";

    //海图更新
    public static final String SHIPPACKAGE = "menusystem/mobile/shippackage.action";

    //app更新
    public static final String UPDATE = "menusystem/mobile/getVersion.action";

    //公司名称
    public static final String COMPANYNAME = "menusystem/mobile/getCompany.action";

    //极光id
    public static final String APPID = "menusystem/mobile/updateAppid.action";
    //船舶资料管理 待选 获取数据
    public static final String GET_DOCUMENTS = "menusystem/computer/getDocuments.action";
    //船舶资料管理 待选 提交数据
    public static final String SUBMIT_DOCUMENTS = "menusystem/computer/submitDocuments.action";
    //船舶资料管理 已选 获取数据
    public static final String GET_SUBMITTED_DOCUMENTS = "menusystem/computer/getSubmittedDocuments.action";



//    ================== 商城接口========================

    //获取商城首页分类
    public static final String GET_SHOP_TYPE = "sit/menu/all";
    //商品列表
    public static final String GET_GOODS_LIST = "sto/goods/list";
    //获取城市列表
    public static final String GET_AREA_LIST = "sit/area/list";
    //个人收货地址查询接口
    public static final String GET_ADDRESS_LIST = "usr/addr/list";
    //个人收货地址新增接口 "id": 1,//id存在为更新，否则进行新增操作
    public static final String GET_CREATE_ADDRESS = "usr/addr/save";
    //订单查询接口
    public static final String GET_LIST_ORDER = "sto/order/list";


//    ================== 商城接口========================

}
