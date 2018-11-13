package com.advertisement.online.oadv.MainActivityFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.advertisement.online.oadv.MainActivity;
import com.advertisement.online.oadv.MainActivityAdapter.HomeAdapter;
import com.advertisement.online.oadv.MainActivityAdapter.SearchAdapter;
import com.advertisement.online.oadv.Model.Post;
import com.advertisement.online.oadv.R;
import com.advertisement.online.oadv.SearchActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.InflaterInputStream;

import static android.support.constraint.Constraints.TAG;

public class SearchFragment extends Fragment {

    GridView searchGridView,searchGridView2;

    Spinner searchRegionSpinner;

    List<String> categoryList;

    DatabaseReference mDatabase;

    String clickValue, searchRegionValue;

    String[] stringRegionArray;

    String[] extra = new String [2];

    ArrayList<String> mUri = new ArrayList<String>();
    ArrayList<String > key = new ArrayList<String>();
    ArrayList<Integer> vCount = new ArrayList<Integer>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search,null);

        List<String> arrayCategoryList = new ArrayList<String>();

        searchRegionSpinner = (Spinner) view.findViewById(R.id.searchRegionSpinner);
        searchGridView = (GridView) view.findViewById(R.id.searchGridView);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        categoryList = Arrays.asList(getResources().getStringArray(R.array.category_array));

        for (int i=0;i<categoryList.size()-1;i++){
            arrayCategoryList.add(categoryList.get(i+1));
        }


        searchGridView.setAdapter(new SearchAdapter(getActivity(),arrayCategoryList));

        stringRegionArray = getResources().getStringArray(R.array.region_array);

        setSpinnerAdapter(stringRegionArray,searchRegionSpinner);

        searchRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchRegionValue = String.valueOf(parent.getItemAtPosition(position));
                extra[0] = searchRegionValue;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickValue = String.valueOf(parent.getItemAtPosition(position));
                extra[1] = clickValue;

                if (!extra[0].contentEquals("Your Region")){
                    Intent intent = new Intent (getActivity(),SearchActivity.class);
                    intent.putExtra(SearchActivity.EXTRA_VALUE,extra);
                    startActivity(intent);
                } else{
                    Toast.makeText(getActivity(),"Choose a Region", Toast.LENGTH_SHORT).show();
                }



            }
        });

        return view;
    }

    private void setSpinnerAdapter (String[] spinnerAdapterID, Spinner spinner){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, spinnerAdapterID);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
    }
}
