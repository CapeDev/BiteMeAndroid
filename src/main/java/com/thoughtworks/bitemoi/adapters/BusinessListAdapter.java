package com.thoughtworks.bitemoi.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.thoughtworks.bitemoi.Business;
import com.thoughtworks.bitemoi.R;

import java.util.List;

public class BusinessListAdapter extends ArrayAdapter<Business> {

    private final Activity context;
    private final List<Business> list;

    public BusinessListAdapter(Activity context, List<Business> businessList) {
        super(context, R.layout.business_list_item, businessList);
        this.context = context;
        this.list = businessList;
    }

    static class ViewHolder{
        protected TextView name;
        protected TextView distance;
        protected TextView price;
        protected RatingBar rating;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = null;
        if(convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.business_list_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.distance = (TextView) view.findViewById(R.id.distance);
            viewHolder.price = (TextView) view.findViewById(R.id.price);
            viewHolder.rating = (RatingBar) view.findViewById(R.id.ratingBar);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.distance.setText(list.get(position).getDistance());
        holder.price.setText(list.get(position).getPrice());
        holder.rating.setNumStars((int) Double.parseDouble(list.get(position).getRating()));
        return view;
    }
}
