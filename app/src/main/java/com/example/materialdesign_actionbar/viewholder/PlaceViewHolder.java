package com.example.materialdesign_actionbar.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.materialdesign_actionbar.R;


/**
 * Created by Phuc on 4/30/2015.
 */
public class PlaceViewHolder extends RecyclerView.ViewHolder {
    public ImageView placeIcon;
    public TextView placeName;
    public TextView placeDistance;
    public TextView placeAddress;
    public ImageView placeColor;
    public CardView placeCardView;


    public PlaceViewHolder(View itemView) {
        super(itemView);
        placeIcon= (ImageView) itemView.findViewById(R.id.place_icon);
        placeName= (TextView) itemView.findViewById(R.id.place_name);
        placeDistance= (TextView) itemView.findViewById(R.id.place_distance);
        placeAddress= (TextView) itemView.findViewById(R.id.place_address);
        placeColor=(ImageView) itemView.findViewById(R.id.place_background);
        placeCardView=(CardView)itemView.findViewById(R.id.place_cardview);
    }
}
