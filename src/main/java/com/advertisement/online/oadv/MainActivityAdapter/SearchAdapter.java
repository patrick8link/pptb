package com.advertisement.online.oadv.MainActivityAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.advertisement.online.oadv.R;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    private Activity mContext;
    private List<String> searchCategories;
    private ImageView searchCategoryImageView;
    private TextView searchCategoryTextView;
    private int[] searchCategoriesImage;

    public SearchAdapter(Activity context, List<String> searchCategory, int[] searchCategoryImage ){
        this.mContext = context;
        this.searchCategories = searchCategory;
        this.searchCategoriesImage = searchCategoryImage;

    }

    @Override
    public int getCount() {
        return searchCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return searchCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_search,null, true);

        searchCategoryImageView = (ImageView) view.findViewById(R.id.searchCategoryImageView);
        searchCategoryTextView = (TextView) view.findViewById(R.id.searchCategoryTextView);

        searchCategoryImageView.setImageResource(searchCategoriesImage[position]);
        searchCategoryTextView.setText(searchCategories.get(position));

        return view;
    }
}
