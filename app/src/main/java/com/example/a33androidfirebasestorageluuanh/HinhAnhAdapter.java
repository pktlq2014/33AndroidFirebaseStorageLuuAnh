package com.example.a33androidfirebasestorageluuanh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HinhAnhAdapter extends BaseAdapter
{
    Context context;
    int myLayout;
    ArrayList<HinhAnh> arrayList;

    public HinhAnhAdapter(Context context, int myLayout, ArrayList<HinhAnh> arrayList) {
        this.context = context;
        this.myLayout = myLayout;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount()
    {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    private class viewHolder
    {
        ImageView imageView2;
        TextView textView1;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        viewHolder viewHolder = new viewHolder();
        if(view == null)
        {
            view = layoutInflater.inflate(myLayout, null);
            viewHolder.textView1 = view.findViewById(R.id.textView1);
            viewHolder.imageView2= view.findViewById(R.id.imageView2);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (HinhAnhAdapter.viewHolder) view.getTag();
        }

        // gán giá trị
        viewHolder.textView1.setText(arrayList.get(position).getTenAnh());
        Picasso.with(context).load(arrayList.get(position).getLinkAnh()).into(viewHolder.imageView2);
        return view;
    }
}
