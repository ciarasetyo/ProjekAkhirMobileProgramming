package com.example.binusezyfoods2021;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper mDatabaseHelper;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.recyclerView);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_layout);

//        Untuk menampilkan back button dan memberikan fungsinya
        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainMenu();
            }
        });

//        Buat dapetin value dari intent di main activity supaya bisa ambil data sesuai kategorinya
        Bundle extras = getIntent().getExtras();
        String value = extras.getString("String");

        TextView myTitleText = (TextView) findViewById(R.id.homeTitle);
        myTitleText.setText("Binus EzyFoody: " + value);

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor dataUser = mDatabaseHelper.getData("user");
        dataUser.moveToFirst();
        int branch_id = dataUser.getInt(3);
        Cursor dataCategory = mDatabaseHelper.getItemCategory(branch_id,value);

        int count = dataCategory.getCount();
        String[] dataName = new String[count];
        int[] dataStock = new int[count];
        int[] dataPrice = new int[count];
        int[] dataImage = new int[count];

        for(int i=0; i<count; i++){
            dataCategory.moveToNext();
            dataName[i] = dataCategory.getString(2);
            dataPrice[i] = dataCategory.getInt(4);
            dataStock[i] = dataCategory.getInt(5);
            dataImage[i] = dataCategory.getInt(6);
        }

//        menggunakan adapter untuk menggunakan recycler view
        AdapterMenu adapterMenu = new AdapterMenu(this, dataName, dataPrice, dataStock, dataImage, value);
        recyclerView.setAdapter(adapterMenu);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

//        Fungsi button pada toolbar
        Button barBtn = (Button) findViewById(R.id.homeBtn);
        barBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyOrder();
            }
        });
    }

    public void openMainMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openMyOrder(){
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }
}