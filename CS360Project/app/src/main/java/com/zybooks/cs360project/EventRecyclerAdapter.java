package com.zybooks.cs360project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private List<Event> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    EventRecyclerAdapter(Context context, List<Event> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = mData.get(position);
        holder.mEventName.setText(event.getTitle());
        holder.mEventDate.setText(event.getFormattedDate());
        holder.mEventId = event.getId();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int mEventId;
        TextView mEventName, mEventDate;

        ViewHolder(View itemView) {
            super(itemView);
            mEventName = itemView.findViewById(R.id.eventName);
            mEventDate = itemView.findViewById(R.id.eventDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(), mEventId);
        }
    }

    int getEvent(int id) {
        return mData.get(id).getId();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int eventId);
    }
}
