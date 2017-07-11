package com.example.abanoub.onlinenotebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Abanoub on 2017-06-27.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {

    Context context;
    ArrayList<Note> list;

    public Adapter(Context context, ArrayList<Note> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.single_item, null);
        viewHolder viewHolder = new viewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

        holder.title.setText(list.get(position).title);
        holder.note.setText(list.get(position).note);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {
        CardView cardView;
        TextView title;
        TextView note;

        public viewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            title = (TextView) itemView.findViewById(R.id.title);
            note = (TextView) itemView.findViewById(R.id.note);

            title.setTypeface(Typeface.createFromAsset(context.getResources().getAssets(), "Roboto-Bold.ttf"));
            note.setTypeface(Typeface.createFromAsset(context.getResources().getAssets(), "Roboto-Regular.ttf"));

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Note note = list.get(position);

            if (note == null) {
                return;
            }
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("note",note);
            context.startActivity(intent);
        }
    }
}
