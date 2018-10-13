package com.advertisement.online.oadv;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.advertisement.online.oadv.Model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImageView,uploadCameraImageView,uploadGalleryImageView;
    TextView uploadCategoryTextView, uploadRegionTextView, uploadDescriptionTextView;
    EditText uploadCategoryEditText, uploadRegionEditText, uploadDescriptionEdiText;
    String uploadCategory, uploadRegion, uploadDescription, key;

    ProgressBar progressBar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    Uri imageHoldUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        progressBar = (ProgressBar) findViewById(R.id.progressBarUpload);

        uploadImageView = (ImageView) findViewById(R.id.uploadImageView);
        uploadCameraImageView = (ImageView) findViewById(R.id.uploadCameraImageView);
        uploadGalleryImageView = (ImageView) findViewById(R.id.uploadGalleryImageView);

        uploadCategoryTextView = (TextView) findViewById(R.id.uploadCategoryTextView);
        uploadRegionTextView = (TextView) findViewById(R.id.uploadRegionTextView);
        uploadDescriptionTextView = (TextView) findViewById(R.id.uploadDescriptionTextView);

        uploadCategoryEditText = (EditText) findViewById(R.id.uploadCategoryEditText);
        uploadRegionEditText = (EditText) findViewById(R.id.uploadRegionEditText);
        uploadDescriptionEdiText = (EditText) findViewById(R.id.uploadDescriptionEditText);

        uploadImageView.setImageDrawable(null);

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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,2);
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
        } else if (requestCode==2 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            imageHoldUri = imageUri;
            uploadImageView.setImageURI(imageHoldUri);
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
            uploadCategory = uploadCategoryEditText.getText().toString().trim();
            uploadDescription = uploadDescriptionEdiText.getText().toString().trim();
            uploadRegion = uploadRegionEditText.getText().toString().trim();
            if((!TextUtils.isEmpty(uploadCategory)) && (!TextUtils.isEmpty(uploadDescription)) && (!TextUtils.isEmpty(uploadRegion)) && (uploadImageView.getDrawable()!=null)){
                progressBar.setVisibility(View.VISIBLE);
                writeNewPost(mUser.getUid(),uploadCategory,uploadRegion,uploadDescription);

                mStorage.child("posts").child(key+".jpg").putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorage.child("posts").child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mDatabase.child("posts").child(key).child("URL").setValue(uri.toString());
                                mDatabase.child("users").child(mUser.getUid()).child("posts").child(key).child("URL").setValue(uri.toString());
                            }
                        });
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
                finish();
                startActivity(new Intent(UploadActivity.this,MainActivity.class));
            }else{
                Toast.makeText(getApplicationContext(),"Please insert picture, category, region, and description",Toast.LENGTH_SHORT).show();
            }

            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }

    private void writeNewPost(String userID, String category, String region, String description){
        key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userID,category,region,description);
        mDatabase.child("posts").child(key).setValue(post);
    }
}
