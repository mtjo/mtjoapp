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
import android.widget.TextView;
import android.widget.Toast;

import com.aframe.http.StringCallBack;
import com.aframe.ui.ViewInject;
import com.aframe.utils.RandomCode;
import com.aframe.utils.StrUtils;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;

import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.ui.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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


                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                    }
                }, null, null);

    }


}

}
