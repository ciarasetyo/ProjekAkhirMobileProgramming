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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    RecyclerView recyclerView;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_layout);

        recyclerView = findViewById(R.id.recyclerView2);

//        Mengubah title toolbar
        TextView myTitleText = (TextView) findViewById(R.id.homeTitle);
        myTitleText.setText("Binus EzyFoody: History Detail");

//        Menghilangkan button pada pada toolbar
        Button barBtn = (Button) findViewById(R.id.homeBtn);
        barBtn.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        int trans_id = extras.getInt("trans_id");

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor dataDetail = mDatabaseHelper.getItemDetail(trans_id);
        int count = dataDetail.getCount();

//        Untuk menampilkan back button dan memberikan fungsinya
        ImageButton myImgBtn = (ImageButton) findViewById(R.id.backBtn);
        myImgBtn.setVisibility(View.VISIBLE);

        myImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        int[] dataImage = new int[count];
        String[] dataName = new String[count];
        int[] dataQty = new int[count];
        int[] dataPrice = new int[count];
        int[] dataID = new int[count];

        for(int i=0; i<count; i++){
            dataDetail.moveToNext();

            Cursor dataMenu = mDatabaseHelper.getItemByID("menu", dataDetail.getInt(2));
            dataMenu.moveToFirst();

//            toastMessage(""+dataCart.getString(1));

            dataName[i] = dataMenu.getString(2);
            dataPrice[i] = dataMenu.getInt(4);
            dataQty[i] = dataDetail.getInt(3);
            dataImage[i] = dataMenu.getInt(6);
            dataID[i] = dataDetail.getInt(0);

            int temp = dataQty[i] * dataPrice[i];
            total = total + temp;

        }

        TextView totalText = (TextView) findViewById(R.id.totalText);
        totalText.setText(String.valueOf(total));

        Button removeBtn = (Button) findViewById(R.id.removeBtn);

//        Menggunakan adapter untuk recycler view
        AdapterMyOrder adapterMyOrder = new AdapterMyOrder(this, dataImage, dataName, dataQty, dataPrice, dataID, true);
        recyclerView.setAdapter(adapterMyOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void back(){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}