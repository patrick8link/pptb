package com.advertisement.online.oadv.MainActivityAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.advertisement.online.oadv.R;
import com.bumptech.glide.Glide;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class HomeAdapter extends BaseAdapter {

    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    private Activity mContext;
    private String[] post;
    private ArrayList<String> mUri;
    public HomeAdapter (Activity context, String[] post,ArrayList<String> mUri){
        this.mContext = context;
        this.post = post;
        this.mUri = mUri;
    }

    @Override
    public int getCount() {
        return post.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_home,null,true);

        ImageView homeImageView = (ImageView) view.findViewById(R.id.homeImageView);
        TextView homeTextView = (TextView) view.findViewById(R.id.homeTextView);

        Glide.with(mContext).load(mUri.get(position)).into(homeImageView);

        return view;
    }


}
