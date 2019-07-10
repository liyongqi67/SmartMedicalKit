package com.example.yyy.medicinekit.MyDrug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.yyy.medicinekit.R;

import java.util.List;

/**
 * Created by YYY on 2016/8/27.
 */

public class ArticalAdapter extends ArrayAdapter<ArticalClass> {
    private int resourceId;

    public ArticalAdapter(Context context , int textViewResourceId, List<ArticalClass> objects) {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArticalClass artical = getItem(position);//获取当前项的Medicine实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.Image = (ImageView) view.findViewById(R.id.articalImage);
            viewHolder.nameText = (TextView) view.findViewById(R.id.articalName);
            viewHolder.intro = (TextView) view.findViewById(R.id.articalintro);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
        }

        if (artical != null&&viewHolder!=null) {
            if(viewHolder.Image!=null)
                viewHolder.Image.setImageResource(artical.getImageId());
            if(viewHolder.nameText!=null)
                viewHolder.nameText.setText(artical.getName());
            if(viewHolder.intro!=null)
                viewHolder.intro.setText(artical.getintro());
        }

        return view;
    }

    class ViewHolder {
        ImageView Image;
        TextView nameText,intro;
    }
}
