package com.MeiHuaNet;

/**
 * 
 * @description 存放程序中用到的全局静态常量
 * @author lee
 * @createTime 2013-6-21下午2:26:51
 * 
 */
public class Constants {

	/**
	 * 分页请求时每页请求的数据的条数
	 */
	public static final int PAGE_SIZE = 10;

	/**
	 * 应用的key（服务器那边提供的）
	 */
	public static final String KEY = "1C7DF98B-1344-4A2F-9559-34F2CA2D4F1E";

	/* 程序中用到的接口名称 */
	/**
	 * 获取资讯列表的接口
	 */
	public static final String GetPostByPage = "GetPostByPage";
	/**
	 * 获取资讯详情的接口
	 */
	public static final String GetPostDetail = "GetPostDetail";
	/**
	 * 获取知识列表
	 */
	public static final String GetArticleByPage = "GetArticleByPage";
	/**
	 * 获取知识详情
	 */
	public static final String GetArticleDetail = "GetArticleDetail";
	/**
	 * 案例列表
	 */
	public static final String GetCaseByPage = "GetCaseByPage";
	/**
	 * 案例详情
	 */
	public static final String GetCaseDetail = "GetCaseDetail";
	/**
	 * 营销列表
	 */
	public static final String GetWikiByPage = "GetWikiByPage";
	/**
	 * 营销详情
	 */
	public static final String GetWikiDetail = "GetWikiDetail";
	/**
	 * 获取媒体类型
	 */
	public static final String GetMediaType = "GetMediaType";
	/**
	 * 检查是否允许下载
	 */
	public static final String checknewdownratecard = "checknewdownratecard";
	/**
	 * 增加下载刊例日志
	 */
	public static final String createnewdownratecardlog = "createnewdownratecardlog";
	/**
	 * 获取媒体详情
	 */
	public static final String getnewmediadetail = "getnewmediadetail";
	/**
	 * 获取媒体列表
	 */
	public static final String getnewmedialist = "getnewmedialist";
	/**
	 * 获取服务商列表
	 */
	public static final String GetVendorList = "GetVendorList";
	/**
	 * 获取服务商详情
	 */
	public static final String GetVendorDetail = "GetVendorDetail";
	/**
	 * 获取职位列表
	 */
	public static final String GetJobList = "GetJobList";
	/**
	 * 获取职位详情
	 */
	public static final String GetJobDetail = "GetJobDetail";
	/**
	 * 获取有效活动列表
	 */
	public static final String GeEffectiveEventList = "GeEffectiveEventList";
	/**
	 * 获取我报名的活动
	 */
	public static final String GeMyEventList = "GeMyEventList";
	/**
	 * 获取过往活动列表
	 */
	public static final String GePastEventList = "GePastEventList";
	/**
	 * 获取活动详情
	 */
	public static final String GetEventDetail = "GetEventDetail";
	/**
	 * 登录梅花网
	 */
	public static final String LoginUserInfo = "LoginUserInfo";
	/**
	 * 注册为梅花网会员接口
	 */
	public static final String SignIn = "SignIn";
	/**
	 * 免费活动报名接口
	 */
	public static final String ApplicationEvent = "ApplicationEvent";
	/**
	 * 付费活动报名接口
	 */
	public static final String ApplicationPayEvent = "ApplicationPayEvent";
	/**
	 * 付费活动支付宝支付成功后 处理订单
	 */
	public static final String DoOrder = "DoOrder";
	/**
	 * 创意分类
	 */
	public static final String CreativeCategory = "CreativeCategory";
	/**
	 * 今日免费
	 */
	public static final String CreativeTodayFrees = "CreativeTodayFrees";
	/**
	 * 创意列表
	 */
	public static final String CreativeList = "CreativeList";
	/**
	 * 创意详情
	 */
	public static final String CreativeDetail = "CreativeDetail";
	/**
	 * 检查是否购买
	 */
	public static final String CheckPurchased = "CheckPurchased";
	/**
	 * 检查试用版是否超过数量
	 */
	public static final String CheckTrailPurchased = "CheckTrailPurchased";
	/**
	 * 检查VIP权限是否超过数量
	 */
	public static final String CheckVIPPurchased = "CheckVIPPurchased";
	/**
	 * 反馈邮件发送接口
	 */
	public static final String SendFeedbackEmail = "SendFeedbackEmail";
	/**
	 * 创建在线支付订单
	 */
	public static final String CreateOrderByString = "CreateOrderByString";
	/**
	 * 处理在线支付订单状态
	 */
	public static final String EditOrderStatus = "EditOrderStatus";

	/* 所有接口对应的webservice地址 */
	/**
	 * 资讯的webservice地址
	 */
	public static final String WEBSERVICE_URL_INFO = "http://www.meihua.info/today/todaynewservice.asmx";
	/**
	 * 知识、营销、创意等资源的webservice地址
	 */
	public static final String WEBSERVICE_URL = "http://www.meihua.info/knowledge/MobileNewService.asmx";
	/**
	 * 招聘站接口的地址
	 */
	public static final String WEBSERVICE_URL_RECRUIT = "http://www.meihua.info/today/job/MobileNewService.asmx";
	/**
	 * 活动站的接口地址
	 */
	public static final String WEBSERVICE_URL_EVENT = "http://www.meihua.info/event/MobileNewService.asmx";
	/**
	 * 中国传媒库的接口地址
	 */
	public static final String WEBSERVICE_URL_MEDIA = "http://www.meihua.info/mediasearch/MobileNewService.asmx";
	/**
	 * 营销服务商的地址
	 */
	public static final String WEBSERVICE_URL_MARKET_SERVICE = "http://www.meihua.info/vendorsearch/MobileNewService.asmx";
	/**
	 * 升级到vip的地址
	 */
	public static final String WEBSERVICE_URL_VIP = "http://crm.meihua.info/service/CRMService.asmx";

	/**
	 * 
	 * @description 联网请求时的请求类型的枚举类
	 * @author lee
	 * @time 2013-12-16 下午4:38:01
	 * 
	 */
	public static enum Request_Type {
		/**
		 * 进入页面，初始化页面时从服务器请求数据
		 */
		init,
		/**
		 * 头部刷新时请求数据
		 */
		head_refresh,
		/**
		 * 底部刷新时请求数据
		 */
		bottom_refresh,
		/**
		 * 搜索时第一次请求数据
		 */
		search_init,
		/**
		 * 搜索时头部刷新
		 */
		search_head_refresh,
		/**
		 * 搜索时底部刷新
		 */
		search_bottom_refresh,
	}

	/**
	 * 用户登录后返回的用户的信息保存的文件名
	 */
	public static final String fileName_Userinfo = "userinfo.obj";
}
