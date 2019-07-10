package com.example.yyy.medicinekit.MyDrug;

import android.content.Context;
import android.util.Log;
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


public class MedicineAdapter extends ArrayAdapter<Medicine> {

    private int resourceId;


    public MedicineAdapter(Context context , int textViewResourceId, List<Medicine> objects) {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Medicine medicine = getItem(position);//获取当前项的Medicine实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.medicineImage = (ImageView) view.findViewById(R.id.medImage);
            viewHolder.nameText = (TextView) view.findViewById(R.id.medName);
            viewHolder.frequencyText = (TextView) view.findViewById(R.id.frequencyText);
            viewHolder.timepieText = (TextView) view.findViewById(R.id.timepieceText);
//            viewHolder.frechangeText = (EditText) view.findViewById(R.id.frechangText);
//            viewHolder.timepiechangeText = (EditText) view.findViewById(R.id.timepiechangeText);
            viewHolder.introText = (TextView) view.findViewById(R.id.introText);
            viewHolder.typeText = (TextView) view.findViewById(R.id.typeText);
            viewHolder.priceText = (TextView) view.findViewById(R.id.priceText);
            viewHolder.pieceText = (TextView) view.findViewById(R.id.pieceText);
            viewHolder.time1 = (TextView) view.findViewById(R.id.time1);
            viewHolder.time2 = (TextView) view.findViewById(R.id.time2);
            viewHolder.time3 = (TextView) view.findViewById(R.id.time3);
            viewHolder.time4 = (TextView) view.findViewById(R.id.time4);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
        }
        notifyDataSetChanged();
        if (medicine != null&&viewHolder!=null) {
            if(viewHolder.medicineImage!=null)
                viewHolder.medicineImage.setImageResource(medicine.getImageId());
            if(viewHolder.nameText!=null)
                viewHolder.nameText.setText("药品名称 : "+medicine.getName());
            if(viewHolder.frequencyText!=null)
                viewHolder.frequencyText.setText("服用次数/日 ："+medicine.getfrequency());
            if(viewHolder.timepieText!=null)
                viewHolder.timepieText.setText("服用量/次 ："+medicine.gettimepiece());
            if(viewHolder.introText!=null)
                viewHolder.introText.setText("药品介绍 ："+medicine.getintro());
            if(viewHolder.typeText!=null)
                viewHolder.typeText.setText("药品类别 ："+medicine.gettype());
            if(viewHolder.priceText!=null)
                viewHolder.priceText.setText("价格 ："+medicine.getprice());
            if(viewHolder.pieceText!=null)
                viewHolder.pieceText.setText("剩余量 ："+medicine.getpiece());
            if(viewHolder.time1!=null)
                viewHolder.time1.setText("上午   "+medicine.getmorning());
            if(viewHolder.time2!=null)
                viewHolder.time2.setText("中午   "+medicine.getnoon());
            if(viewHolder.time3!=null)
                viewHolder.time3.setText("下午   "+medicine.getafternoon());
            if(viewHolder.time4!=null)
                viewHolder.time4.setText("晚上   "+medicine.getnight());

            Log.e("上午",medicine.getmorning());
            Log.e("中午",medicine.getnoon());
            Log.e("下午",medicine.getafternoon());
            Log.e("晚上",medicine.getnight());
        }

        return view;
    }

    class ViewHolder {
        ImageView medicineImage;
        TextView nameText,frequencyText,timepieText,introText,typeText,priceText,pieceText,time1,time2,time3,time4;
        EditText frechangeText,timepiechangeText;
        CheckBox checkBox;
    }
}
