package com.detrening.detrening.Profil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.detrening.detrening.Authentication.Login;
import com.detrening.detrening.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class infoMember extends AppCompatActivity {

    TextView snippet, telp, jadwal, website, id;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;

    public static final String DeTrening_API = "DataAPI";

    String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_member);
        setTitle("Tempat Member");

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, Login.class));
        }

        snippet = (TextView) findViewById(R.id.snippetDetP);
        telp = (TextView) findViewById(R.id.no_telpP);
        jadwal = (TextView) findViewById(R.id.jadwal_tempatP);
        website = (TextView) findViewById(R.id.websiteTempP);

        databaseReference = FirebaseDatabase.getInstance().getReference(DeTrening_API);

        FirebaseUser user = auth.getCurrentUser();

        uID = user.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String snippetV = dataSnapshot.child(uID).child("Tempat").child("snippetAPI").getValue(String.class);
                String telpV = dataSnapshot.child(uID).child("Tempat").child("telpAPI").getValue(String.class);
                String jadwalV = dataSnapshot.child(uID).child("Tempat").child("jadwalAPI").getValue(String.class);
                String websiteV = dataSnapshot.child(uID).child("Tempat").child("websiteAPI").getValue(String.class);

                snippet.setText(snippetV);
                telp.setText(telpV);
                jadwal.setText(jadwalV);
                website.setText(websiteV);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
