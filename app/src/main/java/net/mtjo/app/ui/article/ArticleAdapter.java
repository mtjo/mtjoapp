package net.mtjo.app.ui.article;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import net.mtjo.app.entity.Articles;

public class ArticleAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflate;
	private ArrayList<Articles> datalist;
	private boolean mBusy = false;
	
	public ArticleAdapter(Context ctx, ArrayList<Articles> list){
		this.mContext = ctx;
		this.inflate =  LayoutInflater.from(mContext);
		this.datalist = list;
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflate.inflate(R.layout.lawarticles_listitem,  parent, false);
			holder.thum_img = (ImageView) convertView.findViewById(R.id.lawarticle_thum_img);
			holder.title_tv = (TextView) convertView.findViewById(R.id.lawarticle_title_tv);
			holder.descri_tv = (TextView) convertView.findViewById(R.id.lawarticle_catname_tv);
			holder.time_tv = (TextView) convertView.findViewById(R.id.lawarticle_time_tv);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Articles article = (Articles) getItem(position);
		if(null != article){
			if(null != holder.title_tv){
				holder.title_tv.setText(article.getTitle());
				if (article.getIsRead() == 1) {
					holder.title_tv.setTextColor(mContext.getResources().getColor(R.color.tv_unread_color));
				}else{
					holder.title_tv.setTextColor(mContext.getResources().getColor(R.color.text_dark_color));
				}
			}
			if(null != holder.descri_tv)
				holder.descri_tv.setText(article.getCatname());
			if(null != holder.time_tv)
				holder.time_tv.setText(article.getUpdatetime());
			if(null != holder.thum_img && !mBusy)
				ImageLoader.getInstance(mContext).displayImage(holder.thum_img, 
						article.getThumb(), new ImageCallBack() {
							@Override
							public void onStarted(String imageUri, View view) {
							}
							@Override
							public void onProcess(String imageUri, View view, int percent) {
							}
							@Override
							public void onFailed(String imageUri, View view, FailReason failReason) {
								//设置默认图片
								((ImageView)view).setImageResource(R.drawable.articles_default);
							}
							@Override
							public void onComplete(String imageUri, View view, Bitmap loadedImage) {
								if(view.getTag().equals(imageUri)){
									if(null != loadedImage){
										((ImageView)view).setImageBitmap(loadedImage);
									}else{
										((ImageView)view).setImageResource(R.drawable.articles_default);
									}
								}
							}
						}, false, 0, 
						mContext.getResources().getDimensionPixelSize(R.dimen.article_img_width),
						mContext.getResources().getDimensionPixelSize(R.dimen.article_img_height));
		}
		return convertView;
	}
	
	private class ViewHolder{
		ImageView thum_img;
		TextView title_tv;
		TextView descri_tv;
		TextView time_tv;
	}
}
