package com.thoughtworks.trakemoi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.inject.Inject;
import com.thoughtworks.trakemoi.R;
import com.thoughtworks.trakemoi.models.PunchStatus;

import java.util.ArrayList;

public class StatusListAdapter extends ArrayAdapter<PunchStatus> {
    private final Context context;

    @Inject
    public StatusListAdapter(Context context) {
        super(context, R.layout.punch_status, new ArrayList<PunchStatus>());
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

        final PunchStatus punchStatus = super.getItem(position);

        holder.status.setText(punchStatus.getStatus());
        holder.date.setText(punchStatus.getDate());
        holder.time.setText(punchStatus.getTime());

        return view;
    }

    private View inflateView() {
        View view = LayoutInflater.from(context).inflate(R.layout.punch_status, null);
        ViewHolder viewHolder = new ViewHolder(
                (TextView) view.findViewById(R.id.status),
                (TextView) view.findViewById(R.id.date),
                (TextView) view.findViewById(R.id.time));

        view.setTag(viewHolder);

        return view;

    }

    private static class ViewHolder {
        protected final TextView status;
        protected final TextView date;
        protected final TextView time;

        public ViewHolder(TextView status, TextView date, TextView time) {
            this.status = status;
            this.date = date;
            this.time = time;
        }
    }

    private class ItemViewCache {
        public TextView status;
        public TextView date;
        public TextView time;
    }
}
