package net.mtjo.app.ui.start;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aframe.Loger;
import com.aframe.http.StringCallBack;
import com.aframe.ui.widget.filtermenu.FilterMenuView;
import com.aframe.ui.widget.filtermenu.FilterMenuView.OnButtonClickListener;
import com.aframe.ui.widget.xlistview.DataQueryInerface;
import com.aframe.ui.widget.xlistview.XListView;
import com.aframe.utils.AppUtils;
import com.aframe.utils.StrUtils;
import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.entity.FindLawDefaultSort;
import net.mtjo.app.entity.FindLawResult;
import net.mtjo.app.entity.FindlawCity;
import net.mtjo.app.entity.SelectItem;
import net.mtjo.app.ui.search.FindLawDetailActivity;
import net.mtjo.app.ui.search.FindLawDetailActivity2;
import net.mtjo.app.ui.search.FindLawFilterCity;
import net.mtjo.app.ui.search.FindLawFilterSort;
import net.mtjo.app.ui.search.FindLawFilterType;
import net.mtjo.app.ui.search.FindLawModeAdapter;
import net.mtjo.app.ui.LoadStateView;
import net.mtjo.app.utils.SharedUserInfo;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class FindFragment extends Fragment implements DataQueryInerface{
	private Activity aty;
	
	private int pageSize = 20;
	
	private boolean isReflash;
	private boolean isInit = true;
	
	private LoadStateView load_lt;
	private XListView xlistview;
	private FilterMenuView filterview;
	
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<View> mViewArray = new ArrayList<View>();
	private FindLawFilterType typeView;
	private FindLawFilterCity cityView;
	private FindLawFilterSort sortView;
	
	private String area;
	private String type;
	private boolean isLscn; //是否好律师在线开通的城市
	private boolean hasLoadCity=false, hasLocation=false;
	private JSONObject location_json;
	private ArrayList<SelectItem> lsList = new ArrayList<SelectItem>();//好律师在线城市
	public static String weigth = "weight";
	public static String weigth_text = "默认排序";
	
	private int totalSize; //总条数
	
	private FindLawModeAdapter adapter;
	private ArrayList<FindLawResult> dataList =  new ArrayList<FindLawResult>();
	
	public FindFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.find_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		aty = getActivity();
		initView();
		
		//判断是打开gps
//		if(AppUtils.isGpsOpen(aty)){
//			initLocation();
//			Loger.debug("gps定位");
//		} else {
//			location();
//		}
		location();
		loadLSCity();
	}
	
	private void initView(){
		xlistview = (XListView) getActivity().findViewById(R.id.find_xlistview);
		load_lt = (LoadStateView) getActivity().findViewById(R.id.loadingStateBox);
		filterview = (FilterMenuView) getActivity().findViewById(R.id.filtermenu);
		
		adapter = new FindLawModeAdapter(aty, dataList);
		xlistview.setAdapter(adapter);
		xlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 <= 0 || arg2 > dataList.size()){
					return;
				}
				if (isLscn) {
					FindLawResult result = dataList.get(arg2-1);
					if(null != result){
						FindLawDetailActivity.finLawDetail(aty, result, area, StrUtils.isEmpty(type) == true ? result.getAreaCode() : type ,
								0);
					}
					
				}else{
					FindLawResult result = dataList.get(arg2-1);
					if(null != result){
						FindLawDetailActivity2.finLawDetail(aty, result);
					}
				}
				
			}
		});
		
		xlistview.setDataQueryInerface(this);
		load_lt.setOnReloadClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startLoad();
			}
		});
		
		titles.add("全部专长");
		titles.add("全部地区");
		titles.add("默认排序");
		
		typeView = new FindLawFilterType(getActivity());
		cityView = new FindLawFilterCity(getActivity());
		sortView = new FindLawFilterSort(getActivity());
		mViewArray.add(typeView);
		mViewArray.add(cityView);
		mViewArray.add(sortView);
		filterview.setMenuArray(titles, mViewArray);
		
		initListener();
	}
	
	/**
	 * 筛选结果监听
	 */
	private void initListener(){
		typeView.setOnItemSelectListener(new FindLawFilterType.OnItemSelectListener() {
			@Override
			public void getValue(SelectItem item) {
				type = item.getId();
				if(type.equals("-1"))type = null;
				filterview.setTitle(item.getName(), 0);
				filterview.onPressBack();
				startLoad();
			}
		});
		
		cityView.setOnItemSelectListener(new FindLawFilterCity.OnItemSelectListener() {
			@Override
			public void getValue(FindlawCity item) {
				if(!String.valueOf(isLscn).endsWith(String.valueOf(isOpen(item.getId())))){
					type = null;
					filterview.setTitle("全部专长", 0);
				}
				area = item.getId();
				filterview.setTitle("0".equals(item.getGrade()) ? item.getProvince():item.getCity(), 1);
				filterview.onPressBack();
				isLscn = isOpen(area);
				setSortButtonEnable();
				startLoad();
			}
		});
		
		sortView.setOnItemSelectListener(new FindLawFilterSort.OnItemSelectListener() {			
			@Override
			public void getValue(FindLawDefaultSort item) {
				weigth = item.getKey();
				filterview.setTitle(item.getName(), 2);
				filterview.onPressBack();
				startLoad();
			}
		});
		
		filterview.setOnButtonClickListener(new OnButtonClickListener() {
			@Override
			public void onClick(int selectPosition) {
				switch (selectPosition) {
					case 0:
						typeView.initData(isLscn);
						break;
					case 1:
						cityView.initData();
						break;
					case 2:
						sortView.initData();
						break;
					default:
						break;
				}
			}
		});
	}
	
	private void setSortButtonEnable(){
		if(!isLscn){
			type = null;
			filterview.setTitleEnable(false, 2);
		} else {
			filterview.setTitleEnable(true, 2);
		}
	}
	
	/**
	 * 定位并且加载地区编码
	 */
	private void location(){
		String mobile = null;
		String slocation = null;
		Location  location= AppUtils.getLocation(aty); 
//		Location  location= SharedUserInfo.location;
		if(null != location){
			slocation = location.getLatitude() + "," + location.getLongitude();
			Loger.debug("gps定位："+slocation);
		}
		if(null != SharedUserInfo.getUserInfo(aty) && 
				null != SharedUserInfo.getUserInfo(aty).getMobile()){
			mobile = SharedUserInfo.getUserInfo(aty).getMobile();
		}
		
		HttpPostManager.getAreaCode(mobile, slocation, new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				hasLocation = true;
				
				JSONObject json = this.getJsonContent();
				if(null != json){
					location_json = json;
				}
				if(hasLoadCity){
					showLocation(location_json);
				}
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				hasLocation = true;
				if(hasLoadCity){
					showLocation(location_json);
				}
			}
		}, aty, null);
	}
	
	private void showLocation(JSONObject json){
		if(null == area){
			Loger.debug("定位返回json："+json.toString());
			try {
				if(!json.isNull("areacode")){
					area = json.getString("areacode");
				}
				String province="",city="",s;
				if(json.has("province"))
					province = StrUtils.strToString(json.getString("province"));
				if(json.has("city"))
					city = StrUtils.strToString(json.getString("city"));
				if(province.equals(city)){
					s = city;
				}else{
					s = province + "-" +city;
				}
				if(s.length() > 0)
					filterview.setTitle(s, 1);
				
				isLscn = isOpen(area);
				setSortButtonEnable();
				Loger.debug("找律师定位："+area+",是否开通好律师在线:"+isLscn);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
			
			//获取地区ID，加载数据
			loadData(1);
		}
	}
	
	/**
	 * 加载好律师在线城市
	 */
	private void loadLSCity(){
		HttpPostManager.getSpecialCitys(new StringCallBack(){
			@Override
			public void onSuccess(Object t) {
				hasLoadCity = true;
				if(null != this.getJsonContent()){
					showLSCity(this.getJsonContent());
				}
				
				if(hasLocation){
					showLocation(location_json);
				}
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				hasLoadCity = true;
				Loger.debug("加载好律师在线城市出错");
				if(hasLocation){
					showLocation(location_json);
				}
			}
		}, aty, null);
	}
	
	private void showLSCity(JSONObject obj){
		try {
			ArrayList<SelectItem> list = SelectItem.cityJSONToList(obj);
			if(null != list && list.size() > 0){
				hasLoadCity = true;
				lsList.addAll(list);
			}
//			Loger.debug("找律师定位："+area+",是否开通好律师在线:"+isLscn);
		} catch (JSONException e) {
			Loger.debug("城市列表解析错误", e);
		} catch (Exception e) {
			Loger.debug("城市列表解析错误", e);
		}
	}
	
	/**
	 * 判断当前选择的城市是否开通了好律师在线
	 */
	private boolean isOpen(String id){
		boolean flag = false;
		for(int i= 0; i<lsList.size(); i++){
			SelectItem item = lsList.get(i);
			
			if(id.equals(item.getId())){
				flag = true;
				break;
			}
			if(null != item.getChilds() && item.getChilds().size() > 0){
				boolean flag1 = false;
				ArrayList<SelectItem> childs = item.getChilds();
				for(int j=0; j<childs.size(); j++){
					SelectItem citem = childs.get(j);
					if(id.equals(citem.getId())){
						flag = true;
						flag1 = true;
						break;
					}
				}
				if(flag1){
					break;
				}
			}
		}
		Loger.debug("地区编码："+area);
		Loger.debug("是否开通城市："+true);
		return flag;
	}
	
	private void loadData(int page){
		if (isLscn) {
			Loger.debug("地区编码："+area);
			HttpPostManager.searchLawyer(area, type,page,pageSize,
					weigth,new StringCallBack(){
				@Override
				public void onSuccess(Object t) {
					showData(this.getJsonContent());
					if(isReflash || isInit){
						if(dataList.size() == 0){
							setEmpty("没有搜索到相匹配的律师!");
							return;
						}
					}
					setLoadSuccess();
				}
				
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					setLoadError(this.getDesc());
				}
			}, aty, null);
		}else{
			HttpPostManager.searchFindLawer(area, type, page, pageSize,
					new StringCallBack(){
				@Override
				public void onSuccess(Object t) {
					showData2(this.getJsonArray());
					if(isReflash || isInit){
						if(dataList.size() == 0){
							setEmpty("没有搜索到相匹配的律师!");
							return;
						}
					}
					setLoadSuccess();
				}
				
				@Override
				public void onFailure(Throwable t, int errorNo,
								String strMsg) {
					setLoadError(this.getDesc());
				}
			}, aty, null);
		}
	}
	
	private void showData(JSONObject json){
		try {
			JSONArray arr = null;
			if(null != json){
				if(json.has("total"))
					totalSize = StrUtils.strToInt(json.getString("total"));
				if(json.has("list") && null != json.get("list"))
					arr = json.getJSONArray("list");
			}
			ArrayList<FindLawResult> list = FindLawResult.jsArrToObj(arr);
			if(isReflash || isInit)
				dataList.clear();
			if(list.size() > 0)
				dataList.addAll(list);
			if(null != adapter)
				adapter.notifyDataSetChanged();
			xlistview.setPullLoadEnable(dataList.size() < totalSize ? true:false);
		} catch (JSONException e) {
			Loger.debug("搜索律师结果解析出错", e);
		} catch (Exception e) {
			Loger.debug("搜索律师结果解析出错", e);
		}
	}
	
	private void showData2(JSONArray arr){
		try {
			ArrayList<FindLawResult> list = FindLawResult.findlawArrToObj(arr);
			if(isReflash || isInit)
				dataList.clear();
			if(list.size() > 0)
				dataList.addAll(list);
			if(null != adapter)
				adapter.notifyDataSetChanged();
			xlistview.setPullLoadEnable(list.size() < pageSize? false:true);
		} catch (JSONException e) {
			Loger.debug("找法网搜索律师结果：", e);
		} catch (Exception e) {
			Loger.debug("找法网搜索律师结果：", e);
		}
	}
	
	/**
	 * 设置加载成功
	 */
	private void setLoadSuccess(){
		if(isInit){
			if(null != load_lt)
				load_lt.setVisibility(View.GONE);
			if(null != xlistview)
				xlistview.setVisibility(View.VISIBLE);
		}
		isInit = false;
		isReflash = false;
		if(null != xlistview)
			xlistview.loadOk(true);
	}
	
	/**
	 * 设置加载失败
	 */
	private void setLoadError(String msg){
		if(isInit){
			if(null != load_lt)
				load_lt.showError(msg);
		}
		isReflash = false;
		if(null != xlistview)
			xlistview.loadOk(false);
	}
	
	/**
	 * 设置空内容状态
	 * @param emptytip
	 * 				空白内容提示
	 */
	private void setEmpty(String emptytip){
		if(isInit){
			if(null != load_lt)
				load_lt.showEmpty(emptytip);
		}
		isInit = false;
		isReflash = false;
		if(null != xlistview)
			xlistview.loadOk(false);
	}
	
	public void startLoad(){
		isInit = true;
		isReflash = true;
		if(null != load_lt){
			load_lt.setVisibility(View.VISIBLE);
			load_lt.startLoad();
		}
		if(null != xlistview){
			xlistview.setReload();
			xlistview.setVisibility(View.GONE);
		}
		reload();
	}
	
	/**
	 * 加载失败重新加载
	 */
	private void reload(){
		loadData(1);
	}

	@Override
	public void onRefresh(int pageNo, int pageSize) {
		isReflash = true;
		loadData(1);
	}

	@Override
	public void onLoadMore(int pageNo, int pageSize) {
		loadData(pageNo);
	}
	
	public boolean onBackPressed() {
		if(null == filterview)return false;
		return filterview.onPressBack();
	}
	
	//gps定位
	private LocationListener locationListener;
	private LocationManager locationManager;
	/***********************获取gps定位**************************/
	private void initLocation(){
		locationListener = new LocationListener() {  
		    public void onLocationChanged(Location location) { //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发  
		        if (location != null) {  
		        	Loger.debug("SuperMap:Location changed : Lat: "
				              + location.getLatitude() + " Lng: "  
				              + location.getLongitude());
		        	SharedUserInfo.location = location;
		        }
		        locationFinish();
		    }  
		  
		    public void onProviderDisabled(String provider) {  
		    // Provider被disable时触发此函数，比如GPS被关闭  
		    	Loger.debug("Provider被disable时触发此函数，比如GPS被关闭 ");
		    	locationFinish();
		    }  
		  
		    public void onProviderEnabled(String provider) {  
		    //  Provider被enable时触发此函数，比如GPS被打开  
		    	Loger.debug("Provider被enable时触发此函数，比如GPS被打开  ");
		    	locationFinish();
		    }  
		  
		    public void onStatusChanged(String provider, int status, Bundle extras) {  
		    // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
		    	Loger.debug("Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数");
		    	locationFinish();
		    }  
		};
		
		locationManager = (LocationManager) aty.getSystemService(Context.LOCATION_SERVICE);
		Criteria c = new Criteria();  
		c.setAccuracy(Criteria.ACCURACY_LOW); //精度高  
		c.setPowerRequirement(Criteria.POWER_LOW); //电量消耗低  
		c.setAltitudeRequired(false); //不需要海拔  
		c.setSpeedRequired(false); //不需要速度  
		c.setCostAllowed(false); //不需要费用
		locationManager.getBestProvider(c , true);
		try{
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
					 1000, 0, locationListener);
		} catch (Exception e){
			Loger.debug("定位失败："+e);
		}
		 
	}
	
	private void locationFinish(){
		if(null != locationListener)
			locationManager.removeUpdates(locationListener);
        locationListener = null;
    	locationManager = null;
    	location();
    	Loger.debug("gps定位完成");
	}
}
