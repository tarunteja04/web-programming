package com.example.vijaya.androidhardware;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Random;

public class CameraActivity extends AppCompatActivity {

    int TAKE_PHOTO_CODE = 0;
    ImageView userImage;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    Button saveToGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Button capture = (Button) findViewById(R.id.btn_take_photo);
        userImage = (ImageView) findViewById(R.id.view_photo);
        saveToGallery = (Button) findViewById(R.id.saveButton);
        saveToGallery.setVisibility(View.VISIBLE);
        //Button click eventlistener. Initializes the camera.
        Toast.makeText(this,"In camera activity",Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        // ICP Task2: Write the code to capture the image


    }

    public void callCamera(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        }
    }

    //If the photo is captured then set the image view to the photo captured.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            userImage.setImageBitmap(photo);
            Log.d("CameraDemo", "Pic saved");
        }
    }
    // On Click of Save
    public void saveToGallery(View view) {
        // Get the BitMap Image First
        if(userImage.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable)userImage.getDrawable()).getBitmap();
            // Random Name Generator
            Random random = new Random();
            int n = 10000;
            n = random.nextInt(n);
            // Generating fName every time.
            String fname = "Image-"+ n +".jpg";
            MediaStore.Images.Media.insertImage(this.getApplicationContext().getContentResolver(), bitmap ,
                    fname , "Mobile-ICP4");
            Toast.makeText(this,"Saved Image To Gallery !!",Toast.LENGTH_SHORT).show();
        }
    }
    public void redirectToHome(View v) {
        Intent redirect = new Intent(CameraActivity.this, MainActivity.class);
        startActivity(redirect);
    }
}
