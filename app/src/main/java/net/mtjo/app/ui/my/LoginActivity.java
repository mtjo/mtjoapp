package net.mtjo.app.ui.my;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aframe.http.CookieManager;
import com.aframe.http.FormAgent;
import com.aframe.http.StringCallBack;
import com.aframe.json.parse.ParseJson;
import com.aframe.ui.ViewInject;
import com.aframe.utils.AppUtils;
import com.aframe.utils.RandomCode;
import com.aframe.utils.StrUtils;


import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;

import net.mtjo.app.application.SysApplication;
import net.mtjo.app.entity.UserInfo;
import net.mtjo.app.ui.base.BaseActivity;
import net.mtjo.app.utils.SharedUserInfo;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity {
    private ImageView imageView;
    private final static String TAG = "Login";
    private String username, password, verify_code;
    private EditText username_et, password_et, verify_code_et;
    private LoginActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.my_login_activity);
        mContext = LoginActivity.this;

    }
    @Override
    protected void onInitViews() {
        setTitle("登陆");
        inflateMenu(R.menu.share);
        username_et = (EditText) findViewById(R.id.login_username_et);
        password_et = (EditText) findViewById(R.id.login_password_et);
        verify_code_et = (EditText) findViewById(R.id.login_verify_code_et);

        imageView = (ImageView) findViewById(R.id.imgcode);
        get_code();

        imageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                get_code();
            }
        });
    }


    public void get_code() {
        HttpPostManager.getVerifyCode(
                new StringCallBack() {

                    @Override
                    public void onSuccess(Object t) {
                        String code = this.getStrContent();
                        imageView.setImageBitmap(RandomCode.getInstance().createBitmap(code));

                        username_et.setText("mtjo_00@163.com");
                        password_et.setText("z..whj7q8j9k0000");
                        verify_code_et.setText(code);


                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                    }
                }, null, null);


    }


    public void dologin(View v) {

        username = username_et.getText().toString();
        password = password_et.getText().toString();
        verify_code = verify_code_et.getText().toString();


        if (StrUtils.isEmpty(username)) {

            ViewInject.showToast(mContext, "用户名不能为空!");


        } else if (StrUtils.isEmpty(password)) {

            ViewInject.showToast(mContext, "密码不能为空!");


        } else if (StrUtils.isEmpty(verify_code)) {

            ViewInject.showToast(mContext, "验证码不能为空!");

        } else {

            StringCallBack login = new StringCallBack() {

                @Override
                public void onSuccess(Object t) {
                    UserInfo user = ParseJson.getEntity(this.getJsonContent().toString(), UserInfo.class);
                    SharedUserInfo.saveUserinfo(mContext, user);
                    SysApplication.getInstance().loginSuccess();
                    new Thread() {
                        @Override
                        public void run() {

                            CookieManager cookieManager = FormAgent.getCookieContainer();
                            String cookestring = cookieManager.toString();

                            Pattern pattern = Pattern.compile("PHPSESSID=\\w+");
                            Matcher matcher = pattern.matcher(cookestring);
                            while (matcher.find()) {
                                System.out.println(matcher.group());
                                AppUtils.saveLocalCache(mContext, "PHPSESSID", matcher.group());
                            }

                        }
                    }.start();

                    finish();


                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                }
            };

            HttpPostManager.dologin(username, password, verify_code, login , null, null);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.regist, menu);
        return true;
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.action_regist){
            Intent intent = new Intent();
            intent.setClass(mContext,RegistActivity.class);
            startActivity(intent);
        }
        return true;
    }

}
