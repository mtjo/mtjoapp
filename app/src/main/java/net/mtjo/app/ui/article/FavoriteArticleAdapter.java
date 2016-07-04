package net.mtjo.app.ui.article;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aframe.bitmap.FailReason;
import com.aframe.bitmap.ImageCallBack;
import com.aframe.bitmap.ImageLoader;

import net.mtjo.app.R;
import net.mtjo.app.entity.Articles;
import net.mtjo.app.entity.FavoriteArticles;

import java.util.ArrayList;

public class FavoriteArticleAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflate;
	private ArrayList<FavoriteArticles> datalist;
	private boolean mBusy = false;

	public FavoriteArticleAdapter(Context ctx, ArrayList<FavoriteArticles> list){
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
			convertView = inflate.inflate(R.layout.favorite_article_listitem,  parent, false);
			holder.id_tv = (TextView) convertView.findViewById(R.id.f_id_tv);
			holder.title_tv = (TextView) convertView.findViewById(R.id.f_title_tv);
			holder.button = (Button) convertView.findViewById(R.id.f_op_bt);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		FavoriteArticles article = (FavoriteArticles) getItem(position);
		if(null != article){
			if(null != holder.title_tv){
				holder.title_tv.setText(article.getTitle());
				holder.title_tv.setTextColor(mContext.getResources().getColor(R.color.text_dark_color));
			}
			if(null != holder.time_tv)
				holder.time_tv.setText(article.getCreatetime());

		}
		return convertView;
	}
	
	private class ViewHolder{
		TextView title_tv;
		TextView id_tv;
		TextView time_tv;
		Button button;
	}
}
