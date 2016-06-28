package net.mtjo.app.ui.search;

import java.util.ArrayList;
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
import net.mtjo.app.R;
import net.mtjo.app.entity.FindLawResult;

public class FindLawModeAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<FindLawResult> datalist;
	private LayoutInflater inflater;
	private boolean mBusy = false;
	
	
	public FindLawModeAdapter(Context context, ArrayList<FindLawResult>list){
		this.mContext = context;
		this.datalist = list;
		this.inflater = LayoutInflater.from(mContext);
	}
	
	public void setFlagBusy(boolean busy) {
        this.mBusy = busy;
	}
	
	@Override
	public int getCount() {
		return null == datalist? 0:datalist.size();
	}

	@Override
	public Object getItem(int position) {
		return null==datalist&&datalist.size()>0?null:datalist.get(position%datalist.size());
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.findlaw_listitem, parent, false);
			holder.head_img = (ImageView) convertView.findViewById(R.id.findlaw_head_img);
			holder.name_tv = (TextView) convertView.findViewById(R.id.finlaw_name_tv);
			holder.area_tv = (TextView) convertView.findViewById(R.id.findlaw_area_tv);
			holder.room_tv = (TextView) convertView.findViewById(R.id.findlaw_room_tv);
			
			holder.profession1 = (TextView)convertView.findViewById(R.id.profession1);
			holder.profession2 = (TextView)convertView.findViewById(R.id.profession2);
			holder.profession3 = (TextView)convertView.findViewById(R.id.profession3);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		FindLawResult result = (FindLawResult) getItem(position);
		if(null != result){
			if(null != holder.name_tv){
				holder.name_tv.setText(result.getName());
			}
			if(null != holder.area_tv){
				holder.area_tv.setText(result.getArea());
			}
			if(null != holder.room_tv){
				holder.room_tv.setText(result.getLawroom());
			}
			List<String> listVal = result.getListPre();
			if (listVal != null) {
				
				if (listVal.size() == 1) {
					holder.profession1.setText(listVal.get(0));
					holder.profession2.setText("");
					holder.profession3.setText("");
				}else if(listVal.size() == 2){
					holder.profession1.setText(listVal.get(0));
					holder.profession2.setText(listVal.get(1));
					holder.profession3.setText("");
				}else if(listVal.size() > 2){
					holder.profession1.setText(listVal.get(0));
					holder.profession2.setText(listVal.get(1));
					holder.profession3.setText(listVal.get(2));
				}else{
					holder.profession1.setText("");
					holder.profession2.setText("");
					holder.profession3.setText("");
				}
				
				
			}else{
				holder.profession1.setText("");
				holder.profession2.setText("");
				holder.profession3.setText("");
			}
			
			if(null != holder.head_img && !mBusy){
				ImageLoader.getInstance(mContext).displayImage(
						holder.head_img, result.getPhoto(), new ImageCallBack() {
							@Override
							public void onStarted(String imageUri, View view) {
								
							}
							
							@Override
							public void onProcess(String imageUri, View view, int percent) {
								
							}
							
							@Override
							public void onFailed(String imageUri, View view, FailReason failReason) {
								//设置默认图片
								((ImageView)view).setImageResource(R.drawable.head_default);
							}
							
							@Override
							public void onComplete(String imageUri, View view, Bitmap loadedImage) {
								if(view instanceof ImageView && view.getTag().equals(imageUri)){
									if(null != loadedImage){
										((ImageView)view).setImageBitmap(loadedImage);
									}else{
										((ImageView)view).setImageResource(R.drawable.head_default);
									}
								}
							}
						}, true, mContext.getResources().getDimensionPixelSize(R.dimen.head_round),
						mContext.getResources().getDimensionPixelSize(R.dimen.head_width),
						mContext.getResources().getDimensionPixelSize(R.dimen.head_height));
			}
		}
		
		return convertView;
	}
	
	private static class ViewHolder{
		ImageView head_img;
		TextView name_tv;
		TextView area_tv;
		TextView room_tv;
		TextView profession1;
		TextView profession2;
		TextView profession3;
	}

}
