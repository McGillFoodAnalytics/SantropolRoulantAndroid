package com.example.santropolroulant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santropolroulant.FirebaseClasses.Event;
import com.example.santropolroulant.R;

import java.util.List;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context mCtx;
    private List<Event> eventList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public EventAdapter(Context mCtx, List<Event> eventList) {
        this.mCtx = mCtx;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_layout, parent, false);
        return new EventViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        weekImage(holder,event.getDate_txt());
        holder.txtDate.setText(event.getDate_txt());
        holder.txtType.setText("Event: " + event.getEvent_type());
        holder.txtSlot.setText("Time: " + event.getStart_time() + "-" + event.getEnd_time());
    }

    public void weekImage(@NonNull EventViewHolder holder, String weekString){
        if (weekString.contains("Sunday")){
            holder.imgBar.setImageResource(R.drawable.sunday_event);
        } else if (weekString.contains("Monday")){
            holder.imgBar.setImageResource(R.drawable.monday_event);
        } else if (weekString.contains("Tuesday")){
            holder.imgBar.setImageResource(R.drawable.tuesday_event);
        } else if (weekString.contains("Wednesday")){
            holder.imgBar.setImageResource(R.drawable.wednesday_event);
        } else if (weekString.contains("Thursday")){
            holder.imgBar.setImageResource(R.drawable.thursday_event);
        } else if (weekString.contains("Friday")){
            holder.imgBar.setImageResource(R.drawable.friday_event);
        } else if (weekString.contains("Saturday")){
            holder.imgBar.setImageResource(R.drawable.saturday_event);
        } else {
            holder.imgBar.setImageResource(R.drawable.sunday_event);
        }
    };
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtSlot, txtType;
        ImageView imgBar;

        public EventViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            imgBar = itemView.findViewById(R.id.imgBar);
            txtSlot = itemView.findViewById(R.id.txtSlot);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtType = itemView.findViewById(R.id.txtType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }


    }

    public String typeName (String event_type){
        String properName = "";
        if (event_type == "deliv"){
            properName = "Meal Delivery";
        }
        return properName;
    };

}