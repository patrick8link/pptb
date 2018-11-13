package com.advertisement.online.oadv.MainActivityFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.advertisement.online.oadv.DetailPostActivity;
import com.advertisement.online.oadv.MainActivity;
import com.advertisement.online.oadv.MainActivityAdapter.HomeAdapter;
import com.advertisement.online.oadv.Model.Post;
import com.advertisement.online.oadv.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.getDefaultSize;

public class HomeFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    int length=0;
    int i;

    Post posts = new Post();
    ArrayList<String> mUri = new ArrayList<String>();
    ArrayList<String> key = new ArrayList<String>();
    ArrayList<Integer> vCount = new ArrayList<Integer>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,null);
        final GridView gridView = (GridView) view.findViewById(R.id.homeGridView);

        mDatabase.keepSynced(true);

        mDatabase.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                length = count;
                Log.i(TAG, "onDataChange: "+length);
                String[] post = new String[length];


                for (i=0;i<length;i++){
                    post[i] = String.valueOf(i);
                }

                for (DataSnapshot children : dataSnapshot.getChildren()){
                    posts = children.getValue(Post.class);
                    vCount.add(posts.getViewCount());
                    mUri.add(String.valueOf(children.child("URL").getValue()));
                    key.add(children.getKey());
                }
                Collections.reverse(key);
                Collections.reverse(vCount);
                Collections.reverse(mUri);
                gridView.setAdapter(new HomeAdapter(getActivity(), post, mUri));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDatabase.child("posts").child(key.get(position)).child("viewCount").setValue(vCount.get(position)+1);
                getActivity().finish();
                startActivity(getActivity().getIntent());
                Intent intent = new Intent(getActivity(),DetailPostActivity.class);
                intent.putExtra(DetailPostActivity.EXTRA_POST_KEY,key.get(position));
                startActivity(intent);
                Toast.makeText(getActivity(),""+key.get(position),Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

}
