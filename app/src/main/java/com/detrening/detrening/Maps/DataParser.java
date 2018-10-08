package com.detrening.detrening.Maps;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson){
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName ="--NA--";
        String vicinity ="--NA--";
        String latitude = "";
        String longitude = "";
        String reference = "";
//        boolean openclose ;
        String rating = "--NA--";
        String id = "--NA--";

        Log.d("DataParser","jsonobject ="+googlePlaceJson.toString());

        try {
            if (!googlePlaceJson.isNull("name")){

                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")){
                vicinity = googlePlaceJson.getString("vicinity");
            }

            if (!googlePlaceJson.isNull("rating")){
                rating = googlePlaceJson.getString("rating");
            }
            if (!googlePlaceJson.isNull("place_id")){
                id = googlePlaceJson.getString("place_id");
            }
//            openclose = googlePlaceJson.getJSONObject("opening_hours").getBoolean("open_now");
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            /*if (openclose == true){
                String buka = "buka";
                googlePlaceMap.put("open_now", buka);
            } else {
                String tutup = "tutup";
                googlePlaceMap.put("open_now", tutup);
            }*/

            reference = googlePlaceJson.getString("reference");

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("rating", rating);
            googlePlaceMap.put("place_id", id);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlaceMap;
    }

    private List<HashMap<String, String>>getPlaces(JSONArray jsonArray){
        int count = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for (int i=0;i<count;i++){
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    public List<HashMap<String, String>> parse(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        Log.d("json data", jsonData);

        try {
            jsonObject= new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }
}
