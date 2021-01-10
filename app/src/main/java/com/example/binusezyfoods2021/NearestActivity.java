package com.example.binusezyfoods2021;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class NearestActivity extends AppCompatActivity {
    ListView nearestList;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_layout);

//        Mengubah title toolbar
        TextView myTitleText = (TextView) findViewById(R.id.homeTitle);
        myTitleText.setText("Binus EzyFoody: Nearest");

//        Mengubah tulisan button pada pada toolbar
        Button barBtn = (Button) findViewById(R.id.homeBtn);
        barBtn.setText("Map");

        barBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });


        nearestList = (ListView) findViewById(R.id.nearestList);

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData("branch");

        final ArrayList<String> dataNearest = new ArrayList<>();
        final ArrayList<Double> dataNearestX = new ArrayList<>();
        final ArrayList<Double> dataNearestY = new ArrayList<>();
        final ArrayList<Float> dataNearestDistance = new ArrayList<>();
        final ArrayList<String> dataFinal = new ArrayList<>();

        while(data.moveToNext()){
            dataNearest.add(data.getString(1));
            dataNearestX.add(data.getDouble(2));
            dataNearestY.add(data.getDouble(3));

            float result[] = new float[10];
            Location.distanceBetween(MapsFragment.currentLocation.getLatitude(), MapsFragment.currentLocation.getLongitude(), data.getDouble(2), data.getDouble(3), result);
            dataNearestDistance.add(result[0]/1000);
        }

        final int[] index = argsort(dataNearestDistance);
        for(int i=0; i<dataNearest.size(); i++){

            String input = "Branch = " + dataNearest.get(index[i]) +
                    "\nCoordinate = (" + dataNearestX.get(index[i]) +" , "+ dataNearestY.get(index[i]) +
                    ")\nDistance = " + dataNearestDistance.get(index[i]) + " KM";

            dataFinal.add(input);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataFinal);
        nearestList.setAdapter(arrayAdapter);

        nearestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                mDatabaseHelper.updateName("user", "last_restaurant", index[position]+1, 1);

                Toast.makeText(NearestActivity.this,"Clicked item: "+ dataNearest.get(index[position]), Toast.LENGTH_SHORT).show();

                MapsFragment.pickCoor = new LatLng(dataNearestX.get(index[position]), dataNearestY.get(index[position]));

                Cursor dataCart = mDatabaseHelper.getData("cart");

                while(dataCart.moveToNext()){
                    mDatabaseHelper.deleteName("cart", dataCart.getInt(0));
                }

                openMap();
            }
        });
    }
    public void openMap(){
        Intent intent = new Intent(this, MapsFragment.class);
        startActivity(intent);
    }

    public int[] argsort(ArrayList<Float> distance){
        int[] index = new int[distance.size()];

        for (int i=0; i<distance.size(); i++){
            index[i] = i;
        }

        for(int i=0; i<distance.size(); i++){
            for(int j=i+1; j<distance.size(); j++){
                if(distance.get(i)>distance.get(j)){
                    int temp = index[i];
                    index[i] = index[j];
                    index[j] = temp;
                }
            }
        }

        return index;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}