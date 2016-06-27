package net.mtjo.app.ui.start;

import com.aframe.http.StringCallBack;
import com.aframe.utils.AppUtils;

import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.ui.base.WebViewActivity;

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
import android.widget.Toast;

import com.daimajia.slider.library.*;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.AccordionTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment implements OnClickListener, BaseSliderView.OnSliderClickListener {
    private Activity aty;
    SliderLayout sliderShow;

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

        sliderShow = (SliderLayout) aty.findViewById(R.id.slider);
        HttpPostManager.getHomeSlides(
                new StringCallBack() {

                    @Override
                    public void onSuccess(Object t) {

                        homeSlides = this.getJsonArray();
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

                                sliderShow.setPagerTransformer(false, new AccordionTransformer());
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
    private void showTip() {
        String isMainGuid = AppUtils.getLocalCache(aty, "isMainGuid");
        if (!"true".equals(isMainGuid)) {
            ((MainActivity) getActivity()).showtipView(R.drawable.main_guid);
            AppUtils.saveLocalCache(aty, "isMainGuid", "true");
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(aty, slider.getBundle().get("slide_url") + "", Toast.LENGTH_SHORT).show();
        WebViewActivity.WebView(aty, slider.getBundle().get("slide_url") + "","文章详情");
    }

}
