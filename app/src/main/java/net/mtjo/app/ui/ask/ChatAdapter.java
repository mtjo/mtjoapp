package net.mtjo.app.ui.ask;

import java.util.ArrayList;
import java.util.Date;

import com.aframe.bitmap.FailReason;
import com.aframe.bitmap.ImageCallBack;
import com.aframe.bitmap.ImageLoader;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
import net.mtjo.app.entity.AskLawyerDetailChat;
import net.mtjo.app.utils.SharedUserInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter{
	private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<AskLawyerDetailChat> data_list;
    private	String lawUrl;
    private Bitmap lawmap;
    private String myid;

    public ChatAdapter(Context context, ArrayList<AskLawyerDetailChat> list){
    	this.mContext = context;
        this.data_list = list;
        mInflater = LayoutInflater.from(this.mContext);
        myid = SharedUserInfo.getUserInfo(this.mContext).getUid();
    }
    
    public int getCount() {
        return null== data_list?0 : data_list.size();
    }

    public Object getItem(int position) {
		return null == data_list ? null : data_list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public int getItemViewType(int position) {
		// 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
    	AskLawyerDetailChat chat = data_list.get(position);
		if (StrUtils.strToString(chat.getFromUid()).equals(myid)) {
			return 0;
		} else {
			return 1;
		}
	}
    
    public int getViewTypeCount() {
		// 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
		return 2;
	}
    

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		AskLawyerDetailChat chat = (AskLawyerDetailChat) getItem(position);
		if (convertView == null)
	    {	
			  viewHolder = new ViewHolder();
	    	  if (StrUtils.strToString(chat.getFromUid()).equals(myid))
			  {	
	    			  convertView = mInflater.inflate(R.layout.chat_item_right, null);
			  }else{
				  	convertView = mInflater.inflate(R.layout.chat_item_left, null);
			  }
	    	  
			  viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.chat_item_sendtime_tv);
			  viewHolder.tvContent = (TextView) convertView.findViewById(R.id.chat_item_chatcontent_tv);
			  viewHolder.ivUserImage = (ImageView) convertView.findViewById(R.id.chat_item_userhead_img);
			  viewHolder.sendingImage = (ImageView) convertView.findViewById(R.id.chat_item_sending_img);
			  
			  convertView.setTag(viewHolder);
	    }else{
	        viewHolder = (ViewHolder) convertView.getTag();
	    }
		
		if(null != viewHolder.tvContent)
			viewHolder.tvContent.setText(StrUtils.strToString(chat.getVoice()));
		
		//处理时间
		if(null != viewHolder.tvSendTime){
			String str = "";
			if(position == 0){
				viewHolder.tvSendTime.setVisibility(View.VISIBLE);
				if(StrUtils.strToLong(chat.getCreateTime())>0){
					Date date = new Date(StrUtils.strToLong(chat.getCreateTime())*1000);
	        		Date ndate = new Date();
	        		String sdate = StrUtils.dateToFromat(date, "yyyy-MM-dd");
	        		String sndate = StrUtils.dateToFromat(ndate, "yyyy-MM-dd");
	        		if(null != sdate && null != sndate && sdate.equals(sndate))
	        			str = StrUtils.dateToFromat(date, "HH:mm");
	        		else
	        			str = StrUtils.dateToFromat(date, "yyyy-MM-dd HH:mm");
				}
				viewHolder.tvSendTime.setText(StrUtils.strToString(str));
			}else{
				AskLawyerDetailChat chat1 = (AskLawyerDetailChat) getItem(position-1);
				if(StrUtils.strToLong(chat.getCreateTime())>0 && StrUtils.strToLong(chat1.getCreateTime())>0){
					Date date = new Date(StrUtils.strToLong(chat.getCreateTime())*1000);
					Date date1 = new Date(StrUtils.strToLong(chat1.getCreateTime())*1000);
					long temp = date.getTime() - date1.getTime();    //相差毫秒数
					if(temp > 1000*60*3){
						viewHolder.tvSendTime.setVisibility(View.VISIBLE);
						Date ndate = new Date();
						String sdate = StrUtils.dateToFromat(date, "yyyy-MM-dd");
		        		String sndate = StrUtils.dateToFromat(ndate, "yyyy-MM-dd");
		        		if(null != sdate && null != sndate && sdate.equals(sndate))
		        			str = StrUtils.dateToFromat(date, "HH:mm");
		        		else
		        			str = StrUtils.dateToFromat(date, "yyyy-MM-dd HH:mm");
    	        		viewHolder.tvSendTime.setText(StrUtils.strToString(str));
					}else{
						viewHolder.tvSendTime.setVisibility(View.GONE);
					}
				}else{
					viewHolder.tvSendTime.setVisibility(View.GONE);
				}
			}
		}
		
		//加载头像
		if(null != viewHolder.ivUserImage){
			if(!StrUtils.strToString(chat.getFromUid()).equals(myid)){
				if(null != lawmap)
					viewHolder.ivUserImage.setImageBitmap(lawmap);
				else if(null != lawUrl)
					ImageLoader.getInstance(mContext).displayImage(viewHolder.ivUserImage, lawUrl, new ImageCallBack() {
						@Override
						public void onStarted(String imageUri, View view) {
						}
						
						@Override
						public void onProcess(String imageUri, View view, int percent) {
						}
						
						@Override
						public void onFailed(String imageUri, View view, FailReason failReason) {
						}
						
						@Override
						public void onComplete(String imageUri, View view, Bitmap loadedImage) {
							if(null != loadedImage){
								if(null != imageUri && imageUri.equals(lawUrl))
									lawmap = loadedImage;
								viewHolder.ivUserImage.setImageBitmap(lawmap);
							}
						}
					}, true, mContext.getResources().getDimensionPixelSize(R.dimen.head_round),
					mContext.getResources().getDimensionPixelSize(R.dimen.head_width),
					mContext.getResources().getDimensionPixelSize(R.dimen.head_height));
			}
		}
		
		//是否正在发送
		if(chat.isIssending()){
			//正在发送内容
			if(null !=  viewHolder.sendingImage){
				viewHolder.sendingImage.setVisibility(View.VISIBLE);
				if(null == viewHolder.operatingAnim){
					viewHolder.operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_reflash);  
					LinearInterpolator lin = new LinearInterpolator();  
					viewHolder.operatingAnim.setInterpolator(lin);
				}
				viewHolder.sendingImage.setAnimation(viewHolder.operatingAnim);
				viewHolder.sendingImage.startAnimation(viewHolder.operatingAnim);
			}
		}else{
			if(viewHolder.sendingImage != null){
				viewHolder.sendingImage.setVisibility(View.GONE);
				viewHolder.sendingImage.clearAnimation();
				if(null != viewHolder.operatingAnim)
					viewHolder.operatingAnim = null;
			}
		}
	    return convertView;
	}
	
	private class ViewHolder {
        public TextView tvSendTime;
        public TextView tvContent;
        public ImageView ivUserImage;
        public ImageView sendingImage;
        private Animation operatingAnim;//旋转动画
    }

	public void setLawUrl(String lawUrl) {
		this.lawUrl = lawUrl;
	}
}
