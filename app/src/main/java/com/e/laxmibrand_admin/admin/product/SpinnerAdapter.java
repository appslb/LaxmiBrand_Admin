package com.e.laxmibrand_admin.admin.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.beans.AdminCategory;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    int flags[];
    ArrayList<AdminCategory> categoryList;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext,  ArrayList<AdminCategory> categoryList) {
        this.context = applicationContext;
        this.categoryList = categoryList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item_list, null);
       // ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.tvCategoryName);
        ImageView dropArrow =(ImageView) view.findViewById(R.id.dropDownArrow);


        names.setText(categoryList.get(i).getCatName());
        if(i>0)
        {
            dropArrow.setVisibility(View.GONE);
        }
        else
        {
            dropArrow.setVisibility(View.VISIBLE);

        }

        names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names.setBackgroundColor(context.getResources().getColor(R.color.primaryColor));
            }
        });
        return view;
    }



}