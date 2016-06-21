package net.mtjo.app.ui.msg.cache;

import java.util.ArrayList;
import java.util.List;

import com.aframe.database.WYDB;
import com.aframe.ui.WYActivityManager;
import net.mtjo.app.entity.MessageModel;

public class MessageCache {
	private WYDB db;
	private static MessageCache instance;
	
	  /******************** 使用静态内部类手段创建单例 **********************/
    private MessageCache() {
        db = WYDB.create(WYActivityManager.create().topActivity(),true);
    }

    /**
     * 使用默认配置
     */
    public synchronized static MessageCache create() {
        if (instance == null) {
            instance = new MessageCache();
        }
        return instance;
    }
    
    /**
     * 添加一条消息
     */
    public void add(MessageModel msg, String uid){
    	MessageBean data = new MessageBean();
    	data.alert = msg.getAlert();
    	data.createTime = msg.getCreateTime();
    	data.touid = uid;
    	data.type = msg.getType();
    	data.fromuid = msg.getFromuid();
    	data.msgid = msg.getMsgid();
    	data.params = msg.getParams();
    	
        MessageBean dataInDb = null;
        dataInDb = getMessageType(msg.getFromuid(), msg.getParams(), msg.getMsgid(), uid);
        if (dataInDb == null) {
        	data.setNum(1);
            db.save(data);
        }
    }
    
    /**
     * 获取最后的消息
     */
    public MessageModel getNewlySearch(String uid){
    	MessageModel model = null;
    	List<MessageBean> datas = db.findAllByWhere(MessageBean.class, "touid='"+uid+"'",
                "msgid DESC limit 1 offset 0;");
    	if(null != datas && datas.size() > 0){
    		model = new MessageModel();
    		MessageBean bean = datas.get(0);
    		model.setId(bean.getMsgid());
    		model.setAlert(bean.getAlert());
    		model.setCount(bean.getNum());
    		model.setFromuid(bean.getFromuid());
    		model.setType(bean.getType());
    		
    		datas.clear();
    		datas = null;
    	}
    	return model;
    }
    
    /**
     *获取总消息数 
     */
    public int getCount(String uid){
    	String sql = "select sum(num) from wy_lscn_msg where touid='"+uid+"';";
    	return db.sumSql(MessageBean.class,sql, "sum(num)");
    } 
    
    /**
     * 分页获取消息
     * @param pageno 页数从1开始
     * @param pagesize 一页的大小
     */
    public List<MessageModel> getMsgList(int pageno, int pagesize,String uid){
    	List<MessageModel> list = null;
    	String sql = new StringBuilder("select * from (select id,createTime,touid,alert,params,type,fromuid,").
    			append("sum(num) as num, max(msgid) as msgid from wy_lscn_msg where touid='").
    			append(uid).append("' group by fromuid, params ) order by msgid DESC limit ").
    			append(pagesize).append(" offset ").append((pageno-1) * pagesize).append(";").toString();
  	
    	List<MessageBean> datas = db.findBySql(MessageBean.class, sql);
    	MessageModel model = null;
    	if(null != datas && datas.size() > 0){
    		list = new ArrayList<MessageModel>();
    		
    		for(int i=0,l=datas.size(); i<l; i++){
    			model = new MessageModel();
        		MessageBean bean = datas.get(i);
        		model.setId(bean.getMsgid());
        		model.setAlert(bean.getAlert());
        		model.setCount(bean.getNum());
        		model.setFromuid(bean.getFromuid());
        		model.setType(bean.getType());
        		model.setCreateTime(bean.getCreateTime());
        		model.setParams(bean.getParams());
        		model.setTouid(bean.getTouid());
        		
        		list.add(model);
    		}
    		    		
    		datas.clear();
    		datas = null;
    	}
    	return list;
    }
    
    /**
     * 清除type的消息数
     */
    public void updateType(String fromuid, String params, String uid){
    	String where = new StringBuilder("fromuid='").
    			append(fromuid).
    			append("' and params='").
    			append(params).
    			append("' and touid='").
    			append(uid).append("';").toString();
    	List<MessageBean> datas = db.findAllByWhere(MessageBean.class,where);
    	if (datas != null && !datas.isEmpty()) {
    		MessageBean data = null;
    		for(int i=0, l=datas.size(); i<l; i++){
    			data = datas.get(i);
    			data.setNum(0);
    			db.update(data);
    			data = null;
    		}
    	}
    }
    
    /**
     * 根据问题qid清除消息数
     */
    public void updateTypeByqid(String fromuid, String qid, String uid){
    	String where = new StringBuilder("fromuid='").
    			append(fromuid).
    			append("' and params like '%\"qid\":").
    			append(qid).
    			append("%' and touid='").
    			append(uid).append("';").toString();
    	List<MessageBean> datas = db.findAllByWhere(MessageBean.class,where);
    	if (datas != null && !datas.isEmpty()) {
    		MessageBean data = null;
    		for(int i=0, l=datas.size(); i<l; i++){
    			data = datas.get(i);
    			data.setNum(0);
    			db.update(data);
    			data = null;
    		}
    	}
    }
    
    /**
     * 获取匹配消息
     */
    private MessageBean getMessageType(String fromuid, String params, long msgid,String uid){
    	MessageBean data = null;
    	String where = new StringBuilder("fromuid='").
    			append(fromuid).
    			append("' and params='").
    			append(params).
    			append("' and msgid=").
    			append(msgid).
    			append(" and touid='").
    			append(uid).append("';").toString();
    	
    	List<MessageBean> datas = db.findAllByWhere(MessageBean.class,where);
    	if (datas != null && !datas.isEmpty()) {
    		data = datas.get(0);
    	}
    	return data;
    }
}
