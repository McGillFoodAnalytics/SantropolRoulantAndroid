package com.example.santropolroulant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context mCtx;
    private List<Event> eventList;

    public EventAdapter(Context mCtx, List<Event> eventList) {
        this.mCtx = mCtx;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.txtDate.setText(event.getDate());
//        holder.txtCapacity.setText("Capacity: " + String.valueOf(event.getCap()));
//        holder.txtSlot.setText("Time Slot: " + event.getSlot());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtSlot, txtCapacity;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSlot = itemView.findViewById(R.id.txtSlot);
            txtCapacity = itemView.findViewById(R.id.txtCapacity);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}