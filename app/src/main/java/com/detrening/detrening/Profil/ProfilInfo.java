package com.detrening.detrening.Profil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.detrening.detrening.Authentication.Login;
import com.detrening.detrening.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilInfo extends AppCompatActivity {
    TextView namaProfile, tinggiProfile, beratProfile, emailProfile, beratIdeal, ageProfile;
    CircleImageView fotoP;
    Button infoM;
    ImageView verif;
//    public int umur;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference, dataAPI;

    public static final String Database_Path = "DeTrening";
    public static final String DeTrening_API = "DataAPI";
    String uID;

    RadioButton LK, PR;
    RadioGroup rG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_info);
        setTitle("Profil");

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, Login.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        dataAPI = FirebaseDatabase.getInstance().getReference(DeTrening_API);

        FirebaseUser user = mAuth.getCurrentUser();

        uID = user.getUid();
        String emailsaya = user.getEmail().toString();

        fotoP = (CircleImageView) findViewById(R.id.circleImageView);
        namaProfile = (TextView) findViewById(R.id.namaInfo);
        tinggiProfile = (TextView) findViewById(R.id.tinggiInfo);
        emailProfile = (TextView) findViewById(R.id.emailInfo);
        beratProfile = (TextView) findViewById(R.id.beratInfo);
        beratIdeal = (TextView) findViewById(R.id.txtB);
        ageProfile = (TextView) findViewById(R.id.ageInfo);
        infoM = (Button) findViewById(R.id.infoMember);
        verif = (ImageView) findViewById(R.id.verif);

        emailProfile.setText(emailsaya);

        LK = (RadioButton) findViewById(R.id.LK);
        PR = (RadioButton) findViewById(R.id.PR);

        rG = (RadioGroup) findViewById(R.id.rB);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nama = dataSnapshot.child(uID).child("nama").getValue(String.class);
                String berat = dataSnapshot.child(uID).child("berat").getValue(String.class);
                String tinggi = dataSnapshot.child(uID).child("tinggi").getValue(String.class);
                String gambar = dataSnapshot.child(uID).child("imageURL").getValue(String.class);
                String email = dataSnapshot.child(uID).child("user").getValue(String.class);
                String dob = dataSnapshot.child(uID).child("lahir").getValue(String.class);

                if(dob!=null){
                    String[] items1 = dob.split("/");
                    String day = items1[0];
                    String month = items1[1];
                    String year = items1[2];
                    int d = Integer.parseInt(day);
                    int m = Integer.parseInt(month);
                    int y = Integer.parseInt(year);
                    getAge(d, y, m);

                    int currentAge;
                    GregorianCalendar cal = new GregorianCalendar();
                    int currentYear = cal.get(Calendar.YEAR);
                    int currentMonth = cal.get(Calendar.MONTH);
                    int currentDay = cal.get(Calendar.DAY_OF_MONTH);

                    cal.set(y, m, d);
                    currentAge = currentYear-cal.get(Calendar.YEAR);
                    if ((currentMonth < cal.get(Calendar.MONTH))
                            || ((m == cal.get(Calendar.MONTH)) && (currentDay < cal
                            .get(Calendar.DAY_OF_MONTH)))) {
                        --currentAge;
                    }
                    if(currentAge < 0)
                        throw new IllegalArgumentException("Age < 0");

                    String umur = Integer.toString(currentAge);
                    ageProfile.setText(umur);
                }



                if (nama!=null) {

                    namaProfile.setText(nama);
                    beratProfile.setText(berat);
                    tinggiProfile.setText(tinggi);

                //    emailProfile.setText(email);
                    //       fotoView.setImageDrawable(gambar);
                    Glide.with(ProfilInfo.this).load(gambar).into(fotoP);

                    String tg = tinggi.toString();
                    String br = berat.toString();

                    int tgV = Integer.parseInt(tg);
                    int brV = Integer.parseInt(br);

                    int idealLK = (tgV - 100) - ((tgV - 100) * 10 / 100);
                    int idealPR = (tgV - 100) - ((tgV - 100) * 15 / 100);


                    final String hasilLK = String.valueOf(idealLK);
                    final String hasilPR = String.valueOf(idealPR);


                    LK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            beratIdeal.setText("Your ideal weight " + hasilLK + " Kg");
                        }
                    });

                    PR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            beratIdeal.setText("Your ideal weight " + hasilPR + " Kg");
                        }
                    });
                } else {
                    startActivity(new Intent(ProfilInfo.this, EditProfile.class));
                }

                dataAPI.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nama = dataSnapshot.child(uID).child("nama").getValue(String.class);

                        if (nama != null){
                            infoM.setClickable(true);
                            infoM.setText("Member");
                            infoM.setTextColor(R.color.TRUE);

                            verif.setVisibility(View.VISIBLE);

                            infoM.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProfilInfo.this, infoMember.class);
                                    startActivity(intent);
                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button btnEditProfile = (Button) findViewById(R.id.button2);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilInfo.this, EditProfile.class));
            }
        });
    }

    public int getAge(int day, int month, int year){
        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);

        cal.set(day, month, year);
        a = y-cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            throw new IllegalArgumentException("Age < 0");

//        a = umur;
        String agetest = Integer.toString(a);
        Log.d("Age: ", agetest);
        return a;
    }
}
