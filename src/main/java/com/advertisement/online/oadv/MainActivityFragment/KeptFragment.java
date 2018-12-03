package com.advertisement.online.oadv.MainActivityFragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.advertisement.online.oadv.DetailPostActivity;
import com.advertisement.online.oadv.MainActivityAdapter.HomeAdapter;
import com.advertisement.online.oadv.Model.Post;
import com.advertisement.online.oadv.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

import static android.support.constraint.Constraints.TAG;
import static java.lang.String.valueOf;

public class KeptFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private GridView keptGridView;

    private int length;

    Post posts = new Post();
    ArrayList<String> mUri = new ArrayList<String>();
    ArrayList<String> key = new ArrayList<String>();
    ArrayList<Integer> vCount = new ArrayList<Integer>();
    ArrayList<String> region = new ArrayList<String>();
    ArrayList<String> category = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kept,null);

        keptGridView = (GridView) view.findViewById(R.id.keptGridView);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.keepSynced(true);

        length = 0  ;

        mDatabase.child("users").child(mUser.getUid()).child("kept").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                length = count;
                Log.i(TAG, "onDataChange: "+length);
                String[] post = new String[length];

                for (int i=0;i<length;i++){
                    post[i] = valueOf(i);
                }

                for (DataSnapshot children : dataSnapshot.getChildren()){
                    posts = children.getValue(Post.class);
                    vCount.add(posts.getViewCount());
                    mUri.add(valueOf(children.child("URL").getValue()));
                    key.add(children.getKey());
                    category.add(valueOf(children.child("category").getValue()));
                    region.add(valueOf(children.child("region").getValue()));
                }
                Collections.reverse(key);
                Collections.reverse(vCount);
                Collections.reverse(mUri);
                Collections.reverse(category);
                Collections.reverse(region);
                keptGridView.setAdapter(new HomeAdapter(getActivity(), post, mUri));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        keptGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        keptGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mDatabase.child("users").child(mUser.getUid()).child("kept").child(key.get(position)).removeValue();
                return true;
            }
        });


        return view;
    }
}
