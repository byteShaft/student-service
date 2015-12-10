package com.byteshaft.studentservice.fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byteshaft.studentservice.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TrackFragment extends Fragment {

    private View mBaseView;
    SupportMapFragment myMapFragment;
    FragmentManager fm;
    GoogleMap mMap;
    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng WALL_STREET = new LatLng(40.7064, -74.0094);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.activity_maps, null);
        fm=getChildFragmentManager();
        myMapFragment=(SupportMapFragment) fm.findFragmentById(R.id.map);
        myMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);
                MarkerOptions options = new MarkerOptions();
                options.position(LOWER_MANHATTAN);
//                options.position(BROOKLYN_BRIDGE);
                options.position(WALL_STREET);
                googleMap.addMarker(options);
                String url = getMapsApiDirectionsUrl();
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BROOKLYN_BRIDGE,
                        13));
                addMarkers();
//                LatLng location1 = new LatLng(48.856612, 2.289777);
//                LatLng location2 = new LatLng(48.851522, 2.304782);
//                mMap.addMarker(new MarkerOptions().position(location1).title("Marker in this! ===>>"));
//                mMap.addMarker(new MarkerOptions().position(location2).title("Teacher Location"));
//                Polyline polyline = mMap.addPolyline(new PolylineOptions().add(
//                        new LatLng(48.856612, 2.289777), new LatLng(48.851522, 2.304782)).width(10)
//                        .color(Color.BLUE));
//                polyline.setGeodesic(true);
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.856612, 2.289777), 12.0f));
            }
        });
        return mBaseView;
    }

    private String getMapsApiDirectionsUrl() {
//        String waypoints = "waypoints=optimize:true|"
//                + LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude
//                + "|" + "|" + BROOKLYN_BRIDGE.latitude + ","
//                + BROOKLYN_BRIDGE.longitude + "|" + WALL_STREET.latitude + ","
//                + WALL_STREET.longitude;
        String waypoints = "waypoints=optimize:true|"
                + LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude
                + "|" + "|" +  BROOKLYN_BRIDGE.latitude + ","
                + BROOKLYN_BRIDGE.longitude + "|" + WALL_STREET.latitude + ","
        + WALL_STREET.longitude;
        String OriginDestination = "origin="+LOWER_MANHATTAN.latitude
                +","+LOWER_MANHATTAN.longitude+"&destination="+WALL_STREET.latitude+","+WALL_STREET.longitude;
        String sensor = "sensor=false";
        String params = OriginDestination+"&%20"+waypoints + "&" + sensor;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
    }

    private void addMarkers() {
        if (mMap != null) {
//            mMap.addMarker(new MarkerOptions().position(BROOKLYN_BRIDGE)
//                    .title("First Point"));
            mMap.addMarker(new MarkerOptions().position(LOWER_MANHATTAN)
                    .title("Second Point"));
            mMap.addMarker(new MarkerOptions().position(WALL_STREET)
                    .title("Third Point"));
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }

}
