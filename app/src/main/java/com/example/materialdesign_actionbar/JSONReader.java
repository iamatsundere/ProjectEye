package com.example.materialdesign_actionbar;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
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

/**
 * Created by the_e_000 on 5/7/2015.
 */
public class JSONReader {
    public static String getPlaceUrl(LatLng latLng, String type) {
        StringBuilder googlePlacesUrl = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latLng.latitude + ","
                + latLng.longitude);
        googlePlacesUrl.append("&radius=" + Constants.PROXIMITY_RADIUS);
        googlePlacesUrl.append("&sensor=false");
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&key=" + Constants.GOOGLE_API_KEY);

        return googlePlacesUrl.toString();
    }

    public static String getPlaceDetailUrl(String placeId) {
        StringBuilder googlePlacesUrl = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesUrl.append("placeid=" + placeId);
        googlePlacesUrl.append("&key=" + Constants.GOOGLE_API_KEY);

        return googlePlacesUrl.toString();
    }

    public static String getDistanceUrl(double lat, double lng, int mode, LatLng latLng) {
        StringBuilder googleDistanceUrl = new StringBuilder(
                "https://maps.googleapis.com/maps/api/distancematrix/json?");
        googleDistanceUrl.append("origins=" + latLng.latitude + ","
                + latLng.longitude);
        googleDistanceUrl.append("&destinations=" + lat + ","
                + lng);
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

    public static String getDirectionUrl(LatLng origin, LatLng destination, int mode){
        StringBuilder googleDirectionUrl=new StringBuilder();
        return googleDirectionUrl.toString();
    }

    public static String getJSONFile(String url) throws IOException {
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

    public static Double readDistanceJSONFile(String JSONFile) throws JSONException {
        double distance = 0;
        JSONObject jsonObject = new JSONObject(JSONFile);
        JSONArray rowsArray = jsonObject.getJSONArray("rows");
        for (int i = 0; i < rowsArray.length(); i++) {
            JSONObject jsonChild = rowsArray.getJSONObject(i);
            JSONArray elementsArray = jsonChild.getJSONArray("elements");
            for (int j = 0; j < elementsArray.length(); j++) {
                JSONObject obj = elementsArray.getJSONObject(i);
                JSONObject distanceObj = obj.getJSONObject("distance");
                double d = distanceObj.getDouble("value") / 1000;
                distance = d;
            }
        }
        return distance;
    }

    public static String readPlaceDetailJSONFile(String JSONFile) throws JSONException {
        JSONObject jsonObject = new JSONObject(JSONFile);
        JSONObject result = jsonObject.getJSONObject("result");
        String address = result.getString("formatted_address");
        return address;
    }
}
