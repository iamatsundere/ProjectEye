package com.example.materialdesign_actionbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.materialdesign_actionbar.model.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class EditActivity extends ActionBarActivity {

    public Button btn;
    private ArrayList<Place> places;
    private LatLng latLng;

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
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        places = new ArrayList<>();
        Intent intent = getIntent();
        latLng = intent.getParcelableExtra("LatLng");

    }

    private View.OnClickListener onClickBack = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public void OnBack(View view) {
        if (view.getId() == R.id.mnu_back) {
            finish();
        }
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
                Log.d("Received place: ", place.getName() + " " + place.getAddress());
                places.add(place);
            }
        }
    }
}

