package com.example.materialdesign_actionbar;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.materialdesign_actionbar.model.Place;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by the_e_000 on 5/4/2015.
 */
public class GetPlacesIntentService extends IntentService {

    private LatLng latLng;
    private ArrayList<Place> places;
    private String type;

    public GetPlacesIntentService() {
        super(Constants.TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        latLng = intent.getParcelableExtra("LatLng");
        places = new ArrayList<>();
        type = intent.getStringExtra("Type");

        if (latLng != null) {
            String placeUrl = JSONReader.getPlaceUrl(latLng, type);
            try {
                String placeJSONFile = JSONReader.getJSONFile(placeUrl);
                JSONObject jsonObj = new JSONObject(placeJSONFile);
                JSONArray jsonArr = jsonObj.getJSONArray("results");
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject obj = jsonArr.getJSONObject(i);
                    JSONObject geometry = new JSONObject(
                            obj.getString("geometry"));
                    JSONObject location = new JSONObject(
                            geometry.getString("location"));
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    String name = obj.getString("name");
                    String placeId = obj.getString("place_id");
                    Place place = new Place(name, "", lat, lng, 0, 0, 0);
                    if (type == "restaurant") {
                        place.seticonID(R.drawable.ic_restaurant);
                    } else if (type == "park") {
                        place.seticonID(R.drawable.ic_park);
                    } else {
                        place.seticonID(R.drawable.ic_gasstation);
                    }

                    // Get address of the found location
                    String placeDetailUrl = JSONReader.getPlaceDetailUrl(placeId);
                    String placeDetailJSONFile = JSONReader.getJSONFile(placeDetailUrl);
                    String address = JSONReader.readPlaceDetailJSONFile(placeDetailJSONFile);
                    place.setAddress(address);
                    Log.e("Address: ", place.getAddress());

                    places.add(place);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent broadcastIntent = new Intent("Broadcast address");
            broadcastIntent.putParcelableArrayListExtra("Places", places);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        }
    }
}
