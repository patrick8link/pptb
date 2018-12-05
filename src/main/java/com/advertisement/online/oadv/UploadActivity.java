package com.advertisement.online.oadv;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.advertisement.online.oadv.Model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UploadActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private ProgressDialog progress;
    ImageView uploadImageView,uploadCameraImageView,uploadGalleryImageView;
    TextView uploadCategoryTextView, uploadRegionTextView, uploadDescriptionTextView;
    EditText uploadCategoryEditText, uploadRegionEditText, uploadDescriptionEdiText;
    String uploadCategory = "Choose Category", uploadRegion = "Your Region", uploadDescription, key, value;
    String [] stringCategoryArray, stringRegionArray;

    double progressUpload;

    Spinner uploadCategorySpinner,uploadRegionSpinner;

    ProgressBar progressBar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    Uri imageHoldUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBarUpload);

        uploadImageView = (ImageView) findViewById(R.id.uploadImageView);
        uploadCameraImageView = (ImageView) findViewById(R.id.uploadCameraImageView);
        uploadGalleryImageView = (ImageView) findViewById(R.id.uploadGalleryImageView);

        uploadCategoryTextView = (TextView) findViewById(R.id.uploadCategoryTextView);
        uploadRegionTextView = (TextView) findViewById(R.id.uploadRegionTextView);
        uploadDescriptionTextView = (TextView) findViewById(R.id.uploadDescriptionTextView);

        uploadDescriptionEdiText = (EditText) findViewById(R.id.uploadDescriptionEditText);

        uploadCategorySpinner = (Spinner) findViewById(R.id.uploadCategorySpinner);
        uploadRegionSpinner = (Spinner) findViewById(R.id.uploadRegionSpinner);

        uploadImageView.setImageDrawable(null);

        uploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadActivity.this,String.valueOf(imageHoldUri),Toast.LENGTH_SHORT).show();
            }
        });

        uploadGalleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        uploadCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(UploadActivity.this,
                            Manifest.permission.CAMERA)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(UploadActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }else {
                    CropImage.activity(imageHoldUri).setGuidelines(CropImageView.Guidelines.ON).start(UploadActivity.this);
                }
            }
        });

        stringRegionArray = getResources().getStringArray(R.array.region_array);
        stringCategoryArray = getResources().getStringArray(R.array.category_array);

        setSpinnerAdapter(stringCategoryArray,uploadCategorySpinner);
        setSpinnerAdapter(stringRegionArray,uploadRegionSpinner);


        uploadCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uploadCategory = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uploadRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uploadRegion = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView uploadImageView = (ImageView) findViewById(R.id.uploadImageView);
        if(requestCode==1 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            imageHoldUri = imageUri;
            uploadImageView.setImageURI(imageHoldUri);
        } else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            if (data != null){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageHoldUri = result.getUri();
                uploadImageView.setImageURI(imageHoldUri);
            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_upload_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.action_post){
            actionPost();
            return true;
        }else{
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            return super.onOptionsItemSelected(item);
        }

    }

    private void writeNewPost(String userID, String category, String region, String description){
        key = mDatabase.child("posts").push().getKey();
        int viewCount = 0;
        Post post = new Post(userID,category,region,description,viewCount);
        mDatabase.child("posts").child(key).setValue(post);
        mDatabase.child("users").child(userID).child("posts").child(key).setValue(post);
        mDatabase.child("category").child(category).child(region).child(key).setValue(post);
    }

    private void actionPost(){

        uploadDescription = uploadDescriptionEdiText.getText().toString().trim();
        if((!uploadCategory.contentEquals("Choose Category")) && (!TextUtils.isEmpty(uploadDescription)) && (!uploadRegion.contentEquals("Your Region")) && (uploadImageView.getDrawable()!=null)){

            progress = new ProgressDialog(this);
            progress.setTitle("Uploading your Post");
            progress.setMessage("Loading "+progressUpload);
            progress.setIndeterminate(true);
            progress.show();

            writeNewPost(mUser.getUid(),uploadCategory,uploadRegion,uploadDescription);

            mStorage.child("posts").child(key+".jpg").putFile(imageHoldUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressUpload = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + (int) progressUpload + "% done");
                    int currentProgress = (int) progressUpload;
                    progress.setMessage("Loading "+currentProgress+"%");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is completed");
                    startActivity(new Intent(UploadActivity.this, MainActivity.class));
                    mStorage.child("posts").child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mDatabase.child("posts").child(key).child("URL").setValue(uri.toString());
                            mDatabase.child("users").child(mUser.getUid()).child("posts").child(key).child("URL").setValue(uri.toString());
                            mDatabase.child("category").child(uploadCategory).child(uploadRegion).child(key).child("URL").setValue(uri.toString());
                        }
                    });
                }
            });

        }else{
            Toast.makeText(getApplicationContext(),"Please insert picture, category, region, and description",Toast.LENGTH_SHORT).show();
        }
    }

    private void setSpinnerAdapter (String[] spinnerAdapterID, Spinner spinner){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerAdapterID);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                } else{
                    Toast.makeText(UploadActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
