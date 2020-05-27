package com.mcfac.santropolroulant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcfac.santropolroulant.FirebaseClasses.Event;
import com.mcfac.santropolroulant.R;

import java.util.List;


// Creates views for data items + replaces content of some views with new data items when original is no longer visible
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context mCtx;
    private List<Event> eventList;          // Accesses event data through ArrayList
    private OnItemClickListener mListener;

    // Constructor
    public EventAdapter(Context mCtx, List<Event> eventList) {
        this.mCtx = mCtx;
        this.eventList = eventList;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    // Provides a reference to the views for each data item
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

    // Creates new views (called by/through layout manager)
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {     // Instantiates an EventViewHolder objec
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_layout, parent, false);
        return new EventViewHolder(view, mListener);
    }

    // Binds the view holder to the data + replaces the contents of a view (called by/through the layout manager)
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);                       // Get element from dataset at this position

        //weekImage(holder,event.getEvent_date_txt());
        holder.txtType.setText(mCtx.getString(R.string.event) + typeName(event.getEvent_type()));
        holder.txtDate.setText(event.getEvent_date_txt());                 // Change contents of view with new element
        holder.txtSlot.setText(mCtx.getString(R.string.time) + event.getEvent_time_start() + "-" + event.getEvent_time_end());
    }


    // Counts number of events in Event ArrayList
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // Returns name of event type
    public String typeName (String event_type){
        String properName = "";
        switch(event_type) {
            case "deliv":
                properName = mCtx.getString(R.string.meal_delivery);
                break;
            case "deldr":
                properName = mCtx.getString(R.string.driving_delivery);
                break;
            case "kitam":
                properName = mCtx.getString(R.string.kitchen);
                break;
            case "kitas":
                properName = mCtx.getString(R.string.kitchen);
                break;
            case "kitpm":
                properName = mCtx.getString(R.string.kitchen);
                break;
            case "kitps":
                properName = mCtx.getString(R.string.kitchen);
                break;
            default:
        }

        return properName;
    }



}