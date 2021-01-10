package com.example.binusezyfoods2021;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

    private ImageButton drinksButton;
    private ImageButton snacksButton;
    private ImageButton foodsButton;
    private ImageButton topupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_layout);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setVisibility(View.GONE);

        Button locationBtn = (Button) findViewById(R.id.locationBtn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocation();
            }
        });

        Button historyBtn = (Button) findViewById(R.id.historyBtn);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistory();
            }
        });

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData("user");
        data.moveToFirst();

        TextView myCash = (TextView) findViewById(R.id.myCash);
        myCash.setText("Cash: Rp " + data.getString(2));

        //        untuk pindah ke Menu Category Activity dengan data dari masing masing kategori
        drinksButton = (ImageButton) findViewById(R.id.drinksBtn);
        drinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Drinks");
            }
        });

        snacksButton = (ImageButton) findViewById(R.id.snacksBtn);
        snacksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Snacks");
            }
        });

        foodsButton = (ImageButton) findViewById(R.id.foodsBtn);
        foodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("Foods");
            }
        });

        topupButton = (ImageButton) findViewById(R.id.topupBtn);
        topupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTopUp();
            }
        });

        //        fungsi button pada toolbar
        Button barBtn = (Button) findViewById(R.id.homeBtn);
        barBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyOrder();
            }
        });
    }

    public void openLocation(){
        Intent intent = new Intent(this, MapsFragment.class);
        startActivity(intent);
    }

    public void openHistory(){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void openCategory(String value){
        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData("user");
        data.moveToFirst();

        if(data.getInt(3)==-1){
            toastMessage("Please Choose Restaurant Location First!!!");
            return;
        }

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("String", value);
        startActivity(intent);
    }

    public void openTopUp(){
        Intent intent = new Intent(this, TopUpActivity.class);
        startActivity(intent);
    }

    public void openMyOrder(){
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}