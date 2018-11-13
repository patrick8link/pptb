package com.advertisement.online.oadv;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.advertisement.online.oadv.Model.Post;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

public class DetailPostActivity extends AppCompatActivity {

    public static final String EXTRA_POST_KEY = "post_key";

    String mPostKey;

    TextView detailCategoryTextView,detailRegionTextView,detailDescTextView;
    ImageView detailImageView;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        detailCategoryTextView = (TextView) findViewById(R.id.detailCategoryTextView);
        detailRegionTextView = (TextView) findViewById(R.id.detailRegionTextView);
        detailDescTextView = (TextView) findViewById(R.id.detailDescriptionTextView);

        detailImageView = (ImageView) findViewById(R.id.detailImageView);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey==null){
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mDatabase.child("posts").child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = new Post();
                post = dataSnapshot.getValue(Post.class);
                String uri = (String) dataSnapshot.child("URL").getValue();
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
}
