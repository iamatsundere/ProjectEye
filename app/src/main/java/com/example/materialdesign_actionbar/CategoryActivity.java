package com.example.materialdesign_actionbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.materialdesign_actionbar.adapter.CategoryRecyclerAdapter;
import com.example.materialdesign_actionbar.model.Category;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends ActionBarActivity {

    private CardView cardView;
    public static int typeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        toolbar.setTitleTextColor(getResources().getColor(R.color.primaryColor));
        toolbar.setTitle("");
        toolbar.setElevation(.5f);
        TextView txt = (TextView) findViewById(R.id.app_bar_title);
        txt.setText("LOCATION");
        ImageView imageView = (ImageView) findViewById(R.id.mnu_back);
        imageView.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);


        RecyclerView rc = (RecyclerView) findViewById(R.id.recycler_view);
        rc.setLayoutManager(new LinearLayoutManager(this));

        CategoryRecyclerAdapter catgrAdapter = new CategoryRecyclerAdapter(getData(), this);
        rc.setAdapter(catgrAdapter);
    }


    public void OnBack(View view) {
        if (view.getId() == R.id.mnu_back) {
            startActivity(new Intent(this, EditActivity.class));
        }
    }
    public void onCategoryClick(View view) {
        CardView cd=(CardView) view.getParent().getParent();
        typeID=cd.getId();
        startActivity(new Intent(this,PlaceActivity.class));
    }


    public List<Category> getData() {
        List<Category> categoryList = new ArrayList();
        int[] arrCatgrIcons = {R.drawable.ic_restaurant, R.drawable.ic_park, R.drawable.ic_gasstation};
        String[] arrCatgrContent = {"Restaurant", "Park", "Gas Station"};
        int[] arrCatgrImage = {R.drawable.restaurant, R.drawable.supermarket, R.drawable.gasstation};
        int[] arrCatgrColors = {R.color.mnu_restaurant, R.color.mnu_park, R.color.mnu_gasstation};

        for (int i = 0; i < arrCatgrContent.length; i++) {

//            Log.e("123", arrCatgrContent[i]);
            Category category = new Category();
            category.setIconID(arrCatgrIcons[i]);
            category.setName(arrCatgrContent[i]);
            category.setImageID(arrCatgrImage[i]);
            category.setColorID(getResources().getColor(arrCatgrColors[i]));
            categoryList.add(category);
        }
        return categoryList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);

//        View layout=

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
