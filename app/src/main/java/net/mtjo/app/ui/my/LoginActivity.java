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
        get_code();

        imageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) { get_code();
            }
        });

    }

    public void get_code(){
        String code;


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


}
