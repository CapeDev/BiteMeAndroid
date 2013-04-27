package com.thoughtworks.bitemoi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.thoughtworks.bitemoi.R;
import com.thoughtworks.bitemoi.models.Restaurant;

import java.util.List;

public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {
    private final Context context;
    private final List<Restaurant> restaurants;

    public RestaurantListAdapter(Context context, List<Restaurant> restaurants) {
        super(context, R.layout.restaurant_result, restaurants);
        this.context = context;
        this.restaurants = restaurants;
    }

    @Override
    public View getView(int position, View existingView, ViewGroup existingViewGroup) {
        View view = existingView == null ? inflateView() : existingView;
        ViewHolder holder = (ViewHolder) view.getTag();
        Restaurant restaurant = restaurants.get(position);

        holder.name.setText(restaurant.getName());
        holder.distance.setText(restaurant.getDistance());
        holder.price.setText(restaurant.getPrice());
        holder.rating.setNumStars((int) Double.parseDouble(restaurant.getRating()));

        return view;
    }

    @Override
    public void clear() {
        restaurants.clear();
        notifyDataSetChanged();
    }

    private View inflateView() {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_result, null);
        ViewHolder viewHolder = new ViewHolder(
                (TextView) view.findViewById(R.id.name),
                (TextView) view.findViewById(R.id.distance),
                (TextView) view.findViewById(R.id.price),
                (RatingBar) view.findViewById(R.id.ratingBar));
        view.setTag(viewHolder);
        return view;
    }

    private static class ViewHolder {
        protected TextView name;
        protected TextView distance;
        protected TextView price;
        protected RatingBar rating;

        public ViewHolder(TextView name, TextView distance, TextView price, RatingBar rating) {
            this.name = name;
            this.distance = distance;
            this.price = price;
            this.rating = rating;
        }
    }
}
