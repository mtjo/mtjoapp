package net.mtjo.app.ui.start;

import com.aframe.http.FormAgent;
import com.aframe.http.StringCallBack;
import com.aframe.utils.AppUtils;

import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.ui.base.WebViewActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.daimajia.slider.library.*;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.AccordionTransformer;
import com.daimajia.slider.library.Transformers.BackgroundToForegroundTransformer;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.daimajia.slider.library.Transformers.CubeInTransformer;
import com.daimajia.slider.library.Transformers.DefaultTransformer;
import com.daimajia.slider.library.Transformers.DepthPageTransformer;
import com.daimajia.slider.library.Transformers.FadeTransformer;
import com.daimajia.slider.library.Transformers.FlipHorizontalTransformer;
import com.daimajia.slider.library.Transformers.FlipPageViewTransformer;
import com.daimajia.slider.library.Transformers.ForegroundToBackgroundTransformer;
import com.daimajia.slider.library.Transformers.RotateDownTransformer;
import com.daimajia.slider.library.Transformers.RotateUpTransformer;
import com.daimajia.slider.library.Transformers.StackTransformer;
import com.daimajia.slider.library.Transformers.TabletTransformer;
import com.daimajia.slider.library.Transformers.ZoomInTransformer;
import com.daimajia.slider.library.Transformers.ZoomOutSlideTransformer;
import com.daimajia.slider.library.Transformers.ZoomOutTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HomeFragment extends Fragment implements OnClickListener, BaseSliderView.OnSliderClickListener {
    private Activity aty;
    public SliderLayout sliderShow;
    public  ScrollView scrollview;
    private  ArrayList<Map<String, Object>> data_list;

    private GridView gview;

    private int[] icon = { R.drawable.sns_icon_1, R.drawable.sns_icon_2,
            R.drawable.sns_icon_6, R.drawable.sns_icon_19, R.drawable.sns_icon_22,
            R.drawable.sns_icon_23, R.drawable.sns_icon_24, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher };
    private String[] iconName = { "通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
            "设置", "语音", "天气", "浏览器", "视频" };



    private JSONArray homeSlides = null;


    public HomeFragment() {
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

    private void initView() {
        //getActivity().findViewById(R.id.ask_img).setOnClickListener(this);
        scrollview = ((ScrollView)aty.findViewById(R.id.scrollView));
        sliderShow = (SliderLayout) aty.findViewById(R.id.slider);
        HttpPostManager.getHomeSlides(
                new StringCallBack() {

                    @Override
                    public void onSuccess(Object t) {

                        homeSlides = this.getJsonArray();
                        BaseTransformer transformer = transformer() ;
                        for (int i = 0; i < homeSlides.length(); i++) {
                            try {
                                JSONObject oj = homeSlides.getJSONObject(i);

                                TextSliderView textSliderView = new TextSliderView(aty);
                                textSliderView
                                        .description(oj.getString("slide_name"))
                                        .image(oj.getString("slide_pic"))
                                .setOnSliderClickListener(HomeFragment.this);
                                textSliderView.getBundle()
                                        .putString("slide_url", oj.getString("slide_url"));
                                sliderShow.setPagerTransformer(false, transformer);
                                //sliderShow.setCustomAnimation(new DescriptionAnimation());
                                sliderShow.addSlider(textSliderView);



                                System.out.println(oj);
                            } catch (JSONException e) {

                            }
                        }


                        Log.i("homeSlides", "initView: ");
                        System.out.println(homeSlides);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        Toast.makeText(aty, strMsg, Toast.LENGTH_SHORT).show();
                    }
                }, null, null);


        gview = (GridView) aty.findViewById(R.id.gridView);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        SimpleAdapter sim_adapter = new SimpleAdapter(aty, data_list, R.layout.gridview_item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
        setListViewHeightBasedOnChildren(gview);
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


    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(aty, slider.getBundle().get("slide_url") + "", Toast.LENGTH_SHORT).show();
       // SharedPreferences spf = getSharedPreferences("Cookie", Context.MODE_PRIVATE);

       WebViewActivity.WebView(aty, slider.getBundle().get("slide_url") + "","文章详情");
    }
/**
 * BaseTransformer transformer
 * */
    public BaseTransformer transformer() {
        Random rand = new Random();
        BaseTransformer transformer;

        switch (rand.nextInt(17)) {
            case 0:
                transformer = new AccordionTransformer();
                break;
            case 1:
                transformer = new BackgroundToForegroundTransformer();
                break;
                                    /*case 2:
                                        transformer= new BaseTransformer();
                                        break;*/
            case 3:
                transformer = new CubeInTransformer();
                break;
            case 4:
                transformer = new DefaultTransformer();
                break;
            case 5:
                transformer = new DepthPageTransformer();
                break;
            case 6:
                transformer = new FadeTransformer();
                break;
            case 7:
                transformer = new FlipHorizontalTransformer();
                break;
            case 8:
                transformer = new FlipPageViewTransformer();
                break;
            case 9:
                transformer = new ForegroundToBackgroundTransformer();
                break;
            case 10:
                transformer = new RotateDownTransformer();
                break;
            case 11:
                transformer = new RotateUpTransformer();
                break;
            case 12:
                transformer = new StackTransformer();
                break;
            case 13:
                transformer = new TabletTransformer();
                break;
            case 14:
                transformer = new ZoomInTransformer();
                break;
            case 15:
                transformer = new ZoomOutSlideTransformer();
                break;
            case 16:
                transformer = new ZoomOutTransformer();
                break;
            default:
                transformer = new AccordionTransformer();
                break;

        }
        return transformer;
    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 3;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

}
