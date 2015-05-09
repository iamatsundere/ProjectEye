package com.example.materialdesign_actionbar;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.materialdesign_actionbar.model.Place;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private Toolbar toolbar;
    private GoogleMap map;
    private Location currentLocation;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private ArrayList<Place> places;
    private Polyline polyline;
    private List<LatLng> list;
    private double distance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        places = new ArrayList<>();

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mnu_edit) {
            Intent intent = new Intent(this, EditActivity.class);
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            intent.putExtra("LatLng", latLng);
            intent.putParcelableArrayListExtra("Places", places);
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        if (currentLocation != null) {
            handleLocation(currentLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        handleLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Initialize whatever needed here :3
     */
    private void init() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);
    }

    /**
     * Add a marker and move the map to the specified location
     *
     * @param location: the location that need handling
     */
    private void handleLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions marker = new MarkerOptions().position(latLng).title("You're here");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        map.addMarker(marker);
    }

    private void setUpMap() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        } else if (currentLocation != null) {
            handleLocation(currentLocation);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                try {
                    places = data.getParcelableArrayListExtra("Places");
                    if (places.size() > 0) {
                        getRoute(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(places.get(0).getLat(), places.get(0).getLng()));
                        for (Place p : places) {
                            Log.e("Place ID", "" + p.getTypeID());
                            switch (p.getTypeID()) {
                                case 0:
                                    addMarker(p.getName(), R.drawable.marker_restaurant, new LatLng(p.getLat(), p.getLng()));
                                    break;
                                case 1:
                                    addMarker(p.getName(), R.drawable.marker_park, new LatLng(p.getLat(), p.getLng()));
                                    break;
                                case 2:
                                    addMarker(p.getName(), R.drawable.marker_gas, new LatLng(p.getLat(), p.getLng()));
                                    break;
                            }
                        }
                        for (int i = 0; i < places.size() - 1; i++) {
                            getRoute(new LatLng(places.get(i).getLat(), places.get(i).getLng()), new LatLng(places.get(i + 1).getLat(), places.get(i + 1).getLng()));
                            Log.e("Start point", "" + places.get(i).getLat() + " " + places.get(i).getLng());
                        }
                    }
                } catch (Exception ex) {

                }
            }
        }
    }

    private void addMarker(String title, int path, LatLng ll) {
        MarkerOptions MO = new MarkerOptions()
                .title(title)
                .position(ll)
                .icon(BitmapDescriptorFactory.fromResource(path));

        map.addMarker(MO);
    }

    public void drawRoute() {
        PolylineOptions po;
        if (polyline == null) {
            po = new PolylineOptions().color(Color.RED).width(3).geodesic(true);
            for (int i = 0, tam = list.size(); i < tam; i++) {
                po.add(list.get(i));
            }
            polyline = map.addPolyline(po);
        } else polyline.setPoints(list);
    }

    public List<LatLng> buildJSONRoute(String json) throws JSONException {

        JSONObject result = new JSONObject(json);
        JSONArray routes = result.getJSONArray("routes");

        distance = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");
        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

        List<LatLng> lines = new ArrayList<LatLng>();

        for (int i = 0; i < steps.length(); i++) {
            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for (LatLng p : decodePolyline(polyline)) {
                lines.add(p);
            }
        }
        return lines;
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> listPoints = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng(((double) lat / 1E5), ((double) lng / 1E5));
            listPoints.add(p);
        }
        return listPoints;
    }

    public void getRoute(final LatLng origin, final LatLng destination) throws IOException {
        new Thread() {
            public void run() {
                String url = "http://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin.latitude + "," + origin.longitude
                        + "&destination=" + destination.latitude + "," + destination.longitude
                        + "&sensor=false";

                HttpResponse response;
                HttpGet request;
                AndroidHttpClient client = AndroidHttpClient.newInstance("route");
                request = new HttpGet(url);

                try {
                    response = client.execute(request);
                    final String answer = EntityUtils.toString(response.getEntity());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {

                                list = buildJSONRoute(answer);
                                drawRoute();
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }.start();
    }
}
