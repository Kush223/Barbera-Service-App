package com.barbera.barberaserviceapp.ui.servicedetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.barbera.barberaserviceapp.R;
import com.barbera.barberaserviceapp.ui.service.ImageVerifyActivity;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import static com.barbera.barberaserviceapp.LiveLocationService.person;

public class VehicleDetails extends AppCompatActivity {
    private Button rc,license,submit;
    private Bitmap rc_bitmap,lic_bitmap;
    private Uri filePath;
    public static final int RequestPermissionCode = 1;
    private ImageView rc_image,lic_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        rc=(Button) findViewById(R.id.rc);
        license=findViewById(R.id.license);
        submit=findViewById(R.id.Btn3);
        rc_image=findViewById(R.id.img11);
        lic_image=findViewById(R.id.img1);

        EnableRuntimePermission();
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });
        rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 8);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rc_image.getDrawable()==null){
                    Toast.makeText(getApplicationContext(),"Please enter your RC image",Toast.LENGTH_SHORT).show();
                }
                else if(lic_image.getDrawable()==null){
                    Toast.makeText(getApplicationContext(),"Please enter your Driving license image",Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences preferences = getSharedPreferences("Details",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("vehicle_det",true);
                    editor.apply();
                    Intent intent = new Intent(VehicleDetails.this,ServiceDetails.class);
                    intent.putExtra("license",lic_bitmap);
                    intent.putExtra("rc",rc_bitmap);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            lic_bitmap = (Bitmap) data.getExtras().get("data");
            lic_image.setImageBitmap(lic_bitmap);
            filePath = data.getData();
        }
        if (requestCode == 8 && resultCode == RESULT_OK) {
            rc_bitmap = (Bitmap) data.getExtras().get("data");
            rc_image.setImageBitmap(rc_bitmap);
            filePath = data.getData();
        }
    }

    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(VehicleDetails.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(VehicleDetails.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(VehicleDetails.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(VehicleDetails.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VehicleDetails.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}