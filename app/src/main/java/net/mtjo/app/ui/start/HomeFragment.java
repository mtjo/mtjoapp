package net.mtjo.app.ui.start;

import com.aframe.utils.AppUtils;
import net.mtjo.app.R;
import net.mtjo.app.config.Config;
import net.mtjo.app.ui.ask.AskLawyerActivity;
import net.mtjo.app.utils.UMengUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.slider.library.*;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.AccordionTransformer;

import java.util.HashMap;
import java.util.Iterator;

public class HomeFragment extends Fragment implements OnClickListener{
	private Activity aty;
	public SliderLayout sliderShow;
	
	public HomeFragment(){
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.home_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		aty = getActivity();
		initView();
		//showTip();
	}
	
	private void initView(){
		//getActivity().findViewById(R.id.ask_img).setOnClickListener(this);
		sliderShow = (SliderLayout) aty.findViewById(R.id.slider);


		HashMap<String,String> url_maps = new HashMap<String, String>();
		url_maps.put("1", "http://mtjo.net/themes/simplebootx/Public/images/demo/1.jpg");
		url_maps.put("2", "http://mtjo.net/themes/simplebootx/Public/images/demo/2.jpg");
		url_maps.put("3", "http://mtjo.net/themes/simplebootx/Public/images/demo/3.jpg");

		System.out.println(url_maps);
		for(String name : url_maps.keySet()){
			Log.d("initView", "initView: "+name+":"+url_maps.get(name));
			TextSliderView textSliderView = new TextSliderView(aty);
			textSliderView
					.description(name)
					.image(url_maps.get(name));
			sliderShow.setPagerTransformer(false,new AccordionTransformer());
			sliderShow.addSlider(textSliderView);

		}
	}
	@Override
	public void onStop() {
		sliderShow.stopAutoCycle();
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			/*//case R.id.ask_img:
				UMengUtil.onEvtent(getActivity().getApplicationContext(), Config.UMENG_ASK_LAWYER1);
				AskLawyerActivity.open(getActivity(), 0);
				break;*/

			default:
				break;
		}
	}
	
	/************************首次使用功能引导提示**************************/
	/**
	 * 显示提示
	 */
	private void showTip(){
		String isMainGuid = AppUtils.getLocalCache(aty, "isMainGuid");
		if(!"true".equals(isMainGuid)){
			((MainActivity)getActivity()).showtipView(R.drawable.main_guid);
			AppUtils.saveLocalCache(aty, "isMainGuid", "true");
		}
	}




	/**
	 * Created by daimajia on 14-5-29.
	 */
	public class TransformerAdapter extends BaseAdapter {
		private Context mContext;
		public TransformerAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			return SliderLayout.Transformer.values().length;
		}

		@Override
		public Object getItem(int position) {
			return SliderLayout.Transformer.values()[position].toString();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView t = (TextView)LayoutInflater.from(mContext).inflate(R.layout.item,null);
			t.setText(getItem(position).toString());
			return t;
		}
	}
}
