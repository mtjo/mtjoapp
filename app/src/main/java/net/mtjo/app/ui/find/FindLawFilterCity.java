package net.mtjo.app.ui.find;

import java.util.ArrayList;
import java.util.List;




import com.aframe.Loger;
import net.mtjo.app.R;
import net.mtjo.app.entity.FindlawCity;
import net.mtjo.app.utils.FindLawUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("NewApi")
public class FindLawFilterCity extends LinearLayout {
	private Activity mContext;
	private ListView parentListView;
	private ListView childListView;
	
	private FindLawCityAdapter padapter;
	private FindLawCityAdapter cadapter;
	private ArrayList<FindlawCity> plist = new ArrayList<FindlawCity>();
	private ArrayList<FindlawCity> clist = new ArrayList<FindlawCity>();
	
	
	private int tPPosition = 0;
	private int tCPosition = 0;
	
	private OnItemSelectListener mOnSelectListener;
	
	/**
	 * 设置选择监听
	 */
	public void setOnItemSelectListener(OnItemSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnItemSelectListener {
		public void getValue(FindlawCity item);
	}
	
	public FindLawFilterCity(Context context) {
		super(context);
		init(context);
	}
	
	public FindLawFilterCity(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public FindLawFilterCity(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	public FindLawFilterCity(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}
	
	private void init(Context context){
		mContext = (Activity) context;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.filter_menu_list_list, this, true);
		parentListView = (ListView) findViewById(R.id.listview_parent);
		childListView = (ListView) findViewById(R.id.listview_child);

		padapter = new FindLawCityAdapter(context, plist, 
				R.color.theme_window_background, R.color.theme_window_background_click);
		parentListView.setAdapter(padapter);
		padapter.setOnItemClickListener(new FindLawCityAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(FindlawCity item) {
				if(null == item)return;
				clist.clear();
				if(null == item.getCitys() || item.getCitys().size() == 0){
					if(null != mOnSelectListener)
						mOnSelectListener.getValue(item);
				} else {
					clist.addAll(item.getCitys());
				}
				
				cadapter.notifyDataSetChanged();
			}
		});
		
		cadapter = new FindLawCityAdapter(context, clist,0, 0);
		childListView.setAdapter(cadapter);
		cadapter.setOnItemClickListener(new FindLawCityAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(FindlawCity item) {
				if(null != mOnSelectListener)
					mOnSelectListener.getValue(item);
			}
		});
	}
	
	public void initData(){
		if(plist.size() > 0)return;
		
		loadFindlawCity();
	}
	
	/**
	 * 加载本地找法城市
	 */
	private void loadFindlawCity(){
		new FindLawUtils().readCityList(mContext, new FindLawUtils.FindlawCallBact() {
			@Override
			public void onSuccess(List list) {
				if(null != list && list.size() > 0){
					plist.clear();
					plist.addAll(list);
					if(null != padapter)padapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onfailure(Throwable e) {
				if(null != e)
					Loger.debug("加载本地找法服务城市", e);
			}
		});
	}
}
