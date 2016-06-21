package net.mtjo.app.ui.ask;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aframe.bitmap.FailReason;
import com.aframe.bitmap.ImageCallBack;
import com.aframe.bitmap.ImageLoader;
import com.aframe.ui.widget.CircleImageView;
import net.mtjo.app.R;
import net.mtjo.app.entity.AskLawyerDetailModel;

public class AskLawyerDetailAdapter extends BaseAdapter {
	private Context context;
	private List<AskLawyerDetailModel> list;
	
	public AskLawyerDetailAdapter( Context context,List<AskLawyerDetailModel> list ) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	public int getItemViewType(int position) {
		// 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
		if (position == 0) {
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		AskLawyerDetailModel model= list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			if(position == 0){
				convertView = LayoutInflater.from(context).inflate(R.layout.ask_lawyer_detail_to_item, null);
//				if(list.size() > 1){
					convertView.findViewById(R.id.tip_tv).setVisibility(View.GONE);
//				} else {
//					convertView.findViewById(R.id.tip_tv).setVisibility(View.VISIBLE);
//				}
			} else {
				convertView = LayoutInflater.from(context).inflate(R.layout.ask_lawyer_detail_from_item, null);
			}
			
			holder.head_img = (CircleImageView) convertView.findViewById(R.id.head_img);
			holder.name = (TextView) convertView.findViewById(R.id.name_tv);
			holder.time = (TextView) convertView.findViewById(R.id.time_tv);
			holder.title = (TextView) convertView.findViewById(R.id.content_tv);
			holder.count = (TextView) convertView.findViewById(R.id.count_tv);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		ImageLoader.getInstance(context).displayImage(holder.head_img, model.getLawyerImage(), new ImageCallBack() {
			
			@Override
			public void onStarted(String imageUri, View view) {
				
			}
			
			@Override
			public void onProcess(String imageUri, View view, int percent) {
			}
			
			@Override
			public void onFailed(String imageUri, View view, FailReason failReason) {
				((CircleImageView)view).setImageResource(R.drawable.head_default);
			}
			
			@Override
			public void onComplete(String imageUri, View view, Bitmap loadedImage) {
				if(view instanceof ImageView && view.getTag().equals(imageUri)){
					if(null != loadedImage){
						((CircleImageView)view).setImageBitmap(loadedImage);
					}else{
						((CircleImageView)view).setImageResource(R.drawable.head_default);
					}
				}
				
			}
		}, false, 0, context.getResources().getDimensionPixelSize(R.dimen.ask_head_width),
		context.getResources().getDimensionPixelSize(R.dimen.ask_head_height));
		
		holder.name.setText(model.getLawyerName());
		holder.time.setText(model.getCreateTime());
		holder.title.setText(model.getVoice());
		if(null != holder.count && null != model.getReplyNum())
			holder.count.setText(model.getReplyNum());
		return convertView;
	}
	
	public static class ViewHolder{
		public CircleImageView head_img;
		public TextView name;
		public TextView time;
		public TextView title;
		public TextView count;
	}

}
