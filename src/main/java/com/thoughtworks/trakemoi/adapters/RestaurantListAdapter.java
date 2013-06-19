package com.thoughtworks.trakemoi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.inject.Inject;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.models.Restaurant;
import com.thoughtworks.trakemoi.utilities.BinaryProcedure;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {
    private final Context context;
    private final ImageRegistry imageRegistry = new ImageRegistry();
    private final ConcurrentMap<ImageView, String> uriCache = new ConcurrentHashMap<ImageView, String>();

    @Inject
    public RestaurantListAdapter(Context context) {
        super(context, R.layout.restaurant_result, new ArrayList<Restaurant>());
        this.context = context;
    }

    @Override
    public View getView(int position, View existingView, ViewGroup existingViewGroup) {
        final View view = existingView == null ? inflateView() : existingView;
        final ViewHolder holder = (ViewHolder) view.getTag();
        final Restaurant restaurant = super.getItem(position);

        holder.name.setText(restaurant.getName());
        holder.distance.setText(restaurant.getDistance());
        holder.price.setText(restaurant.getPrice());
        holder.rating.setNumStars(restaurant.getRating().intValue());

        uriCache.put(holder.image, restaurant.getImageUrl());
        imageRegistry.get(restaurant.getImageUrl(), new BinaryProcedure<String, Bitmap>() {
            @Override public void call(String url, Bitmap image) {
                if (uriCache.get(holder.image).equals(url)) {
                    holder.image.setImageBitmap(image);
                }
            }
        });

        return view;
    }

    public void addAll(Iterable<Restaurant> result) {
        for (Restaurant restaurant : result) {
            imageRegistry.prime(restaurant.getImageUrl());
            super.add(restaurant);
        }
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        super.clear();
        notifyDataSetChanged();
    }

    private View inflateView() {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_result, null);
        ViewHolder viewHolder = new ViewHolder(
                (TextView) view.findViewById(R.id.name),
                (TextView) view.findViewById(R.id.distance),
                (TextView) view.findViewById(R.id.price),
                (RatingBar) view.findViewById(R.id.rating),
                (ImageView) view.findViewById(R.id.image));
        view.setTag(viewHolder);
        return view;
    }

    private static class ViewHolder {
        protected final TextView name;
        protected final TextView distance;
        protected final TextView price;
        protected final RatingBar rating;
        protected final ImageView image;

        public ViewHolder(TextView name, TextView distance, TextView price, RatingBar rating, ImageView image) {
            this.name = name;
            this.distance = distance;
            this.price = price;
            this.rating = rating;
            this.image = image;
        }
    }

    public static class ImageRegistry {
        public Map<String, Bitmap> cache = new ConcurrentHashMap<String, Bitmap>();

        public void get(final String uri, final BinaryProcedure<String, Bitmap> callback) {
            if (cache.containsKey(uri)) {
                callback.call(uri, cache.get(uri));
                return;
            }
            doGet(uri, callback);
        }

        public void prime(final String uri) {
            doGet(uri, new BinaryProcedure<String, Bitmap>() {
                @Override public void call(String arg1, Bitmap arg2) {
                    // no-op
                }
            });
        }

        private void doGet(final String uri, final BinaryProcedure<String, Bitmap> callback) {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override protected Bitmap doInBackground(Void... params) {
                    try {
                        InputStream in = new java.net.URL(uri).openStream();
                        return BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override protected void onPostExecute(Bitmap bitmap) {
                    cache.put(uri, bitmap);
                    callback.call(uri, bitmap);
                }
            }.execute();
        }
    }
}
