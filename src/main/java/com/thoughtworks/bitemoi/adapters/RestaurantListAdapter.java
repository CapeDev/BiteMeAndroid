package com.thoughtworks.bitemoi.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.thoughtworks.bitemoi.models.Restaurant;
import com.thoughtworks.bitemoi.R;

import java.util.List;

public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {

    private final Activity context;
    private final List<Restaurant> list;

    public RestaurantListAdapter(Activity context, List<Restaurant> restaurantList) {
        super(context, R.layout.restaurant_result, restaurantList);
        this.context = context;
        this.list = restaurantList;
    }

    private static class ViewHolder{
        protected TextView name;
        protected TextView distance;
        protected TextView price;
        protected RatingBar rating;
    }

    @Override
    public View getView(int position, View existingView, ViewGroup existingViewGroup){
        View view = existingView == null ? inflateView() : existingView;

        ViewHolder holder = (ViewHolder) view.getTag();

        holder.name.setText(list.get(position).getName());
        holder.distance.setText(list.get(position).getDistance());
        holder.price.setText(list.get(position).getPrice());
        holder.rating.setNumStars((int) Double.parseDouble(list.get(position).getRating()));

        return view;
    }

    private View inflateView() {
        LayoutInflater inflator = context.getLayoutInflater();
        View view = inflator.inflate(R.layout.restaurant_result, null);
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.name = (TextView) view.findViewById(R.id.name);
        viewHolder.distance = (TextView) view.findViewById(R.id.distance);
        viewHolder.price = (TextView) view.findViewById(R.id.price);
        viewHolder.rating = (RatingBar) view.findViewById(R.id.ratingBar);
        view.setTag(viewHolder);
        return view;
    }
}
