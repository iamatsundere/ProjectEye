package com.example.materialdesign_actionbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.materialdesign_actionbar.adapter.PlaceRecyclerAdapter;
import com.example.materialdesign_actionbar.model.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class EditActivity extends ActionBarActivity {

    public Button btn;
    private ArrayList<Place> places;
    public static ArrayList<Place> myList;
    private LatLng latLng;
    private TextView text_dis_walk, text_dis_car;
    private PlaceRecyclerAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        toolbar.setTitleTextColor(getResources().getColor(R.color.primaryColor));
        toolbar.setTitle("");
        TextView txt = (TextView) findViewById(R.id.app_bar_title);
        txt.setText("YOUR ROUTE");
        ImageView imageView = (ImageView) findViewById(R.id.mnu_back);
        imageView.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        ImageView btnBack = (ImageView) findViewById(R.id.mnu_back);
        btnBack.setOnClickListener(onClickBack);

        places = new ArrayList<>();
        myList = new ArrayList<>();
        Intent intent = getIntent();
        latLng = intent.getParcelableExtra("LatLng");
        myList = intent.getParcelableArrayListExtra("Places");

//        RecyclerView rc = (RecyclerView) findViewById(R.id.recycler_view);
//        rc.setLayoutManager(new LinearLayoutManager(this));
//        placeAdapter = new PlaceRecyclerAdapter(places, this);
//        rc.setAdapter(placeAdapter);

        text_dis_car = (TextView) findViewById(R.id.text_dis_car);
        text_dis_walk = (TextView) findViewById(R.id.text_dis_walk);
    }

    private View.OnClickListener onClickBack = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("Places", places);
            setResult(1, intent);
            finish();
        }
    };

    public void onVehiceDistance(View v) {
        if (places.size() > 0) {
            LinearLayout linearLayout = (LinearLayout) v.getParent();
            Log.e("Id", "" + linearLayout.getId());
            switch (linearLayout.getId()) {
                case R.id.tab1:
                    double distanceInWalkingMode = 0;
                    for (Place place : places) {
                        distanceInWalkingMode += place.getDistanceInWalkingMode();
                    }
                    text_dis_walk.setText("Total distance: " + distanceInWalkingMode + " km");
                    text_dis_car.setText("");
                    break;
                case R.id.tab3:
                    double distanceInDrivingMode = 0;
                    for (Place place : places) {
                        distanceInDrivingMode += place.getDistanceInDrivingMode();
                    }
                    text_dis_walk.setText("Total distance: " + distanceInDrivingMode + " km");
                    text_dis_walk.setText("");
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView rc = (RecyclerView) findViewById(R.id.recycler_view);
        rc.setLayoutManager(new LinearLayoutManager(this));
        PlaceRecyclerAdapter placeAdapter = new PlaceRecyclerAdapter(myList, this);
        rc.setAdapter(placeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mnu_add) {
            Intent intent = new Intent(this, CategoryActivity.class);
            if (places.size() > 0) {
                double latitude = places.get(places.size() - 1).getLat();
                double longitude = places.get(places.size() - 1).getLng();
                latLng = new LatLng(latitude, longitude);
            }
            intent.putExtra("LatLng", latLng);
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                Place place = data.getParcelableExtra("Place");
                Log.e("Place", "" + place.getTypeID());
                places.add(place);
//                placeAdapter.notifyDataSetChanged();
            }
        }
    }
}

