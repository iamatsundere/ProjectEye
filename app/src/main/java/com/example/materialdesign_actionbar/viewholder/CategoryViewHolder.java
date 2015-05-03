package com.example.materialdesign_actionbar.viewholder;

import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.materialdesign_actionbar.R;

/**
 * Created by Phuc on 4/29/2015.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public ImageView catgrImage;
    public TextView catgrContent;
    public ImageView catgrIcon;
    public ImageView catgrBlend;
    public CardView catgrCardView;


    public CategoryViewHolder(View view){
        super(view);
        catgrImage= (ImageView) view.findViewById(R.id.catgr_img);
        catgrBlend= (ImageView) view.findViewById(R.id.catgr_blend);
        catgrContent= (TextView) view.findViewById(R.id.catgr_content);
        catgrIcon= (ImageView) view.findViewById(R.id.catgr_icon);
        catgrCardView=(CardView) view.findViewById(R.id.catgr_cardview);
    }
}
