package com.advertisement.online.oadv;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.advertisement.online.oadv.Model.Post;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailPostActivity extends AppCompatActivity {

    public static final String EXTRA_POST_KEY = "post_key";

    String mPostKey;

    TextView detailCategoryTextView,detailRegionTextView,detailDescTextView;
    ImageView detailImageView;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        detailCategoryTextView = (TextView) findViewById(R.id.detailCategoryTextView);
        detailRegionTextView = (TextView) findViewById(R.id.detailRegionTextView);
        detailDescTextView = (TextView) findViewById(R.id.detailDescriptionTextView);

        detailImageView = (ImageView) findViewById(R.id.detailImageView);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey==null){
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mDatabase.child("posts").child(mPostKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = new Post();
                String uri = null;
                post = dataSnapshot.getValue(Post.class);
                uri = (String) dataSnapshot.child("URL").getValue();
                detailCategoryTextView.setText(post.getCategory());
                detailDescTextView.setText(post.getDesc());
                detailRegionTextView.setText(post.getRegion());
                Glide.with(getApplicationContext()).load(uri).into(detailImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_keep,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_keep:
                mDatabase.child("posts").child(mPostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Post post = new Post();
                        String uri = null;
                        post = dataSnapshot.getValue(Post.class);
                        uri = (String) dataSnapshot.child("URL").getValue();
                        writeNewPost(post.getUserID(),post.getCategory(),post.getRegion(),post.getDesc(),mPostKey,uri);
                        Toast.makeText(DetailPostActivity.this,"Post Saved",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void writeNewPost(String userID, String category, String region, String description,String key, String uri){
        int viewCount = 0;
        Post post = new Post(userID,category,region,description,viewCount);
        mDatabase.child("users").child(mUser.getUid()).child("kept").child(key).setValue(post);
        mDatabase.child("users").child(mUser.getUid()).child("kept").child(key).child("URL").setValue(uri);
    }
}
