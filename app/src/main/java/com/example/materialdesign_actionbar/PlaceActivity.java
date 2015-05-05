package com.example.materialdesign_actionbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import com.example.materialdesign_actionbar.adapter.PlaceRecyclerAdapter;
import com.example.materialdesign_actionbar.model.Place;

import java.util.ArrayList;


public class PlaceActivity extends ActionBarActivity {

    private CardView cardView;
    private int[] arrPlaceColors = {R.color.mnu_restaurant, R.color.mnu_park, R.color.mnu_gasstation};
    private int[] arrPlaceIcons = {R.drawable.ic_restaurant, R.drawable.ic_park, R.drawable.ic_gasstation};
    private String[] arrCatgr = {"RESTAURANT", "PARK", "GAS STATION"};

    private ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        toolbar.setTitle("");
        TextView txt = (TextView) findViewById(R.id.app_bar_title);
        txt.setText(arrCatgr[CategoryActivity.typeID]);
        txt.setTextColor(getResources().getColor(arrPlaceColors[CategoryActivity.typeID]));
        ImageView imageView = (ImageView) findViewById(R.id.mnu_back);
        imageView.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);

        places = getData();

        RecyclerView rc = (RecyclerView) findViewById(R.id.recycler_view);
        rc.setLayoutManager(new LinearLayoutManager(this));
        PlaceRecyclerAdapter placeAdapter = new PlaceRecyclerAdapter(places, this);
        rc.setAdapter(placeAdapter);
    }


    public void OnBack(View view) {
        if (view.getId() == R.id.mnu_back) {
            finish();
        }
    }

    public void OnRowClick(View v) {
        View cd = (View) v.getParent().getParent();
        int position = cd.getId();
        Log.e("123", String.valueOf(position));
//        Intent intent = new Intent();
//        Place place = places.get(position);
//        intent.putExtra("Place", place);
//        setResult(1, intent);
//        finish();
    }

    private ArrayList<Place> getData() {
        Intent intent = getIntent();
        ArrayList<Place> placeList = intent.getParcelableArrayListExtra("Places");
        return placeList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place, menu);

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
