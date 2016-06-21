package net.mtjo.app.ui;

import net.mtjo.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadStateView extends RelativeLayout {
	private ProgressBar proBar;
	private ImageView errorImg;
	private TextView tip_tv;
	private Button reloadBtn;

	public LoadStateView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		proBar = (ProgressBar) findViewById(R.id.loading_bar);
		errorImg = (ImageView) findViewById(R.id.loaderor_img);
		tip_tv = (TextView) findViewById(R.id.load_tip_tv);
		reloadBtn = (Button) findViewById(R.id.retry_btn);
	}
	
	/**
	 * 开始加载
	 */
	public void startLoad(){
		if(null != proBar){
			proBar.setVisibility(View.VISIBLE);
		}
		if(null != errorImg){
			errorImg.setVisibility(View.GONE);
		}
		if(null != tip_tv){
			tip_tv.setText(R.string.loading);
		}
		if(null != reloadBtn){
			reloadBtn.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 加载失败
	 */
	public void showError(String erroritp){
		if(null != proBar){
			proBar.setVisibility(View.GONE);
		}
		if(null != errorImg){
			errorImg.setVisibility(View.VISIBLE);
			errorImg.setImageResource(R.drawable.error_state);
		}
		if(null != tip_tv){
			tip_tv.setText(erroritp == null ? getResources().getString(R.string.loaderror) : erroritp);
		}
		if(null != reloadBtn){
			reloadBtn.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 空白内容
	 */
	public void showEmpty(String emptytip){
		if(null != proBar){
			proBar.setVisibility(View.GONE);
		}
		if(null != errorImg){
			errorImg.setVisibility(View.VISIBLE);
			errorImg.setImageResource(R.drawable.empty_state);
		}
		if(null != tip_tv){
			tip_tv.setText(emptytip == null ? "暂时没有内容，赶紧去创建数据吧！":emptytip);
		}
		if(null != reloadBtn){
			reloadBtn.setVisibility(View.GONE);
		}
	}

	public void setOnReloadClickListener(OnClickListener onReloadClickListener){
		if(null != reloadBtn){
			reloadBtn.setOnClickListener(onReloadClickListener);
		}
	}
}
