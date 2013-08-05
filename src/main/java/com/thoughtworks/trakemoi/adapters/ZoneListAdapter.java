package com.thoughtworks.trakemoi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.inject.Inject;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.models.Zone;

import java.util.ArrayList;

public class ZoneListAdapter extends ArrayAdapter<Zone> {

    private final Context context;

    @Inject
    public ZoneListAdapter(Context context) {
        super(context, R.layout.zone, new ArrayList<Zone>());
        this.context = context;
    }

    @Override
    public void clear() {
        super.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View existingView, ViewGroup existingViewGroup) {
        final View view = existingView == null ? inflateView() : existingView;
        final ViewHolder holder = (ViewHolder) view.getTag();

        final Zone zone = super.getItem(position);

        holder.name.setText(zone.getName());
        holder.desc.setText(zone.getDesc());

        return view;
    }

    private View inflateView() {
        View view = LayoutInflater.from(context).inflate(R.layout.zone, null);
        ViewHolder viewHolder = new ViewHolder(
                (TextView) view.findViewById(R.id.name),
                (TextView) view.findViewById(R.id.desc));
        view.setTag(viewHolder);
        return view;
    }

    private static class ViewHolder {

        protected final TextView name;
        protected final TextView desc;

        public ViewHolder(TextView name, TextView desc) {
            this.name = name;
            this.desc = desc;
        }
    }

}
