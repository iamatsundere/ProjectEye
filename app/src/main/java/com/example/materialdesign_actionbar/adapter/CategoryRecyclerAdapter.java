package com.example.materialdesign_actionbar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.materialdesign_actionbar.PlaceActivity;
import com.example.materialdesign_actionbar.R;
import com.example.materialdesign_actionbar.model.Category;
import com.example.materialdesign_actionbar.viewholder.CategoryViewHolder;

import java.util.Collections;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Phuc on 4/29/2015.
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private LayoutInflater inflater;
    private List<Category> catgrList = Collections.emptyList();
    private Context context;

    //    public CategoryRecyclerAdapter(ViewGroup parent,List<Category> catgrList){
    public CategoryRecyclerAdapter(List<Category> catgrList, Context context) {
        this.catgrList = catgrList;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_row, null);
        CategoryViewHolder catgrVH = new CategoryViewHolder(view);
        CardView card = (CardView) view;
        card.setOnClickListener(placeOnClick);

        return catgrVH;
    }

    private View.OnClickListener placeOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CardView card=(CardView)v;
            Log.e("RES",String.valueOf(card.getId()));
        }
    };

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final Category currentItem = catgrList.get(position);
        holder.catgrContent.setText(currentItem.getName());
        holder.catgrIcon.setImageResource(currentItem.getIconID());
        holder.catgrBlend.setBackgroundColor(currentItem.getColorID());
        holder.catgrImage.setImageResource(currentItem.getImageID());
        holder.catgrBlend.setAlpha(.8f);
        holder.catgrCardView.setId(position);
    }

    @Override
    public int getItemCount() {
        return catgrList == null ? 0 : catgrList.size();
    }

}
