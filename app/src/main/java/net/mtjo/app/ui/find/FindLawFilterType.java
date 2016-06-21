package net.mtjo.app.ui.find;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.aframe.Loger;
import com.aframe.http.StringCallBack;
import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.entity.SelectItem;
import net.mtjo.app.utils.FindLawUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("NewApi")
public class FindLawFilterType extends LinearLayout {
	private Activity mContext;
	private ListView parentListView;
	private ListView childListView;
	
	private FindLawTypeAdapter padapter;
	private FindLawTypeAdapter cadapter;
	private ArrayList<SelectItem> plist = new ArrayList<SelectItem>();
	private ArrayList<SelectItem> clist = new ArrayList<SelectItem>();
	private ArrayList<SelectItem> lsList = new ArrayList<SelectItem>();
	private ArrayList<SelectItem> flList = new ArrayList<SelectItem>();
	
	
	private int tPPosition = 0;
	private int tCPosition = 0;
	
	private OnItemSelectListener mOnSelectListener;
	
	private boolean isLscn;
	
	/**
	 * 设置选择监听
	 */
	public void setOnItemSelectListener(OnItemSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnItemSelectListener {
		public void getValue(SelectItem item);
	}
	
	public FindLawFilterType(Context context) {
		super(context);
		init(context);
	}
	
	public FindLawFilterType(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public FindLawFilterType(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	public FindLawFilterType(Context context, AttributeSet attrs,
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

		padapter = new FindLawTypeAdapter(context, plist, 
				R.color.theme_window_background, R.color.theme_window_background_click);
		parentListView.setAdapter(padapter);
		padapter.setOnItemClickListener(new FindLawTypeAdapter.OnItemClickListener() {			
			@Override
			public void onItemClick(SelectItem item) {
				clist.clear();
				if(null == item.getChilds() || item.getChilds().size() == 0){
					if(null != mOnSelectListener)
						mOnSelectListener.getValue(item);
				} else {
					clist.addAll(item.getChilds());
				}
				
				cadapter.notifyDataSetChanged();
			}
		});
		
		cadapter = new FindLawTypeAdapter(context, clist,0, 0);
		childListView.setAdapter(cadapter);
		cadapter.setOnItemClickListener(new FindLawTypeAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(SelectItem item) {
				if(null != mOnSelectListener)
					mOnSelectListener.getValue(item);
			}
		});
	}
	
	public void initData(boolean isLscn){
		if(isLscn){
			if(lsList.size() > 0){
				if(this.isLscn){
					return;
				}
				plist.clear();
				plist.addAll(lsList);
				clist.clear();
				padapter.setSelectItem(-1);
				cadapter.setSelectItem(-1);
				padapter.notifyDataSetChanged();
				cadapter.notifyDataSetChanged();
			} else{
				clear();
				loadData();
			}
		}else{
			if(flList.size() > 0){
				if(!this.isLscn){
					return;
				}
				plist.clear();
				plist.addAll(flList);
				clist.clear();
				padapter.setSelectItem(-1);
				cadapter.setSelectItem(-1);
				padapter.notifyDataSetChanged();
				cadapter.notifyDataSetChanged();
			} else {
				clear();
				loadLocalData();
			}
		}
		
		this.isLscn = isLscn;
	}
	
	private void clear(){
		plist.clear();
		clist.clear();
		padapter.setSelectItem(-1);
		cadapter.setSelectItem(-1);
		padapter.notifyDataSetChanged();
		cadapter.notifyDataSetChanged();
	}
	
	private void loadData(){
		HttpPostManager.getSpecialList(new StringCallBack() {
			@Override
			public void onSuccess(Object t) {
				if (null != this.getJsonArray()) {
					showData(this.getJsonArray());
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
			}
		}, mContext, null);
	}
	
	private void showData(JSONArray obj){
		try {
			ArrayList<SelectItem> list = SelectItem.typeJSONToList(obj);
			if(null != list && list.size() > 0){
				lsList.clear();
				SelectItem item = new SelectItem();
				item.setId("-1");
				item.setName("全部专长");
				lsList.add(item);
				lsList.addAll(list);
				plist.clear();
				plist.addAll(lsList);
				clist.clear();
				if(null != padapter)padapter.notifyDataSetChanged();
				if(null != cadapter)cadapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			Loger.debug("城市列表解析错误", e);
		} catch (Exception e) {
			Loger.debug("城市列表解析错误", e);
		}
	}

	/**
	 * 加载找法网咨询类型
	 */
	private void loadLocalData(){
		new FindLawUtils().readTypeList(mContext, new FindLawUtils.FindlawCallBact() {
			@Override
			public void onSuccess(List list) {
				if(null != list && list.size() > 0){
					flList.clear();
					SelectItem item = new SelectItem();
					item.setId("-1");
					item.setName("全部专长");
					flList.add(item);
					flList.addAll(list);
					plist.clear();
					plist.addAll(flList);
					clist.clear();
					if(null != padapter)padapter.notifyDataSetChanged();
					if(null != cadapter)cadapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onfailure(Throwable e) {
				if(null != e)
					Loger.debug("加载本地找法咨询类型", e);
			}
		});
	}
}
