package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<CategoryStruct> objects;

    CategoryAdapter (Context context, ArrayList<CategoryStruct> categoryStructs){
        ctx=context;
        objects=categoryStructs;
        lInflater = (LayoutInflater) ctx
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    //количество элементов
    @Override
    public int getCount(){
        return objects.size();
    }

    //элемент по позиции
    public Object getItem(int position){
        return objects.get(position);
    }

    //id по позиции
    @Override
    public long getItemId(int position){
        return position;
    }

    //пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //используем созданные, но не используемые View
        View view =convertView;
        if (view ==null){
            view = lInflater.inflate(R.layout.list_item, (ViewGroup) parent, false);
        }

        CategoryStruct c = getCategory(position);
        //заполняем View в пункте списка данными из категории: названием и переводом
        ((TextView) view.findViewById(R.id.txtname)).setText(c.getCategoryName());
        ((TextView) view.findViewById(R.id.txttranslate)).setText(c.getTranslates());

        return view;
    }

    //категория по позиции
    CategoryStruct getCategory(int position) {
        return ((CategoryStruct) getItem(position));
    }


}
