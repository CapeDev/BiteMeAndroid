package com.thoughtworks.bitemoi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RestaurantListAdapter extends BaseAdapter {
    private final Context applicationContext;
    private final int layout;
    private final List<Restaurant> restaurants;
    private final ConcurrentHashMap<ImageView, String> uriCache = new ConcurrentHashMap<ImageView, String>();
    private final ImageRegistry imageRegistry;

    public RestaurantListAdapter(Context applicationContext, int layout) {
        this.applicationContext = applicationContext;
        this.layout = layout;
        this.restaurants = new ArrayList<Restaurant>();
        imageRegistry = new ImageRegistry();
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

        final Restaurant restaurant = restaurants.get(i);

        TextView nameView = (TextView) view.findViewById(R.id.name);
        nameView.setText(restaurant.getName());

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating);
        ratingBar.setNumStars(restaurant.getRating().intValue());

        final ImageView imageView = (ImageView) view.findViewById(R.id.image);

        imageView.setImageBitmap(null);

        uriCache.put(imageView, restaurant.getImageUrl());

        imageRegistry.get(restaurant.getImageUrl(), new BinaryProcedure<String, Bitmap>() {
            @Override public void call(String arg1, Bitmap arg2) {
                if (uriCache.get(imageView).equals(arg1)) {
                    imageView.setImageBitmap(arg2);
                }
            }
        });

        return view;
    }

    public void clear() {
        restaurants.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Restaurant> result) {
        restaurants.addAll(result);
        for (Restaurant restaurant : result) {
            imageRegistry.prime(restaurant.getImageUrl());
        }
        notifyDataSetChanged();
    }

    public static class ImageRegistry {
        public Map<String, Bitmap> cache = new ConcurrentHashMap<String, Bitmap>();

        public void get(final String uri, final BinaryProcedure<String, Bitmap> doneback) {
            if (cache.containsKey(uri)) {
                doneback.call(uri, cache.get(uri));
                return;
            }
            new AsyncTask<Void, Void, Bitmap>() {
                @Override protected Bitmap doInBackground(Void... params) {
                    Bitmap image = null;
                    try {
                        InputStream in = new java.net.URL(uri).openStream();
                        image = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        return null;
                    }
                    return image;
                }

                @Override protected void onPostExecute(Bitmap bitmap) {
                    cache.put(uri, bitmap);
                    doneback.call(uri, bitmap);
                }
            }.execute();
        }

        public void prime(final String uri) {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override protected Bitmap doInBackground(Void... params) {
                    Bitmap image = null;
                    try {
                        InputStream in = new java.net.URL(uri).openStream();
                        image = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        return null;
                    }
                    return image;
                }

                @Override protected void onPostExecute(Bitmap bitmap) {
                    cache.put(uri, bitmap);
                }
            }.execute();
        }
    }

    public static interface BinaryProcedure<S, T> {
        public void call(S arg1, T arg2);
    }
}
