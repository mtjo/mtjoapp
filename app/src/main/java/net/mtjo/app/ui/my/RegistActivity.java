package net.mtjo.app.ui.my;

import android.app.Instrumentation;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aframe.http.StringCallBack;
import com.aframe.json.parse.ParseJson;
import com.aframe.ui.ViewInject;
import com.aframe.utils.RandomCode;
import com.aframe.utils.StrUtils;

import net.mtjo.app.R;
import net.mtjo.app.api.HttpPostManager;
import net.mtjo.app.application.SysApplication;

import net.mtjo.app.entity.UserInfo;
import net.mtjo.app.ui.base.BaseActivity;
import net.mtjo.app.ui.start.MainActivity;
import net.mtjo.app.ui.start.MyFragment;
import net.mtjo.app.utils.SharedUserInfo;

public class RegistActivity extends BaseActivity {


    private RegistActivity mContext;
    private EditText email_et, password_et,re_password_et, verify_code_et;
    private ImageView imageView;


    @Override
    protected void onInit() {
        setContentLayout(R.layout.my_regist_activity);
    }

    @Override
    protected void onInitViews() {
        mContext = this;
        initVal();
    }

    //组建的初始化方法。。。。。
    private void initVal() {

        setTitle("注册");
        email_et = (EditText) findViewById(R.id.regist_email_et);
        password_et = (EditText) findViewById(R.id.regist_password_et);
        re_password_et = (EditText) findViewById(R.id.regist_repassword_et);

        verify_code_et = (EditText) findViewById(R.id.regist_verify_code_et);

        imageView = (ImageView) findViewById(R.id.imgcode);
        get_code();

        imageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                get_code();
            }
        });
    }


    public void doRegist(View v) {
        String s = email_et.getText().toString();
        if (StrUtils.isEmpty(s)) {
            ViewInject.showToast(this, getString(R.string.regist_not_empty_msg));
        } else if (StrUtils.isEmpty(password_et.getText().toString())) {
            ViewInject.showToast(this, getString(R.string.regist_not_empty_password_msg));
        } else if (StrUtils.isEmpty(re_password_et.getText().toString())) {
            ViewInject.showToast(this, getString(R.string.regist_not_empty_re_password_msg));
        } else if (StrUtils.isEmpty(verify_code_et.getText().toString())) {
            ViewInject.showToast(this, getString(R.string.regist_not_empty_vecode_msg));
        }else if (!re_password_et.getText().toString().equals(password_et.getText().toString())) {
            ViewInject.showToast(this, getString(R.string.regist_password_differ_msg));
        }else {
            sendRegist();
        }

    }


    /**
     * 注册
     */
    private void sendRegist() {
        //登录统计
        StringCallBack regist = new StringCallBack() {
            @Override
            public void onSuccess(Object t) {
                if (this.getState() == 0 && this.getJsonContent() != null) {
                    UserInfo user = ParseJson.getEntity(this.getJsonContent().toString(), UserInfo.class);
                    if (user != null) {

                        SharedUserInfo.saveUserinfo(mContext, user);
                        ViewInject.showToast(mContext, getString(R.string.regist_succ));
                        setResult(RESULT_OK);
                        mContext.finish();
                        LoginActivity.loginActivity.finish();

                        //上传用户token
                        SysApplication.getInstance().loginSuccess();
                    } else {
                        ViewInject.showToast(RegistActivity.this, this.getDesc());
                    }
                } else {
                    ViewInject.showToast(mContext, this.getDesc());
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                ViewInject.showToast(RegistActivity.this, this.getDesc());
            }
        };

        HttpPostManager.regist(email_et.getText().toString(),password_et.getText().toString(),verify_code_et.getText().toString(), regist, this, "数据提交中，请稍后...");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.submit, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_submit) {
            doRegist(null);
        }
        return true;
    }


    public void get_code() {
        HttpPostManager.getVerifyCode(
                new StringCallBack() {

                    @Override
                    public void onSuccess(Object t) {
                        String code = this.getStrContent();
                        imageView.setImageBitmap(RandomCode.getInstance().createBitmap(code));
                        verify_code_et.setText(code);
                        email_et.setText("184960560@qq.com");
                        password_et.setText("z..whj7q8j9k0000");
                        re_password_et.setText("z..whj7q8j9k0000");


                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        Toast.makeText(RegistActivity.this, strMsg, Toast.LENGTH_SHORT).show();
                    }
                }, null, null);


    }

}
