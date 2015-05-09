package com.example.materialdesign_actionbar;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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

import com.example.materialdesign_actionbar.adapter.CategoryRecyclerAdapter;
import com.example.materialdesign_actionbar.model.Category;
import com.example.materialdesign_actionbar.model.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends ActionBarActivity {

    public static int typeID;
    private PlaceReceiver placeReceiver;
    private LatLng latLng;
    private ProgressDialog progressDialog;
    private IntentFilter addressFilter;

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
        setSupportActionBar(toolbar);

        RecyclerView rc = (RecyclerView) findViewById(R.id.recycler_view);
        rc.setLayoutManager(new LinearLayoutManager(this));

        CategoryRecyclerAdapter catgrAdapter = new CategoryRecyclerAdapter(getData(), this);
        rc.setAdapter(catgrAdapter);

        // Get the sent latitude and longitude;
        Intent intent = getIntent();
        latLng = intent.getParcelableExtra("LatLng");
        Log.e("LatLng", "" + latLng);

        // Register get place intent service
        addressFilter = new IntentFilter("Broadcast address");
        placeReceiver = new PlaceReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(placeReceiver, addressFilter);
    }


//    public void OnBack(View view) {
//        if (view.getId() == R.id.mnu_back) {
//            finish();
//        }
//    }

    public void onCategoryClick(View view) {
        CardView cd = (CardView) view.getParent().getParent();
        typeID = cd.getId();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading data...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String typeSearch;
        if (typeID == 0) {
            typeSearch = "restaurant";
        } else if (typeID == 1) {
            typeSearch = "park";
        } else {
            typeSearch = "gas_station";
        }
        Intent intent = new Intent(this, GetPlacesIntentService.class);
        intent.putExtra("LatLng", latLng);
        intent.putExtra("Type", typeSearch);
        startService(intent);
    }

    public List<Category> getData() {
        List<Category> categoryList = new ArrayList<>();
        int[] arrCatgrIcons = {R.drawable.ic_restaurant, R.drawable.ic_park, R.drawable.ic_gasstation};
        String[] arrCatgrContent = {"Restaurant", "Park", "Gas Station"};
        int[] arrCatgrImage = {R.drawable.restaurant, R.drawable.supermarket, R.drawable.gasstation};
        int[] arrCatgrColors = {R.color.mnu_restaurant, R.color.mnu_park, R.color.mnu_gasstation};

        for (int i = 0; i < arrCatgrContent.length; i++) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data != null) {
                Place place=data.getParcelableExtra("Place");
                Intent intent=new Intent();
                intent.putExtra("Place",place);
                setResult(1, intent);
                finish();
            }
        }
    }

    @Override
      protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(CategoryActivity.this).unregisterReceiver(placeReceiver);
    }

    public class PlaceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            progressDialog.dismiss();
            ArrayList<Place> places = intent.getParcelableArrayListExtra("Places");
            Intent placeIntent = new Intent(CategoryActivity.this, PlaceActivity.class);
            if(places.size()>0){
                Log.e("Place",""+places.get(0).getName());
            }
            placeIntent.putExtra("LatLng", latLng);
            placeIntent.putParcelableArrayListExtra("Places", places);
            startActivityForResult(placeIntent, 2);
        }
    }
}
