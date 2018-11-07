package com.detrening.detrening.Maps;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    String url;
    private Marker mMarker;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        Log.d("nearbyplacesdata","called parse method");
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList){
        for (int i = 0; i <nearbyPlaceList.size(); i++){

            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            String rating = googlePlace.get("rating");
//            String openclose = googlePlace.get("open_close");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String id = googlePlace.get("place_id");


            String snippet = "Nama : "+placeName+"\n Alamat : "+vicinity+"\n Rating : " +rating+"\n Latitude : "+lat+"\n Longitude : "+lng;

            LatLng latLng = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(id)
                    .snippet(snippet);
//            markerOptions.title(placeName + " : "+ vicinity + " : "+ rating);

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));



            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//            mMap.setInfoWindowAdapter(new infoAdapter(null));

        }
    }
}
