package net.mtjo.app.ui.my;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aframe.http.CookieManager;
import com.aframe.http.FormAgent;
import com.aframe.http.StringCallBack;
import com.aframe.ui.ViewInject;
import com.aframe.utils.AppUtils;
import com.aframe.utils.RandomCode;
import com.aframe.utils.StrUtils;
import com.alibaba.fastjson.JSON;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;

import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.config.Config;
import net.mtjo.app.ui.base.BaseActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity {
    private ImageView imageView;
    private final static String TAG = "Login";
    private String username,password,verify_code;
    private TextView username_tv,password_tv,verify_code_tv;
    private LoginActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_login_activity);
        mContext = LoginActivity.this;

        initView();
    }
    private void initView(){
        setTitle("登陆");
        username_tv = (TextView)findViewById(R.id.login_username_tv);
        password_tv = (TextView)findViewById(R.id.login_password_tv);
        verify_code_tv = (TextView)findViewById(R.id.login_verify_code_tv);

        imageView = (ImageView)findViewById(R.id.imgcode);
        get_code();

        imageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) { get_code();
            }
        });

    }

    public void get_code(){
        HttpPostManager.getVerifyCode(
                new StringCallBack() {

                    @Override
                    public void onSuccess(Object t) {
                        String code = this.getStrContent();
                        imageView.setImageBitmap(RandomCode.getInstance().createBitmap(code));

                        username_tv.setText("mtjo_00@163.com");
                        password_tv.setText("z..whj7q8j9k0000");
                        verify_code_tv.setText(code);



                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                    }
                }, null, null);



    }
public void dologin(View v){

    username = username_tv.getText().toString();
    password = password_tv.getText().toString();
    verify_code = verify_code_tv.getText().toString();


    if (StrUtils.isEmpty(username)) {

        ViewInject.showToast(mContext, "用户名不能为空!");


    }else if (StrUtils.isEmpty(password)) {

        ViewInject.showToast(mContext, "密码不能为空!");


    }else if (StrUtils.isEmpty(verify_code)) {

        ViewInject.showToast(mContext, "验证码不能为空!");

    }else{

        HttpPostManager.dologin(username,password,verify_code,
                new StringCallBack() {

                    @Override
                    public void onSuccess(Object t) {
                        JSONArray jsonArray = this.getJsonArray();
                        System.out.println(jsonArray);

                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    HttpClient client = new DefaultHttpClient();
                                    HttpGet get = new HttpGet(Config.API_LOGIN);
                                    HttpResponse response = client.execute(get);
                                    CookieManager cookieManager = FormAgent.getCookieContainer();
                                    String cookestring =cookieManager.toString();

                                    Pattern pattern = Pattern.compile("PHPSESSID=\\w+");
                                    Matcher matcher = pattern.matcher(cookestring);
                                    StringBuffer buffer = new StringBuffer();
                                    while (matcher.find()) {
                                        System.out.println(matcher.group());
                                        AppUtils.saveLocalCache(mContext,"PHPSESSID",matcher.group());
                                    }

                                } catch (ClientProtocolException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }.start();


                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                    }
                }, null, null);

    }


}

}
