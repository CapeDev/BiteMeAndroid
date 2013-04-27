package com.thoughtworks.bitemoi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends BaseAdapter {
    private final Context applicationContext;
    private final int layout;
    private final List<Restaurant> restaurants;

    public RestaurantListAdapter(Context applicationContext, int layout) {
        this.applicationContext = applicationContext;
        this.layout = layout;
        this.restaurants = new ArrayList<Restaurant>();
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurants.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View existing, ViewGroup viewGroup) {
        View view = existing;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(applicationContext);
            view = vi.inflate(layout, null);
        }

        Restaurant restaurant = restaurants.get(i);

        TextView nameView = (TextView)view.findViewById(R.id.name);
        nameView.setText(restaurant.getName());

        return view;
    }

    public void clear() {
        restaurants.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Restaurant> result) {
        restaurants.addAll(result);
        notifyDataSetChanged();
    }
}
