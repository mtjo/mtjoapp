package net.mtjo.app.ui.msg;

import java.util.List;

import net.mtjo.app.R;
import net.mtjo.app.entity.MessageModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private List<MessageModel> list;
	private Context context;
	
	public MessageAdapter(Context context,List<MessageModel> list){
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
		MessageModel model = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.message_listitem, null);
			holder.title = (TextView)convertView.findViewById(R.id.title_tv);
			holder.count = (TextView)convertView.findViewById(R.id.count_tv);
			holder.time = (TextView)convertView.findViewById(R.id.time_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(model.getAlert());
		holder.time.setText(model.getsCreateTime());
		if(model.getCount() > 0){
			holder.count.setVisibility(View.VISIBLE);
			holder.count.setText(String.valueOf(model.getCount()));
		} else {
			holder.count.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public static class ViewHolder{
		private  TextView title;
		private  TextView count;
		private  TextView time;
	}
}
