package net.mtjo.app.config;

public class Config {
	public static String LSCN_URL = "http://ls.cn/?m=";
	public static String API_URL="http://mtjo.net/?g=app&m=";
	
	public static String FINDLAW_URL = "http://api.app.findlaw.cn:8888/router/rest";
//	public static String FINDLAW_URL = "http://192.168.3.235:9081/router/rest";
//	public static String ASK_URL = "http://api.app.findlaw.cn:8888/router/rest";
//	public static String ASK_URL = "http://192.168.3.235:9081/router/rest";
//	public static String ASK_URL = "http://192.168.23.124:8080/router/rest";
	
	/**
	 * lscn http post公开请求地址
	 */
	public static String LSCN_PUB_URL = LSCN_URL+"apppub";


	//文章
	public static String API_ARTICLES =  API_URL+"index&a=article_list";

	//banner
	public static String API_HOME_SLIDES =  API_URL+"index&a=home_slides";
	
	/**
	 * lscn http post登录请求地址
	 */
	public static String LSCN_USER_URL = LSCN_URL+"appuser";
	
	/**
	 * 协议地址
	 */
	public static String LSCN_SERVICE = "http://china.findlaw.cn/appwebview/index.php?m=Lscnapp&a=pact";
	
	/**
	 * 问律师协议地址
	 */
	public static String FINDLAW_SERVICE = "http://china.findlaw.cn/appwebview/index.php?m=Lscnapp&a=find";
	
	/**
	 * 找律师协议地址
	 */
	public static String ASKLAW_SERVICE = "http://china.findlaw.cn/appwebview/index.php?m=Lscnapp&a=ask";
	
	/**
	 * 律师详情地址
	 * 后加律师uid
	 */
	public static String LAW_DETAIL = "http://www.ls.cn/?g=Api&m=OpenWebView&a=lawyerdetail&appkey=0001&uid=";
	
	
	/**
	 * 客服电话
	 */
	public static final String  SERVICE_CALL = "18177627403";
	
	/**
	 * 消息通知id
	 */
	public static int NoticeID;


	// 友盟自定义事件
	public static final String UMENG_COMMIT_ASK_LAWYER = "提交问律师";
	public static final String UMENG_SEND_MOBILE = "拨打400电话";
	public static final String UMENG_TOLD_FRIEND = "告诉好友";
	public static final String UMENG_EXIT = "注销";
	public static final String UMENG_LOGIN = "登录_注册";
	public static final String UMENG_ASK_LAWYER1 = "问律师1";
	public static final String UMENG_ASK_LAWYER2 = "问律师2";
	public static final String UMENG_ASK_LAWYER_NEXT = "下一步";
	public static final String UMENG_CALL_LAWYER = "拨打律师电话";
	public static final String UMENG_COMMIT_REPEAT_NEXT = "重复发送_继续发送";
	public static final String UMENG_COMMIT_REPEAT_LOOK = "重复发送_查看我的咨询";
}
