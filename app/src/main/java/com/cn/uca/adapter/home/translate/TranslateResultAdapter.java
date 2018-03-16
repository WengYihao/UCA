package com.cn.uca.adapter.home.translate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.translate.TranslateResultBean;
import com.cn.uca.util.ToastXutil;
import com.cn.uca.util.voice.VoiceUtils;

import java.util.List;


public class TranslateResultAdapter extends BaseAdapter{
	private List<TranslateResultBean> list;
	private Context context;
	public TranslateResultAdapter(List<TranslateResultBean> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public void setList(List<TranslateResultBean> list) {
		if (list != null) {
			this.list = list;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.translate_result_item, parent, false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder = new ViewHolder();
		holder.src = (TextView) convertView.findViewById(R.id.src);
		holder.dst = (TextView)convertView.findViewById(R.id.dst);
		holder.srcVoice = (TextView)convertView.findViewById(R.id.srcVoice);
		holder.dstVoice = (TextView)convertView.findViewById(R.id.dstVoice);
		holder.copy = (TextView)convertView.findViewById(R.id.copy);
		holder.src.setText(list.get(position).getSrc());
		holder.dst.setText(list.get(position).getDst());
		holder.srcVoice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				VoiceUtils.getInstance().initmTts(context,list.get(position).getSrc());
			}
		});

		holder.dstVoice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				VoiceUtils.getInstance().initmTts(context,list.get(position).getDst());
			}
		});

		holder.copy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//获取剪贴板管理器：
				ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				// 创建普通字符型ClipData
				ClipData mClipData = ClipData.newPlainText("Label", list.get(position).getDst());
				// 将ClipData内容放到系统剪贴板里。
				cm.setPrimaryClip(mClipData);
				ToastXutil.show("复制成功");
			}
		});
		return convertView;
	}
	class ViewHolder {
		TextView src,dst,srcVoice,dstVoice,copy;
	}
}
