package com.detrening.detrening.Maps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.detrening.detrening.Authentication.Login;
import com.detrening.detrening.Profil.EditProfile;
import com.detrening.detrening.Profil.ProfilInfo;
import com.detrening.detrening.R;
import com.detrening.detrening.adapterAPIDeTrening;
import com.detrening.detrening.adapterAPIDeTrening2;
import com.detrening.detrening.adapterProgres;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class details_tempat extends AppCompatActivity {

    private TextView noTelp, snippetDet, jadwalTemp, websiteTemp, idT;
    private String TAG = "OPENING HOUR: ";
    private Button daftarM;

    private FirebaseAuth authF;
    private DatabaseReference dataRef1,dataRef2,dataAPI;

    public static final String Database_Path1 = "DeTrening";
    public static final String Database_Path2 = "ProgressUser";
    public static final String DeTrening_API = "DataAPI";


    Uri FilePathUri;

    ProgressDialog progressDialog;

    String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tempat);

        noTelp = (TextView) findViewById(R.id.no_telp);
        snippetDet = (TextView) findViewById(R.id.snippetDet);
        jadwalTemp = (TextView) findViewById(R.id.jadwal_tempat);
        websiteTemp = (TextView) findViewById(R.id.websiteTemp);
        idT = (TextView) findViewById(R.id.idTempat);
        daftarM = (Button) findViewById(R.id.btnDaftarMember);
        //        bukatutup = (TextView) findViewById(R.id.buka_tutup);

        authF = FirebaseAuth.getInstance();
        if (authF.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, Login.class));
        }

        dataRef1 = FirebaseDatabase.getInstance().getReference(Database_Path1);
        dataRef2 = FirebaseDatabase.getInstance().getReference(Database_Path2);
        dataAPI = FirebaseDatabase.getInstance().getReference(DeTrening_API);


        FirebaseUser user = authF.getCurrentUser();
        String email = user.getEmail().toString();
        uID = user.getUid();



        /*dataRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nama = dataSnapshot.child(uID).child("nama").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/






        daftarM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dataAPI.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(details_tempat.this);

                        String nama = dataSnapshot.child(uID).child("nama").getValue(String.class);

                        if (nama != null){
                            builder.setCancelable(true);
                            builder.setTitle("Perbarui member?");
                            builder.setMessage("Anda sudah terdaftar sebagai member di tempat ini, apakah anda ingin memperbaruinya?");

                            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            });
                            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(details_tempat.this, "Berhasil daftar!", Toast.LENGTH_LONG).show();
                                    clickDaftar();
                                    Intent intent = new Intent(details_tempat.this, ProfilInfo.class);
                                    startActivity(intent);
                                    details_tempat.this.finish();
                                }
                            });

                            builder.show();

                        } else {

                            builder.setCancelable(true);
                            builder.setTitle("Sudah member?");
                            builder.setMessage("Apakah anda member di tempat ini?");

                            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            });
                            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(details_tempat.this, "Berhasil daftar!", Toast.LENGTH_LONG).show();
                                    clickDaftar();
                                    Intent intent = new Intent(details_tempat.this, ProfilInfo.class);
                                    startActivity(intent);
                                    details_tempat.this.finish();
                                }
                            });

                            builder.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

        placeDetail();
    }

    public void placeDetail(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id_pl");
        String snippet = bundle.getString("snippet");
        snippetDet.setText(snippet);
        idT.setText(id);
        Log.d("details_tempat",snippet);
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id="+id+"&fields=name,rating,opening_hours,website,formatted_phone_number,website,reviews&key=AIzaSyCGk57FWCs2BLCzMtCVFSvmZiYrFgDROc4";

        Log.d("LIMIT", url);
        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("result");
                    JSONObject opening_hours = main_object.getJSONObject("opening_hours");
                    JSONArray array_jadwal = opening_hours.getJSONArray("weekday_text");

                    String[] jadArr = new String[array_jadwal.length()];

                    for (int i = 0; i <= 6; i++){
                        jadArr[i] = array_jadwal.getString(i);
                        Log.d(TAG, jadArr[i]);
                        jadwalTemp.setText("Jadwal Tempat : \n"+jadArr[0]+"\n"+jadArr[1]+"\n"+jadArr[2]+"\n"+jadArr[3]+"\n"+jadArr[4]+"\n"+jadArr[5]+"\n"+jadArr[6]);
                    }

                    String phone = main_object.getString("formatted_phone_number");

                    noTelp.setText("No Telp. : "+phone);
                    Log.d("no_Telp", phone);

                    String web = main_object.getString("website");
                    websiteTemp.setText("Website : "+web);
                    Log.d("web", web);


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

    public void clickDaftar(){
        final FirebaseUser user = authF.getCurrentUser();
        final String email = user.getEmail().toString();


        dataRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String progressUser = dataSnapshot.child(uID).child("progress").getValue(String.class);
                final String userE = dataSnapshot.child(uID).child("user").getValue(String.class);
                final String snippetAPI = snippetDet.getText().toString();
                final String idAPI = idT.getText().toString();
                final String telpAPI = noTelp.getText().toString();
                final String websiteAPI = websiteTemp.getText().toString();
                final String jadwalAPI = jadwalTemp.getText().toString();



                dataRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nama = dataSnapshot.child(uID).child("nama").getValue(String.class);
                        String berat = dataSnapshot.child(uID).child("berat").getValue(String.class);
                        String tinggi = dataSnapshot.child(uID).child("tinggi").getValue(String.class);
                        String imgURL = dataSnapshot.child(uID).child("imageURL").getValue(String.class);


                        if (progressUser == null){
                            String infoProg = "0";

//                    Log.d("INFO USER", infoProg);
//                    Log.d("INFO EMAIL", email);

                            adapterAPIDeTrening adapterAPI = new adapterAPIDeTrening(email, infoProg, berat, nama, tinggi, imgURL);
                            dataAPI.child(user.getUid()).setValue(adapterAPI);
                            adapterAPIDeTrening2 adapterAPI2 = new adapterAPIDeTrening2(snippetAPI, idAPI, telpAPI, websiteAPI, jadwalAPI);
                            dataAPI.child(user.getUid()).child("Tempat").setValue(adapterAPI2);
                        }

                        if (nama == null){
                            Intent intent = new Intent(details_tempat.this, EditProfile.class);
                            startActivity(intent);
                            details_tempat.this.finish();
                            Toast.makeText(details_tempat.this, "Mohon lengkapi profil anda terlebih dahulu", Toast.LENGTH_LONG).show();
                        }

                        adapterAPIDeTrening adapterAPI = new adapterAPIDeTrening(userE, progressUser, berat, nama, tinggi, imgURL);
                        dataAPI.child(user.getUid()).setValue(adapterAPI);
                        adapterAPIDeTrening2 adapterAPI2 = new adapterAPIDeTrening2(snippetAPI, idAPI, telpAPI, websiteAPI, jadwalAPI);
                        dataAPI.child(user.getUid()).child("Tempat").setValue(adapterAPI2);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





//                Log.d("INFO USER", progressUser);
//                Log.d("INFO EMAIL", String.valueOf(user));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
