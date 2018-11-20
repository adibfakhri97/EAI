package com.detrening.detrening.Profil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.detrening.detrening.Authentication.Login;
import com.detrening.detrening.Home.Beranda;
import com.detrening.detrening.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

public class EditProfile extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private String dateOfBirth, yearBirth, monthBirth, dateBirth;
    String Storage_Path = "fotoprofile/";
    public static final String Database_Path = "DeTrening";
    EditText namaEdit, beratEdit, tinggiEdit;
    TextView userEdit, ttlEdit;
    ImageView fotoView;
    Uri FilePathUri;
    private StorageReference storageReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;
    Button btnPilih;
    String uID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, Login.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String email = user.getEmail().toString();
        uID = user.getUid();



        storageReference = FirebaseStorage.getInstance().getReference();


        namaEdit = (EditText) findViewById(R.id.namaEdit);
        beratEdit = (EditText) findViewById(R.id.beratEdit);
        tinggiEdit = (EditText) findViewById(R.id.tinggiEdit);
        ttlEdit = (TextView) findViewById(R.id.ttlEdit);
        userEdit = (TextView) findViewById(R.id.userEdit);
        userEdit.setText(email);

        btnPilih = (Button) findViewById(R.id.btnFoto);

        fotoView = (ImageView) findViewById(R.id.fotoEdit);

        ttlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfile.this,
                        android.R.style.Theme_Holo_Light, mDateListener, year, month, day);
                datePickerDialog.getWindow(

                ).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int realMonth = month+1;
                String date = dayOfMonth + "/"+realMonth+"/"+year;
                dateBirth = date;

                ttlEdit.setText(date);
                String date1 = dayOfMonth + ""+month+""+year;

            }
        };

        progressDialog = new ProgressDialog(EditProfile.this);



    }


    public void pilihFoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), Image_Request_Code);
    }

    public void fungsiEdit(View view) {
        if (FilePathUri != null){
            progressDialog.setTitle("Menyimpan Profile...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String nama = namaEdit.getText().toString().trim();
                    String tinggi = tinggiEdit.getText().toString().trim();
                    String berat = beratEdit.getText().toString().trim();
                    String lahir = dateBirth;
                    @SuppressWarnings("VisibleForTests")
                    AdapterProfile imageUploadInfo = new AdapterProfile(nama,tinggi,berat, Beranda.emailUser, taskSnapshot.getDownloadUrl().toString(), lahir);

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    databaseReference.child(user.getUid()).setValue(imageUploadInfo);

                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Profil berhasil disimpan", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditProfile.this, ProfilInfo.class);
                    Log.d("Day of Birth", dateOfBirth );
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setTitle("Menyimpan profile...");

                }
            });

        } else {
            Toast.makeText(EditProfile.this, "Pilih Gambar", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null){

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                fotoView.setImageBitmap(bitmap);
                btnPilih.setText("Image Selected");
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public String GetFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
       return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
