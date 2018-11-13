package com.advertisement.online.oadv;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.advertisement.online.oadv.MainActivityAdapter.HomeAdapter;
import com.advertisement.online.oadv.Model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity {

    public static final String EXTRA_VALUE = "value";

    String [] extra = new String [2];

    String regionExtra, categoryExtra;

    GridView searchGridView;

    DatabaseReference mDatabase;

    ArrayList<String> mUri = new ArrayList<String>();
    ArrayList<String> key = new ArrayList<String>();

    Post post = new Post();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        extra = getIntent().getStringArrayExtra(EXTRA_VALUE);

        searchGridView = (GridView) findViewById(R.id.searchGridView);

        if (extra==null){
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        categoryExtra = extra[1];
        regionExtra = extra[0];

        mDatabase = FirebaseDatabase.getInstance().getReference();

        readDatabase(mDatabase);

        searchGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this,DetailPostActivity.class);
                SearchActivity.this.finish();
                startActivity(SearchActivity.this.getIntent());
                intent.putExtra(DetailPostActivity.EXTRA_POST_KEY,key.get(position));
                startActivity(intent);
            }
        });
    }

    private void readDatabase (DatabaseReference mDatabase){
        mDatabase.child("category").child(categoryExtra).child(regionExtra).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();

                String [] posts = new String [count];

                for (int i=1;i<count;i++){
                    posts[i] = String.valueOf(i);
                }

                for (DataSnapshot searchSnapshot : dataSnapshot.getChildren()) {
                    post = searchSnapshot.getValue(Post.class);
                    mUri.add(String.valueOf(searchSnapshot.child("URL").getValue()));
                    key.add(searchSnapshot.getKey());
                }
                Collections.reverse(key);
                Collections.reverse(mUri);
                searchGridView.setAdapter(new HomeAdapter(SearchActivity.this,posts,mUri));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
