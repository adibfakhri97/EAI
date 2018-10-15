package com.detrening.detrening.Maps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.detrening.detrening.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class details_tempat extends AppCompatActivity {

    private TextView noTelp, snippetDet, jadwalTemp, websiteTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tempat);

        noTelp = (TextView) findViewById(R.id.no_telp);
        snippetDet = (TextView) findViewById(R.id.snippetDet);
        jadwalTemp = (TextView) findViewById(R.id.jadwal_tempat);
        websiteTemp = (TextView) findViewById(R.id.websiteTemp);



        placeDetail();
    }

    public void placeDetail(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id_pl");
        String snippet = bundle.getString("snippet");
        snippetDet.setText(snippet);
        Log.d("details_tempat",snippet);
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+id+"&fields=name,rating,opening_hours,website,formatted_phone_number,website,reviews&key=AIzaSyCGk57FWCs2BLCzMtCVFSvmZiYrFgDROc4";

        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("result");
                    JSONObject opening_hours = main_object.getJSONObject("opening_hours");
                    JSONArray array_jadwal = opening_hours.getJSONArray("weekday_text");
                    String[] jadArr = new String[array_jadwal.length()];


                    for (int i = 0; i < array_jadwal.length(); i++){
                        jadArr[i] = array_jadwal.getString(i);
                        jadwalTemp.setText(jadArr[i]);
                    }



                    String phone = main_object.getString("formatted_phone_number");
                    String web = main_object.getString("website");

                    noTelp.setText("No Telp. : "+phone);
                    websiteTemp.setText(web);







                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        RequestQueue que = Volley.newRequestQueue(details_tempat.this);
        que.add(json);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
