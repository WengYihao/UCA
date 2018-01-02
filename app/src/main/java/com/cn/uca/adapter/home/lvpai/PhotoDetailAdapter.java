package com.cn.uca.adapter.home.lvpai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.uca.R;
import com.cn.uca.bean.home.lvpai.PhotoDetailBean;
import com.cn.uca.bean.home.lvpai.TeamBean;
import com.cn.uca.util.OpenPhoto;
import com.cn.uca.view.CircleImageView;
import com.cn.uca.view.NoScrollGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */

public class PhotoDetailAdapter extends BaseAdapter {
    private List<PhotoDetailBean> list;
    private Context context;

    public PhotoDetailAdapter(List<PhotoDetailBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<PhotoDetailBean> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.look_photo_item, parent, false);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.gridView = (NoScrollGridView)convertView.findViewById(R.id.gridView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getAlbum_name());
        PhotoItemAdapter adapter = new PhotoItemAdapter(list.get(position).getAlbumPictures(),context);
       final List<String> listUrl = new ArrayList<String>();
        for (int i = 0;i<list.get(position).getAlbumPictures().size();i++){
            listUrl.add(list.get(position).getAlbumPictures().get(i).getPicture_url());
        }
        holder.gridView.setAdapter(adapter);
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPhoto.imageUrl(context,position,(ArrayList<String>) listUrl);
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView name;
        NoScrollGridView gridView;
    }
}
