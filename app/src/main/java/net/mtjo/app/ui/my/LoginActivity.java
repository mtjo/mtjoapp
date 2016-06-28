package net.mtjo.app.ui.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aframe.http.StringCallBack;
import com.aframe.utils.RandomCode;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;

import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends BaseActivity {
    private ImageView imageView;
    private final static String TAG = "Login";
    private Thread newThread;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_login_activity);
        initView();
    }
    private void initView(){
        imageView = (ImageView)findViewById(R.id.imgcode);
        imageView.setImageBitmap(RandomCode.getInstance().createBitmap());

        imageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                HttpPostManager.getVerifyCode(
                        new StringCallBack() {

                            @Override
                            public void onSuccess(Object t) {

                               String code = this.getStrContent();
                                imageView.setImageBitmap(RandomCode.getInstance().createBitmap(code));

                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                            }
                        }, null, null);

                System.out.println("验证码："+RandomCode.getInstance().getCode());
            }
        });








    }

    /**
     * 获取网落图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Log.i(TAG, url);
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }


}
