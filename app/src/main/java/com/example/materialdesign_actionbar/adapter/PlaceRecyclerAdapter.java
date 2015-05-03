package com.example.materialdesign_actionbar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.materialdesign_actionbar.EditActivity;
import com.example.materialdesign_actionbar.PlaceActivity;
import com.example.materialdesign_actionbar.R;
import com.example.materialdesign_actionbar.model.Category;
import com.example.materialdesign_actionbar.model.Place;
import com.example.materialdesign_actionbar.viewholder.CategoryViewHolder;
import com.example.materialdesign_actionbar.viewholder.PlaceViewHolder;

import java.util.Collections;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Phuc on 5/1/2015.
 */
public class PlaceRecyclerAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    private LayoutInflater inflater;
    private List<Place> placeList = Collections.emptyList();
    private Context context;

    public PlaceRecyclerAdapter(List<Place> placeList, Context context) {
        this.placeList = placeList;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_row, null);
        PlaceViewHolder catgrVH = new PlaceViewHolder(view);
        CardView card = (CardView) view;
        card.setOnClickListener(placeOnClick);
        return catgrVH;
    }

    private View.OnClickListener placeOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
        }
    };

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        final Place currentItem = placeList.get(position);
        holder.placeName.setText(currentItem.getName());
        holder.placeIcon.setImageResource(currentItem.geticonID());
        holder.placeAddress.setText(currentItem.getAddress());
        holder.placeDistance.setText(String.valueOf(currentItem.getDistance()));
        holder.placeColor.setBackgroundColor(currentItem.getColorID());
        holder.placeCardView.setId(position);
    }

    @Override
    public int getItemCount() {
        return placeList.isEmpty() ? 0 : placeList.size();
    }
}
