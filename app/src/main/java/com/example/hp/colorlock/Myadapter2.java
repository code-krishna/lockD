package com.example.hp.colorlock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Myadapter2 extends ArrayAdapter<User> implements View.OnClickListener {
    private ArrayList<User> dataSet;
    Context context;
    //View look up cache
    private static class ViewHolder{
        TextView user_name;
        TextView user_id;
    }
    public Myadapter2(ArrayList<User> dataSet,Context context){
        super(context,R.layout.list_view_row_item2,dataSet);
        this.dataSet=dataSet;
        this.context=context;
    }
    @Override
    public void onClick(View view){


    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get data item for this position
        User user =getItem(position);
        //Check if an existing view is being reused ,otherwise inflate the view
        Myadapter2.ViewHolder viewHolder;//view look up cache stored in tag
        final View result;
        if(convertView==null){
            viewHolder=new Myadapter2.ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(R.layout.list_view_row_item2,parent,false);
            viewHolder.user_id=convertView.findViewById(R.id.user_id);
            viewHolder.user_name=convertView.findViewById(R.id.user_name);
            result=convertView;
            result.setTag(viewHolder);
        }else{
            viewHolder=(Myadapter2.ViewHolder)convertView.getTag();
            result=convertView;
        }
        viewHolder.user_name.setText(user.getName());
        viewHolder.user_id.setText(user.getUid());
        return  result;
    }



}
