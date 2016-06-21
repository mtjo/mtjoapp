package net.mtjo.app.ui.ask;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.mtjo.app.R;
import net.mtjo.app.entity.AskLawyerModel;

public class AskLawyerAdapter extends BaseAdapter {
	private List<AskLawyerModel> list;
	private Context context;
	
	public AskLawyerAdapter(Context context,List<AskLawyerModel> list){
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		AskLawyerModel model = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.ask_lawyer_list_item, null);
			holder.title = (TextView)convertView.findViewById(R.id.title_tv);
			holder.type = (TextView)convertView.findViewById(R.id.type_tv);
			holder.time = (TextView)convertView.findViewById(R.id.time_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(model.getContent());
		holder.type.setText(model.getSidInfo());
		holder.time.setText(model.getsCreateTime());
		
		return convertView;
	}
	
	public static class ViewHolder{
		private  TextView title;
		private  TextView type;
		private  TextView time;
	}

}
