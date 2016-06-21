package net.mtjo.app.ui.start;

import com.aframe.ui.widget.indicator.CirclePageIndicator;
import net.mtjo.app.R;
import net.mtjo.app.ui.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideActivity extends BaseActivity {
	ViewPager mPager;
	CirclePageIndicator mIndicator;
	ImagePageAdapter mAdapter;
	int[] imgs = {R.drawable.guid1, 
			R.drawable.guid2, 
			R.drawable.guid3, 
			R.drawable.guid4};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_activity);
		
		mAdapter = new ImagePageAdapter();
		mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setCurrentItem(0);
	}
	
	public void dempGuids(View v){
		Intent intent = new Intent(GuideActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	class ImagePageAdapter extends PagerAdapter{
		private LayoutInflater inflater;

		ImagePageAdapter() {
			inflater = getLayoutInflater();
		}
		
		@Override
		public int getCount() {
			return imgs.length;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.bigimg_layout, view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img);
//			@SuppressWarnings("static-access")
//			Bitmap bitmap = new BitmapFactory().decodeResource(
//					getResources(), imgs[position]);
//			imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, 
//					DensityUtils.getScreenW(GuideActivity.this),
//					DensityUtils.getScreenH(GuideActivity.this)));
			imageView.setBackgroundResource(imgs[position]);
			view.addView(imageLayout, 0);
			if(position == imgs.length-1){
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(GuideActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
			
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}


