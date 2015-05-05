package com.example.materialdesign_actionbar;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.materialdesign_actionbar.model.Place;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        List<Address> addresses;

        if (latLng != null) {
            String placeUrl = getPlaceUrl();
            try {
                String placeJSONFile = getJSONFile(placeUrl);
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
                    Place place = new Place(name, "", lat, lng, 0, 0, 0);
                    if (type == "restaurant") {
                        place.seticonID(R.drawable.ic_restaurant);
                    } else if (type == "park") {
                        place.seticonID(R.drawable.ic_park);
                    } else {
                        place.seticonID(R.drawable.ic_gasstation);
                    }

                    // Get the distance between the specified location and the found location in driving and walking mode
                    String distanceUrl = getDistanceUrl(place, 1);
                    String distanceJSONFile = getJSONFile(distanceUrl);
                    readDistanceJSONFile(distanceJSONFile, place, 1);
                    distanceUrl = getDistanceUrl(place, 2);
                    distanceJSONFile = getJSONFile(distanceUrl);
                    readDistanceJSONFile(distanceJSONFile, place, 2);

                    // Get address of the found location
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(place.getLat(),
                            place.getLng(), 1);

                    if (addresses == null || addresses.size() == 0) {
                    } else {
                        int flag = addresses.get(0).getMaxAddressLineIndex();
                        String address = "";
                        if (flag > 0) {
                            address = addresses.get(0).getAddressLine(0) + ", "
                                    + addresses.get(0).getLocality();
                        } else {
                            address = addresses.get(0).getThoroughfare() + ", "
                                    + addresses.get(0).getLocality();
                        }
                        place.setAddress(address);
                    }
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

    private String getPlaceUrl() {
        StringBuilder googlePlacesUrl = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latLng.latitude + ","
                + latLng.longitude);
        googlePlacesUrl.append("&radius=" + Constants.PROXIMITY_RADIUS);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&key=" + Constants.GOOGLE_API_KEY);

        return googlePlacesUrl.toString();
    }

    private String getDistanceUrl(Place place, int mode) {
        StringBuilder googleDistanceUrl = new StringBuilder(
                "https://maps.googleapis.com/maps/api/distancematrix/json?");
        googleDistanceUrl.append("origins=" + latLng.latitude + ","
                + latLng.longitude);
        googleDistanceUrl.append("&destinations=" + place.getLat() + ","
                + place.getLng());
        switch (mode) {
            case 1:
                googleDistanceUrl.append("&mode=driving");
                break;
            case 2:
                googleDistanceUrl.append("&mode=walking");
                break;
        }
        googleDistanceUrl.append("&key=" + Constants.GOOGLE_API_KEY);

        return googleDistanceUrl.toString();
    }

    private String getJSONFile(String url) throws ClientProtocolException,
            IOException {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            InputStreamReader placesInput = new InputStreamReader(inputStream);
            BufferedReader placesReader = new BufferedReader(placesInput);
            String lineIn;
            while ((lineIn = placesReader.readLine()) != null) {
                stringBuilder.append(lineIn);
            }
        }
        return stringBuilder.toString();
    }

    private void readDistanceJSONFile(String JSONFile, Place place, int mode)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(JSONFile);
        JSONArray rowsArray = jsonObject.getJSONArray("rows");
        for (int i = 0; i < rowsArray.length(); i++) {
            JSONObject jsonChild = rowsArray.getJSONObject(i);
            JSONArray elementsArray = jsonChild.getJSONArray("elements");
            for (int j = 0; j < elementsArray.length(); j++) {
                JSONObject obj = elementsArray.getJSONObject(i);
                JSONObject distance = obj.getJSONObject("distance");
                double d = distance.getDouble("value") / 1000;
                switch (mode) {
                    case 1:
                        place.setDistanceInDrivingMode(d);
                        break;
                    case 2:
                        place.setDistanceInWalkingMode(d);
                        break;
                }
            }
        }
    }
}
