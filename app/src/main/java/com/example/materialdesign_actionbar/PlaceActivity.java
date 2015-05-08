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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class PlaceActivity extends ActionBarActivity implements OnGetDistanceListener {

    private int[] arrPlaceColors = {R.color.mnu_restaurant, R.color.mnu_park, R.color.mnu_gasstation};
    private int[] arrPlaceIcons = {R.drawable.ic_restaurant, R.drawable.ic_park, R.drawable.ic_gasstation};
    private String[] arrCatgr = {"RESTAURANT", "PARK", "GAS STATION"};
    private PlaceRecyclerAdapter placeAdapter;
    private LatLng latLng;

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

        getData();

        RecyclerView rc = (RecyclerView) findViewById(R.id.recycler_view);
        rc.setLayoutManager(new LinearLayoutManager(this));
        placeAdapter = new PlaceRecyclerAdapter(places, this);
        rc.setAdapter(placeAdapter);
    }

    public void OnBack(View view) {
        if (view.getId() == R.id.mnu_back) {
//            super.onDestroy();
            finish();
        }
    }

    @Override
    protected void onResume() {
        getData();

        RecyclerView rc = (RecyclerView) findViewById(R.id.recycler_view);
        rc.setLayoutManager(new LinearLayoutManager(this));
        placeAdapter = new PlaceRecyclerAdapter(places, this);
        rc.setAdapter(placeAdapter);
        super.onResume();
    }

    public void OnRowClick(View v) {

        View tempView = v;
        while (!(tempView instanceof CardView)) {
            tempView = (View) tempView.getParent();
        }
        CardView cd = (CardView) findViewById(tempView.getId());
        int position = cd.getId();

        Intent intent = new Intent();
        Place place = places.get(position);
        EditActivity.myList.add((place));

        intent.putExtra("Place", place);
        setResult(1, intent);
//        super.onDestroy();
        finish();
    }

    private void getData() {
        Intent intent = getIntent();
        places = intent.getParcelableArrayListExtra("Places");
        latLng = intent.getParcelableExtra("LatLng");
        for (int i = 0; i < places.size(); i++) {
            places.get(i).setListener(this);
            places.get(i).getFormattedAddress();
            places.get(i).getDistance(latLng);
            places.get(i).setTypeID(CategoryActivity.typeID);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinish(int flag) {
        if (flag == 2) {
            sortAscending(places);
        }
        placeAdapter.notifyDataSetChanged();
    }

    private void sortAscending(ArrayList<Place> places) {
        for (int i = 0; i < places.size() - 1; i++) {
            for (int j = i + 1; j < places.size(); j++) {
                if (places.get(i).getDistanceInDrivingMode() > places.get(j).getDistanceInDrivingMode()) {
                    Place place = places.get(i);
                    places.set(i, places.get(j));
                    places.set(j, place);
                }
            }
        }
    }
}
